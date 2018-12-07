package test;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class StudentTest {
    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = factory.newDocumentBuilder();
        Document document = db.parse(new File("src/test/student.xml"));
         
        NodeList node = document.getElementsByTagName("学生");
        for(int i=0;i<node.getLength();i++){
            Element element = (Element)node.item(i);
            // 获取属性学号
            String content = element.getAttribute("学号");
            System.out.println("学号：" + content);
             
            content = element.getElementsByTagName("姓名").item(0).getFirstChild().getNodeValue();
            System.out.println("姓名：" + content);
             
            content = element.getElementsByTagName("性别").item(0).getFirstChild().getNodeValue();
            System.out.println("性别：" + content);
             
            content = element.getElementsByTagName("年龄").item(0).getFirstChild().getNodeValue();
            System.out.println("年龄：" + content);
            System.out.println();
        }
    }
}
