package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
/**
 *  读出原有xml文件的内容数据，然后将数据写入到磁盘上的XML文件
 */
public class DOM4JWriteDemo {
    
    public static void main(String[] args) throws DocumentException, IOException {
        //读取已存在的Xml文件person.xml
        Document doc=new SAXReader().read(new File("src/util/books.xml"));
        
        //指定文件输出的位置
        FileOutputStream out =new FileOutputStream("src/util/books2.xml");
        
        //1.创建写出对象
       XMLWriter writer=new XMLWriter(out);
       
       //2.写出Document对象
       writer.write(doc);
       
       //3.关闭流
       writer.close();
    }
}
