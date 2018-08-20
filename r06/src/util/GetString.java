package util;

public class GetString {
     /**
      * type=1: get projectName  
      * type=2: get fileName
      * type=3: get subDirectory
      
      * type=5:get base package
      * @param input
      * @param type
      * @return
      */
	 public static String getMessage(String input,int type){
		        
		    String returnStr = "";
		    //one.drl
		    if(type==2){			
			int endIndex2=input.indexOf(".");
			returnStr=input.substring(0, endIndex2);
			System.out.println("file name:"+returnStr);
			return returnStr;
		    }else{
		    	//"/four/plan1/rules"
			    ///four/plan1/rules/personalCommon/behavioralCharac
		    	
		    	String containerName=input.substring(1);
			    int beginIndex=containerName.indexOf("/");
				
				String base=containerName.substring(beginIndex);
				if(type==1){	  			
					returnStr=containerName.substring(0,beginIndex);
					System.out.println("project name:"+returnStr);
					
				    }
				if(type==3){
			    	String projectName=containerName.substring(0,beginIndex);
			    	base=containerName.substring(beginIndex);
			    	returnStr=input.substring(projectName.length()+2);
			    	System.out.println("subDirectory:"+returnStr);
			    			    	
			    }
				 if(type==5){
						String basePackage =base.replace("/",".").substring(1);
						System.out.println("basePackage:"+basePackage);
						return basePackage;
				}
		    }
		    
		    
		   
		    
		    return returnStr;
		    
	 }
	 /**
	  * get Directory
	  * @param s
	  * @return
	  */
	 public static String[] getDirectory(String s){
		 return s.substring(0).split("/");
	 }
	 /**
	  * get package
	  * @param input
	  * @return
	  */
	 public static String getPackage(String s){
		 return s.substring(0).replaceAll("/", ".");
	 }
}
