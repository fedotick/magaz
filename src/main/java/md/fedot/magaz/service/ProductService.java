package md.fedot.magaz.service;

import lombok.AllArgsConstructor;
import md.fedot.magaz.domain.Category;
import md.fedot.magaz.domain.Product;
import md.fedot.magaz.model.ProductRequestDTO;
import md.fedot.magaz.model.ProductResponseDTO;
import md.fedot.magaz.repos.CategoryRepository;
import md.fedot.magaz.repos.ProductRepository;
import md.fedot.magaz.util.BadRequestException;
import md.fedot.magaz.util.NotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public List<ProductResponseDTO> findAll() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> mapToDTO(product, new ProductResponseDTO()))
                .toList();
    }

    public ProductResponseDTO get(final Long id) {
        return productRepository.findById(id)
                .map(product -> mapToDTO(product, new ProductResponseDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public ProductResponseDTO create(final ProductRequestDTO productRequestDTO) {
        final Product product = new Product();
        mapToEntity(productRequestDTO, product);
        return mapToDTO(productRepository.save(product), new ProductResponseDTO());
    }

    public ProductResponseDTO update(final Long id, final ProductRequestDTO productRequestDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(productRequestDTO, product);
        return mapToDTO(productRepository.save(product), new ProductResponseDTO());
    }

    public void delete(final Long id) {
        productRepository.deleteById(id);
    }

    public ProductResponseDTO mapToDTO(final Product product, final ProductResponseDTO productResponseDTO) {
        productResponseDTO.setId(product.getId());
        productResponseDTO.setName(product.getName());
        productResponseDTO.setDescription(product.getDescription());
        productResponseDTO.setImage(product.getImage());
        productResponseDTO.setPrice(product.getPrice());
        productResponseDTO.setQuantity(product.getQuantity());

        final Category category = product.getCategory();
        productResponseDTO.setCategory((category == null) ? null : category.getName());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        productResponseDTO.setCreatedAt(product.getCreatedAt().format(formatter));
        productResponseDTO.setUpdatedAt(product.getUpdatedAt().format(formatter));

        return productResponseDTO;
    }

    public Product mapToEntity(final ProductRequestDTO productRequestDTO, final Product product) {
        product.setName(productRequestDTO.getName());
        product.setDescription(productRequestDTO.getDescription());
        try {
            product.setImage(productRequestDTO.getImage().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        product.setPrice(productRequestDTO.getPrice());
        product.setQuantity(productRequestDTO.getQuantity());
        Category category = (productRequestDTO.getCategory() == null)
                ? null
                :categoryRepository.findById(productRequestDTO.getCategory())
                .orElseThrow(BadRequestException::new);
        product.setCategory(category);
        return product;
    }

}
