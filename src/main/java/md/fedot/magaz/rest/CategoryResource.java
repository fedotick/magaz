package md.fedot.magaz.rest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import md.fedot.magaz.model.CategoryRequestDTO;
import md.fedot.magaz.model.CategoryResponseDTO;
import md.fedot.magaz.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryResource {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getCategories() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategory(@PathVariable final Long id) {
        return ResponseEntity.ok(categoryService.get(id));
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody @Valid final CategoryRequestDTO categoryRequestDTO) {
        return ResponseEntity.ok(categoryService.create(categoryRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable final Long id,
                                                             @RequestBody @Valid final CategoryRequestDTO categoryRequestDTO) {
        return ResponseEntity.ok(categoryService.update(id, categoryRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable final Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
