package com.yuzhyn.hidoc.app.common.enums;

public enum ResponseCode {

    SUCCESS(0, "成功"),

    // 系统相关[9900, 9999]
    UNKONW_ERROR(9900, "未知错误"),
    SYSTEM_ERROR(9901, "系统异常"),
    SERVICE_ERROR(9902, "服务异常"),
    SERVICE_INTERFACE_PARAM_ERROR(9903, "接口参数异常"),
    PARAM_TYPE_ERROR(9904, "参数错误"),
    PARAM_NULL_ERROR(9905, "参数不能为空"),
    PARAM_VALUE_ERROR(9906, "参数值传入错误"),
    SERVICE_INTERFACE_MAYBE_NOT_EXIST(9907, "接口异常，请检查接口是否存在"),
    UNSUPPORTED_ENCODING_EXCEPTION(9908, "消息编码异常"),
    SYS_GLOBAL_ERROR(9907, "访问服务异常"),
    SYS_REDIS_ERROR(9908, "访问服务异常"),
    SYS_ILLEGAL_REQUEST_ERROR(9909, "请求不合法"),
    SYS_DECRYPT_MSG_ERROR(9910, "用户名或密码校验失败"),
    SYS_URL_PERMISS_EXIST_NULL_ERROR(9911, "请求的url地址存在null异常, 可能是数据库配置问题"),
    SYS_SESSION_EXPIRE_TIME_ERROR(9912, "session的过期时间配置需大于0"),
    REDIS_LOCK_KEY_ERROR(9913, "redis锁定key失败"),

    // 用户部分, USER_开头定义变量 [1000, 1200]
    USER_UNLOGIN_ERROR(1000, "用户名或密码错误"),
    USER_NOT_ENABLE(1001, "用户未启用"),
    USER_LOCKED(1002, "用户被锁定"),
    USER_FORBIDDEN(1003, "用户被禁用"),
    USER_DELETED(1004, "用户被删除"),
    USER_NOT_EXIST(1005, "用户不存在"),
    USER_SESSION_NOT_EXIST(1006, "session不存在"),
    USER_SESSION_NOT_ERROR(1007, "session验证错误"),
    USER_RETRY_LOGIN(1008, "登录过期，请重新登录"),
    USER_NO_AUTHORITY(1009, "没有操作权限，请联系管理员"),
    USER_EXISTED(1010, "用户名已存在"),
    USER_CAN_NOT_MODIFY(1011, "不允许修改超级管理员账号"),
    USER_CAN_NOT_DEL_SUPER(1012, "不允许删除超级管理员账号"),
    USER_CAN_NOT_DEL_SYSTEM(1013, "不允许删除系统管理员账号"),
    USER_LOGIN_OA_ERROR(1015, "通过OA认证失败, 用户名或密码错误"),
    USER_LOGIN_OA_EXCEPTION(1016, "通过OA认证异常, 请联系管理员"),
    USER_LOGIN_OA_NULL(1017, "通过OA认证失败, 返回body为空值"),
    USER_LOGIN_OA_PHONE_ERROR(1018, "通过OA认证返回手机号异常, 请联系管理员"),
    OA_USER_NOT_IN_SYSTEM(1019, "OA用户未添加到系统中，请联系管理员"),
    USER_LOGOUT_SUCCESS(1020, "退出登录成功"),

    // 菜单部分[1201, 1300]
    PARAM_NAME_ERROR(1201, "菜单名称重复"),
    PARAM_URL_ERROR(1202, "菜单地址重复"),
    PARAM_NAMEURL_ERROR(1203, "菜单名称和菜单地址重复"),
    ILLEGAL_ARGUMENT(500, "exception.illegal.argument"),
    ILLEGAL_GENERATOR(500, "exception.illegal.generatorId"),
    WAREHOUSE_lOCK_FAILED(16001, "库存锁定失败"),

    // 微服务部分--商品服务[10000, 12000]
    PLU_PACKET_EXIST_SAME_UNIT(10000, "商品包装名称不能相同"),
    PLU_CLS_ERROR(10001, "设置商品时,至少选择三级以上品类"),
    PLU_CODE_EXIST(10002, "商品编码已存在"),
    PLU_BARCODE_EXIST(10003, "商品条码已存在"),
    PLU_DATA_STATUS_IS_STOP(10004, "该商品未启用，不可使用"),
    PLU_NOT_IN_ORG_PLU(10005, "商品不在该组织经营范围下，不可使用"),
    PLU_NOT_IS_PUR(10006, "组织经营范围内该商品不允许进货，不可使用"),
    PLU_NOT_IS_PUR_RTN(10006, "组织经营范围内该商品不允许进货退回，不可使用"),
    PLU_NOT_IS_MGR_WH_STK(10007, "组织经营范围内该商品不允许管理库存，不可使用"),
    PLU_PACKET_NUM_IS_NULL(10008, "商品包装数量不能为空"),
    PLU_PACKET_NUM_NEED_MORE_THAN_ZERO(10009, "包装数量必须大于零"),
    PLU_PACKET_NUM_IS_INVALID(10010, "售价方式为01-计件销售商品，包装数量必须为正整数"),
    PLU_LIST_IS_EMPTY(10011, "商品明细不能为空"),
    PLU_IS_SAME(10012, "录入商品重复，请重新输入"),
    PLU_PUR_IN_PRICE_CAN_NOT_BE_NULL(10013, "商品采购含税进价不能为空"),
    PLU_PUR_IN_PRICE_CAN_NOT_BE_ZERO(10013, "商品采购含税进价不能为0"),
    PLU_PUR_IN_PRICE_CAN_NOT_LESS_THAN_ZERO(10014, "商品采购含税进价不能为负"),
    PLU_PUR_IN_PRICE_MUST_BE_ZERO(10015, "商品有赠品时,采购含税进价必须为0"),
    PLU_PUR_IN_PRICE_CAN_NOT_MORE_THAN_IN_TAX_PRICE(10016, "为固定价不能大于合同进价"),
    PLU_PUR_IN_PRICE_CAN_NOT_MORE_THAN_CTRL_MAX_PRICE(10016, " 为自由价不能大于受控上线价"),
    PLU_NOT_IS_DIS_RTN(10017, "组织经营范围内该商品不允许配送退货，不可使用"),
    PLU_SCATTERED_NUM_IS_INVALID(10018, "售价方式为01-计件销售商品，零散数量必须为正整数"),
    IN_TAX_PRICE_CONNOT_LESS_THAN_CNT_OFFER_PRICE(10019, "合同定价中该商品是自由价，含税价不能低于合同受控下限价。"),
    PLU_NOT_IS_DIS(10020, "组织经营范围内该商品不允许配送，不可使用"),
    PLU_DIS_IN_PRICE_CAN_NOT_BE_NULL(10021, "商品含税配送价不能为空"),
    PLU_DIS_IN_PRICE_CAN_NOT_BE_ZERO(10022, "商品含税配送价不能为0"),
    PLU_DIS_IN_PRICE_CAN_NOT_LESS_THAN_ZERO(10023, "商品含税配送价不能为负"),
    PLU_DIS_IN_PRICE_MUST_BE_ZERO(10015, "商品有赠品时,含税配送价必须为0"),

