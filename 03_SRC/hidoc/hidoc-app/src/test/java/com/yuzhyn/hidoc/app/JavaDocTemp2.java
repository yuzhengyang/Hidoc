package com.yuzhyn.hidoc.app;


import java.util.ArrayList;
import java.util.List;

/**
 * 数据库基本方法
 * #场景：数据查询时，使用平台需要写大量SQL语句执行，但是对于普通增删改查，可使用基本方法工具，省去了手写SQL
 * #限制：工具提供动态修改表名的方法，并发时会影响Spring注入的全局对象
 * #关键字：流水，组合商品、拆包
 *
 * <pre>{@code 示例说明
 *     Dao类创建**示例**
 *     ```java
 *     @Component
 *     @HiDbTable(tableName = "sys_org",
 *             tableColumns = "org_id,org_code,p_org_code,org_name,org_help_name,org_modal,org_abbr_name,status,org_level,is_self_org,crt_time,crt_user_id,upd_time",
 *             primaryKeys = "org_id")
 *     public class SysOrgDao extends HiDbModel<SysOrgDao> {
 *         // 其中可包括其他扩展查询方法
 * <p>
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
 * <pre>{@code 修改记录
 * 版本         修改时间       修改人         修改内容
 * 7.0.0.1     2021-02-24    zhangsan       CMPS-1891配货池增加selectByIdFUdp
 * }</pre>
 */
public class JavaDocTemp2 {


    /**
     * 商品包拆包
     * #场景：数据查询时，使用平台需要写大量SQL语句执行，但是对于普通增删改查，可使用基本方法工具，省去了手写SQL
     * #限制：工具提供动态修改表名的方法，并发时会影响Spring注入的全局对象
     * #关键字：流水，组合商品、拆包
     *
     * <pre>{@code 示例说明
     *      ** 说明 **
     *      ```java
     *      @Component
     *      @HiDbTable(tableName = "sys_org",
     *              tableColumns = "org_id,org_code,p_org_code,org_name,org_help_name,org_modal,org_abbr_name,status,org_level,is_self_org,crt_time,crt_user_id,upd_time",
     *              primaryKeys = "org_id")
     *      public class SysOrgDao extends HiDbModel<SysOrgDao> {
     *          // 其中可包括其他扩展查询方法
     *      }
     *      ```
     * }</pre>
     * <pre>{@code 修改记录
     * 版本         修改时间       修改人         修改内容
     * 7.0.0.1     2021-02-24    zhangsan       CMPS-1891配货池增加selectByIdFUdp
     * }</pre>
     *
     * @param posSalePluList 商品列表
     * @return 拆包后的商品列表
     */
    public List<String> pluGroupSplit(List<String> posSalePluList) {
        List<String> posSalePluSplitedList = new ArrayList<>();
        return posSalePluSplitedList;
    }
}