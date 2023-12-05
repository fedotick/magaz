package md.fedot.magaz.rest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import md.fedot.magaz.model.ProductRequestDTO;
import md.fedot.magaz.model.ProductResponseDTO;
import md.fedot.magaz.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductResource {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable final Long id) {
        return ResponseEntity.ok(productService.get(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@ModelAttribute @Valid ProductRequestDTO productRequestDTO) {
        return ResponseEntity.ok(productService.create(productRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable final Long id,
                                                            @ModelAttribute @Valid ProductRequestDTO productRequestDTO) {
        return ResponseEntity.ok(productService.update(id, productRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable final Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
