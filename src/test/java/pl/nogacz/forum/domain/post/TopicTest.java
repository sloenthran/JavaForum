package pl.nogacz.forum.domain.post;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class TopicTest {
    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Topic.class)
                .suppress(Warning.STRICT_INHERITANCE)
                .verify();
    }
}