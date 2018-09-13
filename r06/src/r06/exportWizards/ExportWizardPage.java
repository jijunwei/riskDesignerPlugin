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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.WizardExportPage;
import org.eclipse.ui.dialogs.WizardExportResourcesPage;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import util.FileUtils;




@SuppressWarnings("deprecation")
public class ExportWizardPage extends  WizardExportPage {
	
	protected FileFieldEditor editor;
	private IStructuredSelection currentResourceSelection;
	String selecteddir;
	private IConfigurationElement fConfigElement;
	public ExportWizardPage(String pageName, IStructuredSelection selection) {
		super(pageName, selection);
		setTitle(pageName); //NON-NLS-1
		setDescription("Export present solution into the local file system from the workspace"); //NON-NLS-1
		this.currentResourceSelection = selection;
	}
	 public void setInitializationData(IConfigurationElement cfig, String propertyName, Object data)
	  {
	/*  81 */     this.fConfigElement = cfig;
	  }
	/* (non-Javadoc)
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#createAdvancedControls(org.eclipse.swt.widgets.Composite)
	 */	
	protected void createAdvancedControls(Composite parent) {
		Composite fileSelectionArea = new Composite(parent, SWT.NONE);
		GridData fileSelectionData = new GridData(GridData.GRAB_HORIZONTAL
				| GridData.FILL_HORIZONTAL);
		fileSelectionArea.setLayoutData(fileSelectionData);

		GridLayout fileSelectionLayout = new GridLayout();
		fileSelectionLayout.numColumns = 3;
		fileSelectionLayout.makeColumnsEqualWidth = false;
		fileSelectionLayout.marginWidth = 0;
		fileSelectionLayout.marginHeight = 0;
		fileSelectionArea.setLayout(fileSelectionLayout);
		//Text t=new Text(fileSelectionArea, 0);
		//新建文件夹（目录）对话框
		DirectoryDialog folderdlg=new DirectoryDialog(getShell());
		//设置文件对话框的标题
		folderdlg.setText("文件夹选择");
		//设置初始路径
		folderdlg.setFilterPath("SystemDrive");
		//设置对话框提示文本信息
		folderdlg.setMessage("请选择相应的文件夹");
		//打开文件对话框，返回选中文件夹目录
		selecteddir=folderdlg.open();
		if(selecteddir==null){
		return ;
		}
		else{
		System.out.println("您选中的文件夹目录为："+selecteddir);
		
		//editor = new FileFieldEditor("verify deploy directory","you choose: ",fileSelectionArea);
		/*editor = new FileFieldEditor("select deploy directory","Select directory: ",fileSelectionArea); //NON-NLS-1 //NON-NLS-2
		editor.getTextControl(fileSelectionArea).addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e) {
				IPath path = new Path(ExportWizardPage.this.editor.getStringValue());
				System.err.println("export path:"+path.toString());
				//setFileName(path.lastSegment());
				setFileName(selecteddir);
			}
		});*/
		//setFileName(selecteddir);
		//t.setText(selecteddir);
		String projectName=""; 
		if (currentResourceSelection != null && currentResourceSelection.isEmpty() == false
				&& currentResourceSelection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) currentResourceSelection;
			/*if (ssel.size() > 1)
				return;*/
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
		
		String content=FileUtils.readFile(filePath);
		String newContent=content.replace("d:\\riskfiles", selecteddir);
		try {
			FileUtils.writeFile(filePath, newContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		BasicNewProjectResourceWizard.updatePerspective(this.fConfigElement);
		//String[] extensions = new String[] { "*.*" }; //NON-NLS-1
		//editor.setFileExtensions(extensions);
		
		//fileSelectionArea.moveAbove(null);
		//openfolderDig(getShell());
		

	}
	
	

 
	protected void openfolderDig(Shell parent){
		//新建文件夹（目录）对话框
		DirectoryDialog folderdlg=new DirectoryDialog(parent);
		//设置文件对话框的标题
		folderdlg.setText("文件夹选择");
		//设置初始路径
		folderdlg.setFilterPath("SystemDrive");
		//设置对话框提示文本信息
		folderdlg.setMessage("请选择相应的文件夹");
		//打开文件对话框，返回选中文件夹目录
		selecteddir=folderdlg.open();
		if(selecteddir==null){
		return ;
		}
		else{
		System.out.println("您选中的文件夹目录为："+selecteddir);
		
		}
	}
	
	 /* (non-Javadoc)
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#createLinkTarget()
	 */
	protected void createLinkTarget() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#getInitialContents()
	 */
	/*protected InputStream getInitialContents() {
		try {
			return new FileInputStream(new File(editor.getStringValue()));
		} catch (FileNotFoundException e) {
			return null;
		}
	}*/

	/* (non-Javadoc)
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#getNewFileLabel()
	 */
	protected String getNewFileLabel() {
		return "New deploy Directory Name:"; //NON-NLS-1
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#validateLinkedResource()
	 */
	protected IStatus validateLinkedResource() {
		return new Status(IStatus.OK, "RiskDesigner", IStatus.OK, "", null); //NON-NLS-1 //NON-NLS-2
	}
	@Override
	protected void createDestinationGroup(Composite arg0) {
		// TODO Auto-generated method stub
		
	}



	
}
