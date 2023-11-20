package md.fedot.magaz.service;

import md.fedot.magaz.domain.Category;
import md.fedot.magaz.model.CategoryDTO;
import md.fedot.magaz.model.ProductDTO;
import md.fedot.magaz.repos.CategoryRepository;
import md.fedot.magaz.util.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDTO> findAll() {
        final List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> mapToDTO(category, new CategoryDTO()))
                .toList();
    }

    public CategoryDTO get(final Long id) {
        return categoryRepository.findById(id)
                .map(category -> mapToDTO(category, new CategoryDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public CategoryDTO create(final CategoryDTO categoryDTO) {
        final Category category = new Category();
        mapToEntity(categoryDTO, category);
        return mapToDTO(categoryRepository.save(category), categoryDTO);
    }

    public CategoryDTO update(final Long id, final CategoryDTO categoryDTO) {
        final Category category = categoryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(categoryDTO, category);
        return mapToDTO(categoryRepository.save(category), new CategoryDTO());
    }

    public void delete(final Long id) {
        categoryRepository.deleteById(id);
    }

    public CategoryDTO mapToDTO(final Category category, final CategoryDTO categoryDTO) {
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setDescription(category.getDescription());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        categoryDTO.setCreatedAt(category.getCreatedAt().format(formatter));
        categoryDTO.setUpdatedAt(category.getUpdatedAt().format(formatter));
        return categoryDTO;
    }

    public Category mapToEntity(final CategoryDTO categoryDTO, final Category category) {
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        return category;
    }

}
