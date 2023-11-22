package md.fedot.magaz.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequestDTO {

    @NotEmpty
    @Size(max = 50)
    private String name;

    private String description;

}
