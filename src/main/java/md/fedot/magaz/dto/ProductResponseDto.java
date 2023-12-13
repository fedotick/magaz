package md.fedot.magaz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import md.fedot.magaz.model.Product;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {

    private Long id;
    private String name;
    private String description;
    private byte[] image;
    private BigDecimal price;
    private Short quantity;
    private CategoryResponseDto category;
    private String createdAt;
    private String updatedAt;

    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.image = product.getImage();
        this.price = product.getPrice();
        this.quantity = product.getQuantity();
        this.category = new CategoryResponseDto(product.getCategory());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        this.createdAt = product.getCreatedAt().format(formatter);
        this.updatedAt = product.getUpdatedAt().format(formatter);
    }

}
