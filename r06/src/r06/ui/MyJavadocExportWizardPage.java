/*     */ package r06.ui;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
/*     */ import org.eclipse.jdt.ui.wizards.JavadocExportWizardPage;
/*     */ import org.eclipse.swt.events.ModifyEvent;
/*     */ import org.eclipse.swt.events.ModifyListener;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.events.SelectionListener;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public class MyJavadocExportWizardPage extends JavadocExportWizardPage
/*     */ {
/*     */   private Text fText;
/*     */   private Text fText2;
/*     */   private Button fButton;
/*     */   private Label fLabel;
/*     */   private Label fLabel2;
/*     */ 
/*     */   public Control createContents(Composite parent)
/*     */   {
/*  59 */     ModifyListener modifyListener = new ModifyListener() {
/*     */       public void modifyText(ModifyEvent e) {
/*  61 */         MyJavadocExportWizardPage.this.validateInputs();
/*     */       }
/*     */     };
/*  65 */     Composite composite = new Composite(parent, 0);
/*  66 */     composite.setLayoutData(new GridData(4, 4, true, true));
/*  67 */     composite.setLayout(new GridLayout(2, false));
/*     */ 
/*  69 */     this.fButton = new Button(composite, 32);
/*  70 */     this.fButton.setLayoutData(new GridData(16384, 128, false, false, 2, 1));
/*  71 */     this.fButton.setText("solution export");
/*  72 */     this.fButton.addSelectionListener(new SelectionListener() {
/*     */       public void widgetDefaultSelected(SelectionEvent e) {
/*  74 */         MyJavadocExportWizardPage.this.validateInputs();
/*     */       }
/*     */ 
/*     */       public void widgetSelected(SelectionEvent e) {
/*  78 */         MyJavadocExportWizardPage.this.validateInputs();
/*     */       }
/*     */     });
/*  81 */     this.fButton.setSelection(false);
/*     */ 
/*  84 */     this.fLabel = new Label(composite, 0);
/*  85 */     this.fLabel.setLayoutData(new GridData(1, 16777216, false, false));
/*  86 */     this.fLabel.setText("Tag:");
/*  87 */     this.fLabel.setEnabled(false);
/*     */ 
/*  89 */     this.fText = new Text(composite, 18436);
/*  90 */     this.fText.setLayoutData(new GridData(4, 128, true, false));
/*  91 */     this.fText.setText("");
/*  92 */     this.fText.addModifyListener(modifyListener);
/*  93 */     this.fText.setEnabled(false);
/*     */ 
/*  95 */     this.fLabel2 = new Label(composite, 0);
/*  96 */     this.fLabel2.setLayoutData(new GridData(1, 16777216, false, false));
/*  97 */     this.fLabel2.setText("Description:");
/*  98 */     this.fLabel2.setEnabled(false);
/*     */ 
/* 100 */     this.fText2 = new Text(composite, 18436);
/* 101 */     this.fText2.setLayoutData(new GridData(4, 128, true, false));
/* 102 */     this.fText2.setText("");
/* 103 */     this.fText2.addModifyListener(modifyListener);
/* 104 */     this.fText2.setEnabled(false);
/*     */ 
/* 106 */     return composite;
/*     */   }
/*     */ 
/*     */   protected void validateInputs() {
/* 110 */     boolean isEnabled = this.fButton.getSelection();
/*     */ 
/* 112 */     this.fLabel.setEnabled(isEnabled);
/* 113 */     this.fText.setEnabled(isEnabled);
/* 114 */     this.fLabel2.setEnabled(isEnabled);
/* 115 */     this.fText2.setEnabled(isEnabled);
/*     */ 
/* 117 */     StatusInfo status = new StatusInfo();
/*     */ 
/* 119 */     if (isEnabled) {
/* 120 */       String text = this.fText.getText().trim();
/* 121 */       if (text.length() == 0) {
/* 122 */         status.setError("Enter a tag");
/*     */       }
/*     */ 
/* 125 */       String text2 = this.fText2.getText().trim();
/* 126 */       if (text2.length() == 0) {
/* 127 */         status.setError("Enter a description");
/*     */       }
/*     */     }
/* 130 */     setStatus(status);
/*     */   }
/*     */ 
/*     */   public void updateArguments(List vmOptions, List toolOptions) {
/* 134 */     if (this.fButton.getSelection()) {
/* 135 */       String tag = this.fText.getText().trim();
/* 136 */       String description = this.fText2.getText().trim();
/*     */ 
/* 138 */       toolOptions.add(0, "-tag");
/* 139 */       toolOptions.add(1, tag + ":a:" + description);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void updateAntScript(Element javadocXMLElement) {
/* 144 */     if (this.fButton.getSelection()) {
/* 145 */       String tag = this.fText.getText().trim();
/* 146 */       String description = this.fText2.getText().trim();
/*     */ 
/* 148 */       Document document = javadocXMLElement.getOwnerDocument();
/*     */ 
/* 150 */       Element tagElement = document.createElement("tag");
/* 151 */       tagElement.setAttribute("name", tag);
/* 152 */       tagElement.setAttribute("description", description);
/*     */ 
/* 154 */       javadocXMLElement.appendChild(tagElement);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\jjw8610\Desktop\EclipsePlugin\org.eclipse.jdt.ui.tests_3.10.101.v20151123-1510\
 * Qualified Name:     org.eclipse.jdt.ui.examples.MyJavadocExportWizardPage
 * JD-Core Version:    0.6.0
 */