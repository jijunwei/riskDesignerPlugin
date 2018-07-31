/*     */ package r06.ui;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import org.eclipse.jdt.internal.ui.wizards.dialogfields.CheckedListDialogField;
/*     */ import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
/*     */ import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
/*     */ import org.eclipse.jdt.internal.ui.wizards.dialogfields.IListAdapter;
/*     */ import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
/*     */ import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
/*     */ import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField;
/*     */ import org.eclipse.jface.viewers.LabelProvider;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Display;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ 
/*     */ public class CheckBoxExample
/*     */ {
/*     */   private Shell fShell;
/*  76 */   private static Random fgRandom = new Random();
/*     */ 
/*     */   public CheckBoxExample close()
/*     */   {
/*  39 */     if ((this.fShell != null) && (!this.fShell.isDisposed())) this.fShell.dispose();
/*  40 */     this.fShell = null;
/*  41 */     return this;
/*     */   }
/*     */ 
/*     */   public CheckBoxExample open() {
/*  45 */     this.fShell = new Shell();
/*  46 */     this.fShell.setText("Message Dialog Example");
/*  47 */     this.fShell.setLayout(new GridLayout());
/*     */ 
/*  49 */     Adapter adapter = new Adapter();
/*     */ 
/*  53 */     String[] addButtons = { 
/*  54 */       "Add1", 
/*  55 */       "Check 0", 
/*  56 */       "Print",
/*  58 */       "0", "Check All", 
/*  59 */       "Uncheck All", 
/*  61 */       "0", "Remove" };
/*     */ 
/*  63 */     CheckedListDialogField list = new CheckedListDialogField(adapter, addButtons, new LabelProvider());
/*  64 */     list.setCheckAllButtonIndex(4);
/*  65 */     list.setUncheckAllButtonIndex(5);
/*  66 */     list.setRemoveButtonIndex(7);
/*  67 */     list.setLabelText("List: ");
/*     */ 
/*  69 */     LayoutUtil.doDefaultLayout(this.fShell, new DialogField[] { list }, false);
/*     */ 
/*  71 */     this.fShell.setSize(400, 500);
/*  72 */     this.fShell.open();
/*  73 */     return this;
/*     */   }
/*     */ 
/*     */   public CheckBoxExample run()
/*     */   {
/* 115 */     Display display = this.fShell.getDisplay();
/* 116 */     while (!this.fShell.isDisposed()) {
/* 117 */       if (display.readAndDispatch()) continue; display.sleep();
/*     */     }
/* 119 */     return this;
/*     */   }
/*     */ 
/*     */   public static void main(String[] args) {
/* 123 */     new CheckBoxExample().open().run().close();
/*     */   }
/*     */ 
/*     */   private class Adapter
/*     */     implements IStringButtonAdapter, IDialogFieldListener, IListAdapter
/*     */   {
/*     */     private Adapter()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void changeControlPressed(DialogField field)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void customButtonPressed(ListDialogField field, int index)
/*     */     {
/*  86 */       if ((field instanceof CheckedListDialogField)) {
/*  87 */         CheckedListDialogField list = (CheckedListDialogField)field;
/*  88 */         if (index == 0) {
/*  89 */           list.addElement("element-" + CheckBoxExample.fgRandom.nextInt() % 1000);
/*  90 */         } else if (index == 2) {
/*  91 */           System.out.println("---- printing all");
/*  92 */           List checked = list.getCheckedElements();
/*  93 */           for (int i = 0; i < checked.size(); i++)
/*  94 */             System.out.println(checked.get(i).toString());
/*     */         }
/*     */         else {
/*  97 */           list.setChecked(list.getElement(0), true);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public void selectionChanged(ListDialogField field)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void dialogFieldChanged(DialogField field)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void doubleClicked(ListDialogField field)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\jjw8610\Desktop\EclipsePlugin\org.eclipse.jdt.ui.tests_3.10.101.v20151123-1510\
 * Qualified Name:     org.eclipse.jdt.ui.examples.CheckBoxExample
 * JD-Core Version:    0.6.0
 */