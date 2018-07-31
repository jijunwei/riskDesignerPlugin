/*     */ package r06.ui;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import org.eclipse.core.runtime.CoreException;
/*     */ import org.eclipse.jdt.core.ICompilationUnit;
/*     */ import org.eclipse.jdt.core.ToolFactory;
/*     */ import org.eclipse.jdt.core.compiler.IScanner;
/*     */ 
/*     */ import org.eclipse.jdt.ui.JavaUI;
/*     */ import org.eclipse.jface.action.Action;
/*     */ import org.eclipse.jface.action.IAction;
/*     */ import org.eclipse.jface.dialogs.MessageDialog;
/*     */ import org.eclipse.jface.viewers.ISelection;
/*     */ import org.eclipse.jface.viewers.IStructuredSelection;
/*     */ import org.eclipse.ui.IActionDelegate;
/*     */ import org.eclipse.ui.texteditor.MarkerUtilities;
/*     */ 
/*     */ public class AddTestMarkersAction extends Action
/*     */   implements IActionDelegate
/*     */ {
/*     */   public static final String MARKER_TYPE = "RiskDesigner marker";
/*     */   private ICompilationUnit fCompilationUnit;
/*     */ 
/*     */   public void run(IAction action)
/*     */   {
/*     */     try
/*     */     {
/*  84 */       JavaUI.openInEditor(this.fCompilationUnit);
/*     */ 
/*  86 */       IScanner scanner = ToolFactory.createScanner(true, false, false, true);
/*  87 */       scanner.setSource(this.fCompilationUnit.getSource().toCharArray());
/*     */ 
/*  89 */       int count = 0;
/*     */       int tok;
/*     */       do
/*     */       {
/*  92 */         tok = scanner.getNextToken();
/*  93 */         if (isComment(tok)) {
/*  94 */           int start = scanner.getCurrentTokenStartPosition();
/*  95 */           int end = scanner.getCurrentTokenEndPosition() + 1;
/*  96 */           int line = scanner.getLineNumber(start);
/*  97 */           createMarker(this.fCompilationUnit, line, start, end - start);
/*  98 */           count++;
/*     */         }
/*     */       }
/* 100 */       while (tok != 158);
/*     */ 
/* 102 */       MessageDialog.openInformation(null, "RiskDesigner Markers", count + " markers added");
/*     */     } catch (Exception e) {
/* 104 */       
/*     */     }
/*     */   }
/*     */ 
/*     */   public static boolean isComment(int token)
/*     */   {
/* 110 */     return (token == 1002) || (token == 1003) || 
/* 111 */       (token == 1001);
/*     */   }
/*     */ 
/*     */   public void selectionChanged(IAction action, ISelection selection)
/*     */   {
/* 118 */     this.fCompilationUnit = null;
/* 119 */     if ((selection instanceof IStructuredSelection)) {
/* 120 */       Object object = ((IStructuredSelection)selection).getFirstElement();
/* 121 */       if ((object instanceof ICompilationUnit))
/* 122 */         this.fCompilationUnit = ((ICompilationUnit)object);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void createMarker(ICompilationUnit cu, int line, int offset, int len)
/*     */     throws CoreException
/*     */   {
/* 130 */     HashMap map = new HashMap();
/* 131 */     map.put("location", cu.getElementName());
/* 132 */     map.put("message", "RiskDesigner marker");
/* 133 */     map.put("severity", new Integer(2));
/* 134 */     map.put("lineNumber", new Integer(line));
/* 135 */     map.put("charStart", new Integer(offset));
/* 136 */     map.put("charEnd", new Integer(offset + len));
/*     */ 
/* 138 */     MarkerUtilities.createMarker(cu.getResource(), map, MARKER_TYPE);
/*     */   }
/*     */ }

/* Location:           C:\Users\jjw8610\Desktop\EclipsePlugin\org.eclipse.jdt.ui.tests_3.10.101.v20151123-1510\
 * Qualified Name:     org.eclipse.jdt.ui.examples.AddTestMarkersAction
 * JD-Core Version:    0.6.0
 */