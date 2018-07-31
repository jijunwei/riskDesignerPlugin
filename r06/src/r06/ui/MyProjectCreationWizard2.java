/*     */ package r06.ui;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import org.eclipse.core.runtime.CoreException;
/*     */ import org.eclipse.core.runtime.IConfigurationElement;
/*     */ import org.eclipse.core.runtime.IExecutableExtension;
/*     */ import org.eclipse.core.runtime.IPath;
/*     */ import org.eclipse.core.runtime.IProgressMonitor;
/*     */ import org.eclipse.core.runtime.Path;
/*     */ import org.eclipse.jdt.core.IClasspathEntry;
/*     */ import org.eclipse.jdt.core.IJavaProject;
/*     */ import org.eclipse.jdt.core.JavaCore;
/*     */ import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageOne;
/*     */ import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageTwo;
/*     */ import org.eclipse.jface.viewers.IStructuredSelection;
/*     */ import org.eclipse.jface.wizard.IWizardContainer;
/*     */ import org.eclipse.jface.wizard.IWizardPage;
/*     */ import org.eclipse.jface.wizard.Wizard;
/*     */ import org.eclipse.jface.wizard.WizardPage;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.ui.INewWizard;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.IWorkingSet;
/*     */ import org.eclipse.ui.IWorkingSetManager;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ import org.eclipse.ui.actions.WorkspaceModifyOperation;
/*     */ import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
/*     */ import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;
/*     */ 
/*     */ public class MyProjectCreationWizard2 extends Wizard
/*     */   implements IExecutableExtension, INewWizard
/*     */ {
/*     */   private NewJavaProjectWizardPageOne fMainPage;
/*     */   private NewJavaProjectWizardPageTwo fJavaPage;
/*     */   private IWizardPage fExtraPage;
/*     */   private IConfigurationElement fConfigElement;
/*     */ 
/*     */   public MyProjectCreationWizard2()
/*     */   {
/*  79 */     setWindowTitle("New solution Project");
/*     */   }
/*     */ 
/*     */   public void setInitializationData(IConfigurationElement cfig, String propertyName, Object data)
/*     */   {
/*  87 */     this.fConfigElement = cfig;
/*     */   }
/*     */ 
/*     */   public void init(IWorkbench workbench, IStructuredSelection selection)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void addPages()
/*     */   {
/* 100 */     super.addPages();
/*     */ 
/* 103 */     this.fMainPage = new NewJavaProjectWizardPageOne() {
/*     */       public void createControl(Composite parent) {
/* 105 */         initializeDialogUnits(parent);
/*     */ 
/* 107 */         Composite composite = new Composite(parent, 0);
/* 108 */         composite.setFont(parent.getFont());
/* 109 */         composite.setLayout(new GridLayout(1, false));
/* 110 */         composite.setLayoutData(new GridData(256));
/*     */ 
/* 113 */         Control nameControl = createNameControl(composite);
/* 114 */         nameControl.setLayoutData(new GridData(768));
/*     */ 
/* 116 */         Control jreControl = createJRESelectionControl(composite);
/* 117 */         jreControl.setLayoutData(new GridData(768));
/*     */ 
/* 119 */         Control workingSetControl = createWorkingSetControl(composite);
/* 120 */         workingSetControl.setLayoutData(new GridData(768));
/*     */ 
/* 122 */         Control infoControl = createInfoControl(composite);
/* 123 */         infoControl.setLayoutData(new GridData(768));
/*     */ 
/* 125 */         setControl(composite);
/*     */       }
/*     */ 
/*     */       public IClasspathEntry[] getSourceClasspathEntries() {
/* 129 */         IPath path1 = new Path(getProjectName()).append("src").makeAbsolute();
/* 130 */         IPath path2 = new Path(getProjectName()).append("tests").makeAbsolute();
/* 131 */         return new IClasspathEntry[] { JavaCore.newSourceEntry(path1), JavaCore.newSourceEntry(path2) };
/*     */       }
/*     */ 
/*     */       public IPath getOutputLocation() {
/* 135 */         IPath path1 = new Path(getProjectName()).append("classes").makeAbsolute();
/* 136 */         return path1;
/*     */       }
/*     */     };
/* 139 */     this.fMainPage.setProjectName("solution");
/*     */ 
/* 142 */     addPage(this.fMainPage);
/*     */ 
/* 145 */     this.fJavaPage = new NewJavaProjectWizardPageTwo(this.fMainPage);
/*     */ 
/* 147 */     addPage(this.fJavaPage);
/*     */ 
/* 149 */     this.fExtraPage = new WizardPage("My Page")
/*     */     {
/*     */       public void createControl(Composite parent) {
/* 152 */         initializeDialogUnits(parent);
/*     */ 
/* 154 */         Button button = new Button(parent, 32);
/* 155 */         button.setText("Make it a special project");
/*     */ 
/* 157 */         setControl(button);
/*     */       }
/*     */     };
/* 163 */     addPage(this.fExtraPage);
/*     */   }
/*     */ 
/*     */   public boolean performFinish()
/*     */   {
/* 171 */     WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
/*     */       protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
/* 173 */         MyProjectCreationWizard2.this.fJavaPage.performFinish(monitor);
/*     */       }
/*     */     };
/*     */     try {
/* 178 */       getContainer().run(false, true, op);
/*     */ 
/* 180 */       IJavaProject newElement = this.fJavaPage.getJavaProject();
/*     */ 
/* 182 */       IWorkingSet[] workingSets = this.fMainPage.getWorkingSets();
/* 183 */       if (workingSets.length > 0) {
/* 184 */         PlatformUI.getWorkbench().getWorkingSetManager().addToWorkingSets(newElement, workingSets);
/*     */       }
/* 186 */       BasicNewProjectResourceWizard.updatePerspective(this.fConfigElement);
/* 187 */       BasicNewResourceWizard.selectAndReveal(newElement.getResource(), PlatformUI.getWorkbench().getActiveWorkbenchWindow());
/*     */     }
/*     */     catch (InvocationTargetException localInvocationTargetException)
/*     */     {
/* 191 */       return false;
/*     */     } catch (InterruptedException localInterruptedException) {
/* 193 */       return false;
/*     */     }
/* 195 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean performCancel() {
/* 199 */     this.fJavaPage.performCancel();
/* 200 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Users\jjw8610\Desktop\EclipsePlugin\org.eclipse.jdt.ui.tests_3.10.101.v20151123-1510\
 * Qualified Name:     org.eclipse.jdt.ui.examples.MyProjectCreationWizard2
 * JD-Core Version:    0.6.0
 */