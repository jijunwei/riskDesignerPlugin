/*     */ package r06.ui;
/*     */ 
/*     */ import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
/*     */ import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
/*     */ import org.eclipse.jdt.internal.ui.wizards.dialogfields.IListAdapter;
/*     */ import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
/*     */ import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
/*     */ import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField;
/*     */ import org.eclipse.jface.viewers.LabelProvider;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Display;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.TabFolder;
/*     */ import org.eclipse.swt.widgets.TabItem;
/*     */ 
/*     */ public class TabExample
/*     */ {
/*     */   private Shell fShell;
/*     */ 
/*     */   public TabExample close()
/*     */   {
/*  41 */     if ((this.fShell != null) && (!this.fShell.isDisposed())) this.fShell.dispose();
/*  42 */     this.fShell = null;
/*  43 */     return this;
/*     */   }
/*     */ 
/*     */   public TabExample open() {
/*  47 */     this.fShell = new Shell();
/*  48 */     this.fShell.setText("TabTest");
/*  49 */     this.fShell.setLayout(new GridLayout());
/*     */ 
/*  51 */     TabFolder folder = new TabFolder(this.fShell, 0);
/*  52 */     folder.setLayoutData(new GridData(1808));
/*     */ 
/*  65 */     TabItem item = new TabItem(folder, 0);
/*  66 */     item.setText("Tab0");
/*     */ 
/*  68 */     String[] addButtons = { 
/*  69 */       "Add1", 
/*  70 */       "Add2", 
/*  72 */       "0", "Remove" };
/*     */ 
/*  74 */     @SuppressWarnings("unchecked")
				ListDialogField list = new ListDialogField(new Adapter(), addButtons, new LabelProvider());
/*  75 */     list.setRemoveButtonIndex(3);
/*  76 */     list.setLabelText("List: ");
/*     */ 
/*  79 */     Composite c1 = new Composite(folder, 0);
/*  80 */     LayoutUtil.doDefaultLayout(c1, new DialogField[] { list }, true);
/*     */ 
/*  82 */     item.setControl(c1);
/*     */ 
/*  84 */     item = new TabItem(folder, 0);
/*  85 */     item.setText("Tab1");
/*  86 */     Label label = new Label(folder, 16384);
/*  87 */     label.setText("Tab1");
/*  88 */     item.setControl(label);
/*     */ 
/*  90 */     item = new TabItem(folder, 0);
/*  91 */     item.setText("Tab2");
/*  92 */     label = new Label(folder, 16384);
/*  93 */     label.setText("Tab2");
/*  94 */     item.setControl(label);
/*     */ 
/*  96 */     item = new TabItem(folder, 0);
/*  97 */     item.setText("Tab3");
/*  98 */     label = new Label(folder, 16384);
/*  99 */     label.setText("Tab3");
/* 100 */     item.setControl(label);
/*     */ 
/* 102 */     this.fShell.setSize(400, 500);
/* 103 */     this.fShell.open();
/* 104 */     return this;
/*     */   }
/*     */ 
/*     */   public TabExample run()
/*     */   {
/* 132 */     Display display = this.fShell.getDisplay();
/* 133 */     while (!this.fShell.isDisposed()) {
/* 134 */       if (display.readAndDispatch()) continue; display.sleep();
/*     */     }
/* 136 */     return this;
/*     */   }
/*     */ 
/*     */   public static void main(String[] args) {
/* 140 */     new TabExample().open().run().close();
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
 * Qualified Name:     org.eclipse.jdt.ui.examples.TabExample
 * JD-Core Version:    0.6.0
 */