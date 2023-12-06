package md.fedot.magaz.service;

import lombok.AllArgsConstructor;
import md.fedot.magaz.model.Category;
import md.fedot.magaz.model.Product;
import md.fedot.magaz.dto.ProductRequestDto;
import md.fedot.magaz.dto.ProductResponseDto;
import md.fedot.magaz.repository.CategoryRepository;
import md.fedot.magaz.repository.ProductRepository;
import md.fedot.magaz.exception.BadRequestException;
import md.fedot.magaz.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public List<ProductResponseDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> mapToDTO(product, new ProductResponseDto()))
                .toList();
    }

    public ProductResponseDto getProduct(Long id) {
        return productRepository.findById(id)
                .map(product -> mapToDTO(product, new ProductResponseDto()))
                .orElseThrow(NotFoundException::new);
    }

    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        Product product = new Product();
        mapToEntity(productRequestDto, product);
        return mapToDTO(productRepository.save(product), new ProductResponseDto());
    }

    public ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(productRequestDto, product);
        return mapToDTO(productRepository.save(product), new ProductResponseDto());
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public ProductResponseDto mapToDTO(Product product, ProductResponseDto productResponseDto) {
        productResponseDto.setId(product.getId());
        productResponseDto.setName(product.getName());
        productResponseDto.setDescription(product.getDescription());
        productResponseDto.setImage(product.getImage());
        productResponseDto.setPrice(product.getPrice());
        productResponseDto.setQuantity(product.getQuantity());

        Category category = product.getCategory();
        productResponseDto.setCategory((category == null) ? null : category.getName());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        productResponseDto.setCreatedAt(product.getCreatedAt().format(formatter));
        productResponseDto.setUpdatedAt(product.getUpdatedAt().format(formatter));

        return productResponseDto;
    }

    public Product mapToEntity(ProductRequestDto productRequestDto, Product product) {
        product.setName(productRequestDto.getName());
        product.setDescription(productRequestDto.getDescription());
        try {
            product.setImage(productRequestDto.getImage().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        product.setPrice(productRequestDto.getPrice());
        product.setQuantity(productRequestDto.getQuantity());
        Category category = (productRequestDto.getCategory() == null)
                ? null
                :categoryRepository.findById(productRequestDto.getCategory())
                .orElseThrow(BadRequestException::new);
        product.setCategory(category);
        return product;
    }

}
