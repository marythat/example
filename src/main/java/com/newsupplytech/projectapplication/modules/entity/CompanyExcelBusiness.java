package com.newsupplytech.projectapplication.modules.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName(value = "v_company_excel_business")
public class CompanyExcelBusiness {
    public static String[] years = new String[]{"2017","2018","2019"};
    public static String[] operationField = new String[]{"allAssets","allDebt","totalOwnerEquity","mainBusinessIncome","totalProfit"
            ,"totalIndustyOutput","rdCost","paymentVat"};
    /**
     * 企业id
     */
    @TableField(value = "companyId")
    private Long companyId;

    /**
     * 所属年份
     */
    @TableField(value = "dataTime")
    private Integer dataTime;

    /**
     * 数据的类型 ‘year’年报数据，‘quarter’季度数据
     */
    @TableField(value = "dataType")
    private String dataType;

    /**
     * 所属月份
     */
    @TableField(value = "month")
    private Integer month;

    /**
     * 资产总额(万元)，对应标志化表格的ZZC1,ZZC2,ZZC3
     */
    @TableField(value = "allAssets")
    private BigDecimal allAssets;

    /**
     * 负债总额(万元) 对应标志化表格的FZZE1,FZZE2,FZZE3
     */
    @TableField(value = "allDebt")
    private BigDecimal allDebt;

    /**
     * 所有者权益(万元) 对应标志化表格的 SYZQY1,SYZQY2,SYZQY3
     */
    @TableField(value = "totalOwnerEquity")
    private BigDecimal totalOwnerEquity;

    /**
     * 主营业务收入(万元)对应标志化表格的 ZYYWSR1,ZYYWSR2,ZYYWSR3
     */
    @TableField(value = "mainBusinessIncome")
    private BigDecimal mainBusinessIncome;

    /**
     * 利润总额(万元)对应标志化表格的 LRZE1,LRZE2,LRZE3
     */
    @TableField(value = "totalProfit")
    private BigDecimal totalProfit;

    /**
     * 工业总产值(万元)对应标志化表格的 GYZCZ1,GYZCZ2,GYZCZ3
     */
    @TableField(value = "totalIndustyOutput")
    private BigDecimal totalIndustyOutput;

    /**
     * 研发费用(万元)对应标志化表格的 YFJFZC1,YFJFZC2,YFJFZC3
     */
    @TableField(value = "rdCost")
    private BigDecimal rdCost;

    /**
     * 实缴税金(万元)对应标志化表格的 对应SJSJ1,SJSJ2,SJSJ3
     */
    @TableField(value = "paymentVat")
    private BigDecimal paymentVat;

    /**
     * 平均用工人数 对应标志化表格的QYZRS
     */
    @TableField(value = "averageWorker")
    private BigDecimal averageWorker;

    /**
     * 登记注册类别(万元) 对应标志化表格的QYLX
     */
    @TableField(value = "reportType")
    private String reportType;

    /**
     * 主要产品或服务名称1 对应标志化表格的XMMC1
     */
    @TableField(value = "businessActive1")
    private String businessActive1;

    /**
     * 主要产品或服务名称2 对应标志化表格的XMMC2
     */
    @TableField(value = "businessActive2")
    private String businessActive2;

    /**
     * 主要产品或服务名称3 对应标志化表格的XMMC3
     */
    @TableField(value = "businessCctive3")
    private String businessCctive3;
}
