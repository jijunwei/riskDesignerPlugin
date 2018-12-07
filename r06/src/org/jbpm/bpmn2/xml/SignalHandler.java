/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
/*    */ import java.util.Map;
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
/*    */ public class SignalHandler extends BaseAbstractHandler
/*    */   implements Handler
/*    */ {
/*    */   public SignalHandler()
/*    */   {
/* 49 */     if ((this.validParents == null) && (this.validPeers == null)) {
/* 50 */       this.validParents = new HashSet();
/* 51 */       this.validParents.add(Definitions.class);
/*    */ 
/* 53 */       this.validPeers = new HashSet();
/* 54 */       this.validPeers.add(null);
/* 55 */       this.validPeers.add(ItemDefinition.class);
/* 56 */       this.validPeers.add(Message.class);
/* 57 */       this.validPeers.add(Interface.class);
/* 58 */       this.validPeers.add(Escalation.class);
/* 59 */       this.validPeers.add(Error.class);
/* 60 */       this.validPeers.add(Signal.class);
/* 61 */       this.validPeers.add(DataStore.class);
/* 62 */       this.validPeers.add(RuleFlowProcess.class);
/*    */ 
/* 64 */       this.allowNesting = false;
/*    */     }
/*    */   }
/*    */ 
/*    */   public Object start(String uri, String localName, Attributes attrs, ExtensibleXmlParser parser)
/*    */     throws SAXException
/*    */   {
/* 72 */     parser.startElementBuilder(localName, attrs);
/*    */ 
/* 75 */     String id = attrs.getValue("id");
/* 76 */     String name = attrs.getValue("name");
/* 77 */     String structureRef = attrs.getValue("structureRef");
/*    */ 
/* 79 */     ProcessBuildData buildData = (ProcessBuildData)parser.getData();
/* 80 */     Map signals = (Map)buildData.getMetaData("Signals");
/* 81 */     if (signals == null) {
/* 82 */       signals = new HashMap();
/* 83 */       buildData.setMetaData("Signals", signals);
/*    */     }
/*    */ 
/* 86 */     Signal s = new Signal(id, name, structureRef);
/* 87 */     signals.put(id, s);
/*    */ 
/* 89 */     return s;
/*    */   }
/*    */ 
/*    */   public Object end(String uri, String localName, ExtensibleXmlParser parser) throws SAXException
/*    */   {
/* 94 */     parser.endElementBuilder();
/* 95 */     return parser.getCurrent();
/*    */   }
/*    */ 
/*    */   public Class<?> generateNodeFor() {
/* 99 */     return Error.class;
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.SignalHandler
 * JD-Core Version:    0.6.0
 */