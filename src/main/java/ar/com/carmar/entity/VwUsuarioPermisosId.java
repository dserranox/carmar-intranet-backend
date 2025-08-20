package ar.com.carmar.entity;

import java.io.Serializable;
import java.util.Objects;

public class VwUsuarioPermisosId implements Serializable {
    private Long usrId;
    private String rolNombre;
    private String prmClave;

    public VwUsuarioPermisosId() {}
    public VwUsuarioPermisosId(Long usrId, String rolNombre, String prmClave) {
        this.usrId = usrId; this.rolNombre = rolNombre; this.prmClave = prmClave;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VwUsuarioPermisosId that = (VwUsuarioPermisosId) o;
        return Objects.equals(usrId, that.usrId) &&
                Objects.equals(rolNombre, that.rolNombre) &&
                Objects.equals(prmClave, that.prmClave);
    }
    @Override public int hashCode() { return Objects.hash(usrId, rolNombre, prmClave); }
}