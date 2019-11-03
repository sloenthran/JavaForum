package pl.nogacz.forum.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.nogacz.forum.domain.user.Role;
import pl.nogacz.forum.domain.user.UserRole;
import pl.nogacz.forum.service.UserService;

import java.util.ArrayList;

@Component 
public class RoleScheduler {
    private UserService userService;
    private boolean isRunned = false;

    public RoleScheduler(UserService userService) {
        this.userService = userService;
    }

    @Scheduled(cron = "* * * * * *")
    public void checkRoleExists() {
        if(!isRunned) {
            for(Role role : Role.values()) {
                if(this.userService.loadUserRoleByRoleWithoutException(role) == null) {
                    UserRole userRole = new UserRole(null, role, new ArrayList<>());
                    this.userService.saveUserRole(userRole);
                }
            }

            this.isRunned = true;
        }
    }
}
