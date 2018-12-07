package test;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GetActivePlanName {
	public static void main(String args[]){
	String filePath="src/test/default.cfg";
     
    try{
    	 String activePlanName= getActivePlanName(filePath);
    	 System.out.println("activePlanName:"+activePlanName);
    	 
    }catch (Exception e) {
       
        e.printStackTrace();
    }
	}
	
	public static String getActivePlanName(String filePath) throws ParserConfigurationException, SAXException, IOException{
   	 String activePlanName="";
   	 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = factory.newDocumentBuilder();
        Document document = db.parse(new File(filePath));
         
        NodeList node = document.getElementsByTagName("Plan");
        for(int i=0;i<node.getLength();i++){
            Element element = (Element)node.item(i);
            // 获取属性
            String isActive = element.getAttribute("isActive");
            System.out.println("isActive:" + isActive);
            if(isActive.equals("true")){
            // 获取属性
            String name = element.getAttribute("name");
            System.out.println("planName:" + name);
            activePlanName=name;
            }
            
        }
        return activePlanName;
    }
}
