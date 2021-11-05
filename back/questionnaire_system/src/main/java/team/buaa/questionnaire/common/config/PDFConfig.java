package team.buaa.questionnaire.common.config;

import com.itextpdf.text.pdf.BaseFont;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileOutputStream;
import java.io.OutputStream;

public class PDFConfig {
    private static final String OUT_PUT_PDF_PATH = "D:/资料/3 大三上/小学期/pdf/"; //pdf文件的存放路径（windows）

    /**
     * @Title 网页转存为PDF文件
     * @param url 链接地址
     * @param outputFileName 转存的PDF文件名
     */
    public static void urlToPdf(String url, String outputFileName) {
        try {
            String folder = outputFileName.substring(0, 6);
            String outputFile = OUT_PUT_PDF_PATH + outputFileName;
            java.io.File targetFile = new java.io.File(outputFile);
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs(); // 创建父级文件路径
            }
            OutputStream os = new FileOutputStream(outputFile);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocument(url);
            ITextFontResolver fontResolver = renderer.getFontResolver();
            //fontResolver.addFont("/usr/share/fonts/chinese/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.EMBEDDED); //Linux
            fontResolver.addFont("c:/Windows/Fonts/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED); //windows
            renderer.layout();
            renderer.createPDF(os);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
