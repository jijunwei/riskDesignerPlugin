package util;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 *  1.读出原有xml文件的内容数据，保存至Document对象
 *  2.修改输出的format（输出格式、编码...）
 *  3.然后将document包含数据写入到磁盘上的XML文件
 */
public class WriteDemo2 {
    
    public static void main(String[] args) throws DocumentException, IOException {
        //读取已存在的Xml文件person.xml
        Document doc=new SAXReader().read(new File("src/util/books.xml"));
        
        //指定文件输出的位置
        FileOutputStream out =new FileOutputStream("src/util/books3.xml");
        /**
         *  指定文本的写出的格式：
         *      紧凑格式 ：项目上线时候使用
         *      漂亮格式：开发调试的时候使用
         */
        //OutputFormat format=OutputFormat.createCompactFormat();  //紧凑格式:去除空格换行
        OutputFormat format=OutputFormat.createPrettyPrint();   //漂亮格式：有空格换行
        
        /**
         * 指定生成的xml文档的编码影响了：
         *         1.xml文档保存时的编码
         *         2.xml文档声明的encoding编码（Xml解析时的编码）
         * 结论：使用该方法生成Xml文档可以避免中文乱码问题
         */
        format.setEncoding("UTF-8");
        
        //1.创建写出对象
       XMLWriter writer=new XMLWriter(out,format);
       
       //2.写出Document对象
       writer.write(doc);
       
       //3.关闭流
       writer.close();
    }
}
