import com.ligg.common.dto.PhoneAndProjectDto;
import com.ligg.common.dto.PhoneDetailDto;
import com.ligg.common.entity.PhoneEntity;
import com.ligg.common.entity.ProjectEntity;
import com.ligg.entrance.EntranceApplication;
import com.ligg.mapper.PhoneNumberMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSON;

@SpringBootTest(classes = EntranceApplication.class)
public class PhoneTest {

    @Autowired
    private PhoneNumberMapper phoneNumberMapper;


    /**
     * 查询号码详情
     */
    @Test
    public void getPhoneDetailTest() {
        // 假设存在ID为1的号码
        Long phoneId = 128L;
        
        // 获取号码基本信息
        PhoneDetailDto phoneDetail = phoneNumberMapper.getPhoneDetail(phoneId);
        
        // 获取号码关联的项目信息
        List<ProjectEntity> projects = phoneNumberMapper.getPhoneProject(phoneId);
        
        // 创建嵌套的结构
        Map<String, Object> result = new HashMap<>();
        
        // 基本信息作为一个对象
        Map<String, Object> basicInfo = new HashMap<>();
        basicInfo.put("phoneId", phoneDetail.getPhoneId());
        basicInfo.put("phoneNumber", phoneDetail.getPhoneNumber());
        basicInfo.put("countryCode", phoneDetail.getCountryCode());
        basicInfo.put("lineStatus", phoneDetail.getLineStatus());
        basicInfo.put("usageStatus", phoneDetail.getUsageStatus());
        basicInfo.put("registrationTime", phoneDetail.getRegistrationTime());
        
        // 项目信息作为列表
        result.put("basicInfo", basicInfo);
        result.put("projects", projects);
        
        // 输出JSON格式
        System.out.println("嵌套JSON结构:");
        System.out.println(JSON.toJSONString(result, true));
    }
    
    /**
     * 使用MyBatis内置的嵌套结果映射查询号码详情
     */
    @Test
    public void getPhoneWithProjectsTest() {
        // 假设存在ID为1的号码
        Long phoneId = 128L;
        
        // 获取手机号信息及其关联的项目（使用嵌套结构）
        Map<String, Object> phoneWithProjects = phoneNumberMapper.getPhoneWithProjects(phoneId);
        
        // 输出JSON格式
        System.out.println("MyBatis嵌套JSON结构:");
        System.out.println(JSON.toJSONString(phoneWithProjects, true));
    }
    
    /**
     * 使用PhoneAndProjectDto对象获取嵌套的手机号和项目信息
     */
    @Test
    public void getPhoneAndProjectDtoTest() {
        // 假设存在ID为1的号码
        Long phoneId = 128L;
        
        // 使用DTO直接获取嵌套结构
        PhoneAndProjectDto phoneAndProject = phoneNumberMapper.getPhoneAndProject(phoneId);
        
        // 输出JSON格式
        System.out.println("基于DTO的嵌套JSON结构:");
        System.out.println(JSON.toJSONString(phoneAndProject, true));
    }
}
