package test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.UUID;

import util.FileUtilsByUs;

public class AddNewPlan {
	public static void main(String[] args) {
		addAndModifyCfgFile("plan3");
		
	}
	private static void addAndModifyCfgFile(String planName) {
		// TODO Auto-generated method stub
		Properties props=System.getProperties(); //系统属性
		String file = props.getProperty("user.dir");
		//String filePath=file+"\\resources\\default.cfg";
		String filePath=file+"\\src\\test\\default.cfg";
		String content=FileUtilsByUs.readFile(filePath);
		BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(content.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));  
		 String line; 
		 
		 try{
			 StringBuffer buf01 = new StringBuffer();
			 String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			 while ( (line = br.readLine()) != null ) {  
			     if(!line.trim().equals("")){
			    	  	 
			    	 
			    	 if(line.contains("</Solution>")){
			    		 						       
						   buf01.append("<Plan id=\""+uuid+"\" name=\""+planName+"\" isActive=\"false\">\n");
			       		   buf01.append("    <Models>\n");
						   buf01.append("      <!-- 添加一个 out model -->\n");
						   buf01.append("      <Model type=\"out\" id=\"*\"/>\n");
						   buf01.append("      <!-- 添加一个或多个in model-->\n");
						   buf01.append("      <Model type=\"in\" id=\"*\"/>\n");
					       buf01.append("    </Models>\n");
					       buf01.append("    </Plan>\n");
					       buf01.append("</Solution>\n");
					       buf01.toString();
				    }
			    	 
			    	 
			    	 
			     }       		    	 
			 } 
			 content=content.replace("</Solution>", buf01.toString());
			 System.out.println(content);
			 FileUtilsByUs.writeFile(filePath,content);
			 }catch(Exception e){
				 System.out.println(e.getMessage());
				 
			 }
		 
		 
	}
	
	

}
