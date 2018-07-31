/*    */ package r06.ui.filters;
/*    */ 
/*    */ import org.eclipse.jdt.core.ICompilationUnit;
/*    */ import org.eclipse.jface.viewers.Viewer;
/*    */ import org.eclipse.jface.viewers.ViewerFilter;
/*    */ 
/*    */ public class AFileFilter extends ViewerFilter
/*    */ {
/*    */   public boolean select(Viewer viewer, Object parentElement, Object element)
/*    */   {
/* 25 */     if ((element instanceof ICompilationUnit)) {
/* 26 */       return !((ICompilationUnit)element).getElementName().equals(".project");
/*    */     }
/* 28 */     return true;
/*    */   }
/*    */ }

/* Location:           C:\Users\jjw8610\Desktop\EclipsePlugin\org.eclipse.jdt.ui.tests_3.10.101.v20151123-1510\
 * Qualified Name:     org.eclipse.jdt.ui.examples.filters.AFileFilter
 * JD-Core Version:    0.6.0
 */