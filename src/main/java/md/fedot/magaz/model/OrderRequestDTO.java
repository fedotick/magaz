package md.fedot.magaz.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {

    @NotNull
    private Long userId;

    @NotNull
    private List<Long> productIds;

}
