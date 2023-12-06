package md.fedot.magaz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import md.fedot.magaz.model.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private Long id;
    private String username;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }

}
