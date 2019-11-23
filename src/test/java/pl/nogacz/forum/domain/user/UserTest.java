package pl.nogacz.forum.domain.user;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class UserTest {
    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(User.class)
                .suppress(Warning.STRICT_INHERITANCE)
                .verify();
    }
}