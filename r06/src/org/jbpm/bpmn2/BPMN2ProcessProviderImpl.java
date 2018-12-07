/*    */ package org.jbpm.bpmn2;
/*    */ 
/*    */ import org.drools.compiler.builder.impl.KnowledgeBuilderConfigurationImpl;
/*    */ import org.drools.compiler.builder.impl.KnowledgeBuilderImpl;
/*    */ import org.drools.compiler.compiler.BPMN2ProcessProvider;
/*    */ import org.drools.core.xml.SemanticModules;
/*    */ import org.jbpm.bpmn2.xml.BPMNDISemanticModule;
/*    */ import org.jbpm.bpmn2.xml.BPMNExtensionsSemanticModule;
/*    */ import org.jbpm.bpmn2.xml.BPMNSemanticModule;
/*    */ import org.jbpm.bpmn2.xpath.XPATHProcessDialect;
/*    */ import org.jbpm.process.builder.dialect.ProcessDialectRegistry;
/*    */ import org.kie.internal.builder.KnowledgeBuilder;
/*    */ 
/*    */ public class BPMN2ProcessProviderImpl
/*    */   implements BPMN2ProcessProvider
/*    */ {
/*    */   public <KnowledgeBuilder> void configurePackageBuilder(KnowledgeBuilder knowledgeBuilder)
/*    */   {
/* 36 */     KnowledgeBuilderConfigurationImpl conf = ((KnowledgeBuilderImpl)knowledgeBuilder).getBuilderConfiguration();
/* 37 */     if (conf.getSemanticModules().getSemanticModule("http://www.omg.org/spec/BPMN/20100524/MODEL") == null) {
/* 38 */       conf.addSemanticModule(new BPMNSemanticModule());
/* 39 */       conf.addSemanticModule(new BPMNDISemanticModule());
/* 40 */       conf.addSemanticModule(new BPMNExtensionsSemanticModule());
/*    */     }
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 32 */     ProcessDialectRegistry.setDialect("XPath", new XPATHProcessDialect());
/*    */   }
/*    */
@Override
public void configurePackageBuilder(org.kie.internal.builder.KnowledgeBuilder arg0) {
	// TODO Auto-generated method stub
	
} }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.BPMN2ProcessProviderImpl
 * JD-Core Version:    0.6.0
 */