/*     */ package r06.ui;
/*     */ 
/*     */ import java.util.Random;
/*     */ import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
/*     */ import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
/*     */ import org.eclipse.jdt.internal.ui.wizards.dialogfields.IListAdapter;
/*     */ import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
/*     */ import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
/*     */ import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField;
/*     */ import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField.ColumnsDescription;
/*     */ import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogField;
/*     */ import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogFieldGroup;
/*     */ import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
/*     */ import org.eclipse.jface.viewers.ITableLabelProvider;
/*     */ import org.eclipse.jface.viewers.LabelProvider;
/*     */ import org.eclipse.swt.graphics.Image;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Display;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ 
/*     */ public class DialogFieldExample
/*     */ {
/*     */   private Shell fShell;
/* 131 */   private static Random fgRandom = new Random();
/*     */ 
/*     */   public DialogFieldExample close()
/*     */   {
/*  44 */     if ((this.fShell != null) && (!this.fShell.isDisposed())) this.fShell.dispose();
/*  45 */     this.fShell = null;
/*  46 */     return this;
/*     */   }
/*     */ 
/*     */   public DialogFieldExample open()
/*     */   {
/*  70 */     this.fShell = new Shell();
/*  71 */     this.fShell.setText("Message Dialog Example");
/*  72 */     this.fShell.setLayout(new GridLayout());
/*     */ 
/*  74 */     Adapter adapter = new Adapter();
/*     */ 
/*  76 */     StringButtonDialogField string1 = new StringButtonDialogField(adapter);
/*  77 */     string1.setLabelText("String1: ");
/*  78 */     string1.setText("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
/*     */ 
/*  80 */     StringButtonDialogField stringbutton = new StringButtonDialogField(adapter);
/*  81 */     stringbutton.setLabelText("StringButton: ");
/*  82 */     stringbutton.setButtonLabel("Click");
/*  83 */     stringbutton.setText("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
/*     */ 
/*  86 */     String[] addButtons = { 
/*  87 */       "Add1", 
/*  88 */       "Add2", 
/*  90 */       "0", "Up", 
/*  91 */       "Down", 
/*  93 */       "0", "Remove" };
/*     */ 
/*  95 */     String[] columnHeaders = { "Name", "Number" };
/*     */ 
/*  98 */     ListDialogField list = new ListDialogField(adapter, addButtons, new MylabelProvider());
/*  99 */     list.setUpButtonIndex(3);
/* 100 */     list.setDownButtonIndex(4);
/* 101 */     list.setRemoveButtonIndex(6);
/* 102 */     list.setLabelText("List: ");
/*     */ 
/* 104 */     list.setTableColumns(new ListDialogField.ColumnsDescription(columnHeaders, true));
/*     */ 
/* 106 */     for (int i = 0; i < 30; i++) {
/* 107 */       list.addElement(i + "firstxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
/*     */     }
/*     */ 
/* 110 */     SelectionButtonDialogField selButton = new SelectionButtonDialogField(8);
/* 111 */     selButton.setLabelText("Press Button");
/*     */ 
/* 113 */     String[] radioButtons1 = { "Option One", "Option Two", "Option Three" };
/* 114 */     SelectionButtonDialogFieldGroup rdgroup1 = new SelectionButtonDialogFieldGroup(16, radioButtons1, 3, 0);
/* 115 */     rdgroup1.setLabelText("Radio Button Group");
/*     */ 
/* 117 */     String[] radioButtons2 = { "Option One", "Option Two", "Option Three" };
/* 118 */     SelectionButtonDialogFieldGroup rdgroup2 = new SelectionButtonDialogFieldGroup(32, radioButtons2, 3);
/* 119 */     rdgroup2.setLabelText("Radio Button Group 2");
/*     */ 
/* 121 */     LayoutUtil.doDefaultLayout(this.fShell, new DialogField[] { string1, rdgroup2, stringbutton, selButton, list, rdgroup1 }, false);
/*     */ 
/* 123 */     ((GridData)string1.getTextControl(null).getLayoutData()).widthHint = 100;
/* 124 */     ((GridData)stringbutton.getTextControl(null).getLayoutData()).widthHint = 100;
/*     */ 
/* 126 */     this.fShell.setSize(this.fShell.computeSize(-1, -1));
/* 127 */     this.fShell.open();
/* 128 */     return this;
/*     */   }
/*     */ 
/*     */   public DialogFieldExample run()
/*     */   {
/* 159 */     Display display = this.fShell.getDisplay();
/* 160 */     while (!this.fShell.isDisposed()) {
/* 161 */       if (display.readAndDispatch()) continue; display.sleep();
/*     */     }
/* 163 */     return this;
/*     */   }
/*     */ 
/*     */   public static void main(String[] args) {
/* 167 */     new DialogFieldExample().open().run().close();
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
/* 141 */       if (field != null)
/* 142 */         field.addElement("elementxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx-" + DialogFieldExample.fgRandom.nextInt());
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
/*     */ 
/*     */   private class MylabelProvider extends LabelProvider
/*     */     implements ITableLabelProvider
/*     */   {
/*     */     private MylabelProvider()
/*     */     {
/*     */     }
/*     */ 
/*     */     public Image getColumnImage(Object element, int columnIndex)
/*     */     {
/*  54 */       return null;
/*     */     }
/*     */ 
/*     */     public String getColumnText(Object element, int columnIndex)
/*     */     {
/*  60 */       if (columnIndex == 0) {
/*  61 */         return element.toString();
/*     */       }
/*  63 */       return new Integer(columnIndex).toString();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\jjw8610\Desktop\EclipsePlugin\org.eclipse.jdt.ui.tests_3.10.101.v20151123-1510\
 * Qualified Name:     org.eclipse.jdt.ui.examples.DialogFieldExample
 * JD-Core Version:    0.6.0
 */