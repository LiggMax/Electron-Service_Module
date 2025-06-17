import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.ligg.common.entity.PhoneEntity;
import com.ligg.common.entity.user.UserEntity;
import com.ligg.common.vo.CustomerBillVo;
import com.ligg.common.vo.PageVo;
import com.ligg.entrance.EntranceApplication;
import com.ligg.mapper.PhoneNumberMapper;
import com.ligg.mapper.adminweb.CustomerBillMapper;
import com.ligg.mapper.user.UserMapper;
import com.ligg.service.common.PhoneNumberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Author Ligg
 * @Time 2025/6/17
 **/
@SpringBootTest(classes = EntranceApplication.class)
public class MybatisPlusPage {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PhoneNumberService phoneNumberService;

    @Autowired
    private CustomerBillMapper customerBillMapper;

    @Test
    public void testPage() {
        IPage<UserEntity> page = new Page<>(1, 5);
        IPage<UserEntity> iPage = userMapper.selectPage(page, null);
        System.out.println(iPage.getRecords());
        System.out.println(iPage.getPages());
        System.out.println(iPage.getTotal());
    }

    @Test
    public void testPage2() {
        IPage<PhoneEntity> page = new Page<>(1, 2);
        IPage<PhoneEntity> iPage = phoneNumberService.page(page, new LambdaQueryWrapper<PhoneEntity>()
                .orderByDesc(PhoneEntity::getRegistrationTime));
        System.out.println(iPage.getRecords());
        System.out.println(iPage.getPages());
        System.out.println(iPage.getTotal());
    }

    //自定义分页查询
    @Test
    public void testPage3() {

    }
}
