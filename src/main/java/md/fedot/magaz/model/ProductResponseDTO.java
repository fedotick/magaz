package md.fedot.magaz.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponseDTO {

    private Long id;
    private String name;
    private String description;
    private byte[] image;
    private BigDecimal price;
    private Short quantity;
    private String category;
    private String createdAt;
    private String updatedAt;

}
