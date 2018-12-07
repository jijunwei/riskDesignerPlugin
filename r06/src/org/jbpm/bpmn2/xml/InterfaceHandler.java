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
/*    */ public class InterfaceHandler extends BaseAbstractHandler
/*    */   implements Handler
/*    */ {
/*    */   public InterfaceHandler()
/*    */   {
/* 37 */     if ((this.validParents == null) && (this.validPeers == null)) {
/* 38 */       this.validParents = new HashSet();
/* 39 */       this.validParents.add(Definitions.class);
/*    */ 
/* 41 */       this.validPeers = new HashSet();
/* 42 */       this.validPeers.add(null);
/* 43 */       this.validPeers.add(ItemDefinition.class);
/* 44 */       this.validPeers.add(Message.class);
/* 45 */       this.validPeers.add(Interface.class);
/* 46 */       this.validPeers.add(Escalation.class);
/* 47 */       this.validPeers.add(Error.class);
/* 48 */       this.validPeers.add(Signal.class);
/* 49 */       this.validPeers.add(DataStore.class);
/* 50 */       this.validPeers.add(RuleFlowProcess.class);
/*    */ 
/* 52 */       this.allowNesting = false;
/*    */     }
/*    */   }
/*    */ 
/*    */   public Object start(String uri, String localName, Attributes attrs, ExtensibleXmlParser parser)
/*    */     throws SAXException
/*    */   {
/* 60 */     parser.startElementBuilder(localName, attrs);
/*    */ 
/* 62 */     String id = attrs.getValue("id");
/* 63 */     String name = attrs.getValue("name");
/* 64 */     String implRef = attrs.getValue("implementationRef");
/*    */ 
/* 66 */     if ((name == null) || (name.isEmpty())) {
/* 67 */       throw new IllegalArgumentException("Interface name is required attribute");
/*    */     }
/*    */ 
/* 70 */     ProcessBuildData buildData = (ProcessBuildData)parser.getData();
/* 71 */     List interfaces = (List)buildData.getMetaData("Interfaces");
/* 72 */     if (interfaces == null) {
/* 73 */       interfaces = new ArrayList();
/* 74 */       buildData.setMetaData("Interfaces", interfaces);
/*    */     }
/* 76 */     Interface i = new Interface(id, name);
/* 77 */     if (implRef != null) {
/* 78 */       i.setImplementationRef(implRef);
/*    */     }
/* 80 */     interfaces.add(i);
/* 81 */     return i;
/*    */   }
/*    */ 
/*    */   public Object end(String uri, String localName, ExtensibleXmlParser parser) throws SAXException
/*    */   {
/* 86 */     parser.endElementBuilder();
/* 87 */     return parser.getCurrent();
/*    */   }
/*    */ 
/*    */   public Class<?> generateNodeFor() {
/* 91 */     return Interface.class;
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.InterfaceHandler
 * JD-Core Version:    0.6.0
 */