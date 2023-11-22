package md.fedot.magaz.model;

import lombok.Data;

@Data
public class CategoryResponseDTO {

    private Long id;
    private String name;
    private String description;
    private String createdAt;
    private String updatedAt;

}
