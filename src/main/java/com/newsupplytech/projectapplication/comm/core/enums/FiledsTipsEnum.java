package com.newsupplytech.projectapplication.comm.core.enums;

import com.newsupplytech.projectapplication.comm.utils.StringUtils;
import com.newsupplytech.projectapplication.modules.entity.CompanyExcelBusiness;

public enum FiledsTipsEnum {

    YEAR1( CompanyExcelBusiness.years[0], "ZZC1,FZZE1,SYZQY1,ZYYWSR1,LRZE1,YFJFZC1,SJSJ1"),
    YEAR2(CompanyExcelBusiness.years[1], "ZZC2,FZZE2,SYZQY2,ZYYWSR2,LRZE2,YFJFZC2,SJSJ2"),
    YEAR3(CompanyExcelBusiness.years[2], "ZZC3,FZZE3,SYZQY3,ZYYWSR3,LRZE3,YFJFZC3,SJSJ3"),
    ALLASSETS1("allAssets_"+ CompanyExcelBusiness.years[0], "ZZC1,FZZE1,SYZQY1,ZYYWSR1,LRZE1,YFJFZC1,SJSJ1"),
    ALLASSETS2("allAssets_"+ CompanyExcelBusiness.years[1], "ZZC2,FZZE2,SYZQY2,ZYYWSR2,LRZE2,YFJFZC2,SJSJ2"),
    ALLASSETS3("allAssets_"+ CompanyExcelBusiness.years[2], "ZZC3,FZZE3,SYZQY3,ZYYWSR3,LRZE3,YFJFZC3,SJSJ3"),
    ALLDEBT1("allDebt_"+ CompanyExcelBusiness.years[0], "ZZC1,FZZE1,SYZQY1,ZYYWSR1,LRZE1,YFJFZC1,SJSJ1"),
    ALLDEBT2("allDebt_"+ CompanyExcelBusiness.years[1], "ZZC2,FZZE2,SYZQY2,ZYYWSR2,LRZE2,YFJFZC2,SJSJ2"),
    ALLDEBT3("allDebt_"+ CompanyExcelBusiness.years[2], "ZZC3,FZZE3,SYZQY3,ZYYWSR3,LRZE3,YFJFZC3,SJSJ3"),
    TOTALOWNEREQUITY1("totalOwnerEquity_"+ CompanyExcelBusiness.years[0], "ZZC1,FZZE1,SYZQY1,ZYYWSR1,LRZE1,YFJFZC1,SJSJ1"),
    TOTALOWNEREQUITY2("totalOwnerEquity_"+ CompanyExcelBusiness.years[1], "ZZC2,FZZE2,SYZQY2,ZYYWSR2,LRZE2,YFJFZC2,SJSJ2"),
    TOTALOWNEREQUITY3("totalOwnerEquity_"+ CompanyExcelBusiness.years[2], "ZZC3,FZZE3,SYZQY3,ZYYWSR3,LRZE3,YFJFZC3,SJSJ3"),
    MAINBUSINESSINCOME1("mainBusinessIncome_"+ CompanyExcelBusiness.years[0], "ZZC1,FZZE1,SYZQY1,ZYYWSR1,LRZE1,YFJFZC1,SJSJ1"),
    MAINBUSINESSINCOME2("mainBusinessIncome_"+ CompanyExcelBusiness.years[1], "ZZC2,FZZE2,SYZQY2,ZYYWSR2,LRZE2,YFJFZC2,SJSJ2"),
    MAINBUSINESSINCOME3("mainBusinessIncome_"+ CompanyExcelBusiness.years[2], "ZZC3,FZZE3,SYZQY3,ZYYWSR3,LRZE3,YFJFZC3,SJSJ3"),
    TOTALPROFIT1("totalProfit_"+ CompanyExcelBusiness.years[0], "ZZC1,FZZE1,SYZQY1,ZYYWSR1,LRZE1,YFJFZC1,SJSJ1"),
    TOTALPROFIT2("totalProfit_"+ CompanyExcelBusiness.years[1], "ZZC2,FZZE2,SYZQY2,ZYYWSR2,LRZE2,YFJFZC2,SJSJ2"),
    TOTALPROFIT3("totalProfit_"+ CompanyExcelBusiness.years[2], "ZZC3,FZZE3,SYZQY3,ZYYWSR3,LRZE3,YFJFZC3,SJSJ3"),
    TOTALINDUSTYOUTPUT1("totalIndustyOutput_"+ CompanyExcelBusiness.years[0], "GYZCZ1"),
    TOTALINDUSTYOUTPUT2("totalIndustyOutput_"+ CompanyExcelBusiness.years[1], "GYZCZ1"),
    TOTALINDUSTYOUTPUT3("totalIndustyOutput_"+ CompanyExcelBusiness.years[2], "GYZCZ1"),
    RDCOST1("rdCost_"+ CompanyExcelBusiness.years[0], "ZZC1,FZZE1,SYZQY1,ZYYWSR1,LRZE1,YFJFZC1,SJSJ1"),
    RDCOST2("rdCost_"+ CompanyExcelBusiness.years[1], "ZZC2,FZZE2,SYZQY2,ZYYWSR2,LRZE2,YFJFZC2,SJSJ2"),
    RDCOST3("rdCost_"+ CompanyExcelBusiness.years[2], "ZZC3,FZZE3,SYZQY3,ZYYWSR3,LRZE3,YFJFZC3,SJSJ3"),
    PAYMENTVAT1("paymentVat_"+ CompanyExcelBusiness.years[0], "ZZC1,FZZE1,SYZQY1,ZYYWSR1,LRZE1,YFJFZC1,SJSJ1"),
    PAYMENTVAT2("paymentVat_"+ CompanyExcelBusiness.years[1], "ZZC2,FZZE2,SYZQY2,ZYYWSR2,LRZE2,YFJFZC2,SJSJ2"),
    PAYMENTVAT3("paymentVat_"+ CompanyExcelBusiness.years[2], "ZZC3,FZZE3,SYZQY3,ZYYWSR3,LRZE3,YFJFZC3,SJSJ3"),
    AVERAGEWORKER("averageWorker_"+ CompanyExcelBusiness.years[2], "QYZRS"),
    REPORTTYPE("reportType_"+ CompanyExcelBusiness.years[2], "QYLX"),
    BUSINESSACTIVE1("businessActive1_"+ CompanyExcelBusiness.years[2], "XMMC1,XMMC2,XMMC3"),
    BUSINESSACTIVE2("businessActive2_"+ CompanyExcelBusiness.years[2], "XMMC1,XMMC2,XMMC3"),
    BUSINESSCCTIVE3("businessCctive3_"+ CompanyExcelBusiness.years[2], "XMMC1,XMMC2,XMMC3");

    String code;
    String message;

    FiledsTipsEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static String getMessage(String code) {
        if (StringUtils.isEmpty(code)) {
            return "";
        } else {
            for (FiledsTipsEnum s : FiledsTipsEnum.values()) {
                if (s.getCode().equals(code)) {
                    return s.getMessage();
                }
            }
            return "";
        }
    }
}
