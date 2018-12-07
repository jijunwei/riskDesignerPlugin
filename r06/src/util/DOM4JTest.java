package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.Test;

public class DOM4JTest {
	/**
	* 1.创建Document对象，
	* 添加：各种标签、属性、文本
	* 修改：属性值、文本
	* 删除：标签、属性
	* 2.修改输出的format（输出格式、编码...）
	* 3.然后将document包含数据写入到磁盘上的XML文件
	*/
	/**
     * 增加：文档、标签、属性 
     */
    @Test
    public static void testAdd() throws DocumentException, IOException {
        //1.创建文档
        Document doc=DocumentHelper.createDocument();
        //2.添加标签
        Element rootElem=doc.addElement("root");
        Element bookElem=rootElem.addElement("book");
        bookElem.addElement("name");
        bookElem.addElement("year");
        bookElem.addElement("price");
        //4.增加属性
        bookElem.addAttribute("id", "3");
        
        //指定文件输出的位置
        FileOutputStream out =new FileOutputStream("src/util/books.xml");
        
        // 指定文本的写出的格式：
        OutputFormat format=OutputFormat.createPrettyPrint();   //漂亮格式：有空格换行
        format.setEncoding("UTF-8");
        
        //1.创建写出对象
        XMLWriter writer=new XMLWriter(out,format);
           
        //2.写出Document对象
        writer.write(doc);
        
        //3.关闭流
        writer.close();
    }
    /**
     * 修改：属性值、文本
     */
    @Test
    public static void testUpdate() throws DocumentException, IOException{
        //创建Document对象，读取已存在的Xml文件person.xml
        Document doc=new SAXReader().read(new File("src/util/books.xml"));
        
        /**
         * 修改属性值（方案一）   
         * 方法：使用Attribute类(属性类)的setValue()方法
         * 1.得到标签对象
         * 2.得到属性对象
         * 3.修改属性值
         */
    /*    //1.1 得到标签对象
        Element stuElem=doc.getRootElement().element("student");
        //1.2 得到id属性对象
        Attribute idAttr=stuElem.attribute("id");
        //1.3 修改属性值
        idAttr.setValue("000001"); 
    */
        
        /**
         *  修改属性值（方案二）
         *  方法：Element标签类的addAttribute("attr","value")方法
         */
    /*    //1.得到属性值标签
        Element stuElem=doc.getRootElement().element("student");
        //2.通过增加同名属性的方法，修改属性值
        stuElem.addAttribute("id", "000009");  //key相同，覆盖；不存在key，则添加
     */
        /**
         * 修改文本
         * 方法：Element标签类的setTest("新文本值")方法
         * 1.得到标签对象
         * 2.修改文本
         */
        Element nameElem=doc.getRootElement().element("book").element("name");
        nameElem.setText("王二麻子");
        
        //指定文件输出的位置
        FileOutputStream out =new FileOutputStream("src/util/books.xml");
        // 指定文本的写出的格式：
        OutputFormat format=OutputFormat.createPrettyPrint();   //漂亮格式：有空格换行
        format.setEncoding("UTF-8");
        //1.创建写出对象
        XMLWriter writer=new XMLWriter(out,format);
        //2.写出Document对象
        writer.write(doc);
        //3.关闭流
        writer.close();
    }
    
