/*    */ package r06.ui;
/*    */ 
/*    */ import org.eclipse.core.resources.IProject;
/*    */ import org.eclipse.jdt.internal.ui.wizards.buildpaths.BuildPathsBlock;
/*    */ 
/*    */ import org.eclipse.jface.action.Action;
/*    */ import org.eclipse.jface.action.IAction;
/*    */ import org.eclipse.jface.viewers.ISelection;
/*    */ import org.eclipse.jface.viewers.IStructuredSelection;
/*    */ import org.eclipse.ui.IActionDelegate;
/*    */ 
/*    */ public class AddJavaNatureAction extends Action
/*    */   implements IActionDelegate
/*    */ {
/*    */   private IProject fProject;
/*    */ 
/*    */   public void run(IAction action)
/*    */   {
/*    */     try
/*    */     {
/* 54 */       if (this.fProject != null)
/* 55 */         BuildPathsBlock.addJavaNature(this.fProject, null);
/*    */     }
/*    */     catch (Exception e) {
/* 58 */      
/*    */     }
/*    */   }
/*    */ 
/*    */   public void selectionChanged(IAction action, ISelection selection)
/*    */   {
/* 66 */     this.fProject = null;
/* 67 */     if ((selection instanceof IStructuredSelection)) {
/* 68 */       Object object = ((IStructuredSelection)selection).getFirstElement();
/* 69 */       if ((object instanceof IProject))
/* 70 */         this.fProject = ((IProject)object);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Users\jjw8610\Desktop\EclipsePlugin\org.eclipse.jdt.ui.tests_3.10.101.v20151123-1510\
 * Qualified Name:     org.eclipse.jdt.ui.examples.AddJavaNatureAction
 * JD-Core Version:    0.6.0
 */