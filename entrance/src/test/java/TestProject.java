import com.ligg.common.dto.ProjectListDto;
import com.ligg.common.dto.RegionCommodityDto;
import com.ligg.entrance.EntranceApplication;
import com.ligg.mapper.ProjectMapper;
import com.ligg.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@Slf4j
@SpringBootTest(classes = EntranceApplication.class)
public class TestProject {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectMapper projectMapper;
//    @Test
//    public void ProjectListTest(){
//
//       String name = "中国香港";
//
//        Integer projectCountByName = projectMapper.getProjectCountByName(name);
//        log.info("数量:{}",projectCountByName);
//    }

    //获取项目列表
    @Test
    public void getAllProjectListTest(){

        List<ProjectListDto> allProjectsList = projectMapper.getAllProjectsList();
        System.out.println(allProjectsList);

    }

    //获取项目购买列表
    @Test
    public void getProjectCommodityListTest(){
        Integer projectId = 2;
        List<RegionCommodityDto> projectCommodityList = projectMapper.getProjectCommodityList(projectId);
        System.out.println(projectCommodityList);

    }
}
