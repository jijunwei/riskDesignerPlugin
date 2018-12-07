package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@SuppressWarnings("restriction")
public class ProjectUtil {
	static MessageConsole console = null;
    static MessageConsoleStream consoleStream = null;
    static IConsoleManager consoleManager = null;
    final static String CONSOLE_NAME = "Console";
    public static IProject project;
    public static String filename=null;
    public static String planname=null;
    public static List<String> dirList = new ArrayList<String>();
    public static File file;
    
    static List<String> dirs=new ArrayList<String>();
    final static Logger log =  Logger.getLogger("r06.util.ProjectUtil");
	public static IProject getCurrentProject(){    
        ISelectionService selectionService =     
            Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService();    
    
        ISelection selection = selectionService.getSelection();    
    
        IProject project = null;    
        if(selection instanceof IStructuredSelection) {    
            Object element = ((IStructuredSelection)selection).getFirstElement();    
    
            if (element instanceof IResource) {    
                project= ((IResource)element).getProject();    
            } else if (element instanceof PackageFragmentRootContainer) {    
                IJavaProject jProject =     
                    ((PackageFragmentRootContainer)element).getJavaProject();    
                project = jProject.getProject();    
            } else if (element instanceof IJavaElement) {    
                IJavaProject jProject= ((IJavaElement)element).getJavaProject();    
                project = jProject.getProject();    
            }    
        }     
        return project;    
    }  
	/**
	 * 先根遍历序递归删除文件夹
	 *
	 * @param dirFile 要被删除的文件或者目录
	 * @return 删除成功返回true, 否则返回false
	 */
	public static boolean deleteFile(File dirFile) {
	    // 如果dir对应的文件不存在，则退出
	    if (!dirFile.exists()) {
	        return false;
	    }

	    if (dirFile.isFile()) {
	        return dirFile.delete();
	    } else {

	        for (File file : dirFile.listFiles()) {
	            deleteFile(file);
	        }
	    }

	    return dirFile.delete();
	}
	public static void updatePerspective(IProject project){
		Workbench workBench=Workbench.getInstance();
		IWorkbenchPage iworkbenchPage=workBench.getActiveWorkbenchWindow().getActivePage();
     

        IPerspectiveDescriptor perspective = workBench.getPerspectiveRegistry().findPerspectiveWithId("org.eclipse.ui.perspectives"); //perspective 的id

        iworkbenchPage.setPerspective(perspective);
    
        BasicNewProjectResourceWizard.selectAndReveal(project, workBench.getActiveWorkbenchWindow());
	}
    
