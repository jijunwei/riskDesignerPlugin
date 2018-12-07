/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import org.drools.core.xml.DefaultSemanticModule;
/*    */ 
/*    */ public class BPMNExtensionsSemanticModule extends DefaultSemanticModule
/*    */ {
/*    */   public static final String BPMN2_EXTENSIONS_URI = "http://www.jboss.org/drools";
/*    */ 
/*    */   public BPMNExtensionsSemanticModule()
/*    */   {
/* 26 */     super("http://www.jboss.org/drools");
/*    */ 
/* 28 */     addHandler("import", new ImportHandler());
/* 29 */     addHandler("global", new GlobalHandler());
/* 30 */     addHandler("metaData", new MetaDataHandler());
/* 31 */     addHandler("metaValue", new MetaValueHandler());
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.BPMNExtensionsSemanticModule
 * JD-Core Version:    0.6.0
 */