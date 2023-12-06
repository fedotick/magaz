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
        return productRepository.findAll()
                .stream()
                .map(ProductResponseDto::new)
                .toList();
    }

    public ProductResponseDto getProduct(Long id) {
        return productRepository.findById(id)
                .map(ProductResponseDto::new)
                .orElseThrow(NotFoundException::new);
    }

    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        Product product = new Product();
        mapToEntity(productRequestDto, product);
        return new ProductResponseDto(productRepository.save(product));
    }

    public ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(productRequestDto, product);
        return new ProductResponseDto(productRepository.save(product));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
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
