import com.ligg.common.utils.http.HttpRequestUtil;
import com.ligg.entrance.EntranceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author Ligg
 * @Time 2025/6/16
 **/
@SpringBootTest(classes = EntranceApplication.class)
public class CrawlerText {

    @Test
    public void testCrawlerBangumi() {
        String url = "https://bangumi.tv/anime/browser/?sort=trends";
        try {
            String content = HttpRequestUtil.sendGetRequest(url);
            System.out.println("网页内容: " + content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
