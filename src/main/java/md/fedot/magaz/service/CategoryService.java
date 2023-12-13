package md.fedot.magaz.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fedot.magaz.dto.CategoryRequestDto;
import md.fedot.magaz.dto.CategoryResponseDto;
import md.fedot.magaz.exception.NotFoundException;
import md.fedot.magaz.model.Category;
import md.fedot.magaz.repository.CategoryRepository;
import md.fedot.magaz.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public List<CategoryResponseDto> getAllCategories() {
        log.info("Getting all categories...");

        List<CategoryResponseDto> categoryResponseDtos = categoryRepository.findAll()
                .stream()
                .map(CategoryResponseDto::new)
                .toList();

        log.info("Found {} categories", categoryResponseDtos.size());

        return categoryResponseDtos;
    }

    public CategoryResponseDto getCategory(Long id) {
        log.info("Getting category with ID: " + id);

        CategoryResponseDto categoryResponseDto = categoryRepository.findById(id)
                .map(CategoryResponseDto::new)
                .orElseThrow(() -> {
                    log.warn("Category not found");
                    return new NotFoundException("Category not found");
                });

        log.info("Found category: " + categoryResponseDto);

        return categoryResponseDto;
    }

    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        log.info("Creating category with data: " + categoryRequestDto);

        Category category = mapToEntity(categoryRequestDto, new Category());

        try {
            Category savedCategory = categoryRepository.save(category);
            log.info("Category created with ID: " + savedCategory.getId());
        } catch (Exception e) {
            log.warn("Category was not created: " + e.getMessage());
        }

        return new CategoryResponseDto(category);
    }

    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto categoryRequestDto) {
        log.info("Updating category with ID: " + id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Category not found");
                    return new NotFoundException("Category not found");
                });

        mapToEntity(categoryRequestDto, category);

        try {
            categoryRepository.save(category);
            log.info("Category updated");
        } catch (Exception e) {
            log.error("Category was not updated: " + e.getMessage());
        }

        return new CategoryResponseDto(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        log.info("Deleting category with ID: " + id);

        categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Category not found");
                    return new NotFoundException("Category not found");
                });

        log.info("Updating all product categories to null for categoryId: " + id);
        try {
            productRepository.updateAllCategoryIdToNullByCategoryId(id);
            log.info("Updated all product categories to null");
        } catch (Exception e) {
            log.error("Error updating product categories to null: " + e.getMessage());
        }

        try {
            categoryRepository.deleteById(id);
            log.info("Category deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting category: " + e.getMessage());
        }
    }

    public Category mapToEntity(CategoryRequestDto categoryRequestDTO, Category category) {
        category.setName(categoryRequestDTO.getName());
        category.setDescription(categoryRequestDTO.getDescription());
        return category;
    }

}
