/*    */ package r06.ui;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import junit.framework.TestCase;
/*    */ import org.eclipse.core.resources.IProject;
/*    */ import org.eclipse.core.resources.IProjectDescription;
/*    */ import org.eclipse.core.resources.IWorkspace;
/*    */ import org.eclipse.core.resources.IWorkspaceRoot;
/*    */ import org.eclipse.core.resources.ResourcesPlugin;
/*    */ import org.eclipse.jdt.core.IBuffer;
/*    */ import org.eclipse.jdt.core.IClasspathEntry;
/*    */ import org.eclipse.jdt.core.ICompilationUnit;
/*    */ import org.eclipse.jdt.core.IJavaProject;
/*    */ import org.eclipse.jdt.core.IPackageFragment;
/*    */ import org.eclipse.jdt.core.IPackageFragmentRoot;
/*    */ import org.eclipse.jdt.core.JavaCore;
/*    */ import org.eclipse.jdt.core.dom.AST;
/*    */ import org.eclipse.jdt.core.dom.ASTParser;
/*    */ import org.eclipse.jdt.core.dom.Block;
/*    */ import org.eclipse.jdt.core.dom.CompilationUnit;
/*    */ import org.eclipse.jdt.core.dom.MethodDeclaration;
/*    */ import org.eclipse.jdt.core.dom.MethodInvocation;
/*    */ import org.eclipse.jdt.core.dom.Statement;
/*    */ import org.eclipse.jdt.core.dom.TypeDeclaration;
/*    */ import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
/*    */ import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
/*    */ import org.eclipse.jdt.launching.JavaRuntime;
/*    */ import org.eclipse.jface.text.Document;
/*    */ import org.eclipse.text.edits.TextEdit;
/*    */ 
/*    */ public class ASTRewriteSnippet extends TestCase
/*    */ {
/*    */   public void testASTRewriteExample()
/*    */     throws Exception
/*    */   {
/* 52 */     IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("Test");
/* 53 */     project.create(null);
/* 54 */     project.open(null);
/*    */     try
/*    */     {
/* 57 */       IProjectDescription description = project.getDescription();
/* 58 */       description.setNatureIds(new String[] { "org.eclipse.jdt.core.javanature" });
/* 59 */       project.setDescription(description, null);
/*    */ 
/* 61 */       IJavaProject javaProject = JavaCore.create(project);
/*    */ 
/* 64 */       IClasspathEntry[] cpentry = { 
/* 65 */         JavaCore.newSourceEntry(javaProject.getPath()), 
/* 66 */         JavaRuntime.getDefaultJREContainerEntry() };
/*    */ 
/* 68 */       javaProject.setRawClasspath(cpentry, javaProject.getPath(), null);
/* 69 */       Map options = new HashMap();
/* 70 */       options.put("org.eclipse.jdt.core.formatter.tabulation.char", "space");
/* 71 */       options.put("org.eclipse.jdt.core.formatter.tabulation.size", "4");
/* 72 */       javaProject.setOptions(options);
/*    */ 
/* 75 */       IPackageFragmentRoot root = javaProject.getPackageFragmentRoot(project);
/* 76 */       IPackageFragment pack1 = root.createPackageFragment("test1", false, null);
/* 77 */       StringBuffer buf = new StringBuffer();
/* 78 */       buf.append("package test1;\n");
/* 79 */       buf.append("public class E {\n");
/* 80 */       buf.append("    public void foo(int i) {\n");
/* 81 */       buf.append("        while (--i > 0) {\n");
/* 82 */       buf.append("            System.beep();\n");
/* 83 */       buf.append("        }\n");
/* 84 */       buf.append("    }\n");
/* 85 */       buf.append("}\n");
/* 86 */       ICompilationUnit cu = pack1.createCompilationUnit("E.java", buf.toString(), false, null);
/*    */ 
/* 89 */       ASTParser parser = ASTParser.newParser(8);
/* 90 */       parser.setSource(cu);
/* 91 */       parser.setResolveBindings(false);
/* 92 */       CompilationUnit astRoot = (CompilationUnit)parser.createAST(null);
/* 93 */       AST ast = astRoot.getAST();
/*    */ 
/* 96 */       ASTRewrite rewrite = ASTRewrite.create(ast);
/*    */ 
/* 99 */       TypeDeclaration typeDecl = (TypeDeclaration)astRoot.types().get(0);
/* 100 */       MethodDeclaration methodDecl = typeDecl.getMethods()[0];
/* 101 */       Block block = methodDecl.getBody();
/*    */ 
/* 104 */       MethodInvocation newInv1 = ast.newMethodInvocation();
/* 105 */       newInv1.setName(ast.newSimpleName("bar1"));
/* 106 */       Statement newStatement1 = ast.newExpressionStatement(newInv1);
/*    */ 
/* 108 */       MethodInvocation newInv2 = ast.newMethodInvocation();
/* 109 */       newInv2.setName(ast.newSimpleName("bar2"));
/* 110 */       Statement newStatement2 = ast.newExpressionStatement(newInv2);
/*    */ 
/* 114 */       ListRewrite listRewrite = rewrite.getListRewrite(block, Block.STATEMENTS_PROPERTY);
/* 115 */       listRewrite.insertFirst(newStatement1, null);
/* 116 */       listRewrite.insertLast(newStatement2, null);
/*    */ 
/* 119 */       TextEdit res = rewrite.rewriteAST();
/*    */ 
/* 122 */       Document document = new Document(cu.getSource());
/* 123 */       res.apply(document);
/* 124 */       cu.getBuffer().setContents(document.get());
/*    */ 
/* 127 */       String preview = cu.getSource();
/*    */ 
/* 129 */       buf = new StringBuffer();
/* 130 */       buf.append("package test1;\n");
/* 131 */       buf.append("public class E {\n");
/* 132 */       buf.append("    public void foo(int i) {\n");
/* 133 */       buf.append("        bar1();\n");
/* 134 */       buf.append("        while (--i > 0) {\n");
/* 135 */       buf.append("            System.beep();\n");
/* 136 */       buf.append("        }\n");
/* 137 */       buf.append("        bar2();\n");
/* 138 */       buf.append("    }\n");
/* 139 */       buf.append("}\n");
/* 140 */       assertEquals(preview, buf.toString());
/*    */     } finally {
/* 142 */       project.delete(true, null);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Users\jjw8610\Desktop\EclipsePlugin\org.eclipse.jdt.ui.tests_3.10.101.v20151123-1510\
 * Qualified Name:     org.eclipse.jdt.ui.examples.ASTRewriteSnippet
 * JD-Core Version:    0.6.0
 */