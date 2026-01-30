package ar.com.carmar.service;

import ar.com.carmar.dto.ClienteResponseDTO;
import ar.com.carmar.entity.Clientes;
import ar.com.carmar.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientesService extends BaseService {
    private final ClienteRepository clienteRepository;

    public ClientesService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> getAllClientes() {
        List<Clientes> clientes = clienteRepository.findAll();
        return clientes.stream().map(this::toDto).toList();
    }

    private ClienteResponseDTO toDto(Clientes cliente) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getCliId());
        dto.setRazonSocial(cliente.getCliRazonSocial());
        dto.setCuit(cliente.getCliCuit());
        return dto;
    }
}
