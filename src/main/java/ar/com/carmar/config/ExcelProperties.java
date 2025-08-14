package ar.com.carmar.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "carmar.excel")
public class ExcelProperties {
    /**
     * Ruta por defecto del archivo Excel.
     */
    private String file;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
