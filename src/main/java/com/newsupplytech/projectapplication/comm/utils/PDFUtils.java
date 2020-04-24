package com.newsupplytech.projectapplication.comm.utils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.sun.imageio.plugins.common.ImageUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PDFUtils {

    /*wordFile word的路径
      pdfFile pdf原来的路径
      text 水印文字
      pdfPath 打印后需要存放的pdf路径
      backgroundPdfPath 背景图片路径
    */
    public static void word2PDF(String wordFile,String pdfFile,String text,String pdfPath,String backgroundPdfPath){

        ActiveXComponent app = null;
        File target = new File(pdfFile);
        try {
            // 打开word
            app = new ActiveXComponent("Word.Application");
            // 获得word中所有打开的文档
            Dispatch documents = app.getProperty("Documents").toDispatch();
            // 打开文档
            Dispatch document = Dispatch.call(documents, "Open", wordFile, false, true).toDispatch();
            // 如果文件存在的话，不会覆盖，会直接报错，所以我们需要判断文件是否存在
            if (target.isFile()) {
                boolean result = target.delete();
                int tryCount = 0;
                while (!result && tryCount++ < 10) {
                    System.gc(); // 回收资源
                    target.delete();
                }
            }

            Dispatch.call(document, "SaveAs", pdfFile,17);
            // 关闭文档
            Dispatch.call(document, "Close", false);
        }catch(Exception e) {
            System.out.println("转换失败"+e.getMessage());
        }finally {
            // 关闭office
            app.invoke("Quit", 0);
        }

        if(StringUtils.isNotBlank(text)){
            waterMarkBackground(pdfFile,pdfPath,text,backgroundPdfPath);//文本添加水印
        }
    }


    /**
     * @param inputFile 你的PDF文件地址
     * @param outputFile 添加水印后生成PDF存放的地址
     * @param waterMarkName 水印文字
     * @param backgroundPdfPath  水印背景图地址
     * @return
     */
    public static void  waterMarkBackground(String inputFile, String outputFile, String waterMarkName,String backgroundPdfPath) {
        int width = 1049;
        int heigth = 729;
        Font font = new Font("微软雅黑", Font.ROMAN_BASELINE, 33);//字体
        BufferedImage bi1 = ImagePdfUtil.waterMarkByText(width, heigth, waterMarkName,
                Color.orange, font, -30d, 1.0f);//给图片添加文字水印
        try {
            ImageIO.write(bi1, "png", new File(backgroundPdfPath));//写入文件
        } catch (IOException e) {
            e.printStackTrace();
        }

        //加载PDF文档
        com.spire.pdf.PdfDocument pdf = new PdfDocument();
        //加载测试文档
        pdf.loadFromFile(inputFile);

        PdfPageBase pb = pdf.getPages().add(); //新增一页
        pdf.getPages().remove(pb); //去除第一页水印

        PdfPageBase page;
        //获取文档的总页数
        int pageCount = pdf.getPages().getCount();

        //遍历页面，设置背景图片
        for(int i = 0; i < pageCount; i ++) {
            page = pdf.getPages().get(i);
            page.setBackgroundImage(backgroundPdfPath);
        }

        //保存文档
        pdf.saveToFile(outputFile);
        pdf.close();
    }

    /**
     * @param inputFile 你的PDF文件地址
     * @param outputFile 添加水印后生成PDF存放的地址
     * @param waterMarkName 你的水印
     * @return
     */
   /* public static boolean waterMark(String inputFile, String outputFile, String waterMarkName) {
        PdfReader reader=null;
        PdfStamper stamper=null;
        try {
            reader = new PdfReader(inputFile);
            stamper = new PdfStamper(reader, new FileOutputStream(outputFile));
            //这里的字体设置比较关键，这个设置是支持中文的写法
            BaseFont base = BaseFont.createFont("STSong-Light",
                    "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);// 使用系统字体
            int total = reader.getNumberOfPages() + 1;

            PdfContentByte under;
            Rectangle pageRect = null;
            for (int i = 1; i < total; i++) {
                for (int j = 1; j < 4; j++) {
                    pageRect = stamper.getReader().getPageSizeWithRotation(i);
                    // 计算水印X,Y坐标
                    float x = pageRect.getWidth()/5;
                    float y = pageRect.getHeight()/3;
                    // 获得PDF最顶层
                    under = stamper.getOverContent(i);
                    under.saveState();
                    // set Transparency
                    PdfGState gs = new PdfGState();
                    // 设置透明度为0.2
                    gs.setFillOpacity(1.f);
                    under.setGState(gs);
                    under.restoreState();
                    under.beginText();
                    under.setFontAndSize(base, 30);
                    under.setColorFill(BaseColor.ORANGE);

                    // 水印文字成45度角倾斜
                    under.showTextAligned(Element.ALIGN_CENTER, waterMarkName, (x + 50) * j, (y+30) * j, 55);
                    under.showTextAligned(Element.ALIGN_CENTER, waterMarkName, (x + 160) * j, (y+30) * j, 55);
                    // 添加水印文字
                    under.endText();
                    under.setLineWidth(1f);
                    under.stroke();
                }

            }
            stamper.close();
            reader.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally {
            try {
                stamper.close();
                reader.close();
            }catch (Exception e){

            }
        }
    }*/




}