    // 微服务部分--采购服务[12001, 14000]
    CNT_STATUS_ISNOTCARRY(13001,"合同未执行，不能使用"),
    CNT_US_ORG_NOT_HAVE_RECORG(13002, "合同的使用组织中不包含该组织，不可用"),
    CNT_OFFER_NOT_HAVE_PLU(13003, "合同的商品明细中不包含该商品，不可用"),
    CNT_IS_DEL(13003, "合同不存在或已被删除，不可使用"),
    CNT_IS_NOT_PUR_RTN(13004, "  合同不允许退货，不可使用"),

    // 微服务部分--销售服务[14001, 16000]
    // 微服务部分--仓库服务[16001, 18000]
    WAREHOUSE_IS_STOP(16001, "仓库未启用，不可使用"),
    ORG_DIS_WAREHOUSE(16002, "默认配送仓库不存在, 不可操作要货"),
    PLU_PRICE_IS_NULL(16003, "商品售价不能为空"),
    INWH_OUTWH_IS_SAME(16004, "移出仓库与移入仓库相同，不可移库"),
    PLU_PICKUP_EXIST(16005, "拣货位已存在"),
    PLU_PICKUP_PLU_EXIST(16006, "拣货位中已存在该商品"),

    // 微服务部分--渠道服务[18001, 20000]
    // 微服务部分--结算服务[20001, 22000]
    PRC_ADJ_PLU_HEAD_IS_ACC(20001, "只能操作未记账的单据"),
    PRC_ADJ_ORG_HEAD_IS_ACC(20002, "已记账的单据, 不可进行记账操作"),
    PRC_ADJ_SELF_APPLY_IS_ACC(20003, "已记账的单据, 不可进行记账操作"),

    // 微服务部分--营销管理服务[22001, 24000]
    // 微服务部分--会员管理服务[24001, 26000]
    // 微服务部分--厅店管理服务[26001, 28000]

    SYS_ORG_IS_STOP(26001, "组织未启用，不可使用"),
    EXSUPPLIER_ISNOT_NOMAL(26002, "供应商未启用，不可使用"),
    EXSUPPLIER_NOTHAVE_RECORG(26003, "供应商使用组织中没有该收货组织，供应商不可用"),
    ACC_ORG_IS_STOP(26004, "核算组织无效，不可使用"),
    EXSUPPLIER_IS_DELETE(26005, "供应商不存在，不可使用"),
    P_SYS_ORG_NOT_USE_SCM(26006, "总部组织未启用供应链，不允许提交审批"),
    EXSUPPLIER_IS_NOT_PUR_COMMIT(26007, " 供应商未使用采购订单确认功能，不允许提交审批"),

    // 微服务部分--订单管理服务[28001, 30000]orderPur
    ORDER_PUR_IS_ACC(28001, "采购订单已记账，不可操作"),
    ORDER_PUR_IS_NOT_ACC(28002, "采购订单未记账，不可操作"),
    ORDER_PUR_IS_COMPLETED(28003, "采购订单已结束，不可操作"),
    ORDER_PUR_DATE_DELAY(28004, "不在有效时间内，不可操作"),
    STK_IN_CHK_IS_ACC(28005, "验收单已记账，不可操作"),
    ORDER_VAL_DAET_AFTER_CNT_STOP_DATE(28006, "采购订单有效到货时间超过合同的结账日期"),

    ORDER_VAL_DATE_AFTER_PUR_CNT_STOP_DATE(28006, "订单有效到货时间超过采购合同的结账日期"),
    ORDER_VAL_DATE_AFTER_DIS_CNT_STOP_DATE(28006, "订单有效到货时间超过配送合同的结账日期"),

