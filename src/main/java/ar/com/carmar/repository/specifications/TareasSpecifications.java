package ar.com.carmar.repository.specifications;

import ar.com.carmar.entity.Operaciones;
import ar.com.carmar.entity.Tareas;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TareasSpecifications {

    public static Specification<Tareas> byUserName(String username) {
        return (root, query, builder) -> {
            if (!StringUtils.hasText(username)) {
                return builder.conjunction();
            }
            return builder.equal(root.get("audUsrIns"), username);
        };
    }

    public static Specification<Tareas> byFechaInicio(LocalDateTime desde, LocalDateTime hasta) {
        return (root, query, builder) -> {
            if (desde == null && hasta == null) return builder.conjunction();
            if (desde != null && hasta != null)
                return builder.between(root.get("tarFechaInicio"), desde, hasta);
            if (desde != null)
                return builder.greaterThanOrEqualTo(root.get("tarFechaInicio"), desde);
            return builder.lessThanOrEqualTo(root.get("tarFechaInicio"), hasta);
        };
    }

    public static Specification<Tareas> byOperacionNombre(String operacion) {
        return (root, query, builder) -> {
            if (!StringUtils.hasText(operacion)) {
                return builder.conjunction();
            }
            Join<Tareas, Operaciones> op = root.join("operacion", JoinType.LEFT);
            String pattern = "%" + operacion.toLowerCase() + "%";
            query.distinct(true);
            return builder.or(
                    builder.like(builder.lower(op.get("opeNombreCorto")), pattern),
                    builder.like(builder.lower(op.get("opeNombre")), pattern)
            );
        };
    }
}
