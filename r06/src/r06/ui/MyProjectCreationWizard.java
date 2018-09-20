package r06.ui;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.ui.wizards.JavaCapabilityConfigurationPage;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;
import org.xml.sax.SAXException;

import util.ProjectUtil;

public class MyProjectCreationWizard extends Wizard
  implements IExecutableExtension, INewWizard
{
  private WizardNewProjectCreationPage fMainPage;
  private JavaCapabilityConfigurationPage fJavaPage;
  private IConfigurationElement fConfigElement;
  private IWorkbench fWorkbench;
  public String projectName;  //"one"
  public String projectPath;//"C:\Users\jjw8610\runtime-EclipseApplication\one"

  public MyProjectCreationWizard()
  {
/*  73 */     setWindowTitle("New Solution Project");
  }

  public void setInitializationData(IConfigurationElement cfig, String propertyName, Object data)
  {
/*  81 */     this.fConfigElement = cfig;
  }

  public void init(IWorkbench workbench, IStructuredSelection selection)
  {
/*  88 */     this.fWorkbench = workbench;
  }

  public void addPages()
  {
     super.addPages();
     this.fMainPage = new WizardNewProjectCreationPage("NewProjectCreationWizard");
    this.fMainPage.setTitle("New");
     this.fMainPage.setDescription("Create a new Solution project.");

    addPage(this.fMainPage);

     this.fJavaPage = new JavaCapabilityConfigurationPage()
    {
      public void setVisible(boolean visible) {
         try {
						MyProjectCreationWizard.this.updatePage();
					} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         super.setVisible(visible);
      }
    };
     addPage(this.fJavaPage);
  }

  private void updatePage() throws CoreException
  {  IProject project =this.fMainPage.getProjectHandle();
     IJavaProject jproject = JavaCore.create(project);
     if (!jproject.equals(this.fJavaPage.getJavaProject())) {
       IClasspathEntry[] buildPath = { 
        
                  //JavaCore.newSourceEntry(jproject.getPath().append("java\\src")),
                  /*JavaCore.newSourceEntry(jproject.getPath().append("java\\src\\maven")),
                  JavaCore.newSourceEntry(jproject.getPath().append("java\\src\\ant")),*/
                  JavaCore.newSourceEntry(jproject.getPath().append("java\\src\\com.xujin")),
    		      //JavaCore.newSourceEntry(jproject.getPath().append("java\\src")),
                  JavaCore.newLibraryEntry(jproject.getPath().append("java\\lib"),null,null,false),
                  //JavaCore.newSourceEntry(jproject.getPath().append("java\\lib")),
                  JavaCore.newSourceEntry(jproject.getPath().append("resources\\dev")),
                  JavaCore.newSourceEntry(jproject.getPath().append("resources\\test")),
                  JavaCore.newSourceEntry(jproject.getPath().append("resources\\pre-product")),
                  JavaCore.newSourceEntry(jproject.getPath().append("resources\\product")),
                  JavaCore.newSourceEntry(jproject.getPath().append("resources\\META-INF")),
                  JavaCore.newSourceEntry(jproject.getPath().append("plan1\\rules\\a\\b")
                  
                 ),
                  
                 
/* 121 */    JavaRuntime.getDefaultJREContainerEntry() };

/* 123 */    IPath outputLocation = jproject.getPath().append("target");

             this.fJavaPage.init(jproject, outputLocation, buildPath, false);
             
             projectPath=jproject.getPath().toString();
             System.err.println("jproject.getPath() in updatePage():"+projectPath);
             String projectName1=projectPath;
             this.projectName=projectName1.substring(1,projectName1.length() );
            
             //IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(jproject.getElementName());
             //project.create(null);
             //project.open(null);0
             
           IProjectDescription description = project.getDescription();
			description.setNatureIds(new String[] { "org.eclipse.jdt.core.javanature" });
			project.setDescription(description, null);
			
			/*IClasspathEntry[] cpentry = { 
					         JavaCore.newSourceEntry(jproject.getPath()), 
					         JavaRuntime.getDefaultJREContainerEntry() };
			jproject.setRawClasspath(cpentry, jproject.getPath(), null);*/
			/*Map options = new HashMap();
			options.put("org.eclipse.jdt.core.formatter.tabulation.char", "space");
			options.put("org.eclipse.jdt.core.formatter.tabulation.size", "4");
			jproject.setOptions(options);*/
			
             System.err.println("jproject.getElementName()in updatePage()::"+jproject.getElementName());
             System.err.println("project name in updatePage()::"+project.getName());
     try
     {
              IPackageFragmentRoot root = jproject.getPackageFragmentRoot(project);


		       /*IPackageFragment packInit = root.createPackageFragment("test1", false, null);
		       StringBuffer buf = new StringBuffer();
		       buf.append("package test1;\n");
		       buf.append("public class E {\n");
		       buf.append("    public void foo(int i) {\n");
		       buf.append("        while (--i > 0) {\n");
		       buf.append("            System.beep();\n");
		       buf.append("        }\n");
		       buf.append("    }\n");
		       buf.append("}\n");
		       packInit.createCompilationUnit("E.java", buf.toString(), false, null);

				
			
				IPackageFragment pck = root.getPackageFragment("net.chenxs");//得到指定包
				if(!pck.exists()){
					root.createPackageFragment("net.chenxs",false,null);//包不存在则创建
				  }
				StringBuffer buf00 = new StringBuffer();
		       buf00.append("package net.chenxs;\n");
		       buf00.append("public class E2 {\n");
		       buf00.append("    public void foo(int i) {\n");
		       buf00.append("        while (--i > 0) {\n");
		       buf00.append("            System.beep();\n");
		       buf00.append("        }\n");
		       buf00.append("    }\n");
		       buf00.append("}\n");
			  ICompilationUnit cu = pck.createCompilationUnit("E2.java", buf.toString(), true, new NullProgressMonitor());//创建获得编译单元 文件(java文件)


			     ICompilationUnit  unit  = pck.getCompilationUnit("E2.java");//获得编译单元 
			  	//对unit做N多操作

				ICompilationUnit copy = cu.getWorkingCopy(null);//获取工作副本
				copy.createPackageDeclaration(pck.getElementName(),null); //创建包声明
				copy.createImport("java.util.*", null, null);//创建导入
				copy.reconcile(0, false, copy.getOwner(), null);//使工作副本与缓冲区同步
				copy.commitWorkingCopy(false, null);//工作副本保存至磁盘(用来替换原始的编译单元)
				copy.discardWorkingCopy();//废弃工作副本
				
				
				IPackageFragmentRoot pkroot = jproject.getPackageFragmentRoot(jproject.getResource());
				IPackageFragment pkg = pkroot.createPackageFragment("com.cownew", false, null);
				pkg.createCompilationUnit("Hello.java", "package com.cownew;", false, null);*/
                
				/**
				 * 修改编译单元
					大部分对Java源代码的简单修改可以使用Java元素API来完成。
					一旦拥有IType实例，就可以使用诸如createField、createInitializer、createMethod、createType等方法将成员添加
					至类型。在这些方法中提供了源代码以及关于成员的位置的信息。
				 */
				 					



				 /*IPackageFragment pack0 = root.getPackageFragment("resources.META-INF");//得到指定包
				if(!pack0.exists()){
					root.createPackageFragment("resources.META-INF",false,null);//包不存在则创建
				  }
		       StringBuffer buf0 = new StringBuffer();
		       buf0.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		       buf0.append("<kmodule xmlns=\"http://jboss.org/kie/6.0.0/kmodule\">\n");
		       buf0.append("    <kbase name=\"process\" packages=\"plan1.rules\">\n");
		       buf0.append("        <ksession name=\"ksession-process\"/>\n");
		       buf0.append("    </kmodule>\n");
       
       
               pack0.createCompilationUnit("kmodule.xml", buf0.toString(), false, null);*/
     
     		}catch(Exception e){
	            e.printStackTrace();
             }

      
    }
  }


  private void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException {
     if (monitor == null)
       monitor = new NullProgressMonitor();
    try
    {
       monitor.beginTask("Creating risk solution project...", 3);

       IProject project = this.fMainPage.getProjectHandle();
       IPath locationPath = this.fMainPage.getLocationPath();
       this.projectName=project.getName();
       final String containerName1 = "/"+projectName;
       
       System.err.println("locationPath.toString() in finishPage:"+locationPath.toString());
       System.err.println("containerName1 in finishPage:"+containerName1);
       
       IProjectDescription desc = project.getWorkspace().newProjectDescription(project.getName());
       if (!this.fMainPage.useDefaults()) {
          desc.setLocation(locationPath);
       }
        project.create(desc, new SubProgressMonitor(monitor, 1));
        project.open(new SubProgressMonitor(monitor, 1));
        updatePage();
        this.fJavaPage.configureJavaProject(new SubProgressMonitor(monitor, 1));

        BasicNewProjectResourceWizard.updatePerspective(this.fConfigElement);
        BasicNewResourceWizard.selectAndReveal(project, this.fWorkbench.getActiveWorkbenchWindow());
        
        /*final String containerName0 = "/"+projectName+"/java/src/maven";
        final String fileName0 = "TestMavenCommand.java";
        
        final String containerName00 = "/"+projectName+"/java/src/ant";
        final String fileName00 = "TestDeployCommand.java";*/
        
        final String containerName = "/"+projectName+"/resources/META-INF";
        final String fileName = "kmodule.xml";
        
        final String fileName2 = "build.xml";
        final String fileName4 = "pom.xml";
        
       
                
        final String container7="/"+projectName+"/plan1/rules";
        final String fileName7 = "demo.drl";
        final String container5="/"+projectName+"/plan1/rules/a/b";
        final String fileName5 = "demo2.drl";
        final String container3="/"+projectName+"/plan1";
        final String fileName3 = "risk.flow";
        
        final String container6="/"+projectName+"/resources/dev";
        final String fileName6 = "config.properties";
        final String fileName9 = "log4j.properties";
        final String fileName10 = "logback.xml";
        
        
        
        final String container8="/"+projectName+"/resources";
        final String fileName8 = "default.cfg";
 		
 	   try{
 		/*doFinish(containerName0, fileName0, monitor);
 		doFinish(containerName00, fileName00, monitor);*/
		doFinish(containerName, fileName, monitor);
		
		doFinish(containerName1, fileName2, monitor);
		doFinish(containerName1, fileName4, monitor);
		
		doFinish(container3, fileName3, monitor);
		
		
		doFinish(container6, fileName6, monitor);
		doFinish(container6, fileName9, monitor);
		doFinish(container6, fileName10, monitor);
		
		doFinish(container7, fileName7, monitor);
		doFinish(container8, fileName8, monitor);
		doFinish(container5, fileName5, monitor);	
		
		
        //add java/lib directory
		
      	addsubfolder(monitor,projectName,"java/lib");
      //为正确获取方式
      		String filePath=project.getLocation().toString()+"/.classpath";
      		try {
      			     			
      			ProjectUtil.addToBuildpath(filePath, "unit");
      			
      		} catch (ParserConfigurationException | SAXException | IOException e) {
      			// TODO Auto-generated catch block
      			e.printStackTrace();
      		}
	      	try {
		     			
	      		ProjectUtil.addToBuildpath(filePath, "maven");
		
			} catch (ParserConfigurationException | SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/*IFile f = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(filePath));
			f.refreshLocal(IResource.DEPTH_ZERO, null);  */	
	      	ProjectUtil.refresh("project",null,projectName);
        BasicNewProjectResourceWizard.updatePerspective(this.fConfigElement);
        BasicNewResourceWizard.selectAndReveal(project, this.fWorkbench.getActiveWorkbenchWindow());
 	   }catch(Exception e){
 		   e.printStackTrace();
 		   System.err.println("after doFinish():"+e.getMessage());
 	   }
 		
        
        
        
     }
     finally {
        monitor.done();
        }
  }
  public void addsubfolder(IProgressMonitor monitor,String projectName,String subfoldername){
		
		//获取工作区根  
		  
		IWorkspaceRoot myWorkspaceRoot = ResourcesPlugin.getWorkspace().getRoot(); 
		//从工作区根获得项目实例  
		IProject project = myWorkspaceRoot.getProject(projectName);
      
		//获取文件夹实例  
		
		
		IFolder folder = project.getFolder(subfoldername);  
	try { 
		if (!folder.exists()) {
			
				folder.create(true, true, monitor);
		
		}	
		
		
	} catch (CoreException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
  public boolean performFinish()
  {
    WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
      protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
       
    	  
    	  MyProjectCreationWizard.this.finishPage(monitor);
		   
	}
    };
    try {
    	 getContainer().run(false, true, op);
    } catch (InvocationTargetException localInvocationTargetException) {
    	 return false;
    } catch (InterruptedException localInterruptedException) {
    	 return false;
    }
    	return true;
    }


  		
		private void doFinish(
		String containerName,
		String fileName,
		IProgressMonitor monitor)
		throws CoreException {
		// create a sample file
		monitor.beginTask("Creating " + fileName, 2);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containerName));
		System.err.println("in doFinish() containerName:"+containerName);
		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException("Container \"" + containerName + "\" does not exist.");
		}
		IContainer container = (IContainer) resource;
		final IFile file = container.getFile(new Path(fileName));
		try {
			
			InputStream stream = openContentStream(fileName);
			if (file.exists()) {
				file.setContents(stream, true, true, monitor);
			} else {
				file.create(stream, true, monitor);
			}
			stream.close();
		} catch (IOException e) {
		}
		if(fMainPage.checked){
		monitor.worked(1);
		monitor.setTaskName("Opening file for editing...");
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page =
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					IDE.openEditor(page, file, true);
				} catch (PartInitException e) {
				}
			}
		});
		monitor.worked(1);
		}
	}
		private void throwCoreException(String message) throws CoreException {
			IStatus status =
				new Status(IStatus.ERROR, "r06", IStatus.OK, message, null);
			throw new CoreException(status);
		}
		private InputStream openContentStream(String fileName) {
			String contents="";
			/*if(fileName.equals("TestMavenCommand.java")){
				StringBuffer buf0=new StringBuffer();
				   buf0.append("import java.io.File;\nimport java.io.InputStream;\nimport java.io.InputStreamReader;\nimport java.util.Properties;\nimport java.util.logging.Logger;\n");
			       buf0.append("public class TestMavenCommand {\n");
			       buf0.append("     final static Logger log =  Logger.getLogger(\"maven.TestMavenCommand\");\n");
			       buf0.append("     public static void main(String[] args) {\n");
			       buf0.append("     	Properties props=System.getProperties(); //系统属性\n");
			       buf0.append("     	System.out.println(\"用户的当前工作目录：\"+props.getProperty(\"user.dir\"));\n");
			       buf0.append("     	String file = props.getProperty(\"user.dir\");\n");
			       buf0.append("        try {\n");
			       buf0.append("              Process process=null;//成功！直接执行\n");
			       buf0.append("              String os = props.getProperty(\"os.name\");\n");
			       buf0.append("              if (os != null && os.toLowerCase().indexOf(\"linux\") > -1) {\n");
			       buf0.append("                    process= Runtime.getRuntime().exec(\"sh mvn install\",null,new File(file));\n");
			       buf0.append("              } else {\n");
			       buf0.append("                    process= Runtime.getRuntime().exec(\"cmd /c mvn install\",null,new File(file));\n");
			       buf0.append("                    InputStream inputStream = process.getInputStream();\n			InputStreamReader isr = new InputStreamReader(inputStream);\n			InputStream errorStream = process.getErrorStream();\n			InputStreamReader esr = new InputStreamReader(errorStream);\n");
			       buf0.append("                    int n1;\n 			char[] c1 = new char[1024];\n			StringBuffer standardOutput = new StringBuffer();\n			while ((n1 = isr.read(c1)) > 0) {\n  			standardOutput.append(c1, 0, n1);}\n");
			       buf0.append("                    System.out.println(\"Standard Output: \" + standardOutput.toString());\n");
			       buf0.append("                    int n2;\n			char[] c2 = new char[1024];\n			StringBuffer standardError = new StringBuffer();\n			while ((n2 = esr.read(c2)) > 0) {\n  			standardError.append(c2, 0, n2);}\n");
			       buf0.append("                    System.out.println(\"Standard Error: \" + standardError.toString());\n");
			       buf0.append("              		}\n					} catch (Exception e) {\n	            e.printStackTrace();\n        }\n");
			       buf0.append("          }\n");
			       buf0.append("    }\n");
			       contents=buf0.toString();
				
			}
			if(fileName.equals("TestDeployCommand.java")){
				StringBuffer buf0=new StringBuffer();
				   buf0.append("import java.io.File;\nimport java.io.InputStream;\nimport java.io.InputStreamReader;\nimport java.util.Properties;\nimport java.util.logging.Logger;\n");
			       buf0.append("public class TestDeployCommand {\n");
			       buf0.append("     final static Logger log =  Logger.getLogger(\"ant.TestDeployCommand\");\n");
			       buf0.append("     public static void main(String[] args) {\n");
			       buf0.append("     Properties props=System.getProperties(); //系统属性\n");
			       buf0.append("     System.out.println(\"用户的当前工作目录：\"+props.getProperty(\"user.dir\"));\n");
			       buf0.append("     String file = props.getProperty(\"user.dir\");\n");
			       buf0.append("     try {\n");
			       buf0.append("          Process process=null;//成功！直接执行\n");
			       buf0.append("          String os = props.getProperty(\"os.name\");\n");
			       buf0.append("          if (os != null && os.toLowerCase().indexOf(\"linux\") > -1) {\n");
			       buf0.append("              process= Runtime.getRuntime().exec(\"sh ant\",null,new File(file));\n");
			       buf0.append("           } else {\n");
			       buf0.append("              process= Runtime.getRuntime().exec(\"cmd /c ant\",null,new File(file));\n");
			       buf0.append("              InputStream inputStream = process.getInputStream();\n				InputStreamReader isr = new InputStreamReader(inputStream);\n			InputStream errorStream = process.getErrorStream();\n			InputStreamReader esr = new InputStreamReader(errorStream);\n");
			       buf0.append("              int n1;\n			char[] c1 = new char[1024];\n			StringBuffer standardOutput = new StringBuffer();\n			while ((n1 = isr.read(c1)) > 0) {\n  			standardOutput.append(c1, 0, n1);}\n");
			       buf0.append("              System.out.println(\"Standard Output: \" + standardOutput.toString());\n");
			       buf0.append("              int n2;\n			char[] c2 = new char[1024];\n				StringBuffer standardError = new StringBuffer();\n			while ((n2 = esr.read(c2)) > 0) {\n  			standardError.append(c2, 0, n2);}\n");
			       buf0.append("              	System.out.println(\"Standard Error: \" + standardError.toString());\n");
			       buf0.append("           			}\n				} catch (Exception e) {\n	            e.printStackTrace();\n        }\n");
			       buf0.append("      }\n");
			       buf0.append("   }\n");
			       contents=buf0.toString();
				
			}*/
			
			if(fileName.equals("demo.drl")){
				StringBuffer buf4 = new StringBuffer();
				   buf4.append("/**section package  ;**/ \n");       
					buf4.append("package com.xujin.demo;\n");
					buf4.append("/**section import  ;**/ \n\n");

			       buf4.append("rule \""+"rules.demo"+"\"\n");
			       buf4.append("        salience 1\n");
			       buf4.append("        no-loop\n");
			       buf4.append("        lock-on-active true\n");
			       buf4.append("        ruleflow-group \""+"rules"+"\"\n\n");
			       buf4.append("        when\n\n");
				   buf4.append("        then\n\n");
				   buf4.append("        end\n\n");
				   contents=buf4.toString();
			}
			if(fileName.equals("demo2.drl")){
			   StringBuffer buf41 = new StringBuffer();
			   buf41.append("/**section package  ;**/ \n");       
			   buf41.append("package com.xujin.demo;\n");
		       buf41.append("/**section import  ;**/ \n\n");
		
		       buf41.append("rule \""+"a.b"+".demo2"+"\"\n");
		       buf41.append("        salience 1\n");
		       buf41.append("        no-loop\n");
		       buf41.append("        lock-on-active true\n");
		       buf41.append("        ruleflow-group \""+"a.b"+"\"\n");
		       buf41.append("        when\n\n");
			   buf41.append("        then\n\n");
			   buf41.append("        end\n\n");
			   contents=buf41.toString();
			}
			if(fileName.equals("build.xml")){
				StringBuffer buf2 = new StringBuffer();
			       buf2.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
			       buf2.append("<project name=\""+projectName+"\" default=\"run\" basedir=\".\">\n");
			       buf2.append("    <property name=\"dest\" value=\"d:\\riskfiles\"/>\n");
			       buf2.append("    <property name=\"project.build.directory\" value=\""+"target\"/>\n");
			       buf2.append("    <property name=\"project.build.finalName\" value=\""+projectName+"-0.0.1-SNAPSHOT.jar\"/>\n");
			       buf2.append("    <property name=\"solution_jar\" value=\"${dest}\\data\\${project.build.finalName}\"/>\n");
			       buf2.append("<target name=\"init\">\n");
					buf2.append("	<mkdir dir=\"${dest}\"/> \n");  
					buf2.append("	<mkdir dir=\"${dest}\\data\"/>\n");
			        buf2.append("	<mkdir dir=\"${dest}\\solution\"/>\n");
					buf2.append("	<mkdir dir=\"${dest}\\solution\\"+projectName+".sln\"/>\n");
					buf2.append("</target>\n");
					buf2.append("<target name=\"deploy\">\n");
					buf2.append("	<copy file=\"${project.build.directory}\\${project.build.finalName}\"\n");   
					buf2.append("		todir=\"${dest}\\data\" overwrite=\"true\"/>\n");
					buf2.append("	<copy todir=\"${dest}\\solution\\"+projectName+".sln\\AllPass.plan\">\n");
					buf2.append("		<fileset dir=\"${project.build.directory}\\classes\\rules\"/>\n");
					buf2.append("	</copy>\n");
					buf2.append("	<copy file=\"${project.build.directory}\\classes\\default.cfg\"\n");   
					buf2.append("		todir=\"${dest}\\solution\\"+projectName+".sln\" overwrite=\"true\"/>\n");
					buf2.append("</target>\n");
					buf2.append("<target name=\"run\" depends=\"init,deploy\">\n");
					buf2.append("	<echo message=\"ok:deploy compeleted\"/> \n");
					buf2.append("</target>\n");
					buf2.append("<target name=\"clean\">\n");
					buf2.append("	<delete dir=\"${dest}\" />\n");
					buf2.append("	<delete file=\"${solution_jar}\" />\n");
					buf2.append("</target>\n");
					buf2.append("<target name=\"rerun\" depends=\"clean,run\">\n");
					buf2.append("	<ant target=\"clean\" />\n");
					buf2.append("	<ant target=\"run\" />\n");
					buf2.append("</target>\n");
					buf2.append("</project>");
					contents=buf2.toString();
			}
			if(fileName.equals("default.cfg")){
				 String uuid = UUID.randomUUID().toString().replaceAll("-", "");
				 StringBuffer buf01 = new StringBuffer();
			       buf01.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n");
			       buf01.append("<Solution isActive=\"true\" name=\""+projectName+"\">\n");
				   buf01.append("<Plan id=\""+uuid+"\" name=\"plan1\" isActive=\"true\">\n");
	       		   buf01.append("    <Models>\n");
				   buf01.append("      <!-- 添加一个 out model -->\n");
				   buf01.append("      <Model type=\"out\" id=\"*\"/>\n");
				   buf01.append("      <!-- 添加一个或多个in model-->\n");
				   buf01.append("      <Model type=\"in\" id=\"*\"/>\n");
			       buf01.append("    </Models>\n");
			       buf01.append("    </Plan>\n");
			       buf01.append("</Solution>\n");
			       contents=buf01.toString();
				}
			if(fileName.equals("kmodule.xml")){
				
				   StringBuffer buf01 = new StringBuffer();
			       buf01.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			       buf01.append("<kmodule xmlns=\"http://jboss.org/kie/6.0.0/kmodule\">\n");
				   buf01.append("    <kbase name=\""+projectName+"-"+"plan"+"\" packages=\"rules\">\n");       
				   buf01.append("       <ksession name=\"ksession-"+projectName+"-plan"+"\"/>\n");
				   buf01.append("    </kbase>\n");
				   buf01.append("</kmodule>\n");
			       
			       contents=buf01.toString();
			}
			if(fileName.equals("risk.flow")){
				   
				   String uuid = UUID.randomUUID().toString().replaceAll("-", "");
				   System.out.println(uuid);
				   StringBuffer buf01 = new StringBuffer();
			       buf01.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			       buf01.append("<definitions id=\"Definition\"\n");
				   buf01.append("    targetNamespace=\"http://www.jboss.org/drools\"\n");
	       
				   buf01.append("    typeLanguage=\"http://www.java.com/javaTypes\"\n");
				   buf01.append("    expressionLanguage=\"http://www.mvel.org/2.0\"\n");
				   buf01.append("    xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"\n");
			       buf01.append("    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
			       buf01.append("    xsi:schemaLocation=\"http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd\"\n");
			       buf01.append("    xmlns:g=\"http://www.jboss.org/drools/flow/gpd\"\n");
			       buf01.append("    xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\"\n");
			       buf01.append("    xmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\"\n");
			       buf01.append("    xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\"\n");
			       buf01.append("    xmlns:tns=\"http://www.jboss.org/drools\">\n");
			       
			       buf01.append("    <process processType=\"Private\" isExecutable=\"true\" id=\""+uuid+"\" name=\""+uuid+"\" >\n");
			       buf01.append("	 </process>\n");
			       buf01.append("<bpmndi:BPMNDiagram>\n");
			       buf01.append("	<bpmndi:BPMNPlane bpmnElement=\""+uuid+"\" >\n");
			       buf01.append("    </bpmndi:BPMNPlane>\n");
			       buf01.append("</bpmndi:BPMNDiagram>\n");
			       buf01.append("</definitions>\n");
			       contents=buf01.toString();
			}
			
			if(fileName.equals("pom.xml")){
				StringBuffer buf1 = new StringBuffer();
			       buf1.append("<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n");
			       buf1.append("<modelVersion>4.0.0</modelVersion>\n");
			       buf1.append("<groupId>xujin</groupId>\n");
			       buf1.append("<artifactId>"+projectName+"</artifactId>\n");
			       buf1.append("<version>0.0.1-SNAPSHOT</version>\n");
			       buf1.append("<name>xujin risk project</name>\n");
			       buf1.append("<description>a starter framework of xujin risk project</description>\n");
			       buf1.append("<properties>\n");
	               buf1.append("<drools.version>6.2.0.Final</drools.version>\n<jbpm.version>6.2.0.Final</jbpm.version>\n<httpclient.version>3.1</httpclient.version>\n<commons-lang.version>2.6</commons-lang.version>\n"
	               		+ "<fastjson.version>1.2.3</fastjson.version>\n"
	               		+ "<logback.version>1.1.3</logback.version>\n"
	               		+ "<lombok.version>1.16.16</lombok.version>"
	               		+ "<slf4j.version>1.7.12</slf4j.version>");
			       buf1.append("</properties>\n\n");
					
					buf1.append("<repositories>\n");
					buf1.append("<repository>\n<id>nexus-aliyun</id>\n<name>Nexus aliyun</name>\n<url>http://maven.aliyun.com/nexus/content/groups/public</url>\n</repository>\n");
					buf1.append("<repository>\n<id>mvnrepository</id>\n<name>mvnrepository</name>\n<url>http://www.mvnrepository.com/</url>\n</repository>\n");
					buf1.append("</repositories>\n\n");
					
					buf1.append("<dependencies>\n");
					buf1.append("<dependency>\n<groupId>org.drools</groupId>\n<artifactId>knowledge-api</artifactId>\n<version>${drools.version}</version>\n</dependency>\n");
					buf1.append("<dependency>\n<groupId>org.drools</groupId>\n<artifactId>drools-core</artifactId>\n<version>${drools.version}</version>\n</dependency>\n");
					buf1.append("<dependency>\n<groupId>org.drools</groupId>\n<artifactId>drools-compiler</artifactId>\n<version>${drools.version}</version>\n</dependency>\n");
					buf1.append("<dependency>\n<groupId>org.jbpm</groupId>\n<artifactId>jbpm-flow</artifactId>\n<version>${jbpm.version}</version>\n</dependency>\n");
					buf1.append("<dependency>\n<groupId>org.jbpm</groupId>\n<artifactId>jbpm-flow-builder</artifactId>\n<version>${jbpm.version}</version>\n</dependency>\n");
					buf1.append("<dependency>\n<groupId>org.jbpm</groupId>\n<artifactId>jbpm-bpmn2</artifactId>\n<version>${jbpm.version}</version>\n</dependency>\n");
					buf1.append("<dependency>\n<groupId>commons-httpclient</groupId>\n<artifactId>commons-httpclient</artifactId>\n<version>${httpclient.version}</version>\n</dependency>\n");
					buf1.append("<dependency>\n<groupId>commons-lang</groupId>\n<artifactId>commons-lang</artifactId>\n<version>${commons-lang.version}</version>\n</dependency>\n");
					buf1.append("<dependency>\n<groupId>com.alibaba</groupId>\n<artifactId>fastjson</artifactId>\n<version>${fastjson.version}</version>\n</dependency>\n");
					
					buf1.append("<dependency>\n<groupId>ch.qos.logback</groupId>\n<artifactId>logback-core</artifactId>\n<version>${logback.version}</version>\n</dependency>\n");
					buf1.append("<dependency>\n<groupId>ch.qos.logback</groupId>\n<artifactId>logback-classic</artifactId>\n<version>${logback.version}</version>\n</dependency>\n");
					buf1.append("<dependency>\n<groupId>org.projectlombok</groupId>\n<artifactId>lombok</artifactId>\n<version>${lombok.version}</version>\n</dependency>\n");
					
					buf1.append("<dependency>\n<groupId>org.slf4j</groupId>\n<artifactId>slf4j-api</artifactId>\n<version>${slf4j.version}</version>\n</dependency>\n");
					buf1.append("<dependency>\n<groupId>org.slf4j</groupId>\n<artifactId>log4j-over-slf4j</artifactId>\n<version>${slf4j.version}</version>\n</dependency>\n");
					buf1.append("<dependency>\n<groupId>org.slf4j</groupId>\n<artifactId>jcl-over-slf4j</artifactId>\n<version>${slf4j.version}</version>\n</dependency>\n");
					//buf1.append("<dependency>\n<groupId>ch.qos.logback</groupId>\n<artifactId>logback-core</artifactId>\n<version>${<logback.version>}</version>\n</dependency>\n");
					//buf1.append("<dependency>\n<groupId>ch.qos.logback</groupId>\n<artifactId>logback-core</artifactId>\n<version>${<logback.version>}</version>\n</dependency>\n");
					
					buf1.append("</dependencies>\n\n");
					
					
					buf1.append("<build>\n");
					buf1.append("<plugins>\n");
					buf1.append("<!-- maven编译插件 -->\n");
					buf1.append("<plugin>  \n");
					buf1.append("   <groupId>org.apache.maven.plugins</groupId>\n");  
					buf1.append("   <artifactId>maven-compiler-plugin</artifactId>\n");  
					buf1.append("   <version>2.3.2</version>  \n");
					buf1.append("   <configuration>    \n");
					buf1.append("   	<source>1.8</source>  \n");
					buf1.append("   	<target>1.8</target> \n"); 
					buf1.append("   	<encoding>UTF-8</encoding>\n");
					buf1.append("   	<compilerArguments>  \n");
					buf1.append("          <verbose />  \n");
					buf1.append("          <bootclasspath>${java.home}/lib/rt.jar;${java.home}/lib/jce.jar</bootclasspath>  \n");
					buf1.append("        </compilerArguments>\n");
					buf1.append("   	</configuration>  \n");
					buf1.append("   </plugin>\n\n");
					buf1.append("<plugin>\n");
					buf1.append("   <groupId>org.codehaus.mojo</groupId>\n");
					buf1.append("   <artifactId>build-helper-maven-plugin</artifactId>\n");
					buf1.append("   <version>1.7</version>\n");
					buf1.append("   <executions>\n");
					buf1.append("      <execution>\n");
					buf1.append("         <id>add-source</id>\n");
					buf1.append("         <phase>generate-sources</phase>\n");
					buf1.append("         <goals><goal>add-source</goal></goals>\n");
					buf1.append("         <configuration>\n");
					buf1.append("            <sources>\n");
					buf1.append("               <source>java/src</source>\n");
			        buf1.append("            </sources>\n");
					buf1.append("         </configuration>\n");
					buf1.append("      </execution>\n");
					buf1.append("   </executions>\n");
					buf1.append("</plugin> \n");
					buf1.append("<plugin>\n");
					buf1.append("   <groupId>org.apache.maven.plugins</groupId>\n");
					buf1.append("   <artifactId>maven-shade-plugin</artifactId>\n");
					buf1.append("   <version>2.4.1</version>\n");
					buf1.append("   <executions>\n");
					buf1.append("      <execution>\n");
					buf1.append("         <phase>package</phase>\n");
					buf1.append("      <goals>\n");
					buf1.append("         <goal>shade</goal>\n");
					buf1.append("      </goals>\n");
					buf1.append("   <configuration>\n");
					buf1.append("      <transformers>\n");
					buf1.append("         <transformer implementation=\"org.apache.maven.plugins.shade.resource.ManifestResourceTransformer\">\n");
					buf1.append("         </transformer>\n");
					buf1.append("      </transformers>\n");
					buf1.append("   </configuration>\n");
					buf1.append("      </execution>\n");
					buf1.append("   </executions>\n");
					buf1.append("</plugin>\n");
				    buf1.append("</plugins>\n");
									
	                buf1.append("<resources>\n");
					buf1.append("<resource>\n<directory>plan1/</directory>\n<includes>\n<include>**/*.flow</include>\n</includes>\n<targetPath>rules</targetPath>\n<filtering>false</filtering>\n</resource>\n");
					buf1.append("<!-- 将规则文件文件打包进jar包 -->\n<resource>\n<directory>plan1/rules</directory>\n<targetPath>rules</targetPath>\n<filtering>false</filtering>\n</resource>\n");
					buf1.append("<!-- 将kmodule.xml文件打包进jar包 -->\n<resource>\n<directory>resources/META-INF</directory>\n<targetPath>META-INF</targetPath>\n<filtering>false</filtering>\n</resource>\n");
					buf1.append("<resource>\n<directory>resources/</directory>\n<includes>\n<include>**/*.cfg</include>\n</includes>\n<filtering>false</filtering>\n</resource>\n");
					buf1.append("<!-- 将application.properties文件打包进jar包的properties文件夹 -->\n<resource>\n<directory>resources/${package.environment}</directory>\n<includes>\n<include>**/*</include>\n</includes>\n<filtering>false</filtering>\n</resource>\n");
					
					buf1.append("</resources>\n");
					
					buf1.append("</build>\n");
					
					buf1.append("<profiles>\n");
					buf1.append("<profile>\n<id>test</id>\n<activation>\n<activeByDefault>true</activeByDefault>\n</activation>\n"
							+ "<properties>\n<package.environment>test</package.environment>\n</properties>\n</profile>\n");
					
					buf1.append("<profile>\n<id>dev</id>\n"
							+ "<properties>\n<package.environment>dev</package.environment>\n</properties>\n</profile>\n");
					
					buf1.append("<profile>\n<id>pre-product</id>\n"
							+ "<properties>\n<package.environment>pre-product</package.environment>\n</properties>\n</profile>\n");
					
					buf1.append("<profile>\n<id>product</id>\n"
							+ "<properties>\n<package.environment>product</package.environment>\n</properties>\n</profile>\n");
					
					buf1.append("	</profiles>\n");
					buf1.append("</project>\n");
					contents=buf1.toString();
			}
			if(fileName.equals("config.properties")){
				contents="#you can put keys, third interface verification information and third interface url in this file.";
				
			}
			if(fileName.equals("log4j.properties")){
				   StringBuffer buf01 = new StringBuffer();
			       buf01.append("log4j.rootLogger=info, ServerDailyRollingFile, stdout\n");
			       buf01.append("#format for log\n");
				   buf01.append("log4j.appender.appenderName.layout=org.apache.log4j.patternLayout\n");
	       
				   buf01.append("#the log to console start\n");
				   buf01.append("log4j.appender.console=org.apache.log4j.ConsoleAppender\n");
				   buf01.append("log4j.appender.console.Threshold=DEBUG\n");
			       buf01.append("log4j.appender.console.ImmediateFlush=true\n");
			       buf01.append("log4j.appender.console.Target=System.out\n");
			       buf01.append("log4j.appender.console.layout=org.apache.log4j.PatternLayout\n");
			       buf01.append("log4j.appender.console.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} [%p] %m%n\n");
			       buf01.append("#the log to console end\n\n\n");
			       buf01.append("log4j.appender.ServerDailyRollingFile=org.apache.log4j.DailyRollingFileAppender\n");
			       buf01.append("log4j.appender.ServerDailyRollingFile.DatePattern='.'yyyy-MM-dd\n");
			       buf01.append("log4j.appender.ServerDailyRollingFile.File=../log4j/rd.log\n");
			       buf01.append("log4j.appender.ServerDailyRollingFile.layout=org.apache.log4j.PatternLayout\n");
			       buf01.append("log4j.appender.ServerDailyRollingFile.layout.ConversionPattern=%d - %m%n\n");
			       buf01.append("log4j.appender.ServerDailyRollingFile.Append=true\n");
			       buf01.append("log4j.appender.stdout=org.apache.log4j.ConsoleAppender\n");
			       buf01.append("log4j.appender.stdout.layout=org.apache.log4j.PatternLayout\n");
			       buf01.append("log4j.appender.stdout.layout.ConversionPattern=%d{yyyy-MM-dd HH\\:mm\\:ss} %p [%c] %m%n\n");
			       buf01.append("\n");
			       contents=buf01.toString()+"\n"+"#you can put keys, third interface verification information and third interface url in this file.";
				
			}
			if(fileName.equals("logback.xml")){
				  StringBuffer buf01 = new StringBuffer();
			       buf01.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			       buf01.append("<configuration>\n");
			       buf01.append("<appender name=\"STDOUT\" class=\"ch.qos.logback.core.ConsoleAppender\">\n");
				   buf01.append("   <layout class=\"ch.qos.logback.classic.PatternLayout\">\n");
	         	   buf01.append("    	<pattern>%d{ISO8601} [%thread] [%-5level] %logger - %msg%n</pattern>\n");
				   buf01.append("    </layout>\n");
				   buf01.append("    </appender>\n");
				   
				   buf01.append("<appender name=\"FILE\" class=\"ch.qos.logback.core.rolling.RollingFileAppender\">\n");
				   buf01.append("   <file>/logs/rd.log</file>\n");
	         	   buf01.append("    	<rollingPolicy class=\"ch.qos.logback.core.rolling.TimeBasedRollingPolicy\">\n");
				   buf01.append("    <fileNamePattern>/logs/rd.log.%d{yyyy-MM-dd}.bak</fileNamePattern>\n");
				   buf01.append("    <maxHistory>30</maxHistory>\n");
				   buf01.append("    </rollingPolicy>\n");
				   buf01.append("    <layout class=\"ch.qos.logback.classic.PatternLayout\">\n");
				   buf01.append("    <Pattern>%d{ISO8601} [%thread] [%-5level] %logger - %msg%n</Pattern>\n");
				   
				   buf01.append("    </layout>\n");
				   buf01.append("    </appender>\n");
				   buf01.append("    <root level=\"info\">\n");
				   buf01.append("    <appender-ref ref=\"STDOUT\"/>\n");
				   buf01.append("    <appender-ref ref=\"FILE\"/>\n");
			       buf01.append("    </root>\n");
			       buf01.append("</configuration>\n");
			       contents=buf01.toString();
				
				
			}
			
			return new ByteArrayInputStream(contents.getBytes());
		}
}

