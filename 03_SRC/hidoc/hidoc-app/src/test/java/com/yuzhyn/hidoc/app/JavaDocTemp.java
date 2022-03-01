package com.yuzhyn.hidoc.app;

/**
 * <div javadoc="info" javadoc-cn="一句话精简概括">
 *     数据库基本方法
 * </div>
 *
 * <div javadoc="scene" javadoc-cn="使用场景说明">
 *     数据查询时，使用平台需要写大量SQL语句执行，但是对于普通增删改查，可使用基本方法工具，省去了手写SQL
 * </div>
 *
 * <div javadoc="limit" javadoc-cn="使用限制说明">
 *     工具提供动态修改表名的方法，并发时会影响Spring注入的全局对象
 * </div>
 *
 * <div javadoc="example" javadoc-cn="使用代码示例">
 *     Dao类创建示例
 *     ```java
 *     @Component
 *     @HiDbTable(tableName = "sys_org",
 *             tableColumns = "org_id,org_code,p_org_code,org_name,org_help_name,org_modal,org_abbr_name,status,org_level,is_self_org,crt_time,crt_user_id,upd_time",
 *             primaryKeys = "org_id")
 *     public class SysOrgDao extends HiDbModel<SysOrgDao> {
 *         // 其中可包括其他扩展查询方法
 *     }
 *     ```
 * <p>
 *     Service类中使用声明
 *     ```java
 *     @Autowired
 *     SysOrgDao sysOrgDao;
 *     ```
 * </div>
 *
 * <div javadoc="log" javadoc-cn="修改日志">
 * 版本         修改时间       修改人         修改内容
 * 7.0.0.1     2021-02-24    zhangsan       CMPS-1891配货池增加selectByIdFUdp
 * </div>
 */

public abstract class JavaDocTemp {

}