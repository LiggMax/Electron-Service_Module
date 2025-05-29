import com.ligg.entrance.EntranceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @Author by Ligg
 * @Time 2025/5/29
 **/
@SpringBootTest(classes = EntranceApplication.class)
public class BigDecimalTest {

    @Test
    public void test() {
        double income1 = 100.0; // 总收入
        BigDecimal income = new BigDecimal(income1);// 收入
        BigDecimal percentage = new BigDecimal("25.00");

        BigDecimal commission = income.multiply(percentage.divide(new BigDecimal("100"), RoundingMode.HALF_UP))
                .setScale(2, RoundingMode.HALF_UP);
        //  计算分成后的剩余金额
        BigDecimal remainingAmount = income.subtract(commission);
        System.out.println(commission);
        System.out.println(remainingAmount);

    }


}
