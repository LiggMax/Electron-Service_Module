import com.ligg.common.utils.BCryptUtil;
import com.ligg.entrance.EntranceApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(classes = EntranceApplication.class)
public class TestBcrypt {



    // 测试加密
    @Test
    public void testEncrypt() {
        String password = "admin";
        String encrypt = BCryptUtil.encrypt(password);
        log.info("encrypt: {}", encrypt);
    }
}
