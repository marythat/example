package com.newsupplytech.projectapplication.comm.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.Map;

/**
 * #通过Freemarker模板生产word文档的工具类
 * 1. 在word中编辑好模版样式，设置好占位符，注意图片最好先黏上去
 * ![模板图片](http://dl2.iteye.com/upload/attachment/0122/2815/6d9b2f4c-c56d-3635-9dd6-0a2dbf72d484.png)
 * 2. 在word中，文件-另存为-XML格式
 * 3. 使用工具格式化上图的XML文件
 * a.在表格数据的TR中加入<#list myListData as tmp> 和</#list>
 * b.将XML文件中图片的BASE64转换为相应的占位符
 * 4. 在Eclipse中新建文件teample-pic.ftl，将XML的数据粘贴进去
 * 5. 使用工具类WordUtil.java生成word文件
 */
@Component
public class FreemarkerWordUtils {


    private String projectDesc="/freemarker-template";

    /**
     * @param dataMap      word中需要展示的动态数据，用map集合来保存
     * @param templateName word模板名称，例如：teample.ftl
     * @param fileName     生成的文件名称
     */
    public String createWord(Map dataMap, String templateName, String fileName) {
        String filePath = FileStoreUtils.getFileBasePath();

        // 输出文件
        File outFile = new File(filePath + File.separator + fileName);

        // 如果输出目标文件夹不存在，则创建
        if (!outFile.getParentFile().exists()) {
            outFile.getParentFile().mkdirs();
        }

        try {
            createWord(dataMap,templateName,new FileOutputStream(outFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return  fileName;
    }

    /**
     *
     * @param dataMap
     * @param templateName
     * @param out
     */
    public void createWord(Map<String, Object> dataMap, String templateName, OutputStream out) {
        try {
            Version version = new Version("2.3.28");
            // 创建配置实例
            Configuration configuration = new Configuration(version);

            // 设置编码
            configuration.setDefaultEncoding("UTF-8");

            // ftl模板文件
            configuration.setClassForTemplateLoading(FreemarkerWordUtils.class,projectDesc );
            // 获取模板
            Template template = configuration.getTemplate(templateName);

            // 将模板和数据模型合并生成文件
            Writer writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));

            // 生成文件
            template.process(dataMap, writer);

            // 关闭流
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 将图片转换为BASE64为字符串
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static String getImageString(String filename) throws IOException {
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(filename);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            throw e;
        } finally {
            if (in != null) in.close();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return data != null ? encoder.encode(data) : "";
    }
}