	public static String getCurrentFile(){    
        ISelectionService selectionService =     
            Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService();    
    
        ISelection selection = selectionService.getSelection();    
    
        if(selection instanceof IStructuredSelection) {    
            Object element = ((IStructuredSelection)selection).getFirstElement();    
               
            IFile jarFile = (IFile) element;  
            System.out.println(jarFile.getName()+":"+jarFile.exists());
            filename=jarFile.getName();
            
        }     
          return filename; 
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
            //System.out.println("isActive:" + isActive);
            if(isActive.equals("true")){
            // 获取属性
            String name = element.getAttribute("name");
            //System.out.println("activeplanName:" + name);
            activePlanName=name;
            }
            
        }
        return activePlanName;
    }
	
	public static String addToBuildpath(String filePath,String filename) throws ParserConfigurationException, SAXException, IOException{
	   	 
		    try {
				XmlUtil.addNode(filePath, "classpathentry",filename);
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return "ok";
	    }
	public static String removeFromBuildpath(String filePath,String filename) throws ParserConfigurationException, SAXException, IOException{
		   XmlUtil.delXmlCode(filePath, "classpathentry",filename);
	        return "ok";
	    }
 
    public static void initDefaultConsole() {
    	// 新建一个MessageConsole，可以自定义Console的名称
    	if(console==null){
    	console = new MessageConsole(CONSOLE_NAME, null);
    	}else{
    	// 通过ConsolePlugin得到IConsoleManager，添加自定义的MessageConsole
    	consoleManager = ConsolePlugin.getDefault().getConsoleManager();
    	consoleManager.addConsoles(new IConsole[] { console });
    	
    	// 新建一个MessageConsoleStream， 作用是接收需要打印的消息
    	consoleStream = console.newMessageStream();
    	}
    }
 
    /**
     * 开启console， 打印相关消息
     * @param message 消息内容
     * @return 
     */
    public  static void printMessage(String message) {
        if (message != null) {
            if (console == null) {
            	initDefaultConsole();
            }
            // 显示Console视图
            consoleManager.showConsoleView(console);
            // 打印消息
            consoleStream.print(message + "\n");
		    
        }
    }

    public static String getUTF8StringFromGBKString(String gbkStr) {  
        try {  
            return new String(getUTF8BytesFromGBKString(gbkStr), "UTF-8");  
        } catch (UnsupportedEncodingException e) {  
            throw new InternalError();  
        }  
    }  
      
    public static String getProjectName(String projectPath){
    	String projectName=projectPath.substring(projectPath.lastIndexOf("/"));
        return projectName;
    }
    
    /**
     * 刷新指定项目的指定资源
     * @param refreshType 刷新类型，project表示刷新整个项目，file表示刷新某个文件等
     * @param resourcePath 需要的资源的相对路径，如果是刷新项目，则为空
     * @param projectName   指定项目名称
     */
    public static void refresh(String refreshType, String resourcePath, 
      String projectName){
     IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
     IProject project = root.getProject(projectName);
     if(project.exists()){
      try {

       //刷新项目
       if("project".equals(refreshType)){
        project.refreshLocal(IResource.DEPTH_INFINITE, null);
       }else if("package".equals(refreshType) || 
         "floder".equals(refreshType)){

      //刷新包或者文件夹
        IFolder floder = project.getFolder(resourcePath);
        if(floder.exists()){
         floder.refreshLocal(IResource.DEPTH_INFINITE, null);
        }
       }else if("packagefile".equals(refreshType) || 
         "floderfile".equals(refreshType) || 
         "file".equals(refreshType)){

       //刷新包文件、文件夹文件或者文件
        IFile file = project.getFile(resourcePath);
        if(file.exists()){
         file.refreshLocal(IResource.DEPTH_INFINITE, null);
        }
       }
      } catch (CoreException e) {
       throw new RuntimeException(e);
      }
     }
    }
    public static void refreshFile(String filename){
    	IFile f = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(filename));
		try {
			f.refreshLocal(IResource.DEPTH_ZERO, null);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public static byte[] getUTF8BytesFromGBKString(String gbkStr) {  
        int n = gbkStr.length();  
        byte[] utfBytes = new byte[3 * n];  
        int k = 0;  
        for (int i = 0; i < n; i++) {  
            int m = gbkStr.charAt(i);  
            if (m < 128 && m >= 0) {  
                utfBytes[k++] = (byte) m;  
                continue;  
            }  
            utfBytes[k++] = (byte) (0xe0 | (m >> 12));  
            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));  
            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));  
        }  
        if (k < utfBytes.length) {  
            byte[] tmp = new byte[k];  
            System.arraycopy(utfBytes, 0, tmp, 0, k);  
            return tmp;  
        }  
        return utfBytes;  
    }
