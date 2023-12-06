package md.fedot.magaz.dto;

import lombok.Data;

@Data
public class CategoryResponseDto {

    private Long id;
    private String name;
    private String description;
    private String createdAt;
    private String updatedAt;

}
