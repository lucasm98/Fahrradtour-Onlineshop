package biketour;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

/**
 * base class for integration tests bootstrapping the core {@Link biketour} configuration class
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Transactional
public class AbstractIntegrationTests {
}
