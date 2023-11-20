package md.fedot.magaz.service;

import md.fedot.magaz.domain.Category;
import md.fedot.magaz.domain.Product;
import md.fedot.magaz.model.ProductDTO;
import md.fedot.magaz.repos.CategoryRepository;
import md.fedot.magaz.repos.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public ProductService(final CategoryRepository categoryRepository,
                          final ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public List<ProductDTO> findAll() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> mapToDTO(product, new ProductDTO()))
                .toList();
    }

    public Product create(ProductDTO productDTO) {
        final Product product = new Product();
        mapToEntity(productDTO, product);
        productRepository.save(product);
        return product;
    }

    public ProductDTO mapToDTO(final Product product, final ProductDTO productDTO) {
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setImage(product.getImage());
        productDTO.setPrice(product.getPrice());
        productDTO.setQuantity(product.getQuantity());
        productDTO.setCategory(product.getCategory().getId());
        return productDTO;
    }

    public void mapToEntity(final ProductDTO productDTO, final Product product) {
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setImage(productDTO.getImage());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        final Category category = categoryRepository.findById(productDTO.getCategory())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);
    }

}
