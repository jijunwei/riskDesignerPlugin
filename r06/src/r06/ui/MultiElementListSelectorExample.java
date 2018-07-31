/*    */ package r06.ui;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.util.Random;
/*    */ import org.eclipse.core.runtime.IStatus;
/*    */ import org.eclipse.jdt.internal.ui.dialogs.MultiElementListSelectionDialog;
/*    */ import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
/*    */ import org.eclipse.jface.viewers.ILabelProvider;
/*    */ import org.eclipse.jface.viewers.LabelProvider;
/*    */ import org.eclipse.swt.widgets.Display;
/*    */ import org.eclipse.swt.widgets.Shell;
/*    */ import org.eclipse.ui.dialogs.ISelectionStatusValidator;
/*    */ 
/*    */ public class MultiElementListSelectorExample
/*    */ {
/*    */   public static void main(String[] args)
/*    */   {
/* 39 */     ISelectionStatusValidator validator = new ISelectionStatusValidator() {
/*    */       public IStatus validate(Object[] selection) {
/* 41 */         if ((selection != null) && (selection.length == 1)) {
/* 42 */           return new StatusInfo();
/*    */         }
/* 44 */         StatusInfo status = new StatusInfo();
/* 45 */         status.setError("Single selection");
/* 46 */         return status;
/*    */       }
/*    */     };
/* 54 */     Random random = new Random();
/*    */ 
/* 56 */     ILabelProvider elementRenderer = new LabelProvider() {
/*    */       public String getText(Object element) {
/* 58 */         return element.toString();
/*    */       }
/*    */     };
/* 62 */     int nPages = 3;
/* 63 */     Object[][] elements = new Object[nPages][];
/* 64 */     for (int i = 0; i < nPages; i++) {
/* 65 */       int size = random.nextInt(15);
/* 66 */       elements[i] = new String[size];
/* 67 */       for (int k = 0; k < size; k++) {
/* 68 */         elements[i][k] = ("elem-" + i + "-" + k);
/*    */       }
/*    */     }
/*    */ 
/* 72 */     Display display = new Display();
/* 73 */     MultiElementListSelectionDialog d = new MultiElementListSelectionDialog(new Shell(display), elementRenderer);
/* 74 */     d.setTitle("Title");
/* 75 */     d.setIgnoreCase(true);
/* 76 */     d.setMessage("this is a message");
/* 77 */     d.setValidator(validator);
/* 78 */     d.setElements(elements);
/*    */ 
/* 80 */     d.open();
/*    */ 
/* 82 */     Object[] res = d.getResult();
/* 83 */     if (res != null)
/* 84 */       for (int i = 0; i < res.length; i++)
/* 85 */         System.out.println(res[i]);
/*    */   }
/*    */ }

/* Location:           C:\Users\jjw8610\Desktop\EclipsePlugin\org.eclipse.jdt.ui.tests_3.10.101.v20151123-1510\
 * Qualified Name:     org.eclipse.jdt.ui.examples.MultiElementListSelectorExample
 * JD-Core Version:    0.6.0
 */