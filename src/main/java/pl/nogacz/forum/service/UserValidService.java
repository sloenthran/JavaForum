package pl.nogacz.forum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.nogacz.forum.exception.validation.EmailExistException;
import pl.nogacz.forum.exception.validation.PasswordTooShortException;
import pl.nogacz.forum.exception.validation.UsernameExistException;
import pl.nogacz.forum.repository.user.UserRepository;
import pl.nogacz.forum.util.email.validate.EmailValidate;

import javax.transaction.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class UserValidService {
    private UserRepository userRepository;
    private EmailValidate emailValidate;

    public void validEmail(String email) throws Exception {
        this.emailValidate.validEmail(email);

        if(this.userRepository.existsByEmail(email)) {
            throw new EmailExistException();
        }
    }

    public void validPassword(String password) throws Exception {
        if(password.length() < 6) {
            throw new PasswordTooShortException();
        }
    }

    public void validUsername(String username) throws Exception {
        if(this.userRepository.existsByUsername(username)) {
            throw new UsernameExistException();
        }
    }
}
