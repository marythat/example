package com.newsupplytech.projectapplication.comm.utils.mybatisplus;


import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

@Slf4j
@Component
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class DatetimeGenerateInterceptor implements Interceptor {
    public static final String FIELD_CREATE_TIME = "createTime";
    public static final String FIELD_UPDATE_TIME = "updateTime";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        Object parameter = invocation.getArgs()[1];

        if (parameter == null) {
            return invocation.proceed();
        }

        if (SqlCommandType.INSERT == sqlCommandType || SqlCommandType.UPDATE == sqlCommandType) {
            Field[] fields = getAllFields(parameter);
            if(parameter instanceof ParamMap) {
                ParamMap<?> p = (ParamMap<?>) parameter;
                if (p.containsKey("et")) {
                    parameter = p.get("et");
                } else {
                    parameter = p.get("param1");
                }
                fields = getAllFields(parameter);
            }

            if (SqlCommandType.INSERT == sqlCommandType) {
                generateFieldTime(parameter, fields, FIELD_CREATE_TIME);
            }
            generateFieldTime(parameter, fields, FIELD_UPDATE_TIME);
        }
        return invocation.proceed();
    }

    private void generateFieldTime(Object parameter, Field[] fields, String timeColumn) {
        for (Field field : fields) {
            try {
                if (timeColumn.equals(field.getName())) {
                    field.setAccessible(true);
                    Object local_createDate = field.get(parameter);
                    field.setAccessible(false);
                    if (FIELD_UPDATE_TIME.equalsIgnoreCase(timeColumn) || local_createDate == null || local_createDate.equals("")) {
                        field.setAccessible(true);
                        field.set(parameter, new Date());
                        field.setAccessible(false);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // TODO Auto-generated method stub
    }

    public static Field[] getAllFields(Object object) {
        Class<?> clazz = object.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }

}
