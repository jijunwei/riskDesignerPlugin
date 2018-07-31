/*     */ package r06.ui;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
/*     */ import org.eclipse.core.resources.IProject;
/*     */ import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
/*     */ import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
/*     */ import org.eclipse.core.runtime.CoreException;
/*     */ import org.eclipse.core.runtime.IConfigurationElement;
/*     */ import org.eclipse.core.runtime.IExecutableExtension;
/*     */ import org.eclipse.core.runtime.IPath;
/*     */ import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
/*     */ import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
/*     */ import org.eclipse.core.runtime.SubProgressMonitor;
/*     */ import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
/*     */ import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
/*     */ import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
/*     */ import org.eclipse.jdt.launching.JavaRuntime;
/*     */ import org.eclipse.jdt.ui.wizards.JavaCapabilityConfigurationPage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.text.Document;
/*     */ import org.eclipse.jface.viewers.IStructuredSelection;
/*     */ import org.eclipse.jface.wizard.IWizardContainer;
/*     */ import org.eclipse.jface.wizard.Wizard;
import org.eclipse.text.edits.TextEdit;
/*     */ import org.eclipse.ui.INewWizard;
/*     */ import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
/*     */ import org.eclipse.ui.actions.WorkspaceModifyOperation;
/*     */ import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.ide.IDE;
/*     */ import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
/*     */ import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;
/*     */ 
/*     */ public class MyProjectCreationWizard extends Wizard
/*     */   implements IExecutableExtension, INewWizard
/*     */ {
/*     */   private WizardNewProjectCreationPage fMainPage;
/*     */   private JavaCapabilityConfigurationPage fJavaPage;
/*     */   private IConfigurationElement fConfigElement;
/*     */   private IWorkbench fWorkbench;
/*     */ 
/*     */   public MyProjectCreationWizard()
/*     */   {
/*  73 */     setWindowTitle("New Solution Project");
/*     */   }
/*     */ 
/*     */   public void setInitializationData(IConfigurationElement cfig, String propertyName, Object data)
/*     */   {
/*  81 */     this.fConfigElement = cfig;
/*     */   }
/*     */ 
/*     */   public void init(IWorkbench workbench, IStructuredSelection selection)
/*     */   {
/*  88 */     this.fWorkbench = workbench;
/*     */   }
/*     */ 
/*     */   public void addPages()
/*     */   {
/*  95 */     super.addPages();
/*  96 */     this.fMainPage = new WizardNewProjectCreationPage("NewProjectCreationWizard");
/*  97 */     this.fMainPage.setTitle("New");
/*  98 */     this.fMainPage.setDescription("Create a new Solution project.");
/*     */ 
/* 101 */     addPage(this.fMainPage);
/*     */ 
/* 104 */     this.fJavaPage = new JavaCapabilityConfigurationPage()
/*     */     {
/*     */       public void setVisible(boolean visible) {
/* 107 */         try {
						MyProjectCreationWizard.this.updatePage();
					} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
/* 108 */         super.setVisible(visible);
/*     */       }
/*     */     };
/* 111 */     addPage(this.fJavaPage);
/*     */   }
/*     */ 
/*     */   private void updatePage() throws CoreException
/*     */   {
/* 117 */     IJavaProject jproject = JavaCore.create(this.fMainPage.getProjectHandle());
/* 118 */     if (!jproject.equals(this.fJavaPage.getJavaProject())) {
/* 119 */       IClasspathEntry[] buildPath = { 
/* 120 */         
                  JavaCore.newSourceEntry(jproject.getPath().append("java\\src")),
                  JavaCore.newSourceEntry(jproject.getPath().append("java\\lib")),
                  
                  JavaCore.newSourceEntry(jproject.getPath().append("resources\\dev")),
                  JavaCore.newSourceEntry(jproject.getPath().append("resources\\test")),
                  JavaCore.newSourceEntry(jproject.getPath().append("resources\\pre-product")),
                  JavaCore.newSourceEntry(jproject.getPath().append("resources\\product")),
                  JavaCore.newSourceEntry(jproject.getPath().append("resources\\META-INF")),
                  JavaCore.newSourceEntry(jproject.getPath().append("plan1\\rules")
                 ),
                  
                 
/* 121 */         JavaRuntime.getDefaultJREContainerEntry() };
/*     */ 
/* 123 */    IPath outputLocation = jproject.getPath().append("target");

             this.fJavaPage.init(jproject, outputLocation, buildPath, false);
            
             System.err.println("jproject.getPath()"+jproject.getPath());
             String projectName1=jproject.getPath().toString();
             String projectName=projectName1.substring(1,projectName1.length() );
             IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(jproject.getElementName());
             System.err.println("jproject.getElementName():"+jproject.getElementName());
             System.err.println("project:"+project.getName());
/*    */     try
/*    */     {
/* 57 */       IProjectDescription description = project.getDescription();
/* 58 */       description.setNatureIds(new String[] { "org.eclipse.jdt.core.javanature" });
/* 59 */       project.setDescription(description, null);


/*    */ 
/* 75 */       IPackageFragmentRoot root = jproject.getPackageFragmentRoot(project);


/* 76 */       IPackageFragment packInit = root.createPackageFragment("test1", false, null);
/* 77 */       StringBuffer buf = new StringBuffer();
/* 78 */       buf.append("package test1;\n");
/* 79 */       buf.append("public class E {\n");
/* 80 */       buf.append("    public void foo(int i) {\n");
/* 81 */       buf.append("        while (--i > 0) {\n");
/* 82 */       buf.append("            System.beep();\n");
/* 83 */       buf.append("        }\n");
/* 84 */       buf.append("    }\n");
/* 85 */       buf.append("}\n");
/* 86 */       packInit.createCompilationUnit("E.java", buf.toString(), false, null);

				
				IPackageFragment pck = root.getPackageFragment("net.chenxs");//得到指定包
				if(!pck.exists()){
					root.createPackageFragment("net.chenxs",false,null);//包不存在则创建
				  }
				  
				StringBuffer buf00 = new StringBuffer();
/* 78 */       buf00.append("package net.chenxs;\n");
/* 79 */       buf00.append("public class E2 {\n");
/* 80 */       buf00.append("    public void foo(int i) {\n");
/* 81 */       buf00.append("        while (--i > 0) {\n");
/* 82 */       buf00.append("            System.beep();\n");
/* 83 */       buf00.append("        }\n");
/* 84 */       buf00.append("    }\n");
/* 85 */       buf00.append("}\n");
			  ICompilationUnit cu = pck.createCompilationUnit("E2.java", buf.toString(), true, new NullProgressMonitor());//创建获得编译单元 文件(java文件)


			  ICompilationUnit  unit  = pck.getCompilationUnit("E2.java");//获得编译单元 
			  	//对unit做N多操作

				ICompilationUnit copy = cu.getWorkingCopy(null);//获取工作副本
				copy.createPackageDeclaration(pck.getElementName(),null); //创建包声明
				copy.createImport("java.util.*", null, null);//创建导入
				copy.reconcile(0, false, copy.getOwner(), null);//使工作副本与缓冲区同步
				copy.commitWorkingCopy(false, null);//工作副本保存至磁盘(用来替换原始的编译单元)
				copy.discardWorkingCopy();//废弃工作副本
				
				
				IPackageFragmentRoot pkroot = jproject.getPackageFragmentRoot(jproject.getResource());
				IPackageFragment pkg = pkroot.createPackageFragment("com.cownew", false, null);
				pkg.createCompilationUnit("Hello.java", "package com.cownew;", false, null);
                
				/**
				 * 修改编译单元
					大部分对Java源代码的简单修改可以使用Java元素API来完成。
					一旦拥有IType实例，就可以使用诸如createField、createInitializer、createMethod、createType等方法将成员添加
					至类型。在这些方法中提供了源代码以及关于成员的位置的信息。
				 */
				 
				
               ASTParser parser = ASTParser.newParser(8);
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
/* 140 */      System.err.println(preview.equals(buf.toString()));

				
				



				IPackageFragment pack0 = root.getPackageFragment("resources.META-INF");//得到指定包
				if(!pack0.exists()){
					root.createPackageFragment("resources.META-INF",false,null);//包不存在则创建
				  }
/* 77 */       StringBuffer buf0 = new StringBuffer();
/* 78 */       buf0.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
/* 79 */       buf0.append("<kmodule xmlns=\"http://jboss.org/kie/6.0.0/kmodule\">\n");
/* 80 */       buf0.append("    <kbase name=\"process\" packages=\"plan1.rules\">\n");
/* 81 */       buf0.append("        <ksession name=\"ksession-process\"/>\n");
/* 82 */       buf0.append("    </kmodule>\n");
/* 83 */       
/* 84 */       
/* 86 */       //pack0.createCompilationUnit("kmodule.xml", buf0.toString(), false, null);
               pack0.createCompilationUnit("kmodule.java", buf0.toString(), false, null);

				IPackageFragment pack01 = root.getPackageFragment("resources");//得到指定包
				if(!pack01.exists()){
					root.createPackageFragment("resources",false,null);//包不存在则创建
				  }
/* 77 */       StringBuffer buf01 = new StringBuffer();
/* 78 */       buf01.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
/* 79 */       buf01.append("<Solution isActive=\"true\" name=\""+projectName+"\">\n");
			   buf01.append("<Plan id=\"1\" name=\"plan1\" isActive=\"true\">\n");
/* 80 */       
			   buf01.append("    <Models>\n");
			   buf01.append("    <!-- 添加<model>:<Model type=\"out\" id=\"*\"/> -->\n");
			   buf01.append("    <!-- 添加<model>:<Model type=\"in\" id=\"*\"/> -->\n");
/* 81 */       buf01.append("    </Models>\n");
/* 82 */       buf01.append("    </Plan>\n");
/* 83 */       buf01.append("</Solution>\n");
/* 84 */       
/* 86 */       pack01.createCompilationUnit("default.cfg", buf01.toString(), false, null);

				IPackageFragment pack1 = root.getPackageFragment("maven");//得到指定包
				if(!pack1.exists()){
					root.createPackageFragment("maven",false,null);//包不存在则创建
				  }
/* 77 */       StringBuffer buf1 = new StringBuffer();
/* 78 */       buf1.append("<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">;\n");
/* 79 */       buf1.append("<modelVersion>4.0.0</modelVersion>\n");
/* 80 */       buf1.append("<groupId>xujin</groupId>{\n");
/* 81 */       buf1.append("<artifactId>"+projectName+"</artifactId>\n");
/* 82 */       buf1.append("<version>0.0.1-SNAPSHOT</version>\n");
/* 83 */       buf1.append("<name>xujin risk project</name>\n");
/* 84 */       buf1.append("<description>a starter framework of xujin risk project</description>\n");
/* 85 */       buf1.append("<properties>\n");
               buf1.append("<drools.version>6.2.0.Final</drools.version>\n<jbpm.version>6.2.0.Final</jbpm.version>\n<httpclient.version>3.1</httpclient.version>\n<commons-lang.version>2.6</commons-lang.version>\n"
               		+ "<fastjson.version>1.2.3</fastjson.version>\n"
               		+ "<logback.version>1.1.3</logback.version>\n"
               		+ "<lombok.version>1.16.16</lombok.version>"
               		+ "<slf4j.version>1.7.12</slf4j.version>");
		       buf1.append("</properties>\n\n");
				
				buf1.append("<repositories>\n");
				buf1.append("<repository>\n<id>nexus-aliyun</id>\n<name>Nexus aliyun</name>\n<url>http://maven.aliyun.com/nexus/content/groups/public</url>\n</repository>\n");
				buf1.append("<repository>\n<id>mvnrepository</id>\n<name>mvnrepository</name>\n<url>http://www.mvnrepository.com/</url>\n</repository>\n");
				buf1.append("</repositories>\n\n");
				
				buf1.append("<dependencies>\n");
				buf1.append("<dependency>\n<groupId>org.drools</groupId>\n<artifactId>knowledge-api</artifactId>\n<version>${drools.version}</version>\n</dependency>\n");
				buf1.append("<dependency>\n<groupId>org.drools</groupId>\n<artifactId>drools-core</artifactId>\n<version>${drools.version}</version>\n</dependency>\n");
				buf1.append("<dependency>\n<groupId>org.drools</groupId>\n<artifactId>drools-compiler</artifactId>\n<version>${drools.version}</version>\n</dependency>\n");
				buf1.append("<dependency>\n<groupId>org.jbpm</groupId>\n<artifactId>jbpm-flow</artifactId>\n<version>${jbpm.version}</version>\n</dependency>\n");
				buf1.append("<dependency>\n<groupId>org.jbpm</groupId>\n<artifactId>jbpm-flow-builder</artifactId>\n<version>${jbpm.version}</version>\n</dependency>\n");
				buf1.append("<dependency>\n<groupId>org.jbpm</groupId>\n<artifactId>jbpm-bpmn2</artifactId>\n<version>${jbpm.version}</version>\n</dependency>\n");
				buf1.append("<dependency>\n<groupId>commons-httpclient</groupId>\n<artifactId>commons-httpclient</artifactId>\n<version>$<httpclient.version}</version>\n</dependency>\n");
				buf1.append("<dependency>\n<groupId>commons-lang</groupId>\n<artifactId>commons-lang</artifactId>\n<version>${commons-lang.version}</version>\n</dependency>\n");
				buf1.append("<dependency>\n<groupId>com.alibaba</groupId>\n<artifactId>fastjson</artifactId>\n<version>${fastjson.version}</version>\n</dependency>\n");
				
				buf1.append("<dependency>\n<groupId>ch.qos.logback</groupId>\n<artifactId>logback-core</artifactId>\n<version>${logback.version}</version>\n</dependency>\n");
				buf1.append("<dependency>\n<groupId>ch.qos.logback</groupId>\n<artifactId>logback-classic</artifactId>\n<version>${logback.version}</version>\n</dependency>\n");
				buf1.append("<dependency>\n<groupId>org.projectlombok</groupId>\n<artifactId>lombok</artifactId>\n<version>${lombok.version}</version>\n</dependency>\n");
				
				buf1.append("<dependency>\n<groupId>org.slf4j</groupId>\n<artifactId>slf4j-api</artifactId>\n<version>${slf4j.version}</version>\n</dependency>\n");
				buf1.append("<dependency>\n<groupId>org.slf4j</groupId>\n<artifactId>log4j-over-slf4j</artifactId>\n<version>${slf4j.version}</version>\n</dependency>\n");
				buf1.append("<dependency>\n<groupId>org.slf4j</groupId>\n<artifactId>jcl-over-slf4j</artifactId>\n<version>${slf4j.version}</version>\n</dependency>\n");
				/*buf1.append("<dependency>\n<groupId>ch.qos.logback</groupId>\n<artifactId>logback-core</artifactId>\n<version>${<logback.version>}</version>\n</dependency>\n");
				buf1.append("<dependency>\n<groupId>ch.qos.logback</groupId>\n<artifactId>logback-core</artifactId>\n<version>${<logback.version>}</version>\n</dependency>\n");
				*/
				buf1.append("</dependencies>\n\n");
				
				
				buf1.append("<build>\n");
				buf1.append("<plugins>\n");
				buf1.append("<!-- maven编译插件 -->\n");
				buf1.append("<plugin>  \n");
				buf1.append("   <groupId>org.apache.maven.plugins</groupId>\n");  
				buf1.append("   <artifactId>maven-compiler-plugin</artifactId>\n");  
				buf1.append("   <version>2.3.2</version>  \n");
				buf1.append("   <configuration>    \n");
				buf1.append("   	<source>1.8</source>  \n");
				buf1.append("   	<target>1.8</target> \n"); 
				buf1.append("   	<encoding>UTF-8</encoding>\n");
				buf1.append("   	<compilerArguments>  \n");
				buf1.append("          <verbose />  \n");
				buf1.append("          <bootclasspath>${java.home}/lib/rt.jar;${java.home}/lib/jce.jar</bootclasspath>  \n");
				buf1.append("        </compilerArguments>\n");
				buf1.append("   	</configuration>  \n");
				buf1.append("   </plugin>\n\n");
				buf1.append("<plugin>\n");
				buf1.append("   <groupId>org.codehaus.mojo</groupId>\n");
				buf1.append("   <artifactId>build-helper-maven-plugin</artifactId>\n");
				buf1.append("   <version>1.7</version>\n");
				buf1.append("   <executions>\n");
				buf1.append("      <execution>\n");
				buf1.append("         <id>add-source</id>\n");
				buf1.append("         <phase>generate-sources</phase>\n");
				buf1.append("         <goals><goal>add-source</goal></goals>\n");
				buf1.append("         <configuration>\n");
				buf1.append("            <sources>\n");
				buf1.append("               <source>java/src</source>\n");
		        buf1.append("            </sources>\n");
				buf1.append("         </configuration>\n");
				buf1.append("      </execution>\n");
				buf1.append("   </executions>\n");
				buf1.append("</plugin> ");
				buf1.append("<plugin>");
				buf1.append("   <groupId>org.apache.maven.plugins</groupId>");
				buf1.append("   <artifactId>maven-shade-plugin</artifactId>");
				buf1.append("   <version>2.4.1</version>");
				buf1.append("   <executions>");
				buf1.append("      <execution>");
				buf1.append("         <phase>package</phase>");
				buf1.append("      <goals>");
				buf1.append("         <goal>shade</goal>");
				buf1.append("      </goals>");
				buf1.append("   <configuration>");
				buf1.append("      <transformers>");
				buf1.append("         <transformer implementation=\"org.apache.maven.plugins.shade.resource.ManifestResourceTransformer\">");
				buf1.append("         </transformer>");
				buf1.append("      </transformers>");
				buf1.append("   </configuration>");
				buf1.append("      </execution>");
				buf1.append("   </executions>");
				buf1.append("</plugin>");
			    buf1.append("</plugins>\n");
								
                buf1.append("<resources>\n");
				buf1.append("<resource>\n<directory>plan1/</directory>\n<includes>\n<include>**/*.flow</include>\n</includes>\n<targetPath>drls</targetPath>\n<filtering>false</filtering>\n</resource>\n");
				buf1.append("<!-- 将规则文件文件打包进jar包 -->\n<resource>\n<directory>plan1/rules</directory>\n<targetPath>drls</targetPath>\n<filtering>false</filtering>\n</resource>\n");
				buf1.append("<!-- 将kmodule.xml文件打包进jar包 -->\n<resource>\n<directory>resources/META-INF</directory>\n<targetPath>META-INF</targetPath>\n<filtering>false</filtering>\n</resource>\n");
				buf1.append("<resource>\n<directory>resources/</directory>\n<includes>\n<include>**/*.cfg</include>\n</includes>\n<filtering>false</filtering>\n</resource>\n");
				buf1.append("<!-- 将application.properties文件打包进jar包的properties文件夹 -->\n<resource>\n<directory>resources/${package.environment}</directory>\n<includes>\n<include>**/*</include>\n</includes>\n<filtering>false</filtering>\n</resource>\n");
				
				buf1.append("</resources>\n");
				
				buf1.append("</build>\n");
				
				buf1.append("<profiles>\n");
				buf1.append("<profile>\n<id>test</id>\n<activation>\n<activeByDefault>true</activeByDefault>\n</activation>\n"
						+ "<properties>\n<package.environment>test</package.environment>\n</properties>\n</profile>\n");
				
				buf1.append("<profile>\n<id>dev</id>\n"
						+ "<properties>\n<package.environment>dev</package.environment>\n</properties>\n</profile>\n");
				
				buf1.append("<profile>\n<id>pre-product</id>\n"
						+ "<properties>\n<package.environment>pre-product</package.environment>\n</properties>\n</profile>\n");
				
				buf1.append("<profile>\n<id>product</id>\n"
						+ "<properties>\n<package.environment>product</package.environment>\n</properties>\n</profile>\n");
				
				buf1.append("	</profiles>\n");
				buf1.append("</project>\n");
				
/* 86 */       pack1.createCompilationUnit("pom.xml", buf1.toString(), false, null);
				IPackageFragment pack2 = root.getPackageFragment("ant");//得到指定包
				if(!pack2.exists()){
					root.createPackageFragment("ant",false,null);//包不存在则创建
				  }
/* 77 */       StringBuffer buf2 = new StringBuffer();
/* 78 */       buf2.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
/* 79 */       buf2.append("<project name=\""+projectName+"\" default=\"run\" basedir=\".\">\n");
/* 80 */       buf2.append("    <property name=\"dest\" value=\"d:\riskfiles\"/>\n");
/* 81 */       buf2.append("    <property name=\"project.build.directory\" value=\""+jproject.getPath()+"target\"/>\n");
/* 82 */       buf2.append("    <property name=\"project.build.finalName\" value=\""+projectName+"-0.0.1-SNAPSHOT.jar\"/>\n");
/* 83 */       buf2.append("    <property name=\"solution_jar\" value=\"${dest}\\data\\${project.build.finalName}\"/>\n");
/* 84 */       buf2.append("<target name=\"init\">\n");
				buf2.append("<mkdir dir=\"${dest}\"/> \n");  
				buf2.append("<mkdir dir=\"${dest}\\data\"/>\n");
		        buf2.append("<mkdir dir=\"${dest}\\solution\"/>\n");
				buf2.append("<mkdir dir=\"${dest}\\solution\\"+projectName+".sln\"/>\n");
				buf2.append("</target>\n");
/* 85 */       buf2.append("<target name=\"deploy\">\n");
				buf2.append("<copy file=\"${project.build.directory}\\${project.build.finalName}\"\n");   
				buf2.append("todir=\"${dest}\\data\" overwrite=\"true\"/>\n");
				buf2.append("<copy todir=\"${dest}\\solution\\test.sln\\AllPass.plan\">\n");
				buf2.append("<fileset dir=\"${project.build.directory}\\classes\\drls\"/>\n");
				buf2.append("</copy>\n");
				buf2.append("<copy file=\"${project.build.directory}\\classes\\default.cfg\"\n");   
				buf2.append("todir=\"${dest}\\solution\\"+projectName+".sln\" overwrite=\"true\"/>\n");
				buf2.append("</target>\n");
				buf2.append("<target name=\"run\" depends=\"init,deploy\">\n");
				buf2.append("<echo message=\"ok:deploy compeleted\"/> \n");
				buf2.append("</target>\n");
				buf2.append("<target name=\"clean\">\n");
				buf2.append("<delete dir=\"${dest}\" />\n");
				buf2.append("<delete file=\"${solution_jar}\" />\n");
				buf2.append("</target>\n");
				buf2.append("<target name=\"rerun\" depends=\"clean,run\">\n");
				buf2.append("<ant target=\"clean\" />\n");
				buf2.append("<ant target=\"run\" />\n");
				buf2.append("</target>\n");
/* 86 */        pack2.createCompilationUnit("build.xml", buf2.toString(), false, null);
               
				IPackageFragment pack3 = root.getPackageFragment("plan1");//得到指定包
				if(!pack3.exists()){
					root.createPackageFragment("plan1",false,null);//包不存在则创建
				  }
/* 76 */       
/* 77 */       StringBuffer buf3 = new StringBuffer();
/* 78 */       buf3.append("package test1;\n");
/* 79 */       buf3.append("public class E {\n");
/* 80 */       buf3.append("    public void foo(int i) {\n");
/* 81 */       buf3.append("        while (--i > 0) {\n");
/* 82 */       buf3.append("            System.beep();\n");
/* 83 */       buf3.append("        }\n");
/* 84 */       buf3.append("    }\n");
/* 85 */       buf3.append("}\n");
/* 86 */       pack3.createCompilationUnit("risk.flow", buf3.toString(), false, null);

				IPackageFragment pack4 = root.getPackageFragment("plan1.rules");//得到指定包
				if(!pack4.exists()){
					root.createPackageFragment("plan1.rules",false,null);//包不存在则创建
				  }
/* 76 */       
/* 77 */       StringBuffer buf4 = new StringBuffer();
/* 78 */	   buf4.append("/**section package  ;**/ {\n");       
				buf4.append("package com.xujin.demo;\n");
/* 79 */       buf4.append("/**section import  ;**/ \n\n");

/* 80 */       buf4.append("rule \""+"one"+"\"\n");
/* 81 */       buf4.append("        salience 1\n");
/* 82 */       buf4.append("        no-loop\n");
/* 83 */       buf4.append("        lock-on-active true\n");
/* 84 */       buf4.append("        ruleflow-group \" "+"."+"\"\n\n");
/* 85 */       buf4.append("        when\n\n");
			   buf4.append("        then\n\n");
			   buf4.append("        end\n\n");
/* 86 */       pack4.createCompilationUnit("one.drl", buf4.toString(), false, null);

/* 76 */
				IPackageFragment pack41 = root.getPackageFragment("plan1.rules.a.b");//得到指定包
				if(!pack41.exists()){
					root.createPackageFragment("plan1.rules.a.b",false,null);//包不存在则创建
				  }       
/* 77 */       StringBuffer buf41 = new StringBuffer();
/* 78 */	   buf41.append("/**section package  ;**/ {\n");       
				buf41.append("package com.xujin.demo;\n");
/* 79 */       buf41.append("/**section import  ;**/ \n\n");

/* 80 */       buf41.append("rule \""+"a.b"+".two"+"\"\n");
/* 81 */       buf41.append("        salience 1\n");
/* 82 */       buf41.append("        no-loop\n");
/* 83 */       buf41.append("        lock-on-active true\n");
/* 84 */       buf41.append("        ruleflow-group \" "+"a.b"+"\"\n");
/* 85 */       buf41.append("        when\n\n");
			   buf41.append("        then\n\n");
			   buf41.append("        end\n\n");
/* 86 */       pack41.createCompilationUnit("two.drl", buf41.toString(), false, null);
/* 140 */      


/*    */     }catch(Exception e){
	            e.printStackTrace();
             }

/* 124 */       
/*     */     }
/*     */   }

/*     */ 
/*     */   private void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException {
/* 129 */     if (monitor == null)
/* 130 */       monitor = new NullProgressMonitor();
/*     */     try
/*     */     {
/* 133 */       monitor.beginTask("Creating risk solution project...", 3);
/*     */ 
/* 135 */       IProject project = this.fMainPage.getProjectHandle();
/* 136 */       IPath locationPath = this.fMainPage.getLocationPath();
/*     */ 
/* 139 */       IProjectDescription desc = project.getWorkspace().newProjectDescription(project.getName());
/* 140 */       if (!this.fMainPage.useDefaults()) {
/* 141 */         desc.setLocation(locationPath);
/*     */       }
/* 143 */       project.create(desc, new SubProgressMonitor(monitor, 1));
/* 144 */       project.open(new SubProgressMonitor(monitor, 1));
/*     */ 
/* 146 */       updatePage();
/* 147 */       this.fJavaPage.configureJavaProject(new SubProgressMonitor(monitor, 1));
/*     */ 
/* 151 */       BasicNewProjectResourceWizard.updatePerspective(this.fConfigElement);
/* 152 */       BasicNewResourceWizard.selectAndReveal(project, this.fWorkbench.getActiveWorkbenchWindow());
/*     */     }
/*     */     finally {
/* 155 */       monitor.done();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean performFinish()
/*     */   {
/* 163 */     WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
/*     */       protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
/* 165 */       
				IProject project = fMainPage.getProjectHandle();
	            IPath locationPath = fMainPage.getLocationPath();
	            final String containerName = locationPath.toString()+project.toString();
	            System.err.println(containerName);
					final String fileName = "plan1\\risk.flow";
					IRunnableWithProgress op = new IRunnableWithProgress() {
						public void run(IProgressMonitor monitor) throws InvocationTargetException {
							try {
								doFinish(containerName, fileName, monitor);
							} catch (CoreException e) {
								throw new InvocationTargetException(e);
							} finally {
								monitor.done();
							}
						}
					};

	              MyProjectCreationWizard.this.finishPage(monitor);
/*     */       } } ;
/*     */     try {
/* 169 */       getContainer().run(false, true, op);
/*     */     } catch (InvocationTargetException localInvocationTargetException) {
/* 171 */       return false;
/*     */     } catch (InterruptedException localInterruptedException) {
/* 173 */       return false;
/*     */     }
/* 175 */     return true;
/*     */   }

private void doFinish(
		String containerName,
		String fileName,
		IProgressMonitor monitor)
		throws CoreException {
		// create a sample file
		monitor.beginTask("Creating " + fileName, 2);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containerName));
		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException("Container \"" + containerName + "\" does not exist.");
		}
		IContainer container = (IContainer) resource;
		final IFile file = container.getFile(new Path(fileName));
		try {
			InputStream stream = openContentStream();
			if (file.exists()) {
				file.setContents(stream, true, true, monitor);
			} else {
				file.create(stream, true, monitor);
			}
			stream.close();
		} catch (IOException e) {
		}
		monitor.worked(1);
		monitor.setTaskName("Opening file for editing...");
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page =
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					IDE.openEditor(page, file, true);
				} catch (PartInitException e) {
				}
			}
		});
		monitor.worked(1);
	}
		private void throwCoreException(String message) throws CoreException {
			IStatus status =
				new Status(IStatus.ERROR, "r06", IStatus.OK, message, null);
			throw new CoreException(status);
		}
		private InputStream openContentStream() {
			String contents =
				"This is the initial file contents for *.flow file that should be word-sorted in the Preview page of the multi-page editor";
			return new ByteArrayInputStream(contents.getBytes());
		}
/*     */ }

/* Location:           C:\Users\jjw8610\Desktop\EclipsePlugin\org.eclipse.jdt.ui.tests_3.10.101.v20151123-1510\
 * Qualified Name:     org.eclipse.jdt.ui.examples.MyProjectCreationWizard
 * JD-Core Version:    0.6.0
 */