/*    */ package r06.ui;
/*    */ 
/*    */ import org.eclipse.core.runtime.CoreException;
/*    */ import org.eclipse.jdt.core.ICompilationUnit;
/*    */ import org.eclipse.jdt.internal.ui.text.correction.proposals.ReplaceCorrectionProposal;
/*    */ import org.eclipse.jdt.ui.text.java.IInvocationContext;
/*    */ import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
/*    */ import org.eclipse.jdt.ui.text.java.IProblemLocation;
/*    */ import org.eclipse.jdt.ui.text.java.IQuickFixProcessor;
/*    */ 
/*    */ public class MyQuickFixProcessor
/*    */   implements IQuickFixProcessor
/*    */ {
/*    */   public boolean hasCorrections(ICompilationUnit unit, int problemId)
/*    */   {
/* 50 */     return problemId == 536871066;
/*    */   }
/*    */ 
/*    */   public IJavaCompletionProposal[] getCorrections(IInvocationContext context, IProblemLocation[] locations)
/*    */     throws CoreException
/*    */   {
/* 57 */     for (int i = 0; i < locations.length; i++) {
/* 58 */       if (locations[i].getProblemId() == 536871066) {
/* 59 */         return getNumericValueOutOfRangeCorrection(context, locations[i]);
/*    */       }
/*    */     }
/* 62 */     return null;
/*    */   }
/*    */ 
/*    */   private IJavaCompletionProposal[] getNumericValueOutOfRangeCorrection(IInvocationContext context, IProblemLocation location) {
/* 66 */     ICompilationUnit cu = context.getCompilationUnit();
/*    */ 
/* 68 */     ReplaceCorrectionProposal proposal = new ReplaceCorrectionProposal("Change to 0", cu, location.getOffset(), location.getLength(), "0", 5);
/* 69 */     return new IJavaCompletionProposal[] { proposal };
/*    */   }
/*    */ }

/* Location:           C:\Users\jjw8610\Desktop\EclipsePlugin\org.eclipse.jdt.ui.tests_3.10.101.v20151123-1510\
 * Qualified Name:     org.eclipse.jdt.ui.examples.MyQuickFixProcessor
 * JD-Core Version:    0.6.0
 */