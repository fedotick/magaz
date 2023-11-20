package md.fedot.magaz.model;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProductDTO {

    private Long id;

    @NotEmpty
    @Size(max = 50)
    private String name;

    private String description;

    @NotEmpty
    private String image;

    @NotNull
    @Min(value = 0)
    @DecimalMax(value = "1000000", inclusive = false)
    private BigDecimal price;

    @NotNull
    @Min(value = 0)
    @Max(value = 32767)
    private Short quantity;

    @NotNull
    private Long category;

    @Transient
    private String createdAt;

    @Transient
    private String updatedAt;

}
