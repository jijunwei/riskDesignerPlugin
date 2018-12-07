package r06.handlers;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.Logger;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import util.ProjectUtil;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class DeployProjectHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	final static Logger log =  Logger.getLogger("r06.handlers.DeployProjectHandler");
	public DeployProjectHandler() {
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
		    IProject project = ProjectUtil.getCurrentProject();
		    if(project==null){
		    	project=ProjectUtil.project;
		    }
		    if(project!=null){
		    	ProjectUtil.project=project;
		    }
		   IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		   ProjectUtil.deployProject(project,window.getShell(),false);
			 
		return null;
	}
}
