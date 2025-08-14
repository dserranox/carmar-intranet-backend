package ar.com.carmar.service;

import ar.com.carmar.config.ExcelProperties;
import ar.com.carmar.dto.OrdenesExcelDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExcelOrderService {

    private final ExcelProperties excelProperties;

    public ExcelOrderService(ExcelProperties excelProperties) {
        this.excelProperties = excelProperties;
    }

    /**
     * Lee una hoja del Excel (por año) y devuelve cada fila como un mapa JSON-friendly.
     * @param year año/hoja a leer (por ejemplo 2025)
     * @param overridePath ruta opcional del archivo; si es null o vacío, usa carmar.excel.file
     */
    public List<Map<String, Object>> readOrders(int year, String overridePath) throws IOException {
        String pathStr = StringUtils.hasText(overridePath) ? overridePath : excelProperties.getFile();
        if (!StringUtils.hasText(pathStr)) {
            throw new IllegalArgumentException("No se especificó la ruta del Excel. Configure carmar.excel.file o pase ?path=");
        }
        Path path = Paths.get(pathStr);
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("No existe el archivo Excel: " + path.toAbsolutePath());
        }

        try (FileInputStream fis = new FileInputStream(path.toFile());
            Workbook wb = new XSSFWorkbook(fis)) {

            String sheetName = String.valueOf(year);
            Sheet sheet = wb.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("No existe la pestaña '" + sheetName + "' en el Excel.");
            }

            // Encabezados en la primera fila con normalización
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                return Collections.emptyList();
            }

            List<String> headers = new ArrayList<>();
            for (int c = 0; c < headerRow.getLastCellNum(); c++) {
                Cell cell = headerRow.getCell(c, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                String raw = (cell == null) ? "" : cell.toString();
                headers.add(normalizeKey(raw));
            }

            List<Map<String, Object>> rows = new ArrayList<>();
            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;

                // Chequear si la fila está completamente vacía
                boolean allBlank = true;
                for (int c = 0; c < headers.size(); c++) {
                    Cell cell = row.getCell(c, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if (cell != null && cell.getCellType() != CellType.BLANK && cell.toString().trim().length() > 0) {
                        allBlank = false;
                        break;
                    }
                }
                if (allBlank) continue;

                Map<String, Object> map = new LinkedHashMap<>();
                for (int c = 0; c < headers.size(); c++) {
                    String key = headers.get(c);
                    if (!StringUtils.hasText(key)) {
                        // Saltar columnas sin nombre
                        continue;
                    }
                    Cell cell = row.getCell(c, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Object val = getCellValue(cell);
                    map.put(key, val);
                }
                rows.add(map);
            }

            // Filtrar filas que no tengan información mínima (ej: plan, producto)
            // Esto es opcional: se puede comentar si se quiere traer todas las filas.
            rows = rows.stream()
                    .filter(m -> m.values().stream().anyMatch(Objects::nonNull))
                    .collect(Collectors.toList());

            return rows;
        }
    }

    private static String normalizeKey(String input) {
        if (input == null) return "";
        String s = input.trim();
        if (s.isEmpty()) return "";
        s = Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        s = s.replaceAll("[^\\p{Alnum}]+", "_");
        s = s.replaceAll("_+", "_");
        s = s.replaceAll("^_|_$", "");
        return s.toLowerCase(Locale.ROOT);
    }

    private static Object getCellValue(Cell cell) {
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case STRING:
                String v = cell.getStringCellValue();
                return v != null ? v.trim() : null;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    if (date == null) return null;
                    LocalDate ld = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return ld.toString(); // ISO-8601 "yyyy-MM-dd"
                } else {
                    double d = cell.getNumericCellValue();
                    // Si es entero, devolver como Long; sino Double
                    if (Math.floor(d) == d) {
                        return (long) d;
                    } else {
                        return d;
                    }
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                try {
                    if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                        if (DateUtil.isCellDateFormatted(cell)) {
                            Date date = cell.getDateCellValue();
                            if (date == null) return null;
                            LocalDate ld = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            return ld.toString();
                        } else {
                            double d = cell.getNumericCellValue();
                            if (Math.floor(d) == d) return (long) d;
                            return d;
                        }
                    } else if (cell.getCachedFormulaResultType() == CellType.STRING) {
                        String sv = cell.getStringCellValue();
                        return sv != null ? sv.trim() : null;
                    } else if (cell.getCachedFormulaResultType() == CellType.BOOLEAN) {
                        return cell.getBooleanCellValue();
                    } else {
                        return null;
                    }
                } catch (Exception e) {
                    return cell.toString();
                }
            default:
                return null;
        }
    }

    public List<OrdenesExcelDTO> getOrdenesActualizarFromExcel(int year, String overridePath) throws IOException {
        List<Map<String, Object>> rows = readOrders(year, overridePath);
        return rows.stream().map(m -> toDto(m)).collect(Collectors.toList());
    }

    private OrdenesExcelDTO toDto(Map<String, Object> m) {
        OrdenesExcelDTO dto = new OrdenesExcelDTO();
        dto.setPlan(asString(firstNonNull(m, "n_de_plan")));
        dto.setOrdenInterna(asString(firstNonNull(m, "orden_interna")));
        dto.setClienteId(asString(firstNonNull(m, "cliente")));
        dto.setProductoSolicitado(asString(firstNonNull(m, "producto_solicitado")));
        dto.setCodigoProducto(asString(firstNonNull(m, "codigo_del_producto")));
        dto.setFechaIncio(asString(firstNonNull(m, "fecha_incio")));
        dto.setCantidad(asString(firstNonNull(m, "cantidad")));
        dto.setHoja(asString(firstNonNull(m, "hoja")));
        dto.setEtiqueta(asString(firstNonNull(m, "etiqueta")));
        dto.setSituacion(asString(firstNonNull(m, "situacion")));
        dto.setFechaFinalizacion(asString(firstNonNull(m, "fecha_finalizacion")));
        dto.setLoteProduccion(asString(firstNonNull(m, "lote_produccion")));
        dto.setSeries(asString(firstNonNull(m, "series")));
        dto.setObservacion(asString(firstNonNull(m, "observacion")));
        return dto;
    }

    private static Object firstNonNull(Map<String, Object> m, String... keys) {
        for (String k : keys) {
            if (m.containsKey(k) && m.get(k) != null) return m.get(k);
        }
        return null;
    }
    private static String asString(Object o) {
        if (o == null) return null;
        String s = String.valueOf(o).trim();
        return s.isEmpty() ? null : s;
    }
}
