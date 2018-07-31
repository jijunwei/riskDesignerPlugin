/*    */ package r06.ui.filters;
/*    */ 
/*    */ import org.eclipse.core.resources.IProject;
/*    */ import org.eclipse.core.resources.IProjectDescription;
/*    */ import org.eclipse.core.runtime.CoreException;
/*    */ import org.eclipse.jdt.core.IJavaProject;
/*    */ import org.eclipse.jface.viewers.Viewer;
/*    */ import org.eclipse.jface.viewers.ViewerFilter;
/*    */ 
/*    */ public class PureProjectFilter extends ViewerFilter
/*    */ {
/*    */   public boolean select(Viewer viewer, Object parentElement, Object element)
/*    */   {
/* 29 */     if ((element instanceof IJavaProject)) {
/* 30 */       element = ((IJavaProject)element).getResource();
/*    */     }
/* 32 */     if ((element instanceof IProject)) {
/* 33 */       IProject proj = (IProject)element;
/* 34 */       if (!proj.isOpen())
/* 35 */         return false;
/*    */       try {
/* 37 */         String[] natureIds = proj.getDescription().getNatureIds();
/* 38 */         return natureIds.length == 1;
/*    */       }
/*    */       catch (CoreException e) {
/* 41 */         e.printStackTrace();
/*    */       }
/*    */     }
/* 44 */     return true;
/*    */   }
/*    */ }

/* Location:           C:\Users\jjw8610\Desktop\EclipsePlugin\org.eclipse.jdt.ui.tests_3.10.101.v20151123-1510\
 * Qualified Name:     org.eclipse.jdt.ui.examples.filters.PureProjectFilter
 * JD-Core Version:    0.6.0
 */