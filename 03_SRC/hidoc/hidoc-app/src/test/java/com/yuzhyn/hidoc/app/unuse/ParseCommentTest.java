package com.yuzhyn.hidoc.app.unuse;

public class ParseCommentTest {
    String comment = "\n" +
            "     * 商品包拆包\n" +
            "     * #场景：数据查询时，使用平台需要写大量SQL语句执行，但是对于普通增删改查，可使用基本方法工具，省去了手写SQL\n" +
            "     * #限制：工具提供动态修改表名的方法，并发时会影响Spring注入的全局对象\n" +
            "     * #关键字：流水，组合商品、拆包\n" +
            "     *\n" +
            "     * <pre>{@code 示例说明\n" +
            "     *      ** 说明 **\n" +
            "     *      ```java\n" +
            "     *      @Component\n" +
            "     *      @HiDbTable(tableName = \"sys_org\",\n" +
            "     *              tableColumns = \"org_id,org_code,p_org_code,org_name,org_help_name,org_modal,org_abbr_name,status,org_level,is_self_org,crt_time,crt_user_id,upd_time\",\n" +
            "     *              primaryKeys = \"org_id\")\n" +
            "     *      public class SysOrgDao extends HiDbModel<SysOrgDao> {\n" +
            "     *          // 其中可包括其他扩展查询方法\n" +
            "     *      }\n" +
            "     *      ```\n" +
            "     * }</pre>\n" +
            "     * <pre>{@code 修改记录\n" +
            "     * 版本         修改时间       修改人         修改内容\n" +
            "     * 7.0.0.1     2021-02-24    zhangsan       CMPS-1891配货池增加selectByIdFUdp\n" +
            "     * }</pre>\n" +
            "     *\n" +
            "     * @param posSalePluList 商品列表\n" +
            "     * @return 拆包后的商品列表\n" +
            "     ";


}
