import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ligg.common.entity.PhoneEntity;
import com.ligg.common.entity.user.UserEntity;
import com.ligg.common.vo.CustomerBillVo;
import com.ligg.entrance.EntranceApplication;
import com.ligg.mapper.adminweb.CustomerBillMapper;
import com.ligg.mapper.user.UserMapper;
import com.ligg.service.common.PhoneNumberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.function.Function;

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

    // 测试通用分页方法
    @Test
    public void testGenericPage() {
        Long pageNum = 1L;
        Long pageSize = 5L;

        // 使用泛型方式测试任意分页查询
        IPage<UserEntity> userPage = queryForPage(pageNum, pageSize, (p) -> userMapper.selectPage(p, null));
        printPageResult(userPage, "UserEntity 分页结果");

        IPage<PhoneEntity> phonePage = queryForPage(pageNum, pageSize, (p) -> phoneNumberService.page(p, null));
        printPageResult(phonePage, "PhoneEntity 分页结果");

        IPage<CustomerBillVo> billPage = queryForPage(pageNum, pageSize, page -> customerBillMapper.selectCustomersBill());
        printPageResult(billPage, "CustomerBillVo 分页结果");
    }

    // 通用分页查询方法
    private <T> IPage<T> queryForPage(Long pageNum, Long pageSize, Function<Page<T>, IPage<T>> queryFunc) {
        Page<T> page = new Page<>(pageNum, pageSize);
        return queryFunc.apply(page);
    }

    // 打印分页结果
    private <T> void printPageResult(IPage<T> page, String title) {
        System.out.println("=== " + title + " ===");
        System.out.println("当前页: " + page.getCurrent());
        System.out.println("每页数量: " + page.getSize());
        System.out.println("总记录数: " + page.getTotal());
        System.out.println("总页数: " + page.getPages());
        System.out.println("数据列表:");
        page.getRecords().forEach(System.out::println);
    }

    // 原有测试方法保持不变
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

    // 自定义分页查询
    @Test
    public void testPage3() {
        long pageNum = 1L;
        long pageSize = 5L;
        IPage<CustomerBillVo> iPage = customerBillMapper.selectCustomersBill();

        System.out.println(iPage.getRecords());
        System.out.println(iPage.getPages());
        System.out.println(iPage.getTotal());
    }
}
