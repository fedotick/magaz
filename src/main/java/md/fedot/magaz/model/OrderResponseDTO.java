package md.fedot.magaz.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderResponseDTO {

    private Long id;
    private Long userId;
    private List<Long> productIds;
    private BigDecimal amount;
    private String createdAt;

}
