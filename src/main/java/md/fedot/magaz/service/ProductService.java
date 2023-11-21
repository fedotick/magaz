package md.fedot.magaz.service;

import md.fedot.magaz.domain.Category;
import md.fedot.magaz.domain.Product;
import md.fedot.magaz.model.ProductDTO;
import md.fedot.magaz.repos.CategoryRepository;
import md.fedot.magaz.repos.ProductRepository;
import md.fedot.magaz.util.BadRequestException;
import md.fedot.magaz.util.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
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

    public ProductDTO get(final Long id) {
        return productRepository.findById(id)
                .map(product -> mapToDTO(product, new ProductDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public ProductDTO create(final ProductDTO productDTO) {
        final Product product = new Product();
        mapToEntity(productDTO, product);
        return mapToDTO(productRepository.save(product), productDTO);
    }

    public ProductDTO update(final Long id, final ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(productDTO, product);
        return mapToDTO(productRepository.save(product), new ProductDTO());
    }

    public void delete(final Long id) {
        productRepository.deleteById(id);
    }

    public ProductDTO mapToDTO(final Product product, final ProductDTO productDTO) {
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setImage(product.getImage());
        productDTO.setPrice(product.getPrice());
        productDTO.setQuantity(product.getQuantity());

        final Category category = product.getCategory();
        productDTO.setCategory((category == null) ? null : category.getId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        productDTO.setCreatedAt(product.getCreatedAt().format(formatter));
        productDTO.setUpdatedAt(product.getUpdatedAt().format(formatter));

        return productDTO;
    }

    public Product mapToEntity(final ProductDTO productDTO, final Product product) {
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setImage(productDTO.getImage());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        
        Category category = (productDTO.getCategory() == null)
                ? null
                :categoryRepository.findById(productDTO.getCategory())
                .orElseThrow(BadRequestException::new);
        product.setCategory(category);
        return product;
    }

}
