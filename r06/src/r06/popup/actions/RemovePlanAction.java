package r06.popup.actions;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.xml.sax.SAXException;

import util.ProjectUtil;

public class RemovePlanAction implements IObjectActionDelegate {

	private Shell shell;
	
	/**
	 * Constructor for Action1.
	 */
	public RemovePlanAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		
		IProject project = ProjectUtil.getCurrentProject();
	    if(project==null){
	    	project=ProjectUtil.project;
	    }
	    if(project!=null){
	    	ProjectUtil.project=project;
	    }
		
		
	    
	    System.out.println(project.getLocation().toString());
	    String status = null;
	    String projectPath=project.getLocation().toString();
	    String projectName=ProjectUtil.getProjectName(projectPath);
	        //为正确获取方式
	  		String filePath=projectPath+"/resources/default.cfg";
	  		String pomfilePath=projectPath+"/pom.xml";
	  			String planName=ProjectUtil.getCurrentPlan();
	  		try {
		  		int n=ProjectUtil.getPlanNum(filePath);	
		  		if(n>1){
		  		String activePlan=ProjectUtil.getActivePlanName(filePath);
		  		
		  		File f=new File(projectPath+"/"+planName);
	  			ProjectUtil.deleteFile(f);
	  			
	  			status=ProjectUtil.removePlan(filePath,planName );
		  			if(status=="ok"){
						MessageDialog.openInformation(
								shell,
								"RiskDesigner",
								"removePlan ok.");
					}
					else{
						MessageDialog.openInformation(
								shell,
								"RiskDesigner",
								"removePlan error,please check log.");
					}
		  			
		  		if(activePlan.equals(planName)){	
		  			activePlan=ProjectUtil.setNextPlanActive(filePath);
		  			status=ProjectUtil.modifyPom(pomfilePath,activePlan,planName);
		  			if(status=="ok"){
						MessageDialog.openInformation(
								shell,
								"RiskDesigner",
								"present Plan is active, after removed,set next plan active.ok.");
					}
					else{
						MessageDialog.openInformation(
								shell,
								"RiskDesigner",
								"present Plan is active, after removed,set next plan active error,please check log.");
					}
		  			ProjectUtil.buildProject(project,shell,false);
					
		  			
		  		}
		  		ProjectUtil.refresh("project",null,projectName);
		  		
	  			
	  			
	  			
		  		}else{
		  			MessageDialog.openInformation(
							shell,
							"RiskDesigner",
							"you only has at least one plan.you can modify it! you can't delete it ");
		  		}
	  		} catch (Exception e) {
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
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