    ORDER_ACC_DATE_AFTER_CNT_STOP_DATE(28007, "采购订单记账时间超过合同的结账时间"),
    ORDER_RTN_IS_ACC(28008, "退货订单已记账，不可操作"),
    ORDER_RTN_IS_NOT_APPROVE(28009, "配送退货订单处理状态不为已审核，不可操作"),
    ORDER_DIS_ACT_PLU_DIS_ATTR_NOT_DIS(28010, "配送属性为不可配送"),
    ORDER_REQ_IS_ACC(28011, "要货订单已记账，不可操作"),
    ORDER_REQ_IS_NOT_ACC(28012,"要货订单未记账，不可以人工结案"),
    ORDER_REQ_IS_HANDLE(28013, "已完成的单据，不能进行人工结案操作"),
    DIS_ORDER_PUR_PRICE_IS_NULL(28014, "对应配送单成本价为空,审核失败"),
    STK_OUT_DIS_NO_WAREHOUSE_ERROR(28015, "默认收货仓库不存在, 不可进行记账操作"),
    DIS_ORDER_IS_NULL(28016, "不存在对应的配送单"),
    DIS_ORDER_TAXES_CODE_IS_NULL(28017, "对应配送单税组合为空,审核失败"),
    ORDER_RTN_IS_NOT_ACC(28018, "退货订单未记账，不可操作"),
    STK_IN_DIS_RTN_PLU_REL_RTN_NUM_IS_NULL(28019, "对应配送退货出库单实退数量为空"),
    ORDER_RTN_IS_NOT_DELIVER(28019, "配送退货订单未发货，不可操作"),
    ORDER_RTN_HAS_HANDEL(28020, "该单据处理状态不是0-未完成，不允许人工结案"),
    ORDER_DIS_REQ_HANDLE_PLU_NO_SKU(28021, "提交处理的商品在指定发货仓库中都没有库存，产生配送单失败"),
    STK_OUT_DIS_STOCK_COUNT_ERROR(28022, "库存不足"),
    STK_OUT_DIS_LOCK_COUNT_ERROR(28023, "锁定库存不足"),
    STK_IN_DIS_WH_STOCK_COUNT_ERROR(28023, "总仓库存数据不存在"),
    COLLAR_IS_ACC(28024, "已记账的单据, 不可进行记账操作"),
    COLLAR_RTN_IS_ACC(28025, "已记账的单据, 不可进行记账操作"),
    STK_APPLY_IS_NOT_ACC(28026, "库存调整申请单未记账，不可操作"),
    STK_APPLY_IS_ACC(28027, "库存调整申请单已记账，不可操作"),
    WH_ALLOT_NTC_IS_ACC(28028, "已记账的单据, 不可进行记账操作"),
    PUR_NUM_LITTLE(28029, "实收数量不能大于采购数量"),

    STK_OUT_DIS_ACC_NOT_ACC_ERROR(28030, "已记账的单据, 不可进行记账操作"),

    STK_OUT_DIS_NO_CONFIRM_NOT_ACC_ERROR(28031, "未确认的单据, 不可进行记账操作"),

    STK_OUT_DIS_NO_DELETE_ERROR(28032, "已提交的单据, 不可进行删除操作"),

    STK_OUT_DIS_COMMIT_NOT_EDIT_ERROR(28033, "已提交的单据, 不可进行编辑操作"),

    STK_OUT_DIS_ACC_NOT_EDIT_ERROR(28034, "已记账的单据, 不可进行编辑操作"),

    STK_OUT_DIS_COMMIT_NOT_COMMIT_ERROR(28035, "已提交的单据, 不可进行提交操作"),

    STK_OUT_DIS_CONFIRM_NOT_COMMIT_ERROR(28036, "已确认的单据, 不可进行提交操作"),

    STK_OUT_DIS_ACC_NOT_COMMIT_ERROR(28037, "已记账的单据, 不可进行提交操作"),

    STK_OUT_DIS_NO_COMMIT_NOT_REVOKE_ERROR(28038, "未提交的单据, 不可进行撤销操作"),

    STK_OUT_DIS_CONFIRM_NOT_REVOKE_ERROR(28039, "已确认的单据, 不可进行撤销操作"),

    STK_OUT_DIS_ACC_NOT_REVOKE_ERROR(28040, "已记账的单据, 不可进行撤销操作"),

    STK_OUT_DIS_NO_COMMIT_NOT_CONFIRM_ERROR(28041, "未提交的单据, 不可进行确认操作"),

    STK_OUT_DIS_CONFIRM_NOT_CONFIRM_ERROR(28042, "已确认的单据, 不可进行确认操作"),

    STK_OUT_DIS_ACC_NOT_CONFIRM_ERROR(28043, "已记账的单据, 不可进行确认操作"),

    ALLOT_NTC_ACC_NOT_EDIT_ERROR(28044, "已记账的单据, 不可进行编辑操作"),

    ALLOT_NTC_ACC_NOT_DELETE_ERROR(28045, "已记账的单据, 不可进行删除操作"),

    ALLOT_NTC_ACC_NOT_ACC_ERROR(28046, "已记账的单据, 不可进行记账操作"),

    ALLOT_ACC_NOT_REJECT_ERROR(28047, "已记账的单据, 不可进行驳回操作"),

    ALLOT_STK_IN_NOT_CONFIRM_ERROR(28048, "已入库的单据, 不可进行确认操作"),

    ORDER_RTN_PASS_NOT_PASS_ERROR(28049, "已通过的单据,不能进行通过操作"),

    ORDER_RTN_PASS_NOT_MISS_ERROR(28050, "已通过的单据,不能进行驳回操作"),

    ORDER_RTN_MISS_NOT_PASS_ERROR(28049, "已驳回的单据,不能进行通过操作"),

    ORDER_RTN_MISS_NOT_MISS_ERROR(28050, "已驳回的单据,不能进行驳回操作"),

