/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.drools.core.xml.ExtensibleXmlParser;
/*    */ import org.jbpm.bpmn2.core.Interface;
/*    */ import org.jbpm.bpmn2.core.Interface.Operation;
/*    */ import org.jbpm.bpmn2.core.Message;
/*    */ import org.jbpm.compiler.xml.ProcessBuildData;
/*    */ import org.jbpm.process.core.Work;
/*    */ import org.jbpm.workflow.core.Node;
/*    */ import org.jbpm.workflow.core.node.WorkItemNode;
/*    */ import org.w3c.dom.Element;
/*    */ import org.xml.sax.Attributes;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public class ServiceTaskHandler extends TaskHandler
/*    */ {
/*    */   protected Node createNode(Attributes attrs)
/*    */   {
/* 34 */     return new WorkItemNode();
/*    */   }
/*    */ 
/*    */   public Class generateNodeFor()
/*    */   {
/* 39 */     return Node.class;
/*    */   }
/*    */ 
/*    */   protected void handleNode(Node node, Element element, String uri, String localName, ExtensibleXmlParser parser)
/*    */     throws SAXException
/*    */   {
/* 45 */     super.handleNode(node, element, uri, localName, parser);
/* 46 */     WorkItemNode workItemNode = (WorkItemNode)node;
/* 47 */     String operationRef = element.getAttribute("operationRef");
/* 48 */     String implementation = element.getAttribute("implementation");
/* 49 */     List<Interface> interfaces = (List)((ProcessBuildData)parser.getData()).getMetaData("Interfaces");
/*    */ 
/* 51 */     workItemNode.setMetaData("OperationRef", operationRef);
/* 52 */     workItemNode.setMetaData("Implementation", implementation);
/* 53 */     workItemNode.setMetaData("Type", "Service Task");
/* 54 */     if (interfaces != null)
/*    */     {
/* 57 */       Interface.Operation operation = null;
/* 58 */       for (Interface i : interfaces) {
/* 59 */         operation = i.getOperation(operationRef);
/* 60 */         if (operation != null) {
/*    */           break;
/*    */         }
/*    */       }
/* 64 */       if (operation == null) {
/* 65 */         throw new IllegalArgumentException("Could not find operation " + operationRef);
/*    */       }
/*    */ 
/* 68 */       if (workItemNode.getWork().getParameter("Interface") == null) {
/* 69 */         workItemNode.getWork().setParameter("Interface", operation.getInterface().getName());
/*    */       }
/* 71 */       if (workItemNode.getWork().getParameter("Operation") == null) {
/* 72 */         workItemNode.getWork().setParameter("Operation", operation.getName());
/*    */       }
/* 74 */       if (workItemNode.getWork().getParameter("ParameterType") == null) {
/* 75 */         workItemNode.getWork().setParameter("ParameterType", operation.getMessage().getType());
/*    */       }
/*    */ 
/* 78 */       if (implementation != null) {
/* 79 */         workItemNode.getWork().setParameter("interfaceImplementationRef", operation.getInterface().getImplementationRef());
/* 80 */         workItemNode.getWork().setParameter("operationImplementationRef", operation.getImplementationRef());
/* 81 */         workItemNode.getWork().setParameter("implementation", implementation);
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   protected String getTaskName(Element element) {
/* 87 */     return "Service Task";
/*    */   }
/*    */ 
/*    */   public void writeNode(Node node, StringBuilder xmlDump, boolean includeMeta) {
/* 91 */     throw new IllegalArgumentException("Writing out should be handled by TaskHandler");
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.ServiceTaskHandler
 * JD-Core Version:    0.6.0
 */