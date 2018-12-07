/*     */ package r06.exportWizards;
			import java.io.File;
/*     */ import java.util.Iterator;

/*     */ import org.eclipse.core.resources.IResource;
/*     */ import org.eclipse.core.runtime.IAdaptable;
/*     */ import org.eclipse.core.runtime.IPath;
/*     */ import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
/*     */ import org.eclipse.swt.widgets.Event;
/*     */ import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
/*     */ import org.eclipse.ui.internal.ide.misc.ResourceAndContainerGroup;
/*     */ @SuppressWarnings("restriction")
/*     */ public class WizardNewExportPage extends WizardPage
/*     */   implements Listener
/*     */ {
/*     */   private IStructuredSelection currentSelection;
/*     */   protected ResourceAndContainerGroup resourceGroup;
/*     */   private IPath initialContainerFullPath;
/* 128 */   private boolean initialAllowExistingResources = false; 
			String selecteddir;
/* 139 */  
/*     */ 
/*     */ 
/*     */   public WizardNewExportPage(String pageName, IStructuredSelection selection)
/*     */   {
/* 153 */     super(pageName);
/* 154 */     setPageComplete(true);
/* 155 */     this.currentSelection = selection;
/*     */   }
/*     */ 
/*     */
/*     */   public void createControl(Composite parent)
/*     */   {
/* 237 */     initializeDialogUnits(parent);
/*     */ 
/* 239 */     Composite topLevel = new Composite(parent, 0);
/* 240 */     topLevel.setLayout(new GridLayout());
/* 241 */     topLevel.setLayoutData(new GridData(272));
/*     */ 
/* 243 */     topLevel.setFont(parent.getFont());
/* 244 */    
/*     */ 
/* 248 */     this.resourceGroup = 
/* 251 */       new ResourceAndContainerGroup(topLevel, this, 
/* 249 */       getNewFileLabel(), 
/* 250 */       IDEWorkbenchMessages.WizardNewFileCreationPage_file, false, 
/* 251 */       250);
/* 252 */     this.resourceGroup.setAllowExistingResources(this.initialAllowExistingResources);
/* 253 */     initialPopulateContainerNameField();
/* 254 */     openfolderDig(getShell());
/* 255 */     
/*     */ 
/* 263 */     setErrorMessage(null);
/* 264 */     setMessage(null);
/* 265 */     setControl(topLevel);
/*     */   }
/*     */ 
  
/*     */ 

   public void openfolderDig(Shell parent){
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
	setFileName(selecteddir);
	}
}

/*     */ 
/*     */   public IPath getContainerFullPath()
/*     */   {
/* 554 */     return this.resourceGroup.getContainerFullPath();
/*     */   }
/*     */ 
/*     */  
/*     */   
/*     */ 
/*     */  
/*     */ 
/*     */   protected String getNewFileLabel()
/*     */   {
/* 613 */     return IDEWorkbenchMessages.WizardNewFileCreationPage_fileLabel;
/*     */   }
/*     */ 


/*     */ 
/*     */   public void handleEvent(Event event)
/*     */   {
/* 687 */     setPageComplete(validatePage());
/*     */   }

			protected boolean validatePage()
/*     */   {
/* 830 */     boolean valid = true;
/*     */ 
/* 832 */     
/*     */     
/* 844 */     String resourceName = this.resourceGroup.getResource();
/* 845 */     File dir=new File(resourceName);
               
               if (!dir.exists()) {
				setErrorMessage("not exists");	
				//dir.mkdir();//创建文件夹
				 valid=false;
				}else{
					if(!dir.isDirectory()){
		            	   setErrorMessage("is not directory");	
		            	   valid=false;
		               }
					valid=true;
				}
				
/* 847 */     

/* 887 */     return valid;
/*     */   }
/*     */ 
/*     */   @SuppressWarnings("rawtypes")
			protected void initialPopulateContainerNameField()
/*     */   {
/* 696 */     if (this.initialContainerFullPath != null) {
/* 697 */       this.resourceGroup.setContainerFullPath(this.initialContainerFullPath);
/*     */     } else {
/* 699 */       Iterator it = this.currentSelection.iterator();
/* 700 */       if (it.hasNext()) {
/* 701 */         Object object = it.next();
/* 702 */         IResource selectedResource = null;
/* 703 */         if ((object instanceof IResource))
/* 704 */           selectedResource = (IResource)object;
/* 705 */         else if ((object instanceof IAdaptable)) {
/* 706 */           selectedResource = (IResource)((IAdaptable)object).getAdapter(IResource.class);
/*     */         }
/* 708 */         if (selectedResource != null) {
/* 709 */           if (selectedResource.getType() == 1) {
/* 710 */             selectedResource = selectedResource.getParent();
/*     */           }
/* 712 */           if (selectedResource.isAccessible())
/* 713 */             this.resourceGroup.setContainerFullPath(selectedResource
/* 714 */               .getFullPath());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setAllowExistingResources(boolean value)
/*     */   {
/* 731 */     if (this.resourceGroup == null)
/* 732 */       this.initialAllowExistingResources = value;
/*     */     else
/* 734 */       this.resourceGroup.setAllowExistingResources(value);
/*     */   }
/*     */ 
/*     */   public void setContainerFullPath(IPath path)
/*     */   {
/* 746 */     if (this.resourceGroup == null)
/* 747 */       this.initialContainerFullPath = path;
/*     */     else
/* 749 */       this.resourceGroup.setContainerFullPath(path);
/*     */   }
/*     */ 
/*     */   public void setFileName(String value)
/*     */   {
/* 764 */       this.resourceGroup.setResource(value);
			    
/*     */   }
/*     */ 
/*     */  
/*     */ 

/*     */ 
/*     */   

/*     */ 
/*     */ 
/*     */   public void setVisible(boolean visible)
/*     */   {
/* 910 */     super.setVisible(visible);
/* 911 */     if (visible)
/* 912 */       this.resourceGroup.setFocus();
/*     */   }
/*     */

 }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\plugins\org.eclipse.ui.ide_3.11.0.v20150510-1749.jar
 * Qualified Name:     org.eclipse.ui.dialogs.WizardNewFileCreationPage
 * JD-Core Version:    0.6.0
 */