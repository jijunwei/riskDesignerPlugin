package r06.drlWizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.operation.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;

import java.io.*;
import org.eclipse.ui.*;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.Workbench;

import util.FileUtils;
import util.GetString;
import util.JsonUtil;
import util.XmlJSON;

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

public class NewDrlWizard extends Wizard implements INewWizard {
	private NewDrlWizardPage page;
	private ISelection selection;

	/**
	 * Constructor for SampleNewWizard.
	 */
	public NewDrlWizard() {
		super();
		setNeedsProgressMonitor(true);
	}
	
	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		page = new NewDrlWizardPage(selection);
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
		monitor.beginTask("Creating " + fileName, 2);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containerName));
		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException("Container \"" + containerName + "\" does not exist.");
		}
		IContainer container = (IContainer) resource;
		final IFile file = container.getFile(new Path(fileName));
		try {
			InputStream stream = openContentStream(fileName,containerName);
			if (file.exists()) {
				/*MessageDialog.openInformation(
						window.getShell(),
						"RiskDesigner",
						"addPlan Action was executed.");*/
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
	 * @throws JavaModelException 
	 */

	
	private InputStream openContentStream(String filename,String containerName) throws JavaModelException {
		//String contents ="This is the initial file contents for drl file that should be word-sorted in the Preview page of the multi-page editor";
		
		/*
		String filecontent=FileUtils.readFile(".project");
        System.out.println(filecontent);
        String json=XmlJSON.xml2JSON(filecontent).replace("@", "");
        System.out.println(json);
        Map<String,Object> m=JsonUtil.jsonToMap(json);
        String name=(String)m.get("name");
        System.out.println(m.get("name"));*/
		
		
		
		
		String projectName=GetString.getMessage(containerName, 1);
		System.out.println("projectName in NewDrlWizard:"+projectName);
	
		
		
		int endIndex2=filename.indexOf(".");
		String name=filename.substring(0, endIndex2);
		
		String subDirectory=GetString.getMessage(containerName, 3);
		
		int begin=subDirectory.indexOf("/");
		String s4=subDirectory.substring(begin+1);
		int begin2=s4.indexOf("/");
		String s5=s4.substring(begin2+1);
		String ruleGroup=GetString.getPackage(s5);
		System.out.println("ruleGroup:"+ruleGroup);
		       StringBuffer buf4 = new StringBuffer();
			   buf4.append("/**section package  ;**/ \n");       
			   buf4.append("package com.xujin."+projectName+";\n");
		       buf4.append("/**section import  ;**/ \n\n");

		       buf4.append("rule \""+ruleGroup+"."+name+"\"\n");
		       buf4.append("        salience 1\n");
		       buf4.append("        no-loop\n");
		       buf4.append("        lock-on-active true\n");
		       buf4.append("        ruleflow-group \""+ruleGroup+"\"\n\n");
		       buf4.append("        when\n\n");
					   buf4.append("        then\n\n");
					   buf4.append("        end\n\n");
		
		return new ByteArrayInputStream(buf4.toString().getBytes());
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