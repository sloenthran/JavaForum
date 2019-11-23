package pl.nogacz.forum.util.email.validate;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.nogacz.forum.exception.validation.BadEmailException;
import pl.nogacz.forum.exception.validation.EmailDisposableException;
import pl.nogacz.forum.exception.validation.EmailDomainNotFound;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class EmailValidateTests {
    @Autowired
    private EmailValidate emailValidate;

    @Test
    public void validEmail() throws Exception {
        //Given
        String email = "sloenthran@gmail.com";

        //When
        boolean valid = this.emailValidate.validEmail(email);

        //Then
        Assert.assertTrue(valid);
    }

    @Test(expected = EmailDisposableException.class)
    public void validDisposableEmail() throws Exception {
        //Given
        String email = "sloenthran@10minutemail.com";

        //When
        boolean valid = this.emailValidate.validEmail(email);

        //Then
        Assert.assertFalse(valid);
    }

    @Test(expected = BadEmailException.class)
    public void validBadEmail() throws Exception {
        //Given
        String email = "sloenthran@10minutemail";

        //When
        boolean valid = this.emailValidate.validEmail(email);

        //Then
        Assert.assertFalse(valid);
    }

    @Test(expected = EmailDomainNotFound.class)
    public void validNotExistedDomainEmail() throws Exception {
        //Given
        String email = "sloenthran@10minutemailnsdjadjasndasndjasndjasndsanjdjqnwjds.com";

        //When
        boolean valid = this.emailValidate.validEmail(email);

        //Then
        Assert.assertFalse(valid);
    }
}