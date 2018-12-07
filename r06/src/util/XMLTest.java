package util;

 import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
 public class XMLTest{
 
 /**
 * 使用dom技术对xml进行解析
 * @param args
 * 从这里我发现： Node 是 Element, document的父类， Element类主要是
 * 增加，删除，修改，返回 等。document 创建 xml中的对象
 * 例：document.getElementById();方法。
 */
 public static void main(String[] args) throws Exception{
 // TODO Auto-generated method stub
 //创建一个documentBuilderFactory实例
 DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
 //创建一个documentBuilder
 DocumentBuilder db=dbf.newDocumentBuilder();
 //指定是那个xml文件
 Document document = db.parse("src/util/class.xml");
 //list(document);
 //red(document);
 update(document);
 upda(document);
 }
 //修改
 public static void update(Document doc){
 //修改元素内容
 Element ele = (Element)doc.getElementsByTagName("mingzi").item(0);
 ele.setTextContent("xiaohai");
 
 //修改属性
 Element element = (Element)doc.getElementsByTagName("xuesheng").item(0);
 element.setAttribute("sex", "nv");
 }
 
 //删除学生
 public static void del(Document doc){
 Node node = doc.getElementsByTagName("xuesheng").item(0);
 //node.getParentNode().removeChild(node);
 //删除属性
 Element ele = (Element)node; 
 ele.removeAttribute("sex");
 }
 
 //添加学生到xml
 public static void add(Document doc){
 //取出这个元素
 Element element = doc.createElement("xuesheng");
 
 //添加属性
 element.setAttribute("sex", "vc");
 
 Element element_name = doc.createElement("mingzi");
 element_name.setTextContent("xiaoming");
 Element element_nianling = doc.createElement("nianling");
 element_nianling.setTextContent("");
 Element element_jieshao = doc.createElement("jieshao");
 element_jieshao.setTextContent("gi sh");
 element.appendChild(element_name);
 element.appendChild(element_nianling);
 element.appendChild(element_jieshao);
 
 
 //添加这个元素
 doc.getDocumentElement().appendChild(element);
 
 }
 //更新java在xml文件中操作的内容
 public static void upda(Document doc) throws Exception{
 //创建一个TransformerFactory实例
 TransformerFactory tff = TransformerFactory.newInstance();
 //通过TransformerFactory 得到一个转换器
 Transformer tf = tff.newTransformer();
 //通过Transformer类的方法 transform(Source xmlSource, Result outputTarget) 
 //将 XML Source 转换为 Result。
 tf.transform(new DOMSource(doc), new StreamResult("src/util/class.xml"));
 }
 
 //遍历xml文件的元素
 public static void list(Node node){
 if(node.getNodeType()==Node.ELEMENT_NODE)
 System.out.println(node.getNodeName());
 //得到该结点的子结点
 NodeList nodelist = node.getChildNodes();
 
 for(int i=0;i<nodelist.getLength();i++){
 Node n = (Node) nodelist.item(i);
 list(n);
 }
 }
 //获取document对象的 元素的 文本
 public static void red(Document docu){
 NodeList nodelist = docu.getElementsByTagName("xuesheng");
 Element element = (Element)nodelist.item(0);
 System.out.println(element.getAttribute("sex"));
 System.out.println(element.getTextContent());
 }
 }
