package md.fedot.magaz.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponseDto {

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
