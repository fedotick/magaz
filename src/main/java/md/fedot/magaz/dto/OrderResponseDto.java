package md.fedot.magaz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import md.fedot.magaz.model.Order;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {

    private Long id;
    private UserResponseDto user;
    private List<ProductResponseDto> products;
    private BigDecimal amount;
    private String createdAt;
    private String updatedAt;

    public OrderResponseDto(Order order) {
        this.id = order.getId();
        this.user = new UserResponseDto(order.getUser());
        this.products = order.getProducts().stream().map(ProductResponseDto::new).toList();
        this.amount = order.getAmount();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        this.createdAt = order.getCreatedAt().format(formatter);
        this.updatedAt = order.getUpdatedAt().format(formatter);
    }

}
