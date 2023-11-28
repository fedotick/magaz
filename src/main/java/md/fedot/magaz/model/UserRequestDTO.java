package md.fedot.magaz.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDTO {

    @NotEmpty
    @Size(max = 30)
    private String username;

    @NotEmpty
    @Size(max = 255)
    private String password;

}
