package md.fedot.magaz.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDto {

    @NotEmpty
    @Size(max = 30)
    private String username;

    @NotEmpty
    @Size(max = 255)
    private String password;

}
