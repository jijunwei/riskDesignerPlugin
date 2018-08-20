package r06.addPlanWizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.operation.*;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import java.io.*;
import org.eclipse.ui.*;
import org.eclipse.ui.ide.IDE;

import util.GetString;

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

	public void addPages() {
		page = new AddPlanWizardPage(selection);
		addPage(page);
	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	public boolean performFinish() {
		final String containerName = page.getContainerName();
		final String fileName = page.getFileName();
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(containerName, fileName, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
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
		
		String s=containerName;
		String projectName=GetString.getMessage(s, 1);
		System.out.println("project name:"+projectName);
		
		
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