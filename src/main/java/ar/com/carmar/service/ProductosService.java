package ar.com.carmar.service;

import ar.com.carmar.dto.ProductoResponseDTO;
import ar.com.carmar.entity.Productos;
import ar.com.carmar.repository.ProductosRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductosService extends BaseService {

    private final ProductosRepository productosRepository;

    public ProductosService(ProductosRepository productosRepository) {
        this.productosRepository = productosRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> buscarProductos(String filtro) {
        List<Productos> productos = productosRepository.buscarPorCodigoODescripcion(filtro);
        return productos.stream().map(this::toDto).toList();
    }

    private ProductoResponseDTO toDto(Productos producto) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(producto.getPrdId());
        dto.setCodigo(producto.getPrdCodigoProducto());
        dto.setDescripcion(producto.getPrdDescripcion());
        return dto;
    }
}
