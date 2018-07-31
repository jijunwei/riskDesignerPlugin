/*    */ package r06.ui;
/*    */ 
/*    */ import org.eclipse.jface.viewers.ILabelDecorator;
/*    */ import org.eclipse.jface.viewers.LabelProvider;
/*    */ import org.eclipse.swt.graphics.Image;
/*    */ 
/*    */ public class JavaElementDecorator extends LabelProvider
/*    */   implements ILabelDecorator
/*    */ {
/*    */   public Image decorateImage(Image image, Object element)
/*    */   {
/* 27 */     return null;
/*    */   }
/*    */ 
/*    */   public String decorateText(String text, Object element)
/*    */   {
/* 34 */     return text + "*";
/*    */   }
/*    */ }

/* Location:           C:\Users\jjw8610\Desktop\EclipsePlugin\org.eclipse.jdt.ui.tests_3.10.101.v20151123-1510\
 * Qualified Name:     org.eclipse.jdt.ui.examples.JavaElementDecorator
 * JD-Core Version:    0.6.0
 */