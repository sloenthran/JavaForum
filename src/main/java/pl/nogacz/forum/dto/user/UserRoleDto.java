package pl.nogacz.forum.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.nogacz.forum.domain.user.Role;

@Data
@AllArgsConstructor
public class UserRoleDto {
    private Long id;
    private Role role;
}
