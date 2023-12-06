package md.fedot.magaz.service;

import lombok.AllArgsConstructor;
import md.fedot.magaz.model.Category;
import md.fedot.magaz.dto.CategoryRequestDto;
import md.fedot.magaz.dto.CategoryResponseDto;
import md.fedot.magaz.repository.CategoryRepository;
import md.fedot.magaz.repository.ProductRepository;
import md.fedot.magaz.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryResponseDto::new)
                .toList();
    }

    public CategoryResponseDto getCategory(Long id) {
        return categoryRepository.findById(id)
                .map(CategoryResponseDto::new)
                .orElseThrow(NotFoundException::new);
    }

    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        Category category = new Category();
        mapToEntity(categoryRequestDto, category);
        return new CategoryResponseDto(categoryRepository.save(category));
    }

    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(categoryRequestDto, category);
        return new CategoryResponseDto(categoryRepository.save(category));
    }

    public void deleteCategory(Long id) {
        if (categoryRepository.findById(id).isPresent()) {
            productRepository.deleteAllByCategoryId(id);
        }
        categoryRepository.deleteById(id);
    }

    public Category mapToEntity(CategoryRequestDto categoryRequestDTO, Category category) {
        category.setName(categoryRequestDTO.getName());
        category.setDescription(categoryRequestDTO.getDescription());
        return category;
    }

}