public static void initConsole() {
	    consoleManager = ConsolePlugin.getDefault().getConsoleManager();
	    IConsole[] consoles = consoleManager.getConsoles();
	    if(consoles.length > 0){
	        console = (MessageConsole)consoles[0];
	    } else{
	        console = new MessageConsole(CONSOLE_NAME, null);
	        consoleManager.addConsoles(new IConsole[] { console });
	    }
	    consoleStream = console.newMessageStream();
}
public static String modifyPom(String filePath,String oldPlanName,String planName){
	String content=FileUtilsByUs.readFile(filePath);
	 content=content.replace(oldPlanName, planName);
	 System.out.println(content);
	 try {
		FileUtilsByUs.writeFile(filePath,content);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 return "ok";
}
public static String setPersentPlanActive(String filePath, String planName) {
	
	XmlUtil.setNodeAttribute(filePath,"Plan", planName);
     return "ok";
}

public static String removePlan(String filePath, String planName) {
	// TODO Auto-generated method stub
	XmlUtil.delPlan(filePath, "Plan",planName);
    return "ok";
}

public static String getCurrentPlan() {
	// TODO Auto-generated method stub
	ISelectionService selectionService =     
            Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService();    
    
        ISelection selection = selectionService.getSelection();    
    
        if(selection instanceof IStructuredSelection) {    
            Object element = ((IStructuredSelection)selection).getFirstElement();    
               
            IFolder folder = (IFolder) element;  
            System.out.println(folder.getName()+":"+folder.exists());
            planname=folder.getName();
            
        }     
          return planname; 
}

public static String getDestDir(String filePath, String string, String string2) {
	// TODO Auto-generated method stub
	return XmlUtil.getDestDir(filePath,string,string2);
}

public static void deployProject(IProject project,Shell shell,Boolean dialog) {
	// TODO Auto-generated method stub
	
	Properties props=System.getProperties();
 	String file = null;
 	if(project!=null){
 		file = project.getLocation().toString();
 	}
    String directory=file+"\\target\\classes\\rules";
	
	//CheckDirectoryChange(filepath, directory);
	
	
    	try {
			
			dirs=getDirs(directory);
    	for(String dir:dirs){
    		processDirectory(file,new File(dir));
    	}
    	} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	System.out.println("用户的当前工作目录："+file);
	log.info("用户的当前工作目录："+file);
	ProjectUtil.initDefaultConsole();
	ProjectUtil.printMessage("用户的当前工作目录："+file);
    try {
          Process process=null;//成功！直接执行
          String os = props.getProperty("os.name");
          if (os != null && os.toLowerCase().indexOf("linux") > -1) {
        	    
                process= Runtime.getRuntime().exec("sh ant clean deploy",null,new File(file));
          } else {
        	   
                process= Runtime.getRuntime().exec("cmd /c ant clean deploy",null,new File(file));
                InputStream inputStream = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(inputStream);
		InputStream errorStream = process.getErrorStream();
		InputStreamReader esr = new InputStreamReader(errorStream);
                int n1;
			char[] c1 = new char[1024];
		StringBuffer standardOutput = new StringBuffer();
		while ((n1 = isr.read(c1)) > 0) {
			standardOutput.append(c1, 0, n1);}
		String std=standardOutput.toString();
		String s= ProjectUtil.getUTF8StringFromGBKString(std);
                System.out.println("Standard Output: " + s);
                if(dialog){
                   MessageDialog.openInformation(
							shell,
							"RiskDesigner",
							"build ok");
                }
                ProjectUtil.printMessage(s);
                int n2;
		char[] c2 = new char[1024];
		StringBuffer standardError = new StringBuffer();
		while ((n2 = esr.read(c2)) > 0) {
			standardError.append(c2, 0, n2);}
		String error=standardError.toString();
		//System.out.println("Standard Error: " +error );  
                if(error.length()!=0){
                	if(dialog){
                MessageDialog.openInformation(
						shell,
						"RiskDesigner",
						error); 
                	}
                	ProjectUtil.printMessage(error);
          		}
		
              }
				} catch (Exception e) {
				if(dialog){
				MessageDialog.openInformation(
							shell,
							"RiskDesigner",
							e.getMessage()); 
					}
				ProjectUtil.printMessage(e.getMessage());
                e.printStackTrace();
    }
}

public static void buildProject(IProject project,Shell shell,Boolean dialog) {
	// TODO Auto-generated method stub
	Properties props=System.getProperties();
 	String file = null;
 	if(project!=null){
 		file = project.getLocation().toString();
 	}
	
	System.out.println("用户的当前工作目录："+file);
	log.info("用户的当前工作目录："+file);
	ProjectUtil.initDefaultConsole();
	ProjectUtil.printMessage("用户的当前工作目录："+file);
    try {
          Process process=null;//成功！直接执行
          String os = props.getProperty("os.name");
          if (os != null && os.toLowerCase().indexOf("linux") > -1) {
        	   
                process= Runtime.getRuntime().exec("sh mvn clean install",null,new File(file));
          } else {
        	    
                process= Runtime.getRuntime().exec("cmd /c mvn clean install",null,new File(file));
                InputStream inputStream = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(inputStream);
		InputStream errorStream = process.getErrorStream();
		InputStreamReader esr = new InputStreamReader(errorStream);
                int n1;
			char[] c1 = new char[1024];
		StringBuffer standardOutput = new StringBuffer();
		while ((n1 = isr.read(c1)) > 0) {
			standardOutput.append(c1, 0, n1);}
		String std=standardOutput.toString();
		String s= ProjectUtil.getUTF8StringFromGBKString(std);
                System.out.println("Standard Output: " + s);
                   if(dialog){
                   MessageDialog.openInformation(
							shell,
							"RiskDesigner",
							"build ok");
                   }
                ProjectUtil.printMessage(s);
                int n2;
		char[] c2 = new char[1024];
		StringBuffer standardError = new StringBuffer();
		while ((n2 = esr.read(c2)) > 0) {
			standardError.append(c2, 0, n2);}
		String error=standardError.toString();
		//System.out.println("Standard Error: " +error );  
                if(error.length()!=0){
                if(dialog){
                MessageDialog.openInformation(
						shell,
						"RiskDesigner",
						error); 
                }
                	ProjectUtil.printMessage(error);
          		}
		
              }
				} catch (Exception e) {
					if(dialog){
				MessageDialog.openInformation(
							shell,
							"RiskDesigner",
							e.getMessage()); 
					}
				ProjectUtil.printMessage(e.getMessage());
                e.printStackTrace();
    }
}

public static int getPlanNum(String filePath) {
	// TODO Auto-generated method stub
	int k=XmlUtil.getNodeList(filePath,"Plan");
	return k;
}
public static String setNextPlanActive(String filePath) {
	// TODO Auto-generated method stub
	String activePlan=XmlUtil.setNodeAttribute(filePath,"Plan");
    return activePlan;
}
/**
 * 遍历指定文件夹及子文件夹下的文件
 * 
 * @author testcs_dn
 * @date	2014年12月12日下午2:33:49
 * @param file 要遍历的指定文件夹
 * @param collector 符合条件的结果加入到此List<File>中
 * @param pathInclude 路径中包括指定的字符串
 * @param fileNameInclude 文件名称（不包括扩展名）中包括指定的字符串
 * @param extnEquals 文件扩展名为指定字符串
 * @throws IOException
 */
public static void listFiles(File file,List<File> collector, String pathInclude, String fileNameInclude, String extnEquals) throws IOException {
	if (file.isFile() 
			&& (StringUtils.isBlank(pathInclude) || file.getAbsolutePath().indexOf(pathInclude) != -1)
			&& (StringUtils.isBlank(fileNameInclude) || file.getName().indexOf(fileNameInclude) != -1)
			&& (StringUtils.isBlank(extnEquals) || file.getName().endsWith(extnEquals))
			){
		collector.add(file);
	}
	if((!file.isHidden() && file.isDirectory()) && !isIgnoreFile(file)) {
		File[] subFiles = file.listFiles();
		for(int i = 0; i < subFiles.length; i++) {
			listFiles(subFiles[i],collector, pathInclude, fileNameInclude, extnEquals);
		}
	}
}
//推断文件夹是否须要忽略

private static boolean isIgnoreFile(File file) {
	List<String> ignoreList = new ArrayList<String>();
	ignoreList.add(".svn");
	ignoreList.add("CVS");
	ignoreList.add(".cvsignore");
	ignoreList.add("SCCS");
	ignoreList.add("vssver.scc");
	ignoreList.add(".DS_Store");
	for(int i = 0; i < ignoreList.size(); i++) {
		if(file.getName().equals(ignoreList.get(i))) {
			return true;
		}
	}
	return false;
}
public static ArrayList<String> processDrlFiles(String path) {
	ArrayList<String> files= new ArrayList<String>();
    File file = new File(path);
    File[] tempList = file.listFiles();
    if (tempList == null) {// 如果目录为空，直接退出
		return null ;
	}
    for (int i = 0; i < tempList.length; i++) {
       
        if (!tempList[i].isDirectory()) {
            
        	files.add(tempList[i].getName());
        }
        
        
        
    }
    return files;
}

public static void processDirectory(String projectPath,File dir) throws InterruptedException{
    File[] files = dir.listFiles();
    List<File> sourceFileList=new ArrayList<File>();
    for(File a:files){
        //System.out.println(a.getAbsolutePath());
    	String name=a.getName();
    	
        if(a.isFile()){
        	if(!name.substring(name.lastIndexOf(".")).equals(".flow")){
        	sourceFileList.add(a);
        	}
        }
        
    }
    processFiles(dir,sourceFileList,projectPath);
    
   
}

public static void processFiles(File dir,List<File> sourceFileList,String projectPath){
	 
	if(sourceFileList.size()>1){
   	 mergedrlsFile(sourceFileList,dir);
   	 String fname=sourceFileList.get(0).getName();
  	 String name=fname.substring(0, fname.indexOf("."));
  	 File f=new File(projectPath+"\\target\\rules"+"\\"+name+".merge.drl");
  	 if(f.exists()){
  	  f.delete();
   	 }
  	 moveFile(new File(dir.getAbsolutePath()+"\\"+name+".merge.drl"),projectPath+"\\target\\rules");
   	 
   }
   if(sourceFileList.size()==1){
   	//moveFile(new File(dir.getAbsolutePath()+"\\"+sourceFileList.get(0).getName()),projectPath+"\\target\\rules");
	 try {
		 File f=new File(projectPath+"\\target\\rules"+"\\"+sourceFileList.get(0).getName());
		if(f.exists()){
		   f.delete();		
		}
		FileUtils.copyFileToDirectory(new File(dir.getAbsolutePath()+"\\"+sourceFileList.get(0).getName()),new File(projectPath+"\\target\\rules"),true);
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	};
   }
   if(sourceFileList.size()==0){
   	
   }
}

public static boolean mergedrlsFile(List<File> sourceFiles,File dir){
	System.out.println("begin merge:");
	/*for(int i=1;i<sourceFiles.size();i++){
		System.out.println(sourceFiles.get(i).getAbsolutePath());}*/
	 boolean result=false;
	 String fname=sourceFiles.get(0).getName();
  	 String name=fname.substring(0, fname.indexOf("."));
		try {
			
		    List<File> fileList=sourceFiles;
		    File fout=new File(dir.getAbsolutePath()+"\\"+name+".merge.drl");
		    //String rulegroup=getRuleGroup(fout);
		    String packageSection="";
		    List<String> importSection=new ArrayList<String>();
		    List<String> ruleSection=new ArrayList<String>();
		    StringBuffer bf=null;
		    
		    FileWriter fw=new FileWriter(fout);
		    for(int i=0;i<fileList.size();i++){
		    	
		    	File f=fileList.get(i);
		    	   	
		        FileReader fr = new FileReader(f);
		        BufferedReader br = new BufferedReader(fr);
		        String line = br.readLine();
		        while (line != null) {
		            if(line.contains("package")&&!line.contains("**")){
		            	if(!packageSection.equals(line)){
		            	packageSection=line;
		            	}
		            }
		            if(line.contains("import")&&!line.contains("**")){
		            	if(!checkExist(importSection,line)){
		            		importSection.add(line+"\n");
		            	}
		            }
		            if(line.contains("rule")){
		            	bf=new StringBuffer();
		            	bf.append(line+"\n");
		            	line=br.readLine();
		            	while(line!=null && !line.contains("end")){
		            		bf.append(line+"\n");
		            		line=br.readLine();
		            	}
		            	bf.append(line+"\n");
		            	ruleSection.add(bf.toString());
		            	
		            	
		            }
		            
		        	
		            
		        	line=br.readLine();
		        }
		        fr.close();
		    	
		        
		    	
		    }
		    
		    
		    
		    
		    String first="/**section package  ;**/\n";
		    String second="\n\n/**section import  ;**/\n\n";
		    fw.append(first);
		    fw.append(packageSection);
		    fw.append(second);
		    for(String s:importSection){
		    	fw.append(s);
		    }
		    fw.append("\n\n");
		    for(String rule:ruleSection){
		    	fw.append(rule);
		    }
		    fw.close();
		    result=true;
		   
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return result;
	}
	
private static boolean checkExist(List<String> importSection, String line) {
	// TODO Auto-generated method stub
	for(int i=0;i<importSection.size();i++){
		String s=importSection.get(i).replace("import", "").replace(";","").trim();
		if(s.equals(line.replace("import", "").replace(";","").trim())){
			return true;
		}
	}
	return false;
}
public static String getRuleGroup(File fout) throws IOException{
	 FileReader fr1 = new FileReader(fout);
	    BufferedReader br1 = new BufferedReader(fr1);
	    String line = br1.readLine();
	    String s="";
	    while (line != null) {
	        if(line.contains("ruleflow-group")){
	        	s=line;
	        	break;
	        }
	        line=br1.readLine();
	    }
	    br1.close();
	    String result=s.substring(s.indexOf("\"")+1);
	    result=result.replace("\"", "");
	    return result;
}


public static void moveFile(File sourceFile,String dstDir){
	System.out.println("move "+sourceFile.getName()+" from " +sourceFile.getAbsolutePath()+" to "+dstDir);
	try {
		FileUtils.moveFileToDirectory(sourceFile, new File(dstDir), false);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
}
private static List<String> getDirs(String fileDir) {
	
	file = new File(fileDir);
	//file1 = new File(fileDir);
	File[] files = file.listFiles();// 获取目录下的所有文件或文件夹
	if (files == null) {// 如果目录为空，直接退出
		return null ;
	}
	// 遍历，目录下的所有文件
	for (File f : files) {
		if (!f.isFile()) {
			/*if(f.getParentFile()==file){
			dirList.add(f.getName());
			}else{
			dirList.add(f.getParentFile().getName()+"."+f.getName());
			}*/
			dirList.add(f.getAbsolutePath());
			//System.out.println(f.getAbsolutePath());
			getDirs(f.getAbsolutePath());
		} 
	}
	
	/*for (File f1 : dirList) {
		System.out.println(f1.getName());
	}*/
	
	return dirList;
	
}

 
public static boolean unionFile(String outfile,String dictionary) throws IOException{
    boolean result=false;
    List<File> fileList=getFiles(dictionary);
    File fout=new File(outfile);
    FileWriter fw=new FileWriter(fout);
    for(File f:fileList){
    	String name=f.getName();
    	if(!name.substring(name.lastIndexOf(".")).equals(".flow")){
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        while (line != null) {
            fw.append(line+"\n");
            line=br.readLine();
        }
        fr.close();
    	}
    }
    fw.close();
    result=true;
    return result;
}
public static List<File> getFiles(String path){
    File root = new File(path);
    List<File> files = new ArrayList<File>();
    if(!root.isDirectory()){
        files.add(root);
    }else{
        File[] subFiles = root.listFiles();
        for(File f : subFiles){
            files.addAll(getFiles(f.getAbsolutePath()));
        }
    }
    return files;
}

 
public static void CheckDirectoryChange(String filePath,String directory){
	List<String> dirs=getDirs(directory);
	int k=dirs.size();
	int i=0;
	int n=directory.lastIndexOf("\\");
	String ruleDirectory=directory.substring(n+1);
	StringBuffer s=new StringBuffer();
	int m=directory.length();
	for (String f1 : dirs) {
		
		System.out.println(f1);
		f1=f1.substring(m+1);
		if(f1.contains("\\")){
		f1=f1.replace("\\", ".");
		}
		i++;
		if(i!=k){
			s.append(ruleDirectory+"."+f1+",");
		}else{
			s.append(ruleDirectory+"."+f1);
		}
		
	}
	
	System.out.println(s.toString());
	XmlUtil.setNodeAttribute(filePath, "kbase","rules,"+s.toString());
	
}

public static void main(String args[]){
	/*List<File> dirs=getDirs("C:\\Users\\jjw8610\\runtime-EclipseApplication\\20180920\\plan1\\rules");
	for (File f1 : dirs) {
		System.out.println(f1.getName());
	}*/
	String projectPath="C:\\Users\\jjw8610\\runtime-EclipseApplication\\20180920";
	String filepath=projectPath+"\\resources\\META-INF\\kmodule.xml";
	String directory=projectPath+"\\target\\classes\\rules";
	
	//CheckDirectoryChange(filepath, directory);
	
	
    	try {
			
			dirs=getDirs(directory);
    	for(String dir:dirs){
    		processDirectory(projectPath,new File(dir));
    	}
    	} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	/*for(File dir:dirs){
    		if(dir.exists()){
    			deleteFile(dir);
    		}
    	}*/
	//把所有的合并到一个文件中
	/*try {
		unionFile(directory+"\\outputFileName.txt",directory);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/

	 
    	
    }
    
    

}
