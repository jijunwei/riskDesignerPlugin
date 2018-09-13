package r06.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.actions.NewWizardShortcutAction;
import org.eclipse.ui.wizards.IWizardDescriptor;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class CreateProjectHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public CreateProjectHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	@SuppressWarnings("restriction")
	public Object execute(ExecutionEvent event) throws ExecutionException {
		/*IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		MessageDialog.openInformation(
				window.getShell(),
				"RiskDesigner",
				"Hello, RiskDesigner");*/
		IWizardDescriptor wizardDesc = WorkbenchPlugin.getDefault()
				.getNewWizardRegistry().findWizard("r06.ui.MyProjectCreationWizard"); 
		if (wizardDesc != null) {
			NewWizardShortcutAction shortcutAction = new NewWizardShortcutAction(
					PlatformUI.getWorkbench().getActiveWorkbenchWindow(),
					wizardDesc);
			shortcutAction.run();
		}
		return null;
	}
}
