import com.ligg.common.dto.ProjectListDto;
import com.ligg.common.entity.PhoneEntity;
import com.ligg.entrance.EntranceApplication;
import com.ligg.service.ProjectService;
import com.ligg.service.impl.PhoneNumberServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = EntranceApplication.class)
public class TestProject {

    @Autowired
    private ProjectService projectService;

    @Test
    public void ProjectListTest(){

        List<ProjectListDto> allProjects = projectService.getAllProjects();
        System.out.println(allProjects);

    }
}
