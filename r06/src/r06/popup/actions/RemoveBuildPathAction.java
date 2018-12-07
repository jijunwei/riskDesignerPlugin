package r06.popup.actions;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IProject;
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

public class RemoveBuildPathAction implements IObjectActionDelegate {

	private Shell shell;
	
	/**
	 * Constructor for Action1.
	 */
	public RemoveBuildPathAction() {
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
	  		String filePath=projectPath+"/.classpath";
	  		try {
	  			String fileName=ProjectUtil.getCurrentFile();
	  			status=ProjectUtil.removeFromBuildpath(filePath,fileName );
	  			
	  			ProjectUtil.refresh("project",null,projectName);
	  		} catch (Exception e) {
	  			// TODO Auto-generated catch block
	  			e.printStackTrace();
	  		}
	  		
			if(status=="ok"){
				MessageDialog.openInformation(
						shell,
						"RiskDesigner",
						"removeBuildPath ok.");
			}
			else{
				MessageDialog.openInformation(
						shell,
						"RiskDesigner",
						"removeBuildPath error,please check log.");
			}
		
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
