package ar.com.carmar.service;

import ar.com.carmar.dto.OrdenResponseDTO;
import ar.com.carmar.dto.OrdenesDocumentosDTO;
import ar.com.carmar.dto.OrdenesExcelDTO;
import ar.com.carmar.entity.Clientes;
import ar.com.carmar.entity.Ordenes;
import ar.com.carmar.entity.Productos;
import ar.com.carmar.entity.Situaciones;
import ar.com.carmar.repository.ClienteRepository;
import ar.com.carmar.repository.OrdenesRepository;
import ar.com.carmar.repository.ProductosRepository;
import ar.com.carmar.repository.SituacionesRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrdenesService extends BaseService{
    private final OrdenesRepository ordenesRepository;
    private final ExcelOrderService excelOrderService;
    private final ProductosRepository productosRepository;
    private final ClienteRepository clienteRepository;
    private final SituacionesRepository situacionesRepository;

    public OrdenesService(OrdenesRepository ordenesRepository,
                          ExcelOrderService excelOrderService,
                          ProductosRepository productosRepository,
                          ClienteRepository clienteRepository,
                          SituacionesRepository situacionesRepository) {
        this.ordenesRepository = ordenesRepository;
        this.excelOrderService = excelOrderService;
        this.productosRepository = productosRepository;
        this.clienteRepository = clienteRepository;
        this.situacionesRepository = situacionesRepository;
    }

    @Transactional(readOnly = true)
    public List<OrdenResponseDTO> getAllByYear(Integer year) {
        List<Ordenes> ordenes = ordenesRepository.findByOrdAnioOrderByOrdNroPlanAsc(year);
        return ordenes.stream().map(this::toDto).toList();
    }

    private OrdenResponseDTO toDto(Ordenes ordenes) {
        OrdenResponseDTO ordenResponseDTO = new OrdenResponseDTO();
        ordenResponseDTO.setId(ordenes.getOrdId());
        ordenResponseDTO.setOrdNroPlan(ordenes.getOrdNroPlan());
        ordenResponseDTO.setAnio(ordenes.getOrdAnio());
        ordenResponseDTO.setFechaInicio(ordenes.getFechaIncio());
        ordenResponseDTO.setFechaFinalizacion(ordenes.getFechaFinalizacion());
        ordenResponseDTO.setOrdenInterna(ordenes.getOrdenInterna());
        ordenResponseDTO.setCantidad(ordenes.getCantidad());
        ordenResponseDTO.setHoja(ordenes.getHoja());
        ordenResponseDTO.setEtiqueta(ordenes.getEtiqueta());
        ordenResponseDTO.setObservacion(ordenes.getOrdObservacion());
        if (ordenes.getCliente()!=null) {
            ordenResponseDTO.setClienteId(ordenes.getCliente().getCliId());
        }
        if (ordenes.getProducto()!=null) {
            ordenResponseDTO.setProductoCodigo(ordenes.getProducto().getPrdCodigoProducto());
            ordenResponseDTO.setProductoDescripcion(ordenes.getProducto().getPrdDescripcion());
        }
        if (ordenes.getSituacion()!=null) {
            ordenResponseDTO.setSituacionClave(ordenes.getSituacion().getSitEstadoClave());
        }
        if (!ordenes.getOrdenesDocumentos().isEmpty()){
            List<OrdenesDocumentosDTO> documentosDTOs = ordenes.getOrdenesDocumentos()
                    .stream()
                    .map(doc -> {
                        OrdenesDocumentosDTO d = new OrdenesDocumentosDTO();
                        d.setOdoId(doc.getOdoId());
                        d.setOdoNombre(doc.getOdoNombre());
                        d.setOdoDriveUrl(doc.getOdoDriveUrl());
                        return d;
                    })
                    .toList();
            ordenResponseDTO.setOrdenesDocumentosDTOs(documentosDTOs);
        }
        return ordenResponseDTO;
    }
    @Transactional
    public Ordenes saveFromDto(OrdenesExcelDTO dto, int year) throws IOException {
        if (dto == null || !StringUtils.hasText(dto.getPlan())) {
            throw new IllegalArgumentException("El DTO de orden debe contener el número de plan.");
        }

        Ordenes entity = ordenesRepository.findByOrdNroPlan(dto.getPlan().trim())
                .orElseGet(Ordenes::new);
        entity.setOrdNroPlan(dto.getPlan().trim());
        entity.setOrdObservacion(dto.getObservacion());
        entity.setOrdAnio(year);
        entity.setEtiqueta(dto.getEtiqueta());
        entity.setHoja(dto.getHoja());
        entity.setFechaIncio(dto.getFechaIncio() != null ? LocalDate.parse(dto.getFechaIncio()).atStartOfDay() : null);
        entity.setFechaFinalizacion(dto.getFechaFinalizacion() != null ? LocalDate.parse(dto.getFechaFinalizacion()).atStartOfDay() : null);
        entity.setCantidad(dto.getCantidad() != null ? Integer.parseInt(dto.getCantidad()) : null);
        entity.setOrdenInterna(dto.getOrdenInterna());
//        entity.setSeries(dto.getSeries());
//        entity.setLoteProduccion();

        // Situación
        if (StringUtils.hasText(dto.getSituacion())) {
            Optional<Situaciones> optSituacion = situacionesRepository.findBySitEstadoClaveIgnoreCase(dto.getSituacion().trim());
            entity.setSituacion(optSituacion.get());
        }

        // Producto
        if (StringUtils.hasText(dto.getCodigoProducto())) {
            Optional<Productos> optProducto = productosRepository.findByPrdCodigoProducto(dto.getCodigoProducto().trim());
            entity.setProducto(optProducto.get());
        }

        // Cliente
        if (StringUtils.hasText(dto.getClienteId())) {
            Optional<Clientes> optCliente = clienteRepository.findById(Long.parseLong(dto.getClienteId()));
            entity.setCliente(optCliente.get());
        }

        auditar(entity, "JOB_ORDENES");
        return ordenesRepository.save(entity);
    }

    @Transactional
    public int bulkSaveFromDtos(int year, String overridePath) throws IOException {
        int count = 0;
        List<OrdenesExcelDTO> ordenesActualizarFromExcel = excelOrderService.getOrdenesActualizarFromExcel(year, overridePath);
        if (ordenesActualizarFromExcel == null) return 0;
        for (OrdenesExcelDTO dto : ordenesActualizarFromExcel) {
            saveFromDto(dto, year);
            count++;
        }
        return count;
    }

    public OrdenResponseDTO finalizarOrden(OrdenResponseDTO ordenDto) {
        Ordenes orden = ordenesRepository.findById(ordenDto.getId()).orElseGet(Ordenes::new);

        if (orden == null){
            throw new EntityNotFoundException("Orden no encontrada.");
        }
        Optional<Situaciones> situacionTerminada = situacionesRepository.findBySitEstadoClaveIgnoreCase("TERMINADO");

        orden.setFechaFinalizacion(LocalDateTime.now());
        orden.setSituacion(situacionTerminada.get());
        auditar(orden, SecurityContextHolder.getContext().getAuthentication().getName() );

        ordenesRepository.save(orden);
        return toDto(orden);
    }
}
