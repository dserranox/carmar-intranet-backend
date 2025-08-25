package ar.com.carmar.service;

import ar.com.carmar.entity.*;
import ar.com.carmar.repository.*;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OperacionesService {
    private final OperacionesRepository ordenesRepository;

    public OperacionesService(OperacionesRepository ordenesRepository) {
        this.ordenesRepository = ordenesRepository;
    }

    public List<Operaciones> getAllOrderByOpeOrden() {
        return ordenesRepository.findAllByOrderByOpeOrdenAsc();
    }
}
