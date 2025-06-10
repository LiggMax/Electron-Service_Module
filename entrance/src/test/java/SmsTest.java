import com.ligg.common.utils.SmsParserUtil;
import com.ligg.common.utils.SmsUtil;
import com.ligg.entrance.EntranceApplication;
import com.ligg.service.common.SmsMassageService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

/**
 * @Author Ligg
 * @Time 2025/6/10
 **/
@Slf4j
@SpringBootTest(classes = EntranceApplication.class)
public class SmsTest {

    @Autowired
    private SmsMassageService smsMassageService;

    /**
     * 短信解析
     */
    @Test
    public void testSmsParser() {
        String sms = "COM5,47567094,【京东】註冊驗證碼：651229。京東客服絕不會索取此驗證碼，切勿轉發或告知他人。COM5,47567094,\u001BRed\u001BYour verification code is 142007, for verification code login, please verify within 5 mins. Do not share the verification code to others.COM5,47567094,[抖音] 4718 is your verification code, valid for 5 minutes.COM15,47548365,[抖音] 7216 is your verification code, valid for 5 minutes.COM15,47548365,【京东】註冊驗證碼：。京東客服絕不會 173532, for verification code login,索取此驗證碼，切勿轉發或告知他人。COM7,47548177,\u001BRed\u001BYour verification code is please verify within 5 mins. Do not share the verification code to others.COM7,47548177,[抖音] 8888 is your verification code, valid for 5 minutes.";
        String keyword = "【京东】註冊驗證碼：";
        int codeLength = 6;
        List<Map<String, String>> maps = SmsUtil.extractSmsContent(sms, keyword, codeLength);
        log.info("{}", maps);
    }

    @Test
    public void testSmsMassageService() {
        String sms = "COM5,47567094,【京东】註冊驗證碼：651229。京東客服絕不會索取此驗證碼，切勿轉發或告知他人。COM5,47567094,\u001BRed\u001BYour verification code is 142007, for verification code login, please verify within 5 mins. Do not share the verification code to others.COM5,47567094,[抖音] 4718 is your verification code, valid for 5 minutes.COM15,47548365,[抖音] 7216 is your verification code, valid for 5 minutes.COM15,47548365,【京东】註冊驗證碼：。京東客服絕不會 173532, for verification code login,索取此驗證碼，切勿轉發或告知他人。COM7,47548177,\u001BRed\u001BYour verification code is please verify within 5 mins. Do not share the verification code to others.COM7,47548177,[抖音] 8888 is your verification code, valid for 5 minutes.";

        List<Map<String, String>> maps = smsMassageService.extractCodeAndSms(sms);

        log.info("结果：" + "{}", maps);

    }
}
