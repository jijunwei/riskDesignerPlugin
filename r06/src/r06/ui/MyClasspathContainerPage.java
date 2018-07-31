/*    */ package r06.ui;
/*    */ 
/*    */ import org.eclipse.core.runtime.Path;
/*    */ import org.eclipse.jdt.core.IClasspathEntry;
/*    */ import org.eclipse.jdt.core.JavaCore;
/*    */ import org.eclipse.jdt.ui.wizards.IClasspathContainerPage;
/*    */ import org.eclipse.jface.wizard.WizardPage;
/*    */ import org.eclipse.swt.widgets.Composite;
/*    */ import org.eclipse.swt.widgets.Label;
/*    */ 
/*    */ public class MyClasspathContainerPage extends WizardPage
/*    */   implements IClasspathContainerPage
/*    */ {
/*    */   private IClasspathEntry fEntry;
/*    */ 
/*    */   public MyClasspathContainerPage()
/*    */   {
/* 31 */     super("MyClasspathContainerPage");
/* 32 */     setTitle("My Example Container");
/*    */   }
/*    */ 
/*    */   public void createControl(Composite parent) {
/* 36 */     Label label = new Label(parent, 0);
/* 37 */     if (this.fEntry == null) {
/* 38 */       label.setText("Nothing to configure. Press 'Finish' to add new entry");
/*    */     } else {
/* 40 */       label.setText("Nothing to configure.");
/* 41 */       setPageComplete(false);
/*    */     }
/* 43 */     setControl(label);
/*    */   }
/*    */ 
/*    */   public boolean finish() {
/* 47 */     if (this.fEntry == null) {
/* 48 */       this.fEntry = JavaCore.newContainerEntry(new Path("org.eclipse.jdt.EXAMPLE_CONTAINER"));
/*    */     }
/* 50 */     return true;
/*    */   }
/*    */ 
/*    */   public IClasspathEntry getSelection() {
/* 54 */     return this.fEntry;
/*    */   }
/*    */ 
/*    */   public void setSelection(IClasspathEntry containerEntry) {
/* 58 */     this.fEntry = containerEntry;
/*    */   }
/*    */ }

/* Location:           C:\Users\jjw8610\Desktop\EclipsePlugin\org.eclipse.jdt.ui.tests_3.10.101.v20151123-1510\
 * Qualified Name:     org.eclipse.jdt.ui.examples.MyClasspathContainerPage
 * JD-Core Version:    0.6.0
 */