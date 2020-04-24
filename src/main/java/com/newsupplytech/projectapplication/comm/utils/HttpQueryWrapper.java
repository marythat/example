package com.newsupplytech.projectapplication.comm.utils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;


public class HttpQueryWrapper {
    private enum ParamType{
        S,  //字符串类型
        N,  //整形
        D  //日期型 yyyy-MM-dd
    }

    private static final String START = "_start";
    private static final String END = "_end";
    private static final String STAR = "*";
    private static final String COMMA = ",";
    private static final String NOT_EQUAL = "!";

    /**排序列*/
    private static final String ORDER_COLUMN = "column";
    /**排序方式*/
    private static final String ORDER_TYPE = "order";
    private static final String ORDER_TYPE_ASC = "ASC";

    /**
     * 根据request对象构建查询条件
     * @param parameterMap
     * @param <T>
     * @return
     */
    public static <T> QueryWrapper<T> genQueryWrapper(Map<String, String[]> parameterMap){
        QueryWrapper<T> queryWrapper = new QueryWrapper();
        if(parameterMap == null){
            return queryWrapper;
        }

        Object columnValue = null;
        for (String columnName:parameterMap.keySet()){
            //排除固定的特殊变量名称
            if("pageNo".equalsIgnoreCase(columnName)
                    ||"pageSize".equalsIgnoreCase(columnName)
                    ||ORDER_COLUMN.equalsIgnoreCase(columnName)
                    ||ORDER_TYPE.equalsIgnoreCase(columnName)){
                continue;
            }
            //变量值为空
            if(StringUtils.isBlank(parameterMap.get(columnName)[0])){
                continue;
            }

            columnValue = parseValue(parameterMap.get(columnName));
            // 1.判断是否有区间值
            if (columnName.endsWith(START)) {
                //去除后面部分并驼峰命名转换为下划线
                columnName = StringUtils.humpToLine(columnName.replace(START,""));
                queryWrapper.ge(columnName,columnValue);
            }

            else if (columnName.endsWith(END)) {
                columnName = StringUtils.humpToLine(columnName.replace(END,""));
                queryWrapper.le(columnName,columnValue);
            }

            else if(columnValue instanceof String ){
                String strVal = (String)columnValue;
                // 2.模糊查询
                if(strVal.contains(STAR)) {
                    if (strVal.startsWith(STAR)&&!strVal.endsWith(STAR)) {
                        strVal = StringUtils.substringAfter(strVal, "*");
                        queryWrapper.likeLeft(StringUtils.humpToLine(columnName), strVal);
                    }else if (!strVal.startsWith(STAR)&&strVal.endsWith(STAR)) {
                        strVal = StringUtils.substringBeforeLast(strVal, "*");
                        queryWrapper.likeRight(StringUtils.humpToLine(columnName), strVal);
                    }else {
                        strVal = StringUtils.substringBeforeLast(StringUtils.substringAfter(strVal, "*"),"*");
                        queryWrapper.like(StringUtils.humpToLine(columnName), strVal);
                    }
                }
                // 3.非查询
                else if(strVal.startsWith(NOT_EQUAL)){
                    queryWrapper.ne(StringUtils.humpToLine(columnName), strVal.substring(1));
                }else{
                    queryWrapper.eq(StringUtils.humpToLine(columnName),columnValue);
                }
            }
            // 4.普通查询
            else{
                queryWrapper.eq(StringUtils.humpToLine(columnName),columnValue);
            }

        }
        // 5.排序查询
        String[] orderColumns = parameterMap.get(ORDER_COLUMN);
        if(orderColumns!=null) {
            for (int i = 0; i < orderColumns.length; i++) {
                orderColumns[i] = StringUtils.humpToLine(orderColumns[i]);
            }
        }
        String[] orderType = parameterMap.get(ORDER_TYPE);
        if(orderType!=null && orderType.length>0){
            if(ORDER_TYPE_ASC.equalsIgnoreCase(orderType[0])){
                queryWrapper.orderByAsc(orderColumns);
            }else{
                queryWrapper.orderByDesc(orderColumns);
            }
        }else {
            queryWrapper.orderByAsc(orderColumns);
        }
        return queryWrapper;
    }

    private static Object parseValue(String[] valueArr){
        if(valueArr==null||valueArr.length<1){
            return null;
        }
        String [] value = valueArr[0].split("@");

        if(value.length==1){
            return valueArr[0];
        }

        switch (ParamType.valueOf(value[0])){
            case S:
                return value[1];
            case N:
                return  Long.parseLong(value[1]);
            case D:
                return DateUtils.convert(value[1]);//.str2Date(value[1],DateUtils.date_sdf);
        }
        return valueArr[0];
    }

    public static <T> QueryWrapper<T> genQueryWrapper(T pageData) throws Exception{
        QueryWrapper<T> queryWrapper = new QueryWrapper();
        Object columnValue = null;
        for(Method method:pageData.getClass().getDeclaredMethods()) {
            if (method.getName().contains("get")) {
                String name = method.getName().replace("get", "");
                name = name.substring(0, 1).toLowerCase() + name.substring(1);
                columnValue = method.invoke(pageData);
                if(columnValue==null)continue;
                Field field = pageData.getClass().getDeclaredField(name);
                String columnName = null;
                if ("id".equals(name)) {
                    TableId annotation = field.getAnnotation(TableId.class);
                    columnName = annotation.value();
                } else {
                    TableField annotation = field.getAnnotation(TableField.class);
                    columnName = annotation.value();
                }
                queryWrapper.eq(columnName, columnValue);
            }
        }
        return queryWrapper;
    }

}
