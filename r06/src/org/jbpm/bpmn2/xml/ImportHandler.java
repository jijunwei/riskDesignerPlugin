/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import org.drools.core.xml.BaseAbstractHandler;
/*    */ import org.drools.core.xml.ExtensibleXmlParser;
/*    */ import org.drools.core.xml.Handler;
/*    */ import org.jbpm.workflow.core.impl.WorkflowProcessImpl;
/*    */ import org.kie.api.definition.process.Process;
/*    */ import org.xml.sax.Attributes;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public class ImportHandler extends BaseAbstractHandler
/*    */   implements Handler
/*    */ {
/*    */   public ImportHandler()
/*    */   {
/* 34 */     if ((this.validParents == null) && (this.validPeers == null)) {
/* 35 */       this.validParents = new HashSet();
/* 36 */       this.validParents.add(Process.class);
/*    */ 
/* 38 */       this.validPeers = new HashSet();
/* 39 */       this.validPeers.add(null);
/*    */ 
/* 41 */       this.allowNesting = false;
/*    */     }
/*    */   }
/*    */ 
/*    */   public Object start(String uri, String localName, Attributes attrs, ExtensibleXmlParser parser)
/*    */     throws SAXException
/*    */   {
/* 48 */     parser.startElementBuilder(localName, attrs);
/* 49 */     WorkflowProcessImpl process = (WorkflowProcessImpl)parser.getParent();
/*    */ 
/* 51 */     String name = attrs.getValue("name");
/* 52 */     String type = attrs.getValue("importType");
/* 53 */     String location = attrs.getValue("location");
/* 54 */     String namespace = attrs.getValue("namespace");
/* 55 */     emptyAttributeCheck(localName, "name", name, parser);
/*    */ 
/* 57 */     if ((type != null) && (location != null) && (namespace != null)) {
/* 58 */       Map typedImports = (Map)process.getMetaData(type);
/* 59 */       if (typedImports == null) {
/* 60 */         typedImports = new HashMap();
/* 61 */         process.setMetaData(type, typedImports);
/*    */       }
/* 63 */       typedImports.put(namespace, location);
/*    */     }
/*    */     else {
/* 66 */       Set list = process.getImports();
/* 67 */       if (list == null) {
/* 68 */         list = new HashSet();
/* 69 */         process.setImports(list);
/*    */       }
/* 71 */       list.add(name);
/*    */     }
/* 73 */     return null;
/*    */   }
/*    */ 
/*    */   public Object end(String uri, String localName, ExtensibleXmlParser parser) throws SAXException
/*    */   {
/* 78 */     parser.endElementBuilder();
/* 79 */     return null;
/*    */   }
/*    */ 
/*    */   public Class generateNodeFor() {
/* 83 */     return null;
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.ImportHandler
 * JD-Core Version:    0.6.0
 */