package md.fedot.magaz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import md.fedot.magaz.model.Category;

import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDto {

    private Long id;
    private String name;
    private String description;
    private String createdAt;
    private String updatedAt;

    public CategoryResponseDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        this.createdAt = category.getCreatedAt().format(formatter);
        this.updatedAt = category.getUpdatedAt().format(formatter);
    }

}
