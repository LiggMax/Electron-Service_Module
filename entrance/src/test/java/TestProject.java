import com.ligg.entrance.EntranceApplication;
import com.ligg.mapper.ProjectMapper;
import com.ligg.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@Slf4j
@SpringBootTest(classes = EntranceApplication.class)
public class TestProject {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectMapper projectMapper;
    @Test
    public void ProjectListTest(){

       String name = "中国香港";

        Integer projectCountByName = projectMapper.getProjectCountByName(name);
        log.info("数量:{}",projectCountByName);
    }
}
