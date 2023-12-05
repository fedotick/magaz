package md.fedot.magaz.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    @NotEmpty
    @Size(max = 30)
    private String username;

    @NotEmpty
    @Size(max = 255)
    private String password;

}