    STK_IN_CHECK_NO_PDT_DATE_ERROR(28051, "生产日期不能为空"),

    ALLOT_NOT_ORG_PLU(28052, "不在调入组织的经营范围内"),

    ORDER_REQ_IS_END(28053, "要货订单已经结案，不可再审核"),

    ORDER_REQ_IS_AUDIT(28053, "要货订单已完成审核，不可再审核"),

    WHOLESALE_ORDER_IS_ACC_NOT_DEL(28054, "已记账的单据，不能进行删除操作"),

    WHOLESALE_ORDER_NOT_ACC_NOT_CLOSE(28055, "未记账的单据，不能进行结案操作"),

    WHOLESALE_ORDER_IS_ACC_NOT_EDIT(28056, "已记账的单据，不能进行编辑操作"),

    WHOLESALE_ORDER_PLU_NOT_EMPTY(28057, "批发订单商品明细不能为空"),

    WHOLESALE_ORDER_CREDIT_NOT_ENOUGH(28058, "可用订货金额不足，不可批发。"),

    WHOLESALE_ORDER_CREDIT_NOT_EXIST(28059, "当前客户未配置可用订货额度，不可批发。"),

    WHOLESALE_ORDER_IS_ACC_NOT_ACC(28060, "已记账的单据，不能再执行记账操作"),

    ORDER_PLU_NO_MOQ_RULE(28061, "不满足起订规则"),

    INVENTORY_PLATFORM_EXIST(28062, "盘点组织已存在可盘点的盘点平台"),

    INVENTORY_PLATFORM_NO_PLU(28063, "盘点组织下没有可盘点的商品"),

    INVENTORY_PLATFORM_NO_INBENTORY_STATUS(28064, "只能删除未启动的盘点平台"),

    INVENTORY_PLATFORM_NO_START(28065, "只能启动未启动的盘点平台"),

    INVENTORY_PLATFORM_NO_CANCEL(28066, "只能作废已启动、复盘中和已汇总的盘点平台"),

    INVENTORY_PLATFORM_NO_TOTAL(28067, "只能汇总已启动和已汇总的盘点平台"),

    INVENTORY_PLATFORM_NO_INVENTORY_NO_TOTAL(28068, "实盘数据不存在，不能进行汇总操作"),

    INVENTORY_PLATFORM_INVENTORY_ACC_NO_TOTAL(28069, "已有复盘单记账，不能进行汇总操作"),

    INVENTORY_PLATFORM_NO_REALITY_BILL(28068, "盘点平台不存在实盘单，不允许添加复盘单"),

    INVENTORY_PLATFORM_REPLY_BILL(28069, "盘点平台已存在复盘单，不允许添加实盘单"),

    INVENTORY_PLATFORM_ERROR_BILL(28070, "盘点平台盘点状态为审核失败，不允许添加实盘单"),

    PROTAL_NOT_ORDER_ERROR(28070, "单据不存在"),

    HIROP_DELETE_NOT_ACC(28071, "只能删除未记账的单据"),

    HIROP_EDIT_NOT_ACC(28072, "只能编辑未记账的单据"),

    HIROP_ACC_NOT_ACC(28073, "只能记账未记账的单据"),

    HIROP_NOT_PLU(28074, "单据不存在商品信息"),

    DIS_ORDER_CREDIT_NOT_ENOUGH(28075, "客户订货付款方式为 1-先款后货，可用订货金额不足，不可配货"),

    DIS_ORDER_CREDIT_NOT_EXIST(28076, "客户订货付款方式为 1-先款后货，未设置客户信用额度，不可配货"),

    INVENTORY_PLATFORM_REALITY_BILL_NOT_ACC(28077, "盘点平台存在未记账的实盘单，不允许添加复盘单"),

    INVENTORY_PLATFORM_NOT_TOTAL(28078, "盘点平台未汇总，不允许添加复盘单"),

    INVENTORY_PLATFORM_NO_APPROVE(28079, "只能审核已汇总、复盘中和审核失败的盘点平台"),

    HIROP_NOT_STOCK(28080, "没有初始化库存"),

    HIROP_APPROVE_ERROR(28081, "审核失败"),

    HIROP_ACC_ERROR(28082, "记账失败"),

    WHOLESALE_ORDER_ISNOT_ACC_CANNOT_TO_SALE(28083, "未记账单据，不能转销售单"),

    WHOLESALE_ORDER_ALLTOSALE_CANNOT_TO_SALE(28084, "全部转销售单的单据，不能转销售单"),

    WHOLESALE_ORDER_END_CANNOT_TO_SALE(28085, "已结案或已完成单据，不能转销售单"),

    INVENTORY_PLATFORM_REALITY_NO_ACC_NO_TOTAL(28086, "存在实盘单未记账，不能进行汇总操作"),

    SALE_NUM_PASS_WHOLESALE_NUM(28087, "转销售数量超过了原批发数量，不能保存"),

    WHOLESALE_BILL_IS_COMMIT(28088, "已提交的单据, 不能再进行提交操作"),

    WHOLESALE_BILL_IS_NOT_COMMIT(28089, "未提交的单据，不能进行撤销提交操作"),

    WHOLESALE_BILL_IS_ACC_CONNOT_UNCOMMIT(28090, "已记账的单据，不能进行撤销提交操作"),

    WHOLESALE_BILL_ISNOT_COMMIT_CONNOT_ACC(28091, "非提交状态的单据，不能进行记账操作"),

    WHOLESALE_BILL_ISNOTACC_CONNOT_REC(28092, "未记账的销售单，不能进行收款操作"),