    /**
     * 删除：标签、属性
     */
    @Test
    public static void testDelete() throws DocumentException, IOException{
        //创建Document对象，读取已存在的Xml文件person.xml
        Document doc=new SAXReader().read(new File("src/util/books.xml"));
        
        /**
         * 删除标签
         * 1.得到标签对象
         * 2.删除标签对象（可以通过父类删除子标签，也可以自己删自己）
         */
/*        Element ageElement=doc.getRootElement().element("student").element("age");
         ageElement.detach();  //直接detch()删除
        //ageElement.getParent().remove(ageElement); //先获取父标签，然后remove()删除子标签
*/
        /**
         * 删除属性
         */
        //1.得到标签对象
        //等同于Element stuElem=doc.getRootElement().element("student");
        Element stuElem=(Element) doc.getRootElement().elements().get(0);
        //2.得到属性对象
        Attribute idAttr=stuElem.attribute("id");
        //3.删除属性
        idAttr.detach();
        
        
        //指定文件输出的位置
        FileOutputStream out =new FileOutputStream("src/util/books.xml");
        // 指定文本的写出的格式：
        OutputFormat format=OutputFormat.createPrettyPrint();   //漂亮格式：有空格换行
        format.setEncoding("UTF-8");
        //1.创建写出对象
        XMLWriter writer=new XMLWriter(out,format);
        //2.写出Document对象
        writer.write(doc);
        //3.关闭流
        writer.close();
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        /*// 解析books.xml文件
        // 创建SAXReader的对象reader
        SAXReader reader = new SAXReader();
        List<Book> list=new ArrayList<Book>();
        try {
            // 通过reader对象的read方法加载books.xml文件,获取docuemnt对象。
            Document document = reader.read(new File("src/util/books.xml"));
            // 通过document对象获取根节点bookstore
            Element bookStore = document.getRootElement();
            // 通过element对象的elementIterator方法获取迭代器
            Iterator it = bookStore.elementIterator();
            // 遍历迭代器，获取根节点中的信息（书籍）
            while (it.hasNext()) {
                System.out.println("=====开始遍历某一本书=====");
                Book book1=new Book();
                Element book = (Element) it.next();
                // 获取book的属性名以及 属性值
                List<Attribute> bookAttrs = book.attributes();
                for (Attribute attr : bookAttrs) {
                	
                    System.out.println("属性名：" + attr.getName() + "--属性值："
                            + attr.getValue());  
                    book1.setId(book.attributeValue("id"));     //读取属性
                }
               
                Iterator itt = book.elementIterator();
                while (itt.hasNext()) {
                    Element bookChild = (Element) itt.next();
                    if(bookChild.getName().equals("name")){
                    System.out.println("节点名：" + bookChild.getName() + "--节点值：" + bookChild.getStringValue());
                    
                    
                }                
                    }
                System.out.println("=====结束遍历某一本书=====");
                book1.setName(book.elementText("name"));  //读取文本
                book1.setAuthor(book.elementText("author"));
                book1.setYear(Integer.parseInt(book.elementText("year")));
                book1.setPrice(Double.parseDouble(book.elementText("price")));
                book1.setLanguage(book.elementText("language"));
                list.add(book1);
           
                for(Book book2 : list){
                    System.out.println(book2.toString());
                }
            }
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        /*try {
			testAdd();
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			testUpdate();
		} catch (DocumentException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
			testDelete();
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        
       // System.out.println("value:"+XMLReader.readFromXML(".project", "projectDescription", null));
        
     // 创建SAXReader的对象reader
     /*   SAXReader reader = new SAXReader();
        try {
            // 通过reader对象的read方法加载books.xml文件,获取docuemnt对象。
            Document document = reader.read(new File("src/util/.project"));
            // 通过document对象获取根节点bookstore
            Element bookStore = document.getRootElement();
            // 通过element对象的elementIterator方法获取迭代器
            Iterator it = bookStore.elementIterator();
            // 遍历迭代器，获取根节点中的信息（书籍）
            while (it.hasNext()) {
                System.out.println("=====开始遍历=====");
                Element elm = (Element) it.next();
                // 获取book的属性名以及 属性值
                List<Attribute> Attrs = elm.attributes();
                for (Attribute attr : Attrs) {
                	
                    System.out.println("属性名：" + attr.getName() + "--属性值："
                            + attr.getValue());  
                }
               
                Iterator itt = elm.elementIterator();
                while (itt.hasNext()) {
                    Element child = (Element) itt.next();
                    System.out.println("节点名：" + child.getName() + "--节点值：" + child.getStringValue());
                    
                    
                    }
                System.out.println("=====结束遍历=====");
    }
        }catch (DocumentException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }*/
        
        String filecontent=FileUtilsByUs.readFile("src/util/.project");
        //System.out.println(filecontent);
        String json=XmlJSON.xml2JSON(filecontent).replace("@", "");
        System.out.println(json);
        Map<String,Object> m=JsonUtil.jsonToMap(json);
        System.out.println(m.get("name"));
        
 }
    
   
}
