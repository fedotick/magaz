package md.fedot.magaz.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fedot.magaz.dto.ProductRequestDto;
import md.fedot.magaz.dto.ProductResponseDto;
import md.fedot.magaz.exception.NotFoundException;
import md.fedot.magaz.model.Category;
import md.fedot.magaz.model.Product;
import md.fedot.magaz.repository.CategoryRepository;
import md.fedot.magaz.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public List<ProductResponseDto> getAllProducts() {
        log.info("Getting all products...");

        List<ProductResponseDto> productResponseDtos = productRepository.findAll()
                .stream()
                .map(ProductResponseDto::new)
                .toList();

        log.info("Found {} products.", productResponseDtos.size());

        return productResponseDtos;
    }

    public ProductResponseDto getProduct(Long id) {
        log.info("Getting product with ID: " + id);

        ProductResponseDto productResponseDto = productRepository.findById(id)
                .map(ProductResponseDto::new)
                .orElseThrow(() -> {
                    log.warn("Product not found");
                    return new NotFoundException("Product not found");
                });

        log.info("Found product: " + productResponseDto);

        return productResponseDto;
    }

    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        log.info("Creating product with data: " + productRequestDto);

        Product product = mapToEntity(productRequestDto, new Product());

        try {
            Product savedProduct = productRepository.save(product);
            log.info("Product created with ID: " + savedProduct.getId());
        } catch (Exception e) {
            log.warn("Product was not created: " + e.getMessage());
        }

        return new ProductResponseDto(product);
    }

    public ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto) {
        log.info("Updating product with ID: " + id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product not found");
                    return new NotFoundException("Product not found");
                });

        mapToEntity(productRequestDto, product);

        try {
            productRepository.save(product);
            log.info("Category updated");
        } catch (Exception e) {
            log.error("Product was not updated: " + e.getMessage());
        }

        return new ProductResponseDto(product);
    }

    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: " + id);

        productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product not found");
                    return new NotFoundException("Product not found");
                });

        try {
            productRepository.deleteById(id);
            log.info("Product deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting product: " + e.getMessage());
        }
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
                .orElseThrow(() -> new NotFoundException("Category not found"));
        product.setCategory(category);
        return product;
    }

}
