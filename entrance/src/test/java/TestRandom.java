import com.ligg.entrance.EntranceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

@SpringBootTest(classes = EntranceApplication.class)
public class TestRandom {

    //生成随机数
    @Test
    public void random(){
        // new一个生成随机数对象
        Random random = new java.util.Random();

        Long i = random.nextLong(9000000000L + 1000000000L);
        System.out.println(i);
    }
}
