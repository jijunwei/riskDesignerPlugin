/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import org.drools.core.xml.BaseAbstractHandler;
/*    */ import org.drools.core.xml.ExtensibleXmlParser;
/*    */ import org.drools.core.xml.Handler;
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
/*    */ public class ErrorHandler extends BaseAbstractHandler
/*    */   implements Handler
/*    */ {
/*    */   public ErrorHandler()
/*    */   {
/* 39 */     if ((this.validParents == null) && (this.validPeers == null)) {
/* 40 */       this.validParents = new HashSet();
/* 41 */       this.validParents.add(Definitions.class);
/*    */ 
/* 43 */       this.validPeers = new HashSet();
/* 44 */       this.validPeers.add(null);
/* 45 */       this.validPeers.add(ItemDefinition.class);
/* 46 */       this.validPeers.add(Message.class);
/* 47 */       this.validPeers.add(Interface.class);
/* 48 */       this.validPeers.add(Escalation.class);
/* 49 */       this.validPeers.add(Error.class);
/* 50 */       this.validPeers.add(Signal.class);
/* 51 */       this.validPeers.add(DataStore.class);
/* 52 */       this.validPeers.add(RuleFlowProcess.class);
/*    */ 
/* 54 */       this.allowNesting = false;
/*    */     }
/*    */   }
/*    */ 
/*    */   public Object start(String uri, String localName, Attributes attrs, ExtensibleXmlParser parser)
/*    */     throws SAXException
/*    */   {
/* 62 */     parser.startElementBuilder(localName, attrs);
/*    */ 
/* 64 */     String id = attrs.getValue("id");
/* 65 */     String errorCode = attrs.getValue("errorCode");
/* 66 */     String structureRef = attrs.getValue("structureRef");
/*    */ 
/* 68 */     Definitions definitions = (Definitions)parser.getParent();
/*    */ 
/* 70 */     List errors = definitions.getErrors();
/* 71 */     if (errors == null) {
/* 72 */       errors = new ArrayList();
/* 73 */       definitions.setErrors(errors);
/* 74 */       ((ProcessBuildData)parser.getData()).setMetaData("Errors", errors);
/*    */     }
/* 76 */     Error e = new Error(id, errorCode, structureRef);
/* 77 */     errors.add(e);
/*    */ 
/* 79 */     return e;
/*    */   }
/*    */ 
/*    */   public Object end(String uri, String localName, ExtensibleXmlParser parser) throws SAXException
/*    */   {
/* 84 */     parser.endElementBuilder();
/* 85 */     return parser.getCurrent();
/*    */   }
/*    */ 
/*    */   public Class<?> generateNodeFor() {
/* 89 */     return Error.class;
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.ErrorHandler
 * JD-Core Version:    0.6.0
 */