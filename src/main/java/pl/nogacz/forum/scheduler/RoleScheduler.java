package pl.nogacz.forum.scheduler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.nogacz.forum.domain.user.Role;
import pl.nogacz.forum.domain.user.UserRole;
import pl.nogacz.forum.service.UserRoleService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

@Component
@AllArgsConstructor
public class RoleScheduler {
    private UserRoleService userRoleService;

    @PostConstruct
    public void checkRoleExists() {
        for(Role role : Role.values()) {
            if(this.userRoleService.loadUserRoleByRoleWithoutException(role) == null) {
                UserRole userRole = new UserRole(null, role, new ArrayList<>());
                this.userRoleService.saveUserRole(userRole);
            }
        }
    }
}
