/*     */ package r06.ui;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
/*     */ import org.eclipse.jdt.internal.ui.wizards.dialogfields.ITreeListAdapter;
/*     */ import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
/*     */ import org.eclipse.jdt.internal.ui.wizards.dialogfields.TreeListDialogField;
/*     */ import org.eclipse.jface.viewers.LabelProvider;
/*     */ import org.eclipse.swt.events.KeyEvent;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Display;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ 
/*     */ public class TreeListDialogFieldExample
/*     */ {
/*     */   private Shell fShell;
/*  81 */   private static Random fgRandom = new Random();
/*     */ 
/*     */   public TreeListDialogFieldExample close()
/*     */   {
/*  37 */     if ((this.fShell != null) && (!this.fShell.isDisposed())) this.fShell.dispose();
/*  38 */     this.fShell = null;
/*  39 */     return this;
/*     */   }
/*     */ 
/*     */   public TreeListDialogFieldExample open()
/*     */   {
/*  45 */     this.fShell = new Shell();
/*  46 */     this.fShell.setText("Message Dialog Example");
/*  47 */     this.fShell.setLayout(new GridLayout());
/*     */ 
/*  49 */     Adapter adapter = new Adapter();
/*     */ 
/*  52 */     String[] addButtons = { 
/*  53 */       "Add1", 
/*  54 */       "Add2", 
/*  56 */       "0", "Up", 
/*  57 */       "Down", 
/*  59 */       "0", "Remove" };
/*     */ 
/*  62 */     TreeListDialogField list = new TreeListDialogField(adapter, addButtons, new LabelProvider());
/*  63 */     list.setUpButtonIndex(3);
/*  64 */     list.setDownButtonIndex(4);
/*  65 */     list.setRemoveButtonIndex(6);
/*  66 */     list.setLabelText("List: ");
/*     */ 
/*  69 */     for (int i = 0; i < 30; i++) {
/*  70 */       list.addElement(i + "firstxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
/*     */     }
/*     */ 
/*  74 */     LayoutUtil.doDefaultLayout(this.fShell, new DialogField[] { list }, false);
/*     */ 
/*  76 */     this.fShell.setSize(this.fShell.computeSize(-1, -1));
/*  77 */     this.fShell.open();
/*  78 */     return this;
/*     */   }
/*     */ 
/*     */   public TreeListDialogFieldExample run()
/*     */   {
/* 119 */     Display display = this.fShell.getDisplay();
/* 120 */     while (!this.fShell.isDisposed()) {
/* 121 */       if (display.readAndDispatch()) continue; display.sleep();
/*     */     }
/* 123 */     return this;
/*     */   }
/*     */ 
/*     */   public static void main(String[] args) {
/* 127 */     new TreeListDialogFieldExample().open().run().close();
/*     */   }
/*     */ 
/*     */   private class Adapter
/*     */     implements ITreeListAdapter
/*     */   {
/*     */     private Adapter()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void customButtonPressed(TreeListDialogField field, int index)
/*     */     {
/*  87 */       field.addElement("elementxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx-" + TreeListDialogFieldExample.fgRandom.nextInt());
/*     */     }
/*     */     public void selectionChanged(TreeListDialogField field) {
/*     */     }
/*     */ 
/*     */     public Object[] getChildren(TreeListDialogField field, Object element) {
/*  93 */       if (field.getElements().contains(element)) {
/*  94 */         return new String[] { 
/*  95 */           "Source Attachment: c:/hello/z.zip", 
/*  96 */           "Javadoc Location: http://www.oo.com/doc" };
/*     */       }
/*     */ 
/*  99 */       return new String[0];
/*     */     }
/*     */ 
/*     */     public Object getParent(TreeListDialogField field, Object element) {
/* 103 */       return null;
/*     */     }
/*     */ 
/*     */     public boolean hasChildren(TreeListDialogField field, Object element) {
/* 107 */       return field.getElements().contains(element);
/*     */     }
/*     */ 
/*     */     public void doubleClicked(TreeListDialogField field)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void keyPressed(TreeListDialogField field, KeyEvent event)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\jjw8610\Desktop\EclipsePlugin\org.eclipse.jdt.ui.tests_3.10.101.v20151123-1510\
 * Qualified Name:     org.eclipse.jdt.ui.examples.TreeListDialogFieldExample
 * JD-Core Version:    0.6.0
 */