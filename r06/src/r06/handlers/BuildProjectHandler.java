package r06.handlers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.internal.Workbench;
import org.xml.sax.SAXException;

import util.ProjectUtil;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class BuildProjectHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	
	final static Logger log =  Logger.getLogger("r06.handlers.BuildProjectHandler");
	public BuildProjectHandler() {
	}
	

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		/*IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		MessageDialog.openInformation(
				window.getShell(),
				"RiskDesigner",
				"Hello, RiskDesigner");*/
		/*IWizardDescriptor wizardDesc = WorkbenchPlugin.getDefault()
				.getNewWizardRegistry().findWizard("r06.ui.MyProjectCreationWizard"); 
		if (wizardDesc != null) {
			NewWizardShortcutAction shortcutAction = new NewWizardShortcutAction(
					PlatformUI.getWorkbench().getActiveWorkbenchWindow(),
					wizardDesc);
			shortcutAction.run();
		}*/
		
		//String projectName=""; 
		/*IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);*/
		   
		
		   /* 
		    IWorkspaceRoot myWorkspaceRoot = ResourcesPlugin.getWorkspace().getRoot(); 
			//从工作区根获得项目实例  
			IProject project = myWorkspaceRoot.getProject(projectName);*/
			
		    //为正确获取方式
			//String filePath=project.getLocation().toString()+"/build.xml";
		    
		    IProject project = ProjectUtil.getCurrentProject();
		    
		    if(project==null){
		    	project=ProjectUtil.project;
		    }
		    if(project!=null){
		    	ProjectUtil.project=project;
		    }
		   String projectPath=project.getLocation().toString();
		   IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		   String filePath=projectPath+"/resources/default.cfg";
		   String activePlan = null;
		   try {
			activePlan=ProjectUtil.getActivePlanName(filePath);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   ProjectUtil.CheckDirectoryChange(projectPath+"/resources/META-INF/kmodule.xml", projectPath+"/"+activePlan+"/rules");
		   ProjectUtil.buildProject(project,window.getShell(),true);
           ProjectUtil.refresh("project",null,ProjectUtil.getProjectName(projectPath));
	        
			 
		return null;
	}
}
