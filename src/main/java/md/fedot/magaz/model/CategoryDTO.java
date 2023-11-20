package md.fedot.magaz.model;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {

    private Long id;

    @NotEmpty
    @Size(max = 50)
    private String name;

    private String description;

    @Transient
    private String createdAt;

    @Transient
    private String updatedAt;

}
