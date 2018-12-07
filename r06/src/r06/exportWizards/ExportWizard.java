/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package r06.exportWizards;

import java.io.IOException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

import util.FileUtilsByUs;
import util.ProjectUtil;

public class ExportWizard extends Wizard implements IExportWizard {
	
	ExportWizardPage mainPage;

	public ExportWizard() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	public boolean performFinish() {
		MessageDialog.openInformation(
				getShell(),
				"RiskDesigner",
				"waiting......"); 
        
		IStructuredSelection currentResourceSelection=this.mainPage.currentResourceSelection;
		 String projectName=""; 
			if (currentResourceSelection != null && currentResourceSelection.isEmpty() == false
			&& currentResourceSelection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) currentResourceSelection;
			
			Object obj = ssel.getFirstElement();
			if (obj instanceof IResource) {
			IContainer container;
			if (obj instanceof IContainer)
				container = (IContainer) obj;
			else
				container = ((IResource) obj).getParent();
			projectName=container.getFullPath().toString().substring(1);
			}
			}
			//获取工作区根  
			
			IWorkspaceRoot myWorkspaceRoot = ResourcesPlugin.getWorkspace().getRoot(); 
			//从工作区根获得项目实例  
			IProject project = myWorkspaceRoot.getProject(projectName);
			
			//为正确获取方式
			String filePath=project.getLocation().toString()+"/build.xml";
			
			String content=FileUtilsByUs.readFile(filePath);
			String selecteddir=this.mainPage.selecteddir;
			System.out.println(selecteddir);
			String destdir=ProjectUtil.getDestDir(filePath,"property","dest");
			String newContent=content.replace(destdir, selecteddir);
			try {
			FileUtilsByUs.writeFile(filePath, newContent);
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
			ProjectUtil.refresh("project",null,projectName);
            ProjectUtil.buildProject(project,getShell(),false);
            ProjectUtil.refresh("project",null,projectName);
             
            ProjectUtil.deployProject(project,getShell(),false);
            MessageDialog.openInformation(
            		getShell(),
    				"RiskDesigner",
    				"execute end"); 
       
		
		
        return true;
	}
	 
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle("solution export Wizard"); //NON-NLS-1
		setNeedsProgressMonitor(true);
		mainPage = new ExportWizardPage("Export present solution",selection); //NON-NLS-1
		
	}
	
	/* (non-Javadoc)
     * @see org.eclipse.jface.wizard.IWizard#addPages()
     */
    public void addPages() {
        super.addPages(); 
        addPage(mainPage);        
    }

}
