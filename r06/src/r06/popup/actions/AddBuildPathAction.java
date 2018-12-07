package r06.popup.actions;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.actions.NewWizardShortcutAction;
import org.eclipse.ui.wizards.IWizardDescriptor;
import org.xml.sax.SAXException;

import util.ProjectUtil;

public class AddBuildPathAction implements IObjectActionDelegate {

	private Shell shell;
	
	/**
	 * Constructor for Action1.
	 */
	public AddBuildPathAction() {
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
	@SuppressWarnings("unused")
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
		String filePath=projectPath+"/.classpath";
		String status = null;
		try {
			
			
			String fileName=ProjectUtil.getCurrentFile();
			status=ProjectUtil.addToBuildpath(filePath, fileName);
			ProjectUtil.updatePerspective(project);
			ProjectUtil.refreshFile(project.getLocation().toString()+"java/lib/"+fileName);
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(status=="ok"){
			MessageDialog.openInformation(
					shell,
					"RiskDesigner",
					"AddBuildPath ok");
			ProjectUtil.refresh("project",null,projectName);
		}
		else{
			MessageDialog.openInformation(
					shell,
					"RiskDesigner",
					"AddBuildPath error,please check log.");
		}
	    
		
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
