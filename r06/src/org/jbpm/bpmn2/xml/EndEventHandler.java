/*     */ package org.jbpm.bpmn2.xml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.drools.core.xml.ExtensibleXmlParser;
/*     */ import org.jbpm.bpmn2.core.Error;
/*     */ import org.jbpm.bpmn2.core.Escalation;
/*     */ import org.jbpm.bpmn2.core.Message;
/*     */ import org.jbpm.compiler.xml.ProcessBuildData;
/*     */ import org.jbpm.workflow.core.NodeContainer;
/*     */ import org.jbpm.workflow.core.impl.DroolsConsequenceAction;
/*     */ import org.jbpm.workflow.core.node.EndNode;
/*     */ import org.jbpm.workflow.core.node.FaultNode;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class EndEventHandler extends AbstractNodeHandler
/*     */ {
/*     */   protected org.jbpm.workflow.core.Node createNode(Attributes attrs)
/*     */   {
/*  42 */     EndNode node = new EndNode();
/*  43 */     node.setTerminate(false);
/*  44 */     return node;
/*     */   }
/*     */ 
/*     */   public Class generateNodeFor()
/*     */   {
/*  49 */     return EndNode.class;
/*     */   }
/*     */ 
/*     */   public Object end(String uri, String localName, ExtensibleXmlParser parser) throws SAXException
/*     */   {
/*  54 */     Element element = parser.endElementBuilder();
/*  55 */     org.jbpm.workflow.core.Node node = (org.jbpm.workflow.core.Node)parser.getCurrent();
/*     */ 
/*  58 */     super.handleNode(node, element, uri, localName, parser);
/*  59 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/*  60 */     while (xmlNode != null) {
/*  61 */       String nodeName = xmlNode.getNodeName();
/*  62 */       if ("terminateEventDefinition".equals(nodeName))
/*     */       {
/*  64 */         handleTerminateNode(node, element, uri, localName, parser);
/*  65 */         break;
/*  66 */       }if ("signalEventDefinition".equals(nodeName)) {
/*  67 */         handleSignalNode(node, element, uri, localName, parser);
/*  68 */       } else if ("messageEventDefinition".equals(nodeName)) {
/*  69 */         handleMessageNode(node, element, uri, localName, parser); } else {
/*  70 */         if ("errorEventDefinition".equals(nodeName))
/*     */         {
/*  72 */           FaultNode faultNode = new FaultNode();
/*  73 */           faultNode.setId(node.getId());
/*  74 */           faultNode.setName(node.getName());
/*  75 */           faultNode.setTerminateParent(true);
/*  76 */           faultNode.setMetaData("UniqueId", node.getMetaData().get("UniqueId"));
/*  77 */           node = faultNode;
/*  78 */           super.handleNode(node, element, uri, localName, parser);
/*  79 */           handleErrorNode(node, element, uri, localName, parser);
/*  80 */           break;
/*  81 */         }if ("escalationEventDefinition".equals(nodeName))
/*     */         {
/*  83 */           FaultNode faultNode = new FaultNode();
/*  84 */           faultNode.setId(node.getId());
/*  85 */           faultNode.setName(node.getName());
/*  86 */           faultNode.setMetaData("UniqueId", node.getMetaData().get("UniqueId"));
/*  87 */           node = faultNode;
/*  88 */           super.handleNode(node, element, uri, localName, parser);
/*  89 */           handleEscalationNode(node, element, uri, localName, parser);
/*  90 */           break;
/*  91 */         }if ("compensateEventDefinition".equals(nodeName))
/*     */         {
/*  93 */           handleThrowCompensationEventNode(node, element, uri, localName, parser);
/*  94 */           break;
/*     */         }
/*     */       }
/*  96 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*  98 */     NodeContainer nodeContainer = (NodeContainer)parser.getParent();
/*  99 */     nodeContainer.addNode(node);
/* 100 */     ((ProcessBuildData)parser.getData()).addNode(node);
/* 101 */     return node;
/*     */   }
/*     */ 
/*     */   public void handleTerminateNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser) throws SAXException
/*     */   {
/* 106 */     ((EndNode)node).setTerminate(true);
/*     */ 
/* 108 */     EndNode endNode = (EndNode)node;
/* 109 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/* 110 */     while (xmlNode != null) {
/* 111 */       String nodeName = xmlNode.getNodeName();
/* 112 */       if ("terminateEventDefinition".equals(nodeName))
/*     */       {
/* 114 */         String scope = ((Element)xmlNode).getAttribute("scope");
/* 115 */         if ("process".equalsIgnoreCase(scope))
/* 116 */           endNode.setScope(1);
/*     */         else {
/* 118 */           endNode.setScope(0);
/*     */         }
/*     */       }
/* 121 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void handleSignalNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser) throws SAXException
/*     */   {
/* 127 */     EndNode endNode = (EndNode)node;
/* 128 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/* 129 */     while (xmlNode != null) {
/* 130 */       String nodeName = xmlNode.getNodeName();
/* 131 */       if ("dataInput".equals(nodeName)) {
/* 132 */         String id = ((Element)xmlNode).getAttribute("id");
/* 133 */         String inputName = ((Element)xmlNode).getAttribute("name");
/* 134 */         this.dataInputs.put(id, inputName);
/* 135 */       } else if ("dataInputAssociation".equals(nodeName)) {
/* 136 */         readEndDataInputAssociation(xmlNode, endNode);
/* 137 */       } else if ("signalEventDefinition".equals(nodeName)) {
/* 138 */         String signalName = ((Element)xmlNode).getAttribute("signalRef");
/* 139 */         String variable = (String)endNode.getMetaData("MappingVariable");
/*     */ 
/* 141 */         signalName = checkSignalAndConvertToRealSignalNam(parser, signalName);
/*     */ 
/* 143 */         endNode.setMetaData("EventType", "signal");
/* 144 */         endNode.setMetaData("Ref", signalName);
/* 145 */         endNode.setMetaData("Variable", variable);
/*     */ 
/* 148 */         if (this.dataInputs.containsValue("async")) {
/* 149 */           signalName = new StringBuilder().append("ASYNC-").append(signalName).toString();
/*     */         }
/*     */ 
/* 152 */         String signalExpression = getSignalExpression(endNode, signalName, variable);
/*     */ 
/* 154 */         List actions = new ArrayList();
/* 155 */         actions.add(new DroolsConsequenceAction("mvel", signalExpression));
/* 156 */         endNode.setActions("onEntry", actions);
/*     */       }
/* 158 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void handleMessageNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/* 165 */     EndNode endNode = (EndNode)node;
/* 166 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/* 167 */     while (xmlNode != null) {
/* 168 */       String nodeName = xmlNode.getNodeName();
/* 169 */       if ("dataInputAssociation".equals(nodeName)) {
/* 170 */         readEndDataInputAssociation(xmlNode, endNode);
/* 171 */       } else if ("messageEventDefinition".equals(nodeName)) {
/* 172 */         String messageRef = ((Element)xmlNode).getAttribute("messageRef");
/*     */ 
/* 174 */         Map messages = (Map)((ProcessBuildData)parser
/* 174 */           .getData()).getMetaData("Messages");
/* 175 */         if (messages == null) {
/* 176 */           throw new IllegalArgumentException("No messages found");
/*     */         }
/* 178 */         Message message = (Message)messages.get(messageRef);
/* 179 */         if (message == null) {
/* 180 */           throw new IllegalArgumentException(new StringBuilder().append("Could not find message ").append(messageRef).toString());
/*     */         }
/* 182 */         String variable = (String)endNode.getMetaData("MappingVariable");
/* 183 */         endNode.setMetaData("MessageType", message.getType());
/* 184 */         List actions = new ArrayList();
/*     */ 
/* 186 */         actions.add(
/* 192 */           new DroolsConsequenceAction("java", new StringBuilder().append("org.drools.core.process.instance.impl.WorkItemImpl workItem = new org.drools.core.process.instance.impl.WorkItemImpl();").append(EOL).append("workItem.setName(\"Send Task\");").append(EOL).append("workItem.setNodeInstanceId(kcontext.getNodeInstance().getId());").append(EOL).append("workItem.setProcessInstanceId(kcontext.getProcessInstance().getId());").append(EOL).append("workItem.setNodeId(kcontext.getNodeInstance().getNodeId());").append(EOL).append("workItem.setParameter(\"MessageType\", \"")
/* 192 */           .append(message
/* 192 */           .getType()).append("\");").append(EOL).append(variable == null ? "" : new StringBuilder().append("workItem.setParameter(\"Message\", ").append(variable).append(");").append(EOL).toString()).append("workItem.setDeploymentId((String) kcontext.getKnowledgeRuntime().getEnvironment().get(\"deploymentId\"));").append(EOL).append("((org.drools.core.process.instance.WorkItemManager) kcontext.getKnowledgeRuntime().getWorkItemManager()).internalExecuteWorkItem(workItem);").toString()));
/*     */ 
/* 196 */         endNode.setActions("onEntry", actions);
/*     */       }
/* 198 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void readEndDataInputAssociation(org.w3c.dom.Node xmlNode, EndNode endNode)
/*     */   {
/* 204 */     org.w3c.dom.Node subNode = xmlNode.getFirstChild();
/* 205 */     String eventVariable = subNode.getTextContent();
/* 206 */     if ((eventVariable != null) && (eventVariable.trim().length() > 0))
/* 207 */       endNode.setMetaData("MappingVariable", eventVariable);
/*     */   }
/*     */ 
/*     */   public void handleErrorNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/* 214 */     FaultNode faultNode = (FaultNode)node;
/* 215 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/* 216 */     while (xmlNode != null) {
/* 217 */       String nodeName = xmlNode.getNodeName();
/* 218 */       if ("dataInputAssociation".equals(nodeName)) {
/* 219 */         readFaultDataInputAssociation(xmlNode, faultNode);
/* 220 */       } else if ("errorEventDefinition".equals(nodeName)) {
/* 221 */         String errorRef = ((Element)xmlNode).getAttribute("errorRef");
/* 222 */         if ((errorRef != null) && (errorRef.trim().length() > 0)) {
/* 223 */           List<Error> errors = (List)((ProcessBuildData)parser.getData()).getMetaData("Errors");
/* 224 */           if (errors == null) {
/* 225 */             throw new IllegalArgumentException("No errors found");
/*     */           }
/* 227 */           Error error = null;
/* 228 */           for (Error listError : errors) {
/* 229 */             if (errorRef.equals(listError.getId())) {
/* 230 */               error = listError;
/* 231 */               break;
/*     */             }
/*     */           }
/* 234 */           if (error == null) {
/* 235 */             throw new IllegalArgumentException(new StringBuilder().append("Could not find error ").append(errorRef).toString());
/*     */           }
/* 237 */           faultNode.setFaultName(error.getErrorCode());
/* 238 */           faultNode.setTerminateParent(true);
/*     */         }
/*     */       }
/* 241 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void handleEscalationNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/* 248 */     FaultNode faultNode = (FaultNode)node;
/* 249 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/* 250 */     while (xmlNode != null) {
/* 251 */       String nodeName = xmlNode.getNodeName();
/* 252 */       if ("dataInputAssociation".equals(nodeName)) {
/* 253 */         readFaultDataInputAssociation(xmlNode, faultNode);
/* 254 */       } else if ("escalationEventDefinition".equals(nodeName)) {
/* 255 */         String escalationRef = ((Element)xmlNode).getAttribute("escalationRef");
/* 256 */         if ((escalationRef != null) && (escalationRef.trim().length() > 0))
/*     */         {
/* 258 */           Map escalations = (Map)((ProcessBuildData)parser
/* 258 */             .getData()).getMetaData("BPMN.Escalations");
/* 259 */           if (escalations == null) {
/* 260 */             throw new IllegalArgumentException("No escalations found");
/*     */           }
/* 262 */           Escalation escalation = (Escalation)escalations.get(escalationRef);
/* 263 */           if (escalation == null) {
/* 264 */             throw new IllegalArgumentException(new StringBuilder().append("Could not find escalation ").append(escalationRef).toString());
/*     */           }
/* 266 */           faultNode.setFaultName(escalation.getEscalationCode());
/*     */         }
/*     */         else
/*     */         {
/* 270 */           throw new IllegalArgumentException("End events throwing an escalation must throw *specific* escalations (and not general ones).");
/*     */         }
/*     */       }
/* 273 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void readFaultDataInputAssociation(org.w3c.dom.Node xmlNode, FaultNode faultNode)
/*     */   {
/* 279 */     org.w3c.dom.Node subNode = xmlNode.getFirstChild();
/* 280 */     String faultVariable = subNode.getTextContent();
/* 281 */     faultNode.setFaultVariable(faultVariable);
/*     */   }
/*     */ 
/*     */   public void writeNode(org.jbpm.workflow.core.Node node, StringBuilder xmlDump, int metaDataType) {
/* 285 */     throw new IllegalArgumentException("Writing out should be handled by specific handlers");
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.EndEventHandler
 * JD-Core Version:    0.6.0
 */