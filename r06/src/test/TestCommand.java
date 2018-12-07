package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import util.ProjectUtil;
import util.XmlUtil;
public class TestCommand {
     final static Logger log =  Logger.getLogger("TestCommand");
     @SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws FileNotFoundException {
     	/*Properties props=System.getProperties(); //系统属性
     	System.out.println("用户的当前工作目录："+props.getProperty("user.dir"));
     	
     	String file = props.getProperty("user.dir");
     	//将设为true的plan 在Target下新建为"plan"的方案名，并将同目录下的规则文件合并，以及拷贝risk flow文件到target目录的plan下。
     	String filePath=file+"/src/test/default.cfg";
     	     
        try{
        	 String activePlanName= ProjectUtil.getActivePlanName(filePath);
        	 System.out.println("activePlanName:"+activePlanName);
        	 
        }catch (Exception e) {
           
            e.printStackTrace();
        }*/
       
        
    	 
     /* //为正确获取方式
  		String filePath1="/src/test/.classpath";
  		try {
  			ProjectUtil.addToBuildpath(filePath1, "commons-lang3-3.8.jar");
  		} catch (ParserConfigurationException | SAXException | IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
  		
  		
  		String filePath2="/src/test/.classpath";
  		try {
  			ProjectUtil.removeFromBuildpath(filePath1, "commons-lang3-3.8.jar");
  		} catch (ParserConfigurationException | SAXException | IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}*/
			 
    	 List<Map> xmlMap=XmlUtil.readXml("src\\test\\errDevice.xml");
    	 for(int i=0;i<xmlMap.size();i++){
    		 Map m=xmlMap.get(i);
    		 //System.out.println(m.isEmpty());
    		 Set<String> s=m.keySet();
    		 Iterator it=s.iterator();
    		 String key = null;
    		 while(it.hasNext()){
    			 key=(String) it.next();
    			 //System.out.println("key:"+key);
    		 }
    		 System.out.println("key:"+key+"--value:"+m.get(key));
    	 }
    	 /*try {
			XmlUtil.addNode("src\\test\\errDevice.xml","192.168.1.1");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    	 
    	 XmlUtil.delXmlCode("src\\test\\errDevice.xml","192.168.1.1");
    	 
       /* try {
              Process process=null;//成功！直接执行
              String os = props.getProperty("os.name");
              if (os != null && os.toLowerCase().indexOf("linux") > -1) {
                    process= Runtime.getRuntime().exec("sh mvn install",null,new File(file));
              } else {
                    process= Runtime.getRuntime().exec("cmd /c mvn install",null,new File(file));
                    InputStream inputStream = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(inputStream);
			InputStream errorStream = process.getErrorStream();
			InputStreamReader esr = new InputStreamReader(errorStream);
                    int n1;
 			char[] c1 = new char[1024];
			StringBuffer standardOutput = new StringBuffer();
			while ((n1 = isr.read(c1)) > 0) {
  			standardOutput.append(c1, 0, n1);}
                    System.out.println("Standard Output: " + standardOutput.toString());
                    int n2;
			char[] c2 = new char[1024];
			StringBuffer standardError = new StringBuffer();
			while ((n2 = esr.read(c2)) > 0) {
  			standardError.append(c2, 0, n2);}
                    System.out.println("Standard Error: " + standardError.toString());
              		}
					} catch (Exception e) {
	            e.printStackTrace();
        }*/
      
     }

     
     
     
     }
