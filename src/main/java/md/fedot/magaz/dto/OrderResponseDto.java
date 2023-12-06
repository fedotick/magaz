package md.fedot.magaz.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderResponseDto {

    private Long id;
    private Long userId;
    private List<Long> productIds;
    private BigDecimal amount;
    private String createdAt;

}
