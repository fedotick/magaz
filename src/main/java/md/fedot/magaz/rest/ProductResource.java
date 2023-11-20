package md.fedot.magaz.rest;

import jakarta.validation.Valid;
import md.fedot.magaz.domain.Product;
import md.fedot.magaz.model.ProductDTO;
import md.fedot.magaz.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductResource {

    private final ProductService productService;

    public ProductResource(final ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody @Valid final ProductDTO productDTO) {
        return ResponseEntity.ok(productService.create(productDTO));
    }

}
