package util;
import java.lang.*;

import net.sf.json.JSONObject;


public class StringDemo {

  public static void main(String[] args) {
  
    /*String str = "www.yiibai.com";
    System.out.println(str);

    // the start string to be checked
    String startstr1 = "www";
    String startstr2 = "http://";

    // checks that string starts with given substring
    boolean retval1 = str.startsWith(startstr1);
    boolean retval2 = str.startsWith(startstr2);

    // prints true if the string starts with given substring
    System.out.println("starts with " + startstr1 + " ? " + retval1);
    System.out.println("starts with " + startstr2 + " ? " + retval2);
    try{
    JSONObject params = new JSONObject();
    
    params.put("applyNo", "201810222312");
    params.put("resultType", true);
    params.put("resultMessage", "");
    System.out.println(params.toString());*/
    
    
    /*String projectName1="/three";
    String projectName=projectName1.substring(1,projectName1.length() );
    System.out.println(projectName);*/
//    }
//    catch(Exception e){
//    	e.printStackTrace();
//    }
	
	
	/*String filename="one.drl";	
	String name=GetString.getMessage(filename, 2);
	System.out.println("name:"+name);
	
	//String s="/four/plan1/rules/personalCommon/behavioralCharac";
	String s="/four/plan1/rules";
	String projectName=GetString.getMessage(s, 1);
	System.out.println("project name:"+projectName);
	
	String subDirectory=GetString.getMessage(s, 3);
	
	int begin=subDirectory.indexOf("/");
	String s4=subDirectory.substring(begin+1);
	int begin2=s4.indexOf("/");
	String s5=s4.substring(begin2+1);
	String ruleGroup=GetString.getPackage(s5);
	System.out.println("ruleGroup:"+ruleGroup);
	
	String[] s2=GetString.getDirectory(s5);
	for(int i=0;i<s2.length;i++){
		System.out.println(s2[i]);
	}*/
	
	StringBuffer buf4 = new StringBuffer();
	buf4.append("/**section package  ;**/ {\n");       
    buf4.append("package com.xujin.demo;\n");
	buf4.append("/**section import  ;**/ \n\n");

    buf4.append("rule \""+"rules.one"+"\"\n");
    buf4.append("        salience 1\n");
    buf4.append("        no-loop\n");
    buf4.append("        lock-on-active true\n");
    buf4.append("        ruleflow-group \""+"rules"+"\"\n\n");
    buf4.append("        when\n\n");
	buf4.append("        then\n\n");
	buf4.append("        end\n\n");
	String contents=buf4.toString();
	
	//System.out.printf("contents:{}",contents);
	System.out.println("old contents:\n"+contents);
	String[] s=contents.split("\n");
	String salienceValue="";
	String oldSalienceRow="";
	String oldLockOnActiveRow="";
    String lockOnActiveValue="";
	for(int i=0;i<s.length;i++){
		if(s[i].contains("salience")){
			System.out.println(s[i]);
			oldSalienceRow=s[i];
			String[] subs=s[i].split(" ");
			for(int j=0;j<subs.length;j++){
				if(subs[j].equals("salience")){
					salienceValue=subs[j+1];
				}
			}
			
			System.out.println("value:"+salienceValue);
			
		}
		if(s[i].contains("lock-on-active")){
			oldLockOnActiveRow=s[i];
			System.out.println(s[i]);
			String[] subs=s[i].split(" ");
			for(int j=0;j<subs.length;j++){
				
				if(subs[j].equals("lock-on-active")){
					lockOnActiveValue=subs[j+1];
				}
			}
			System.out.println("value:"+lockOnActiveValue);
		}
	}
	String newsalienceValue="2";
	String newSalienceRow=oldSalienceRow.replace(salienceValue, newsalienceValue);
	contents=contents.replace(oldSalienceRow, newSalienceRow);
			
	String newlockOnActiveValue="f alse";
	String newLockOnActiveRow=oldLockOnActiveRow.replace(lockOnActiveValue, newlockOnActiveValue);
	contents=contents.replace(oldLockOnActiveRow, newLockOnActiveRow);
	System.out.println("new Contents:\n"+contents);
	
	
	
	
	
	
    //System.out.printf("salience:%s%nlock-on-active:%s%n",salienceValue,lockOnActiveValue);
    
	
	/*System.out.println("contents:\n"+contents);
	String[] s=contents.split("\n");
	String salienceValue="";
    String lockOnActiveValue="";
	for(int i=0;i<s.length;i++){
		if(s[i].contains("salience")){
			System.out.println(s[i]);
			salienceValue=s[i].split(" ")[1];
			System.out.println("value:"+salienceValue);
			
		}
		if(s[i].contains("lock-on-active")){
			lockOnActiveValue=s[i].split(" ")[1];	
			System.out.println(s[i]);
			System.out.println("value:"+lockOnActiveValue);
		}
	}
    */
	
	
	String s1=getProjectName("C:\\Users\\jjw8610\\runtime-EclipseApplication\\2018091402");
	System.out.println(s1);
	
	
	

    
  }
  
  public static String getProjectName(String projectPath){
  	String projectName=projectPath.substring(projectPath.lastIndexOf("\\")+1);
      return projectName;
  }
}