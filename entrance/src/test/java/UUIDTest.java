import com.ligg.entrance.EntranceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest(classes = EntranceApplication.class)
public class UUIDTest {

    @Test
    public void testUUID() {
        String substring = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 20);
        System.out.println(substring);
    }
}
