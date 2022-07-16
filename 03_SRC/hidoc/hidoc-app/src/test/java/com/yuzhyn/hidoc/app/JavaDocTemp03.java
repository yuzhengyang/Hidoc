package com.yuzhyn.hidoc.app;

import org.apache.commons.math3.exception.MathParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库基本方法
 * <p>
 * #场景 数据查询时，使用平台需要写大量SQL语句执行，但是对于普通增删改查，可使用基本方法工具，省去了手写SQL
 * 使用时需配合HiDevTools工具，更方便的生成基础代码
 * 工具已更新至SVN
 * <p>
 * #限制 工具提供动态修改表名的方法，并发时会影响Spring注入的全局对象
 * <p>
 * #关键字 数据库操作，数据库查询，DAO，增删改查
 * #目录 公共/数据库/基本方法
 *
 * <pre>{@code 示例说明
 *     Dao类创建示例
 *     ```java
 *     @Component
 *     @HiDbTable(tableName = "sys_org",
 *             tableColumns = "org_id,org_code,p_org_code,org_name,org_help_name,org_modal,org_abbr_name,status,org_level,is_self_org,crt_time,crt_user_id,upd_time",
 *             primaryKeys = "org_id")
 *     public class SysOrgDao extends HiDbModel<SysOrgDao> {
 *         // 其中可包括其他扩展查询方法
 *
 *     }
 *     ```
 *     Service类中使用声明
 *     ```java
 *     @Autowired
 *     SysOrgDao sysOrgDao;
 *     ```
 *
 *     **乐观锁支持**
 *     支持对更新处理时的乐观锁自动处理，使用方法如下：
 *     第一步，在 @HiDbTable 中增加字段信息
 *     1、在 tableColumns 中增加乐观锁字段名称
 *     2、在 version 中增加乐观锁字段名称
 *     ```java
 *     @Component
 *     @HiDbTable(tableName = "order_dis_wait_req",
 *             tableColumns = "order_dis_wait_req_id,.........,version", //该表乐观锁字段为version
 *             primaryKeys = "order_dis_wait_req_id",
 *             version = "version") //该表乐观锁字段为version
 *     ```
 *     第二步，调用更新时，乐观锁字段要赋值当前记录的版本号
 *     完成上述两步，即可使用自动处理的乐观锁功能。
 *
 *     注意：如果乐观锁冲突，会抛出异常提示
 * }</pre>
 * <p>
 * 版本         修改时间       修改人         修改内容
 * 7.0.0.1     2021-01-07    yuzhengyang    调整
 * 7.0.0.2     2021-02-24    fanxueping     CMPS-1891配货池增加selectByIdFUdp
 */
public class JavaDocTemp03 {

    /**
     * 构造方法
     * <p>
     * #场景：构造方法，推荐使用 @Autowired 注入创建时使用
     * <p>
     * #限制：未传入数据连接，查询时需要使用带数据连接参数的方法
     * 这种构造创建后，禁止使用修改表名功能
     * #关键字：构造方法，自动注入，Autowired
     *
     * <pre>{@code 示例说明
     *     ```java
     *     @Autowired
     *     private ComItemDao comItemDao;
     *     ```
     * }</pre>
     */
    public JavaDocTemp03() {

    }


    /**
     * 商品包拆包
     * #场景：数据查询时，使用平台需要写大量SQL语句执行，但是对于普通增删改查，可使用基本方法工具，省去了手写SQL
     *
     * @param posSalePluList 商品列表
     *                       传入参数不能为空
     * @return 返回商品名称列表
     * @throws CloneNotSupportedException 不支持复制
     * @throws IOException                输出错误
     */
    public List<String> pluGroupSplit2(List<String> posSalePluList) throws CloneNotSupportedException, IOException {
        List<String> posSalePluSplitedList = new ArrayList<>();
        if (System.currentTimeMillis() > 95561156) {
            throw new CloneNotSupportedException("时间错误");
        }

        if (System.currentTimeMillis() > 95561156) {
            throw new IOException("不能为空");
        }

        return posSalePluSplitedList;
    }


    public static void main(String[] args) {
//        JavaDocTemp03.pluGroupSplit2()
    }
}
