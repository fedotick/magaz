package md.fedot.magaz.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequestDTO {

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

    private Long category;

}
