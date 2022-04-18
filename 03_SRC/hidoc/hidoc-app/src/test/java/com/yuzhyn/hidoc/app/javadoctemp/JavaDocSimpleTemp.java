package com.yuzhyn.hidoc.app.javadoctemp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库基本方法
 * #场景 数据查询时，使用平台需要写大量SQL语句执行，但是对于普通增删改查，可使用基本方法工具，省去了手写SQL
 * 版本         修改时间       修改人         修改内容
 * 7.0.0.1     2021-01-07    yuzhengyang    调整
 * 7.0.0.2     2021-02-24    fanxueping     CMPS-1891配货池增加selectByIdFUdp
 *
 * @sence
 */
public class JavaDocSimpleTemp {

    /**
     * 商品包拆包
     * <p>
     * #场景 数据查询时，使用平台需要写大量SQL语句执行，但是对于普通增删改查，可使用基本方法工具，省去了手写SQL
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
}
