/*     */ package r06.ui;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import org.eclipse.core.resources.IFile;
/*     */ import org.eclipse.core.resources.IFolder;
/*     */ import org.eclipse.core.runtime.CoreException;
/*     */ import org.eclipse.core.runtime.IProgressMonitor;
/*     */ import org.eclipse.jdt.core.ICompilationUnit;
/*     */ import org.eclipse.jdt.core.IJavaProject;
/*     */ import org.eclipse.jdt.core.IPackageFragmentRoot;
/*     */ import org.eclipse.jdt.core.refactoring.descriptors.MoveDescriptor;

/*     */ import org.eclipse.jface.action.Action;
/*     */ import org.eclipse.jface.action.IAction;
/*     */ import org.eclipse.jface.operation.IRunnableWithProgress;
/*     */ import org.eclipse.jface.viewers.ISelection;
/*     */ import org.eclipse.jface.viewers.IStructuredSelection;
/*     */ import org.eclipse.ltk.core.refactoring.PerformRefactoringOperation;
/*     */ import org.eclipse.ltk.core.refactoring.RefactoringContext;
/*     */ import org.eclipse.ltk.core.refactoring.RefactoringContribution;
/*     */ import org.eclipse.ltk.core.refactoring.RefactoringCore;
/*     */ import org.eclipse.ltk.core.refactoring.RefactoringDescriptor;
/*     */ import org.eclipse.ltk.core.refactoring.RefactoringStatus;
/*     */ import org.eclipse.ui.IActionDelegate;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ import org.eclipse.ui.progress.IProgressService;
/*     */ 
/*     */ public class TestMoveDescriptorAction extends Action
/*     */   implements IActionDelegate
/*     */ {
/*     */   private ICompilationUnit fCU;
/*     */ 
/*     */   public void run(IAction action)
/*     */   {
/*     */     try
/*     */     {
/*  74 */       if (this.fCU != null)
/*  75 */         PlatformUI.getWorkbench().getProgressService().run(true, true, new IRunnableWithProgress() {
/*     */           public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
/*     */             try {
/*  78 */               TestMoveDescriptorAction.this.performAction(monitor);
/*     */             } catch (CoreException e) {
/*  80 */               throw new InvocationTargetException(e);
/*     */             }
/*     */           } } );
/*     */     }
/*     */     catch (Exception e) {
/*  86 */       
/*     */     }
/*     */   }
/*     */ 
/*     */   private void performAction(IProgressMonitor monitor) throws CoreException
/*     */   {
/*  92 */     IPackageFragmentRoot root = (IPackageFragmentRoot)this.fCU.getAncestor(3);
/*     */ 
/*  94 */     RefactoringContribution refactoringContribution = RefactoringCore.getRefactoringContribution("org.eclipse.jdt.ui.move");
/*  95 */     RefactoringDescriptor desc = refactoringContribution.createDescriptor();
/*  96 */     MoveDescriptor moveDes = (MoveDescriptor)desc;
/*  97 */     moveDes.setComment("Moving cu");
/*  98 */     moveDes.setDescription("Moving cu");
/*  99 */     moveDes.setDestination(root.getPackageFragment(""));
/* 100 */     moveDes.setProject(root.getJavaProject().getElementName());
/* 101 */     moveDes.setMoveResources(new IFile[0], new IFolder[0], new ICompilationUnit[] { this.fCU });
/* 102 */     moveDes.setUpdateReferences(true);
/*     */ 
/* 104 */     RefactoringStatus status = new RefactoringStatus();
/*     */ 
/* 106 */     RefactoringContext context = moveDes.createRefactoringContext(status);
/* 107 */     PerformRefactoringOperation op = new PerformRefactoringOperation(context, 6);
/* 108 */     op.run(monitor);
/*     */   }
/*     */ 
/*     */   public void selectionChanged(IAction action, ISelection selection)
/*     */   {
/* 115 */     this.fCU = null;
/* 116 */     if ((selection instanceof IStructuredSelection)) {
/* 117 */       Object object = ((IStructuredSelection)selection).getFirstElement();
/* 118 */       if ((object instanceof ICompilationUnit))
/* 119 */         this.fCU = ((ICompilationUnit)object);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\jjw8610\Desktop\EclipsePlugin\org.eclipse.jdt.ui.tests_3.10.101.v20151123-1510\
 * Qualified Name:     org.eclipse.jdt.ui.examples.TestMoveDescriptorAction
 * JD-Core Version:    0.6.0
 */