package pl.nogacz.forum.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserChangePasswordDto {
    private String oldPassword;
    private String newPassword;
}
