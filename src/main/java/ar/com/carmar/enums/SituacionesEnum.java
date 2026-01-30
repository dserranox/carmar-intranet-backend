package ar.com.carmar.enums;

public enum SituacionesEnum {
    REVISAR_PLANO("REVISAR PLANO"),
    PLANIFICADO("PLANIFICADO"),
    EN_PROCESO("EN PROCESO"),
    TERMINADO("TERMINADO"),
    FALTA_MATERIAL("FALTA MATERIAL");

    private final String descripcion;

    SituacionesEnum(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
