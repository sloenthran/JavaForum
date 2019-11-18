package pl.nogacz.forum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.nogacz.forum.domain.Log;
import pl.nogacz.forum.domain.user.User;
import pl.nogacz.forum.repository.LogRepository;

import javax.transaction.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class LogService {
    private LogRepository logRepository;

    public void addLog(User user, String message) {
        Log log = Log.builder()
                .user(user)
                .message(message)
                .build();

        this.logRepository.save(log);
    }
}