    WHOLESALE_BILL_STOCK_NOT_ENOUGH(28093, " 库存可用数量不足，不能保存"),

    WHOLESALE_BILL_STOCK_NOT_EXIST(28094, " 在仓库库存中没有数据，不可保存"),

    HIROP_PLU_STOCK(28095,"存在库存信息，不允许淘汰"),

    STK_OUT_DIS_ACC_NOT_CANCEL_ERROR(28096, "已记账的单据, 不可进行作废操作"),

    STK_OUT_DIS_CONFIRM_NOT_EDIT_ERROR(28097, "已确认的单据, 不可进行编辑操作"),

    STK_OUT_DIS_CANCEL_NOT_EDIT_ERROR(28098, "已废除的单据, 不可进行编辑操作"),

    STK_OUT_DIS_CANCEL_NOT_COMMIT_ERROR(28099, "已废除的单据, 不可进行提交操作"),

    STK_OUT_DIS_CANCEL_NOT_REVOKE_ERROR(280100, "已废除的单据, 不可进行撤销操作"),

    WHOLESALE_RTN_ACC_CANNOT_EDIT(280101, "已经记账的单据，不能进行编辑操作"),

    WHOLESALE_RTN_ACC_CANNOT_DEL(28102, "已记账的单据，不能进行删除操作"),

    WHOLESALE_RTN_COMMIT_CANNOT_DEL(28103, "已提交的单据，不能进行删除操作"),

    WHOLESALE_RTN_NOTACC_CANNOT_REFUND(28104, "未记账的单据，不能进行退款操作"),

    WHOLESALE_RTN_REFUND_CANNOT_REFUND(28105, "已退款的单据，不能进行退款操作"),

    HIROP_CHECK_NUM_ERROR(280106, " 实收数量大于可验收数量"),

    WHOLESALE_ORDER_END_NOT_TO_CLOSE(280107, "已结案或完成的单据，不能进行结案操作"),

    WHOLESALE_BILL_STK_NOT_ENOUGH(280108, "待转销售的商品在仓库中库存不足，无法转销售单。"),

    WHOLESALE_BILL_COMMIT_CANNOT_CLOSE(280108, "生成的销售单存在已提交出库的单据，不能进行结案"),

    WHOLESALE_BILL_ACCOUNT_CANNOT_CLOSE(280108, "生成的销售单存在已记账出库的单据，不能进行结案"),

    WHOLESALE_BILL_END_CANNOT_CLOSE(280108, "生成的销售单存在已收款的单据，不能进行结案"),

    WHOLESALE_ORDER_CLOSE_CANNOT_COMMIT(280108, "对应的原批发订单已结案，不能提交出库"),

    WH_ALLOT_PLU_NO_STK(280109, " 在移出仓库中，该商品数量不足，不可进行仓库移库操作。"),

    WH_ALLOT_PLU_NO_AVAILABLE_STK(280109, " 在移出仓库中，该商品可用数量不足，不可进行仓库移库操作。"),

    HIROP_BILL_NO_ERROR(280110, "订单号不能为空"),

    HIROP_PLU_ERROR(280111, "商品重复"),
    PUR_REQ_IS_ACC(280112, "采购申请已记账，不可操作"),

    HIROP_REPEAT_CREAT_ORDER(280113, "已审核，不可进行审核操作"),

    COLLAR_STOCK_COUNT_NOT_ENOUGH(280112, " 可用库存不足，不可领用"),

    COM_PLU_NOT_EXIST(280113, "采购订单中，不存在该商品"),

    ALLIANCE_PLU_ACC_CANNOT_DEL(280114, "已记账的商品，不能进行删除操作"),

    ALLIANCE_PLU_AUDIT_CANNOT_DEL(280115, "待审核的商品，不能进行删除操作"),

    ALLIANCE_PLU_ACC_CANNOT_UPD(280116, "已记账的商品，不能进行编辑操作"),

    ALLIANCE_PLU_AUDIT_CANNOT_UPD(280117, "待审核的商品，不能进行编辑操作"),

    ALLIANCE_PLU_ACC_CANNOT_SUB_AUDIT(280118, "已记账的商品，不能进行提交审核操作"),

    ALLIANCE_PLU_AUDIT_CANNOT_SUB_AUDIT(280119, "待审核的商品，不能进行提交审核操作"),

    ALLIANCE_PLU_SUB_AUDIT_CANNOT_SUB_AUDIT(280120, "已建档的商品，不能进行提交审核操作"),

    ALLIANCE_PLU_NOT_ERROR(280121, "商品不存在"),

    ALLIANCE_PLU_REJECT_CANNOT_AUDIT(280122, "已驳回的商品，不能进行审核操作"),

    ALLIANCE_PLU_ADD_CANNOT_AUDIT(280123, "新建的商品，不能进行审核操作"),

    ALLIANCE_PLU_SUB_AUDIT_CANNOT_AUDIT(280124, "已建档的商品，不能进行审核操作"),

    ALLIANCE_PLU_ACC_CANNOT_AUDIT(280125, "已记账的商品，不能进行审核操作"),

    ALLIANCE_PLU_REJECT_CANNOT_ACC(280126, "已驳回的商品，不能进行记账操作"),

    ALLIANCE_PLU_ADD_CANNOT_ACC(280127, "新建的商品，不能进行记账操作"),

    ALLIANCE_PLU_AUDIT_CANNOT_ACC(280128, "待审核的商品，不能进行记账操作"),

    ALLIANCE_PLU_ACC_CANNOT_ACC(280129, "已记账的商品，不能进行记账操作"),

