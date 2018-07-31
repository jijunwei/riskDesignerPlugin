/*    */ package r06.ui;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.util.Random;
/*    */ import org.eclipse.jface.viewers.ILabelProvider;
/*    */ import org.eclipse.jface.viewers.LabelProvider;
/*    */ import org.eclipse.swt.widgets.Display;
/*    */ import org.eclipse.swt.widgets.Shell;
/*    */ import org.eclipse.ui.dialogs.TwoPaneElementSelector;
/*    */ 
/*    */ public class TwoPaneElementSelectorExample
/*    */ {
/*    */   public static void main(String[] args)
/*    */   {
/* 28 */     Random random = new Random();
/* 29 */     Object[] elements = new Object[8000];
/* 30 */     for (int i = 0; i < elements.length; i++)
/* 31 */       elements[i] = new Integer(random.nextInt()).toString();
/* 32 */     ILabelProvider elementRenderer = new LabelProvider() {
/*    */       public String getText(Object element) {
/* 34 */         return element.toString();
/*    */       }
/*    */     };
/* 37 */     ILabelProvider qualfierRenderer = new LabelProvider() {
/*    */       public String getText(Object element) {
/* 39 */         return element.toString();
/*    */       }
/*    */     };
/* 42 */     Display display = new Display();
/* 43 */     TwoPaneElementSelector d = new TwoPaneElementSelector(new Shell(display), elementRenderer, qualfierRenderer);
/* 44 */     d.setTitle("Title");
/* 45 */     d.setMessage("this is a message");
/* 46 */     d.setElements(elements);
/*    */ 
/* 48 */     d.open();
/*    */ 
/* 50 */     Object res = d.getResult();
/* 51 */     System.out.println("res= " + res);
/*    */   }
/*    */ }

/* Location:           C:\Users\jjw8610\Desktop\EclipsePlugin\org.eclipse.jdt.ui.tests_3.10.101.v20151123-1510\
 * Qualified Name:     org.eclipse.jdt.ui.examples.TwoPaneElementSelectorExample
 * JD-Core Version:    0.6.0
 */