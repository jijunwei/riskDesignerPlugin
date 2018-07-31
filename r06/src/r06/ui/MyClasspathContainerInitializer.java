/*    */ package r06.ui;
/*    */ 
/*    */ import org.eclipse.core.runtime.CoreException;
/*    */ import org.eclipse.core.runtime.IPath;
/*    */ import org.eclipse.core.runtime.Path;
/*    */ import org.eclipse.jdt.core.ClasspathContainerInitializer;
/*    */ import org.eclipse.jdt.core.IClasspathContainer;
/*    */ import org.eclipse.jdt.core.IClasspathEntry;
/*    */ import org.eclipse.jdt.core.IJavaProject;
/*    */ import org.eclipse.jdt.core.JavaCore;
/*    */ 
/*    */ public class MyClasspathContainerInitializer extends ClasspathContainerInitializer
/*    */ {
/*    */   public void initialize(IPath containerPath, IJavaProject project)
/*    */     throws CoreException
/*    */   {
/* 50 */     IClasspathContainer[] containers = { new MyClasspathContainer(containerPath) };
/* 51 */     JavaCore.setClasspathContainer(containerPath, new IJavaProject[] { project }, containers, null);
/*    */   }
/*    */ 
/*    */   public static class MyClasspathContainer
/*    */     implements IClasspathContainer
/*    */   {
/*    */     private final IPath fPath;
/* 31 */     private static final IPath MY_ARCHIVE = new Path("C:\\xy.jar");
/*    */ 
/*    */     public MyClasspathContainer(IPath path) {
/* 34 */       this.fPath = path;
/*    */     }
/*    */ 
/*    */     public IClasspathEntry[] getClasspathEntries() {
/* 38 */       return new IClasspathEntry[] { JavaCore.newLibraryEntry(MY_ARCHIVE, null, null) };
/*    */     }
/*    */     public String getDescription() {
/* 41 */       return "My example";
/*    */     }
/* 43 */     public int getKind() { return 1; } 
/*    */     public IPath getPath() {
/* 45 */       return this.fPath;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Users\jjw8610\Desktop\EclipsePlugin\org.eclipse.jdt.ui.tests_3.10.101.v20151123-1510\
 * Qualified Name:     org.eclipse.jdt.ui.examples.MyClasspathContainerInitializer
 * JD-Core Version:    0.6.0
 */