    HIROP_CANCEL_NOT_EDIT(280130, "已作废的单据，不能进行编辑操作"),

    HIROP_ACC_NOT_CANCEL(280131, "已记账的单据，不能进行作废操作"),

    HIROP_CANCEL_NOT_CANCEL(280132, "已作废的单据，不能进行作废操作"),

    HIROP_IS_ACC_NOT_ACC(280133, "已记账的单据，不能进行记账操作"),

    HIROP_CANCEL_NOT_ACC(280134, "已作废的单据，不能进行记账操作"),

    ORDER_PUR_DRC_PLU_DIS_ATTR_NOT_DRC(280135, "商品配送属性不是直送"),

    ORDER_COM_PUR_PLU_DIS_ATTR_NOT_PUR(280114, "商品配送属性不是自采"),

    HIROP_ORG_NOT_AUTHORITY(280136, "当前组织没有审核权限"),

    HIROP_IMPL_ORG_CODE_ERROR(280137, "组织编码不能为空"),

    HIROP_IMPL_BAR_CODE_ERROR(280138, "商品条码不能为空"),

    HIROP_IMPL_PLU_NAME_ERROR(280139, "商品名称不能为空"),

    HIROP_IMPL_UNIT_NAME_ERROR(280140, "单位不能为空"),

    HIROP_IMPL_QA_DAYS_ERROR(280141, "保质期（天）不能为空"),

    HIROP_IMPL_PUR_TAXES_CODE_ERROR(280142, "默认进项税不能为空"),

    HIROP_IMPL_SAL_TAXES_CODE_ERROR(280143, "默认销项税不能为空"),

    HIROP_IMPL_PUR_IN_PRICE_ERROR(280144, "含税进价不能为空"),

    HIROP_IMPL_SUG_PRICE_ERROR(280145, "建议零售价不能为空"),

    HIROP_IMPL_VIP_PRICE_ERROR(280146, "会员价不能为空"),

    HIROP_IMPL_VIP1_PRICE_ERROR(280147, "会员价1不能为空"),

    HIROP_IMPL_SUG_VIP_PRICE_ERROR(280148, "会员价不能大于建议零售价"),

    HIROP_IMPL_VIP_VIP1_PRICE_ERROR(280149, "会员价1不能大于会员价"),

    HIROP_IMPL_SALE_SEASON_ERROR(280150, "销售季节格式异常"),

    HIROP_IMPL_MARKET_YEAR_ERROR(280151, "上市年份格式异常"),

    HIROP_IMPL_UNIT_NAME_NOT_EXIST(280152, "单位不存在"),

    HIROP_IMPL_PUR_TAXES_CODE_NOT_EXIST(280153, "默认进项税不存在"),

    HIROP_IMPL_SAL_TAXES_CODE_NOT_EXIST(280154, "默认销项税不存在"),

    HIROP_IMPL_MARKET_SEASON_NOT_EXIST(280155, "上市季节不存在"),

    HIROP_IMPL_NOT_EXIST(280156, "在系统中不存在"),

    HIROP_IMPL_ORG_BAR_CODE_EXIST(280156, "该组织下已存在该商品"),

    HIROP_EXPORT_DAY_ERROR(280157, "只能导出三个月以内的数据"),

    HIROP_CLOSE_NOT_CLOSE(280158, "已结案的单据，不能进行结案操作"),

    HIROP_PLU_DIS_NOT_CLOSE(280159, "主动配送订单生成的客户要货订单已配送处理，不能进行结案操作"),

    HIROP_CLOSE_NOT_ACC(280160, "已结案的单据，不能进行记账操作"),

    HIROP_ACC_NOT_DEL(280161, "已记账的单据，不能进行删除操作"),

    OREER_RTN_DRC_PLU_IMP_ERROR(280162, "当前退货组织未找到符合条件的商品，不能进行退货操作"),

    HIROP_IMPL_SCATTERED_NUM_ERROR(280163, "零散数量不能为空"),

    HIROP_IMPL_SCATTERED_NUM_IS_FLOAT_ERROR(280164, "零散数量不能为小数"),

    HIROP_IMPL_SCATTERED_NUM_IS_ZERO_ERROR(280165, "零散数量不能小于等于0"),

    HIROP_IMPL_RTN_REASON_ERROR(280166, "无效的退货原因"),

    HIROP_IMPL_DIS_IN_PRICE_ERROR(280167, "无效的配送价"),

    HIVENTORY_RANDOM_ACC_NOT_ACC(280168, "只能记账未记账或记账失败的单据"),

    HIROP_IMPL_PLU_DIS_PUR_ERROR(280169, "请检查商品的配送属性是否为直送、商品是否允许直送退货、商品是否在经营范围内、商品是否在配送合同和采购合同中"),

    HIROP_PRE_PLU_EXIST(280170, "新品已存在，请重新输入商品条码"),

    HIROP_PLU_EXIST(280171, "商品已存在，请重新输入商品条码"),

    HIROP_PRICE_TYPE_ERROR(280172, "价格类型不能为空"),

    HIROP_ORDER_PUR_DRC_IS_CHECK(280173, "该直送采购订单已被验收，不可重复验收"),

    HIROP_IMPL_CHANGE_CODE_NOT_NULL_ERROR(280174, "组织编码/经营范围编码不能为空"),

    HIROP_IMPL_BAR_CODE_NOT_EXIST(280176, "商品条码不存在"),

    HIROP_IMPL_IS_DEPOSIT_ERROR(280177, "寄存不能未空"),

    HIROP_IMPL_IS_DEPOSIT_NOT_EXIST(280178, "无效的寄存，请输入0或1"),

