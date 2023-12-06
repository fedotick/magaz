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

    public List<CategoryResponseDto> findAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> mapToDTO(category, new CategoryResponseDto()))
                .toList();
    }

    public CategoryResponseDto getCategory(Long id) {
        return categoryRepository.findById(id)
                .map(category -> mapToDTO(category, new CategoryResponseDto()))
                .orElseThrow(NotFoundException::new);
    }

    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        Category category = new Category();
        mapToEntity(categoryRequestDto, category);
        return mapToDTO(categoryRepository.save(category), new CategoryResponseDto());
    }

    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto categoryRequestDto) {
        final Category category = categoryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(categoryRequestDto, category);
        return mapToDTO(categoryRepository.save(category), new CategoryResponseDto());
    }

    public void deleteCategory(Long id) {
        if (categoryRepository.findById(id).isPresent()) {
            productRepository.deleteAllByCategoryId(id);
        }
        categoryRepository.deleteById(id);
    }

    public CategoryResponseDto mapToDTO(Category category, CategoryResponseDto categoryResponseDto) {
        categoryResponseDto.setId(category.getId());
        categoryResponseDto.setName(category.getName());
        categoryResponseDto.setDescription(category.getDescription());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        categoryResponseDto.setCreatedAt(category.getCreatedAt().format(formatter));
        categoryResponseDto.setUpdatedAt(category.getUpdatedAt().format(formatter));
        return categoryResponseDto;
    }

    public Category mapToEntity(CategoryRequestDto categoryRequestDTO, Category category) {
        category.setName(categoryRequestDTO.getName());
        category.setDescription(categoryRequestDTO.getDescription());
        return category;
    }

}
