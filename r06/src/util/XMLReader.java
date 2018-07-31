package util;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XMLReader {
	public static String readFromXML(String filename,String nodeName,String attributeName){
	// 解析books.xml文件
    // 创建SAXReader的对象reader
	String returnValue="";
    SAXReader reader = new SAXReader();
    try {
        // 通过reader对象的read方法加载books.xml文件,获取docuemnt对象。
        Document document = reader.read(new File(filename));
        // 通过document对象获取根节点bookstore
        Element bookStore = document.getRootElement();
        // 通过element对象的elementIterator方法获取迭代器
        Iterator it = bookStore.elementIterator();
        // 遍历迭代器，获取根节点中的信息（书籍）
        while (it.hasNext()) {
            Element book = (Element) it.next();
            // 获取book的属性名以及 属性值
            List<Attribute> bookAttrs = book.attributes();
            for (Attribute attr : bookAttrs) {
            	
            	if(attr.getName().equals(attributeName)&& !Objects.isNull(attributeName)){
                System.out.println("属性名：" + attr.getName() + "--属性值："
                        + attr.getValue());
                returnValue=attr.getValue();
            	}
            	
            }
            if(Objects.isNull(attributeName)){
            	Iterator itt = book.elementIterator();
                while (itt.hasNext()) {
                    Element bookChild = (Element) itt.next();
                    if(bookChild.getName().equals(nodeName)){
                        System.out.println("nodeName:"+bookChild.getName());
                    	returnValue=bookChild.getStringValue();
                    	System.out.println("value:"+returnValue);
                  }
                    }
            } 
            
        }
    } catch (DocumentException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        return "error";
    }
	return returnValue;
	
	}
}