    HIROP_IMPL_IS_DIS_ERROR(280179, "配货不能未空"),

    HIROP_IMPL_IS_DIS_NOT_EXIST(280180, "无效的配货，请输入0或1"),

    HIROP_IMPL_IS_DIS_RTN_ERROR(280181, "配货退货不能未空"),

    HIROP_IMPL_IS_DIS_RTN_NOT_EXIST(280182, "无效的配货退货，请输入0或1"),

    HIROP_IMPL_IS_GIFT_ERROR(280183, "赠送不能未空"),

    HIROP_IMPL_IS_GIGT_NOT_EXIST(280184, "无效的赠送，请输入0或1"),

    HIROP_IMPL_IS_LOW_PRICE_SALE_ERROR(280185, "低于最低价销售不能未空"),

    HIROP_IMPL_IS_LOW_PRICE_SALE_NOT_EXIST(280186, "无效的低于最低价销售，请输入0或1"),

    HIROP_IMPL_IS_MOVE_IN_ERROR(280187, "调入不能未空"),

    HIROP_IMPL_IS_MOVE_IN_NOT_EXIST(280188, "无效的调入，请输入0或1"),

    HIROP_IMPL_IS_MOVE_OUT_ERROR(280189, "调出不能未空"),

    HIROP_IMPL_IS_MOVE_OUT_NOT_EXIST(280190, "无效的调出，请输入0或1"),

    HIROP_IMPL_IS_PUR_ERROR(280191, "进货不能未空"),

    HIROP_IMPL_IS_PUR_NOT_EXIST(280192, "无效的进货，请输入0或1"),

    HIROP_IMPL_IS_PUR_RTN_ERROR(280193, "进货退货不能未空"),

    HIROP_IMPL_IS_PUR_RTN_NOT_EXIST(280194, "无效的进货退货，请输入0或1"),

    HIROP_IMPL_IS_SALE_ERROR(280195, "销售不能未空"),

    HIROP_IMPL_IS_SALE_NOT_EXIST(280196, "无效的销售，请输入0或1"),

    HIROP_IMPL_IS_SALE_RTN_ERROR(280197, "销售退货不能未空"),

    HIROP_IMPL_IS_SALE_RTN_NOT_EXIST(280198, "无效的销售退货，请输入0或1"),

    HIROP_IMPL_CHANGE_CODE_ERROR(280199, "经营范围编码不存在"),

    HIROP_IMPL_RNG_NOT_PLU_ERROR(280200, "经营范围下没有该商品"),

    HIROP_PLU_NULL_ERROR(280201, "商品不能为空"),

    HIROP_APPROVE_ERROR_MSG(280202, "存在商品盘亏数量大于库存数量，请查看单据详情"),

    PERFORMANCE_IS_NULL(280203, "绩效目标设置不能为空"),

    PERFORMANCE_EXCEPTION(280204, "绩效目标设置异常"),

    PERFORMANCE_IS_ACC_NOT_DEL(280205, "考核指标已记账，不可进行删除操作"),

    PERFORMANCE_IS_ACC_NOT_ACC(280206, "考核指标已记账，不可进行记账操作"),

    PERFORMANCE_NOT_ACC_NOT_NULLIFY(280207, "考核指标未记账，不可进行作废操作"),

    PERFORMANCE_IS_NULLIFY_NOT_NULLIFY(280208, "考核指标已作废，不可进行作废操作"),

    PERFORMANCE_IS_NULLIFY_NOT_EDIT(280209, "考核指标已作废，不可进行编辑操作"),

    HIROP_CREATE_PLU_NOT_CREATE(280210, "已生成盘点单的商品，不可进行生成盘点单操作"),

    HIROP_CANCEL_PLU_NOT_CREATE(280211, "已作废的商品，不可进行生成盘点单操作"),

    HIROP_CREATE_PLU_NOT_CANCEL(280212, "已生成盘点单的商品，不可进行作废操作"),

    HIROP_CANCEL_PLU_NOT_CANCEL(280213, "已作废的商品，不可进行作废操作"),

    ACC_DATA_NOT_ACC(280214, "已记账的单据，不能进行记账操作"),

    STOP_DATA_NOT_ACC(280215, "已终止的单据，不能进行记账操作"),

    ACC_DATA_NOT_EDIT(280216, "已记账的单据，不能进行编辑操作"),

    STOP_DATA_NOT_EDIT(280217, "已终止的单据，不能进行编辑操作"),

    NOT_ACC_DATA_NOT_STOP(280218, "未记账的单据，不能进行终止操作"),

    STOP_DATA_NOT_STOP(280219, "已终止的单据，不能进行终止操作"),

    ACC_DATA_NOT_DEL(280220, "已记账的单据，不能进行删除操作"),

    STOP_DATA_NOT_DEL(280221, "已终止的单据，不能进行删除操作"),

    HIROP_ORG_NOT_UPDATE(280222, "当前组织没有编辑权限"),

    ALLIANCE_PLU_ADD_CANNOT_UPD(280223, "新建的商品，不能进行编辑操作"),

    ALLIANCE_PLU_REJECT_CANNOT_UPD(280224, "已驳回的商品，不能进行编辑操作"),

    ALLIANCE_PLU_SUB_AUDIT_CANNOT_UPD(280224, "已建档的商品，不能进行编辑操作"),

    DUPLICATE_COMMIT(280225, "重复提交，请稍等"),

    PLU_IS_INIT(280226, "商品已记账,不可重复记账"),

    PLU_INFO_IS_ERROR(280227, "商品批次初始化数据有误,请修改后重新记账"),

