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
     * 使用PhoneAndProjectDto对象获取嵌套的手机号和项目信息
     */
    @Test
    public void getPhoneAndProjectDtoTest() {
        // 假设存在ID为1的号码
        Long phoneId = 128L;
        Long adminUserId = 1922883816214745090L;
        
        // 使用DTO直接获取嵌套结构
        PhoneAndProjectDto phoneAndProject = phoneNumberMapper.getPhoneAndProject(phoneId,adminUserId);
        
        // 输出JSON格式
        System.out.println("基于DTO的嵌套JSON结构:");
        System.out.println(JSON.toJSONString(phoneAndProject, true));
    }
}
