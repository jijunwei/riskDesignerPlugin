/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import org.drools.core.xml.BaseAbstractHandler;
/*    */ import org.drools.core.xml.ExtensibleXmlParser;
/*    */ import org.drools.core.xml.Handler;
/*    */ import org.jbpm.bpmn2.core.Bpmn2Import;
/*    */ import org.jbpm.bpmn2.core.DataStore;
/*    */ import org.jbpm.bpmn2.core.Definitions;
/*    */ import org.jbpm.bpmn2.core.Error;
/*    */ import org.jbpm.bpmn2.core.Escalation;
/*    */ import org.jbpm.bpmn2.core.Interface;
/*    */ import org.jbpm.bpmn2.core.ItemDefinition;
/*    */ import org.jbpm.bpmn2.core.Message;
/*    */ import org.jbpm.bpmn2.core.Signal;
/*    */ import org.jbpm.compiler.xml.ProcessBuildData;
/*    */ import org.jbpm.ruleflow.core.RuleFlowProcess;
/*    */ import org.xml.sax.Attributes;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public class Bpmn2ImportHandler extends BaseAbstractHandler
/*    */   implements Handler
/*    */ {
/*    */   public Bpmn2ImportHandler()
/*    */   {
/* 35 */     if ((this.validParents == null) && (this.validPeers == null)) {
/* 36 */       this.validParents = new HashSet();
/* 37 */       this.validParents.add(Definitions.class);
/*    */ 
/* 39 */       this.validPeers = new HashSet();
/* 40 */       this.validPeers.add(null);
/* 41 */       this.validPeers.add(ItemDefinition.class);
/* 42 */       this.validPeers.add(Message.class);
/* 43 */       this.validPeers.add(Interface.class);
/* 44 */       this.validPeers.add(Escalation.class);
/* 45 */       this.validPeers.add(Error.class);
/* 46 */       this.validPeers.add(Signal.class);
/* 47 */       this.validPeers.add(DataStore.class);
/* 48 */       this.validPeers.add(RuleFlowProcess.class);
/*    */ 
/* 50 */       this.allowNesting = false;
/*    */     }
/*    */   }
/*    */ 
/*    */   public Object start(String uri, String localName, Attributes attrs, ExtensibleXmlParser parser)
/*    */     throws SAXException
/*    */   {
/* 57 */     parser.startElementBuilder(localName, attrs);
/*    */ 
/* 59 */     String type = attrs.getValue("importType");
/* 60 */     String location = attrs.getValue("location");
/* 61 */     String namespace = attrs.getValue("namespace");
/* 62 */     ProcessBuildData buildData = (ProcessBuildData)parser.getData();
/*    */ 
/* 64 */     if ((type != null) && (location != null) && (namespace != null)) {
/* 65 */       List typedImports = (List)buildData.getMetaData("Bpmn2Imports");
/* 66 */       if (typedImports == null) {
/* 67 */         typedImports = new ArrayList();
/* 68 */         buildData.setMetaData("Bpmn2Imports", typedImports);
/*    */       }
/* 70 */       typedImports.add(new Bpmn2Import(type, location, namespace));
/*    */     }
/* 72 */     return null;
/*    */   }
/*    */ 
/*    */   public Object end(String uri, String localName, ExtensibleXmlParser parser) throws SAXException
/*    */   {
/* 77 */     parser.endElementBuilder();
/* 78 */     return null;
/*    */   }
/*    */ 
/*    */   public Class generateNodeFor() {
/* 82 */     return null;
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.Bpmn2ImportHandler
 * JD-Core Version:    0.6.0
 */