    HIROP_IMPL_BRAND_CODE_ERROR(280228, "品牌编码不能为空"),

    HIROP_IMPL_CLS_CODE_ERROR(280229, "品类编码不能为空"),

    HIROP_ALLIANCE_PLU_ERROR(280230, "总部或供应商商品，不允许进行门店商品新增操作"),

    HIROP_LOGIN_ORG_NO_ORG_MODAL_1(280231, "当前登陆组织不为区域总部，没有操作权限"),

    HIROP_ORG_EXIST_BRAND(280232, "区域总部已存在当前品牌"),

    ORDER_ACCOUNT_NOT_COMMIT(280233, "单据已记账，不允许提交审核"),

    ORDER_COMMIT_NOT_COMMIT(280234, "单据已提交审核，不允许重复提交审核"),

    ORDER_COMMIT_PASS_NOT_COMMIT(280235, "单据已审核通过，不允许重复提交审核"),

    ORDER_NOT_AUDIT_NOT_ACC(280236, "单据需供应商审核通过后才可记账"),

    ORDER_COMMIT_AUDIT_NOT_EDIT(280237, "待审批的单据，不能进行编辑操作"),

    ORDER_AUDITED_NOT_EDIT(280238, "已审批通过的单据，不能进行编辑操作"),

    ORDER_COMMIT_AUDIT_NOT_DEL(280239, "待审批的单据，不能进行删除操作"),

    ORDER_AUDITED_NOT_DEL(280240, "已审批通过的单据，不能进行删除操作"),

    ORDER_FINISHED_NOT_COMMIT(280241, "已完成的单据，不能进行提交审批操作"),

    ORDER_NOT_AUDIT_NOT_COMMIT(280242, "没有内部审核通过的单据，不能进行提交审批操作"),

    PLU_NOT_EXIST_IN_SOURCE_ORDER(280243, "原单据不存在该商品信息"),

    ORDER_RTN_DRC_NOT_ACC_NOT_AUDIT(280244, "未记账的单据，不能进行审核操作"),

    ORDER_RTN_DRC_NOT_SUP_AUDIT_NOT_AUDIT(280245, "待审核的单据，不能进行审核操作"),

    ORDER_RTN_DRC_CONFIRM_NOT_AUDIT(280245, "已确认的单据，不能进行审核操作"),

    ORDER_RTN_DRC_SUP_NOT_PASS_NOT_CONFIRM(280246, "供应商未审批通过的单据，不能进行确认操作"),

    ORDER_RTN_DRC_CONFIRMED_NOT_CONFIRM(280246, "已确认的单据，不能进行确认操作"),

    STK_APPLY_NOT_LOWER_AVAILABLE_STOCK(280247, "审批后的可用库存数量小于 0，审批不可通过"),

    HIROP_EDIT_NO_COMMIT(280248, "只能编辑待提交或者已驳回的单据"),
    HIROP_DELETE_NO_COMMIT(280249, "只能删除待提交或者已驳回的单据"),
    HIROP_COMMIT_NO_COMMIT(280250, "只能提交待提交或者已驳回的单据"),
    HIROP_AUDIT_NO_AUDIT(280251, "只能审核待审核的单据"),

    ORDER_RTN_REQ_CLOSE_NOT_OPERATION(280252,"退货订单已结案，不允许进行操作"),

    // 微服务部分--酬金管理服务[30001, 32000]
    // 微服务部分--供应链管理服务[32001, 34000]
    // 微服务部分--lis物流管理服务[34001, 36000]
    // 微服务部分--报表管理服务[36001, 38000]
    COA_AP_PRE_PAY_HEAD_IS_ACC(36001, "预付款单已对账不可操作"),

    POS_OVER_SHOP_RPT_RPT_DATE_IS_NULL(36002, "营业日期不能为空, 请选择"),
    RPT_DATE_POS_NOT_MIN(36003, "请从最小店结日期开始店结"),
    RPT_DATE_POS_GT_RPT_DATE(36004, "店结日期大于营业日期"),
    RPT_DATE_POS_GT_NOW(36005, "店结日期大于当前日期"),
    BEYOND_LIMIT_TIME(36006, "超出规定时间范围"),
    EXIST_POS_SALE_ERR(36007, "存在异常流水"),
    POS_SALE_UNBALANCE(36008, "流水不平衡"),
    NON_EXIST_PLAT_POS_SALE(36009, "没有台结流水"),
    EXIST_POS_SALE_BEFORE(36007, "存在营业日期小于店结日期的销售流水"),
    ACCOUNT_SHOP_SUCCESS(36008, "店结成功"),
    NON_EXIST_POS_OVER_JOB(36009, "没有日结任务"),
    RPT_DATE_ROP_NOT_MIN(36010, "请从最小日结日期开始日结"),
    EXIST_NO_REDUCE_STOCK_SALE(36011, "存在未冲减库存的流水"),
    EXIST_BEFORE_POS_SALE_SHOP(36012, "存在小于汇总日期的店结流水"),
    NON_EXIST_POS_SALE_SHOP(36013, "没有店结流水"),
    ACCOUNT_DAY_SUCCESS(36014, "日结成功"),
    EXIST_POS_SALE_IN_STOP_POS(36015, "已停用的pos机存在流水"),
    EXIST_POS_SALE_ACC_POS(36016, "存在台结流水"),
    HAVING_ACCOUNT_SHOP(36017, "已经店结了")

    ;
    private int code;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(int code, String msg) {
        this.code = code;

    }

    public int getCode() {
        return code;
    }

    private ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}