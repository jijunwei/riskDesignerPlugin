package r06.addPlanWizards;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.UUID;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import r06.Activator;
import util.FileUtils;
import util.ProjectUtil;

/**
 * This is a sample new wizard. Its role is to create a new file 
 * resource in the provided container. If the container resource
 * (a folder or a project) is selected in the workspace 
 * when the wizard is opened, it will accept it as the target
 * container. The wizard creates one file with the extension
 * "cfg". If a sample multi-page editor (also available
 * as a template) is registered for the same extension, it will
 * be able to open it.
 */

public class AddPlanWizard extends Wizard implements INewWizard {
	private AddPlanWizardPage page;
	private ISelection selection;
	private IConfigurationElement fConfigElement;
	/**
	 * Constructor for SampleNewWizard.
	 */
	public AddPlanWizard() {
		super();
		setNeedsProgressMonitor(true);
	}
	
	/**
	 * Adding the page to the wizard.
	 */
	 public void setInitializationData(IConfigurationElement cfig, String propertyName, Object data)
	  {
	/*  81 */     this.fConfigElement = cfig;
	  }
	public void addPages() {
		page = new AddPlanWizardPage(selection);
		addPage(page);
	}
	
	public void addsubfolder(IProgressMonitor monitor,String projectName,String containerName,String subfoldername){
		
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
	public void addfolder(IProgressMonitor monitor,String projectName,String containerName,String planName){
	
			//获取工作区根  
			  
			IWorkspaceRoot myWorkspaceRoot = ResourcesPlugin.getWorkspace().getRoot(); 
			//从工作区根获得项目实例  
			IProject project = myWorkspaceRoot.getProject(projectName);
            
			//获取文件夹实例  
			  
			IFolder folder = project.getFolder(planName);  
		try { 
			if (!folder.exists()) {
				
					folder.create(true, true, monitor);
			
			}	
			
			
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	public boolean performFinish() {
		final String containerName = page.getContainerName();
		final String planName = page.getFileName();
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {			
				        
				    String projectName=containerName.substring(1);
					String container3=containerName+"/"+planName;
			        String fileName3 = "risk.flow";
			        
				    String container7=containerName+"/"+planName+"/rules";
			        String fileName7 = "demo.drl";
			        String container5=containerName+"/"+planName+"/rules/a/b";
			        String fileName5 = "demo2.drl";
			        addfolder(monitor,projectName,container3,planName);
			        doFinish(container3, fileName3, monitor);
			        
			       
			        
			        addsubfolder(monitor,projectName,container7,planName+"/rules");
			        doFinish(container7, fileName7, monitor);
			        
			        addsubfolder(monitor,projectName,container5,planName+"/rules/a");
			        addsubfolder(monitor,projectName,container5,planName+"/rules/a/b");
			        doFinish(container5, fileName5, monitor);
			        
			        addAndModifyCfgFile(projectName,planName);
					//doFinish(containerName, planName, monitor);
			        
				} catch (CoreException e) {
					
					   e.printStackTrace();
			 		   System.err.println("after doFinish():"+e.getMessage());
			 		  throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}

			private void addAndModifyCfgFile(String projectName,String planName){
				// TODO Auto-generated method stub
				

				//获取工作区根  
				  
				IWorkspaceRoot myWorkspaceRoot = ResourcesPlugin.getWorkspace().getRoot(); 
				//从工作区根获得项目实例  
				IProject project = myWorkspaceRoot.getProject(projectName);
				
			    //为正确获取方式
				String filePath=project.getLocation().toString()+"/resources/default.cfg";
				
				String content=FileUtils.readFile(filePath);
				BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(content.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));  
				 String line; 
				 
				 try{
					 StringBuffer buf01 = new StringBuffer();
					 String uuid = UUID.randomUUID().toString().replaceAll("-", "");
					 while ( (line = br.readLine()) != null ) {  
					     if(!line.trim().equals("")){
					    	  	 
					    	 
					    	 if(line.contains("</Solution>")){
					    		 						       
								   buf01.append("\n<Plan id=\""+uuid+"\" name=\""+planName+"\" isActive=\"false\">\n");
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
					 FileUtils.writeFile(filePath,content);
					 //BasicNewProjectResourceWizard.updatePerspective(fConfigElement);
					 
					 ProjectUtil.refresh("project",null,projectName);
					/* IEditorPart[] parts = 
							 PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditors();*/
					 	 
					 
					/* IWorkbenchPage[] pages = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getPages();
					 for(int i=0;i<pages.length;i++){
						 String title=pages[i].getActiveEditor().getTitle();
					 if(title.equals("default.cfg")){
						 Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().setActivePage(pages[i]);	
						 pages[i].saveEditor(pages[i].getActiveEditor(), true);
					 }
					
						
					 }*/
					 
					 }catch(Exception e){
						 System.out.println(e.getMessage());
						 
					 }
				 //ProjectUtil.updatePerspective(project);
				 /*IFile f = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(filePath));
				try {
					f.refreshLocal(IResource.DEPTH_ZERO, null);
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				 ProjectUtil.refresh("project",null,projectName);
				 
			}
			
			
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * The worker method. It will find the container, create the
	 * file if missing or just replace its contents, and open
	 * the editor on the newly created file.
	 */

	private void doFinish(
		String containerName,
		String fileName,
		IProgressMonitor monitor)
		throws CoreException {
		// create a sample file
		
		
	       
	        
		monitor.beginTask("Creating " + fileName, 2);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containerName));
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
	
	/**
	 * We will initialize file contents with a sample text.
	 */

	private InputStream openContentStream(String fileName) {
		String contents ="";
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
		return new ByteArrayInputStream(contents.getBytes());
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status =
			new Status(IStatus.ERROR, "r06", IStatus.OK, message, null);
		throw new CoreException(status);
	}

	/**
	 * We will accept the selection in the workbench to see if
	 * we can initialize from it.
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}