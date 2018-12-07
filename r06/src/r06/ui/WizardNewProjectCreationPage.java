/*     */ package r06.ui;
/*     */ 
/*     */ import java.net.URI;
/*     */ import org.eclipse.core.resources.IProject;
/*     */ import org.eclipse.core.resources.IWorkspace;
/*     */ import org.eclipse.core.resources.IWorkspaceRoot;
/*     */ import org.eclipse.core.resources.ResourcesPlugin;
/*     */ import org.eclipse.core.runtime.IPath;
/*     */ import org.eclipse.core.runtime.IStatus;
/*     */ import org.eclipse.core.runtime.Path;
/*     */ import org.eclipse.jface.dialogs.Dialog;
/*     */ import org.eclipse.jface.util.BidiUtils;
/*     */ import org.eclipse.jface.viewers.IStructuredSelection;
/*     */ import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Event;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Listener;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.IWorkingSet;
/*     */ import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WorkingSetGroup;
/*     */ import org.eclipse.ui.help.IWorkbenchHelpSystem;
/*     */ import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
/*     */ import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
/*     */ import org.eclipse.ui.internal.ide.dialogs.ProjectContentsLocationArea;
/*     */ import org.eclipse.ui.internal.ide.dialogs.ProjectContentsLocationArea.IErrorMessageReporter;
/*     */ 
/*     */ public class WizardNewProjectCreationPage extends WizardPage
/*     */ {
/*     */   private String initialProjectFieldValue;
/*     */   Text projectNameField;
            boolean checked=true;
/*  68 */   private Listener nameModifyListener = new Listener()
/*     */   {
/*     */     public void handleEvent(Event e) {
/*  71 */       WizardNewProjectCreationPage.this.setLocationForSelection();
/*  72 */       boolean valid = WizardNewProjectCreationPage.this.validatePage();
/*  73 */       WizardNewProjectCreationPage.this.setPageComplete(valid);
/*     */     }
/*  68 */   };
/*     */   private ProjectContentsLocationArea locationArea;
/*     */   private WorkingSetGroup workingSetGroup;
/*     */   private static final int SIZING_TEXT_FIELD_WIDTH = 250;
/*     */ 
/*     */   public WizardNewProjectCreationPage(String pageName)
/*     */   {
/*  91 */     super(pageName);
/*  92 */     setPageComplete(false);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public WizardNewProjectCreationPage(String pageName, IStructuredSelection selection, String[] workingSetTypes)
/*     */   {
/* 112 */     this(pageName);
/*     */   }
/*     */ 
/*     */   public void createControl(Composite parent)
/*     */   {
/* 120 */     Composite composite = new Composite(parent, 0);
/*     */ 
/* 123 */     initializeDialogUnits(parent);
/*     */ 
/* 125 */     PlatformUI.getWorkbench().getHelpSystem().setHelp(composite, 
/* 126 */       "org.eclipse.ui.ide.new_project_wizard_page_context");
/*     */ 
/* 128 */     composite.setLayout(new GridLayout());
/* 129 */     composite.setLayoutData(new GridData(1808));
/*     */ 
/* 131 */     createProjectNameGroup(composite);
/* 132 */     this.locationArea = new ProjectContentsLocationArea(getErrorReporter(), composite);
/* 133 */     if (this.initialProjectFieldValue != null) {
/* 134 */       this.locationArea.updateProjectName(this.initialProjectFieldValue);
/*     */     }
/*     */ 
/* 138 */     setButtonLayoutData(this.locationArea.getBrowseButton());
/*     */ 
/* 140 */     setPageComplete(validatePage());
/*     */ 
/* 142 */     setErrorMessage(null);
/* 143 */     setMessage(null);
/* 144 */     setControl(composite);
/* 145 */     Dialog.applyDialogFont(composite);
/*     */   }
/*     */ 
/*     */   public WorkingSetGroup createWorkingSetGroup(Composite composite, IStructuredSelection selection, String[] supportedWorkingSetTypes)
/*     */   {
/* 165 */     if (this.workingSetGroup != null)
/* 166 */       return this.workingSetGroup;
/* 167 */     this.workingSetGroup = 
/* 168 */       new WorkingSetGroup(composite, selection, 
/* 168 */       supportedWorkingSetTypes);
/* 169 */     return this.workingSetGroup;
/*     */   }
/*     */ 
/*     */   private ProjectContentsLocationArea.IErrorMessageReporter getErrorReporter()
/*     */   {
/* 177 */     return new ProjectContentsLocationArea.IErrorMessageReporter()
/*     */     {
/*     */       public void reportError(String errorMessage, boolean infoOnly) {
/* 180 */         if (infoOnly) {
/* 181 */           WizardNewProjectCreationPage.this.setMessage(errorMessage, 1);
/* 182 */           WizardNewProjectCreationPage.this.setErrorMessage(null);
/*     */         }
/*     */         else {
/* 185 */           WizardNewProjectCreationPage.this.setErrorMessage(errorMessage);
/* 186 */         }boolean valid = errorMessage == null;
/* 187 */         if (valid) {
/* 188 */           valid = WizardNewProjectCreationPage.this.validatePage();
/*     */         }
/*     */ 
/* 191 */         WizardNewProjectCreationPage.this.setPageComplete(valid);
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   private final void createProjectNameGroup(Composite parent)
/*     */   {
/* 203 */     Composite projectGroup = new Composite(parent, 0);
/* 204 */     GridLayout layout = new GridLayout();
/* 205 */     layout.numColumns = 2;
/* 206 */     projectGroup.setLayout(layout);
/* 207 */     projectGroup.setLayoutData(new GridData(768));
/*     */ 
/* 210 */     Label projectLabel = new Label(projectGroup, 0);
/* 211 */     projectLabel.setText(IDEWorkbenchMessages.WizardNewProjectCreationPage_nameLabel);
/* 212 */     projectLabel.setFont(parent.getFont());
/*     */ 
/* 215 */     this.projectNameField = new Text(projectGroup, 2048);

/* 216 */     GridData data = new GridData(768);
/* 217 */     data.widthHint = 250;
/* 218 */     this.projectNameField.setLayoutData(data);
/* 219 */     this.projectNameField.setFont(parent.getFont());
/*     */ 
/* 223 */     if (this.initialProjectFieldValue != null) {
/* 224 */       this.projectNameField.setText(this.initialProjectFieldValue);
/*     */     }
/* 226 */     this.projectNameField.addListener(24, this.nameModifyListener);
/* 227 */     BidiUtils.applyBidiProcessing(this.projectNameField, "default");

			//创建复选框. 复选框不能带只读属性(SWT.READ_ONLY), 不然会认为你是按钮.
			final Button checkbox = new Button(projectGroup, SWT.CHECK);
			checkbox.setText("打开项目文件");
			checkbox.setSelection(true);
			checkbox.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					//复选框, 选中取消时触发此事件.
					//checkbox.setSelection(false); //设置复选框选中.
					checked=checkbox.getSelection();
					 String str = "";
					if(checkbox.getSelection()) {
						str += checkbox.getText();
						System.out.println(str+checkbox.getSelection());
						
					}else{
						
						System.out.println(checkbox.getSelection());
					}
					
					
					
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
					checkbox.setSelection(false);
				}
			
			

				
			});
/*     */   }
/*     */ 
/*     */   public IPath getLocationPath()
/*     */   {
/* 241 */     return new Path(this.locationArea.getProjectLocation());
/*     */   }
/*     */ 
/*     */   public URI getLocationURI()
/*     */   {
/* 254 */     return this.locationArea.getProjectLocationURI();
/*     */   }
/*     */ 
/*     */   public IProject getProjectHandle()
/*     */   {
/* 269 */     return ResourcesPlugin.getWorkspace().getRoot().getProject(
/* 270 */       getProjectName());
/*     */   }
/*     */ 
/*     */   public String getProjectName()
/*     */   {
/* 281 */     if (this.projectNameField == null) {
/* 282 */       return this.initialProjectFieldValue;
/*     */     }
/*     */ 
/* 285 */     return getProjectNameFieldValue();
/*     */   }
/*     */ 
/*     */   private String getProjectNameFieldValue()
/*     */   {
/* 295 */     if (this.projectNameField == null) {
/* 296 */       return "";
/*     */     }
/*     */ 
/* 299 */     return this.projectNameField.getText().trim();
/*     */   }
/*     */ 
/*     */   public void setInitialProjectName(String name)
/*     */   {
/* 318 */     if (name == null) {
/* 319 */       this.initialProjectFieldValue = null;
/*     */     } else {
/* 321 */       this.initialProjectFieldValue = name.trim();
/* 322 */       if (this.locationArea != null)
/* 323 */         this.locationArea.updateProjectName(name.trim());
/*     */     }
/*     */   }
/*     */ 
/*     */   void setLocationForSelection()
/*     */   {
/* 332 */     this.locationArea.updateProjectName(getProjectNameFieldValue());
/*     */   }
/*     */ 
/*     */   protected boolean validatePage()
/*     */   {
/* 344 */     IWorkspace workspace = IDEWorkbenchPlugin.getPluginWorkspace();
/*     */ 
/* 346 */     String projectFieldContents = getProjectNameFieldValue();
/* 347 */     if (projectFieldContents.equals("")) {
/* 348 */       setErrorMessage(null);
/* 349 */       setMessage(IDEWorkbenchMessages.WizardNewProjectCreationPage_projectNameEmpty);
/* 350 */       return false;
/*     */     }
/*     */ 
/* 353 */     IStatus nameStatus = workspace.validateName(projectFieldContents, 
/* 354 */       4);
/* 355 */     if (!nameStatus.isOK()) {
/* 356 */       setErrorMessage(nameStatus.getMessage());
/* 357 */       return false;
/*     */     }
/*     */ 
/* 360 */     IProject handle = getProjectHandle();
/* 361 */     if (handle.exists()) {
/* 362 */       setErrorMessage(IDEWorkbenchMessages.WizardNewProjectCreationPage_projectExistsMessage);
/* 363 */       return false;
/*     */     }
/*     */ 
/* 366 */     IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(
/* 367 */       getProjectNameFieldValue());
/* 368 */     this.locationArea.setExistingProject(project);
/*     */ 
/* 370 */     String validLocationMessage = this.locationArea.checkValidLocation();
/* 371 */     if (validLocationMessage != null) {
/* 372 */       setErrorMessage(validLocationMessage);
/* 373 */       return false;
/*     */     }
/*     */ 
/* 376 */     setErrorMessage(null);
/* 377 */     setMessage(null);
/* 378 */     return true;
/*     */   }
/*     */ 
/*     */   public void setVisible(boolean visible)
/*     */   {
/* 386 */     super.setVisible(visible);
/* 387 */     if (visible)
/* 388 */       this.projectNameField.setFocus();
/*     */   }
/*     */ 
/*     */   public boolean useDefaults()
/*     */   {
/* 397 */     return this.locationArea.isDefault();
/*     */   }
/*     */ 
/*     */   public IWorkingSet[] getSelectedWorkingSets()
/*     */   {
/* 408 */     return this.workingSetGroup == null ? new IWorkingSet[0] : this.workingSetGroup
/* 409 */       .getSelectedWorkingSets();
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\plugins\org.eclipse.ui.ide_3.11.0.v20150510-1749.jar
 * Qualified Name:     org.eclipse.ui.dialogs.WizardNewProjectCreationPage
 * JD-Core Version:    0.6.0
 */