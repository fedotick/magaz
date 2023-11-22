package md.fedot.magaz.service;

import lombok.AllArgsConstructor;
import md.fedot.magaz.domain.Category;
import md.fedot.magaz.model.CategoryRequestDTO;
import md.fedot.magaz.model.CategoryResponseDTO;
import md.fedot.magaz.repos.CategoryRepository;
import md.fedot.magaz.repos.ProductRepository;
import md.fedot.magaz.util.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public List<CategoryResponseDTO> findAll() {
        final List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> mapToDTO(category, new CategoryResponseDTO()))
                .toList();
    }

    public CategoryResponseDTO get(final Long id) {
        return categoryRepository.findById(id)
                .map(category -> mapToDTO(category, new CategoryResponseDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public CategoryResponseDTO create(final CategoryRequestDTO categoryRequestDTO) {
        final Category category = new Category();
        mapToEntity(categoryRequestDTO, category);
        return mapToDTO(categoryRepository.save(category), new CategoryResponseDTO());
    }

    public CategoryResponseDTO update(final Long id, final CategoryRequestDTO categoryRequestDTO) {
        final Category category = categoryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(categoryRequestDTO, category);
        return mapToDTO(categoryRepository.save(category), new CategoryResponseDTO());
    }

    public void delete(final Long id) {
        if (categoryRepository.findById(id).isPresent()) {
            productRepository.deleteAllByCategoryId(id);
        }
        categoryRepository.deleteById(id);
    }

    public CategoryResponseDTO mapToDTO(final Category category, final CategoryResponseDTO categoryResponseDTO) {
        categoryResponseDTO.setId(category.getId());
        categoryResponseDTO.setName(category.getName());
        categoryResponseDTO.setDescription(category.getDescription());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        categoryResponseDTO.setCreatedAt(category.getCreatedAt().format(formatter));
        categoryResponseDTO.setUpdatedAt(category.getUpdatedAt().format(formatter));
        return categoryResponseDTO;
    }

    public Category mapToEntity(final CategoryRequestDTO categoryRequestDTO, final Category category) {
        category.setName(categoryRequestDTO.getName());
        category.setDescription(categoryRequestDTO.getDescription());
        return category;
    }

}
