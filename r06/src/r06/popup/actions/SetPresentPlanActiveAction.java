package r06.popup.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import util.ProjectUtil;

public class SetPresentPlanActiveAction implements IObjectActionDelegate {

	private Shell shell;
	
	/**
	 * Constructor for Action1.
	 */
	public SetPresentPlanActiveAction() {
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
		
		
	    
	    System.out.println("location:"+project.getLocation().toString());
	    
		String projectPath=project.getLocation().toString();
		String projectName=ProjectUtil.getProjectName(projectPath);
		
		
	    //为正确获取方式
		String filePath=projectPath+"/resources/default.cfg";
		String pomfilePath=projectPath+"/pom.xml";
		String status = null;
		try {
			
			
			String planName=ProjectUtil.getCurrentPlan();
			String activePlan=ProjectUtil.getActivePlanName(filePath);
			if(!activePlan.equals(planName)){
			ProjectUtil.modifyPom(pomfilePath,activePlan,planName);
			status=ProjectUtil.setPersentPlanActive(filePath, planName);
				if(status=="ok"){
					MessageDialog.openInformation(
							shell,
							"RiskDesigner",
							"set PresentPlanAction ok");
				}
				else{
					MessageDialog.openInformation(
							shell,
							"RiskDesigner",
							"Set PresentPlanAction error,please check log.");
				}
				ProjectUtil.refresh("project",null,projectName);
				ProjectUtil.buildProject(project,shell,false);
			}else{
				MessageDialog.openInformation(
						shell,
						"RiskDesigner",
						"Present plan has been actived,no need to set again! ");
			}
			
			
			ProjectUtil.refresh("project",null,projectName);
		} catch (Exception e) {
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


