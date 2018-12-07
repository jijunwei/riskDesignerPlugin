/*     */ package org.jbpm.bpmn2.xml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.drools.core.xml.ExtensibleXmlParser;
/*     */ import org.jbpm.bpmn2.core.Escalation;
/*     */ import org.jbpm.bpmn2.core.IntermediateLink;
/*     */ import org.jbpm.bpmn2.core.Message;
/*     */ import org.jbpm.compiler.xml.ProcessBuildData;
/*     */ import org.jbpm.process.core.impl.DataTransformerRegistry;
/*     */ import org.jbpm.ruleflow.core.RuleFlowProcess;
/*     */ import org.jbpm.workflow.core.NodeContainer;
/*     */ import org.jbpm.workflow.core.impl.DroolsConsequenceAction;
/*     */ import org.jbpm.workflow.core.node.ActionNode;
/*     */ import org.jbpm.workflow.core.node.CompositeNode;
/*     */ import org.jbpm.workflow.core.node.ThrowLinkNode;
/*     */ import org.jbpm.workflow.core.node.Transformation;
/*     */ import org.kie.api.runtime.process.DataTransformer;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class IntermediateThrowEventHandler extends AbstractNodeHandler
/*     */ {
/*  46 */   private DataTransformerRegistry transformerRegistry = DataTransformerRegistry.get();
/*     */   public static final String LINK_NAME = "linkName";
/*     */   public static final String LINK_SOURCE = "source";
/*     */   public static final String LINK_TARGET = "target";
/*     */ 
/*     */   protected org.jbpm.workflow.core.Node createNode(Attributes attrs)
/*     */   {
/*  53 */     return new ActionNode();
/*     */   }
/*     */ 
/*     */   public Class generateNodeFor()
/*     */   {
/*  58 */     return org.jbpm.workflow.core.Node.class;
/*     */   }
/*     */ 
/*     */   public Object end(String uri, String localName, ExtensibleXmlParser parser) throws SAXException
/*     */   {
/*  63 */     Element element = parser.endElementBuilder();
/*  64 */     ActionNode node = (ActionNode)parser.getCurrent();
/*     */ 
/*  67 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/*  68 */     while (xmlNode != null) {
/*  69 */       String nodeName = xmlNode.getNodeName();
/*  70 */       if ("signalEventDefinition".equals(nodeName))
/*     */       {
/*  72 */         handleSignalNode(node, element, uri, localName, parser);
/*  73 */         break;
/*  74 */       }if ("messageEventDefinition".equals(nodeName))
/*     */       {
/*  76 */         handleMessageNode(node, element, uri, localName, parser);
/*  77 */         break;
/*  78 */       }if ("escalationEventDefinition".equals(nodeName))
/*     */       {
/*  80 */         handleEscalationNode(node, element, uri, localName, parser);
/*  81 */         break;
/*  82 */       }if ("compensateEventDefinition".equals(nodeName))
/*     */       {
/*  84 */         handleThrowCompensationEventNode(node, element, uri, localName, parser);
/*  85 */         break;
/*  86 */       }if ("linkEventDefinition".equals(nodeName)) {
/*  87 */         ThrowLinkNode linkNode = new ThrowLinkNode();
/*  88 */         linkNode.setId(node.getId());
/*  89 */         handleLinkNode(element, linkNode, xmlNode, parser);
/*     */ 
/*  91 */         NodeContainer nodeContainer = (NodeContainer)parser
/*  91 */           .getParent();
/*  92 */         nodeContainer.addNode(linkNode);
/*  93 */         ((ProcessBuildData)parser.getData()).addNode(node);
/*     */ 
/*  95 */         return linkNode;
/*     */       }
/*  97 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*     */ 
/* 100 */     if (node.getAction() == null) {
/* 101 */       node.setAction(new DroolsConsequenceAction("mvel", ""));
/* 102 */       node.setMetaData("NodeType", "IntermediateThrowEvent-None");
/*     */     }
/* 104 */     NodeContainer nodeContainer = (NodeContainer)parser.getParent();
/* 105 */     nodeContainer.addNode(node);
/* 106 */     return node;
/*     */   }
/*     */ 
/*     */   protected void handleLinkNode(Element element, org.jbpm.workflow.core.Node node, org.w3c.dom.Node xmlLinkNode, ExtensibleXmlParser parser)
/*     */   {
/* 112 */     node.setName(element.getAttribute("name"));
/*     */ 
/* 114 */     NamedNodeMap linkAttr = xmlLinkNode.getAttributes();
/* 115 */     String name = linkAttr.getNamedItem("name").getNodeValue();
/*     */ 
/* 118 */     String id = element.getAttribute("id");
/* 119 */     node.setMetaData("UniqueId", id);
/* 120 */     node.setMetaData("linkName", name);
/*     */ 
/* 122 */     org.w3c.dom.Node xmlNode = xmlLinkNode.getFirstChild();
/*     */ 
/* 124 */     NodeContainer nodeContainer = (NodeContainer)parser.getParent();
/*     */ 
/* 126 */     IntermediateLink aLink = new IntermediateLink();
/* 127 */     aLink.setName(name);
/* 128 */     aLink.setUniqueId(id);
/* 129 */     while (null != xmlNode) {
/* 130 */       String nodeName = xmlNode.getNodeName();
/*     */ 
/* 132 */       if ("target".equals(nodeName)) {
/* 133 */         String target = xmlNode.getTextContent();
/* 134 */         node.setMetaData("target", target);
/*     */       }
/*     */ 
/* 137 */       if ("source".equals(nodeName)) {
/* 138 */         String source = xmlNode.getTextContent();
/*     */ 
/* 140 */         ArrayList sources = (ArrayList)node
/* 140 */           .getMetaData().get("source");
/*     */ 
/* 143 */         if (null == sources) {
/* 144 */           sources = new ArrayList();
/*     */         }
/*     */ 
/* 148 */         aLink.addSource(source);
/*     */ 
/* 151 */         sources.add(source);
/* 152 */         node.setMetaData("source", sources);
/*     */       }
/* 154 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/* 156 */     aLink.configureThrow();
/*     */ 
/* 158 */     if ((nodeContainer instanceof RuleFlowProcess)) {
/* 159 */       RuleFlowProcess process = (RuleFlowProcess)nodeContainer;
/*     */ 
/* 161 */       List links = (List)process
/* 161 */         .getMetaData().get("BPMN.ThrowLinks");
/* 162 */       if (null == links) {
/* 163 */         links = new ArrayList();
/*     */       }
/* 165 */       links.add(aLink);
/* 166 */       process.setMetaData("BPMN.ThrowLinks", links);
/* 167 */     } else if ((nodeContainer instanceof CompositeNode)) {
/* 168 */       CompositeNode subprocess = (CompositeNode)nodeContainer;
/*     */ 
/* 170 */       List links = (List)subprocess
/* 170 */         .getMetaData().get("BPMN.ThrowLinks");
/* 171 */       if (null == links) {
/* 172 */         links = new ArrayList();
/*     */       }
/* 174 */       links.add(aLink);
/* 175 */       subprocess.setMetaData("BPMN.ThrowLinks", links);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void handleSignalNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/* 183 */     ActionNode actionNode = (ActionNode)node;
/* 184 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/* 185 */     while (xmlNode != null) {
/* 186 */       String nodeName = xmlNode.getNodeName();
/* 187 */       if ("dataInput".equals(nodeName)) {
/* 188 */         String id = ((Element)xmlNode).getAttribute("id");
/* 189 */         String inputName = ((Element)xmlNode).getAttribute("name");
/* 190 */         this.dataInputs.put(id, inputName);
/* 191 */       } else if ("dataInputAssociation".equals(nodeName)) {
/* 192 */         readDataInputAssociation(xmlNode, actionNode);
/* 193 */       } else if ("signalEventDefinition".equals(nodeName)) {
/* 194 */         String signalName = ((Element)xmlNode).getAttribute("signalRef");
/* 195 */         String variable = (String)actionNode.getMetaData("MappingVariable");
/*     */ 
/* 197 */         signalName = checkSignalAndConvertToRealSignalNam(parser, signalName);
/*     */ 
/* 199 */         actionNode.setMetaData("EventType", "signal");
/* 200 */         actionNode.setMetaData("Ref", signalName);
/* 201 */         actionNode.setMetaData("Variable", variable);
/*     */ 
/* 204 */         if (this.dataInputs.containsValue("async")) {
/* 205 */           signalName = new StringBuilder().append("ASYNC-").append(signalName).toString();
/*     */         }
/*     */ 
/* 208 */         String signalExpression = getSignalExpression(actionNode, signalName, "tVariable");
/*     */ 
/* 210 */         actionNode
/* 211 */           .setAction(new DroolsConsequenceAction("java", new StringBuilder().append(" Object tVariable = ").append(variable == null ? "null" : variable).append(";org.jbpm.workflow.core.node.Transformation transformation = (org.jbpm.workflow.core.node.Transformation)kcontext.getNodeInstance().getNode().getMetaData().get(\"Transformation\");if (transformation != null) {  tVariable = new org.jbpm.process.core.event.EventTransformerImpl(transformation)  .transformEvent(").append(variable == null ? "null" : variable).append(");}").append(signalExpression).toString()));
/*     */       }
/*     */ 
/* 221 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void handleMessageNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/* 229 */     ActionNode actionNode = (ActionNode)node;
/* 230 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/* 231 */     while (xmlNode != null) {
/* 232 */       String nodeName = xmlNode.getNodeName();
/* 233 */       if ("dataInput".equals(nodeName)) {
/* 234 */         String id = ((Element)xmlNode).getAttribute("id");
/* 235 */         String inputName = ((Element)xmlNode).getAttribute("name");
/* 236 */         this.dataInputs.put(id, inputName);
/* 237 */       } else if ("dataInputAssociation".equals(nodeName)) {
/* 238 */         readDataInputAssociation(xmlNode, actionNode);
/* 239 */       } else if ("messageEventDefinition".equals(nodeName))
/*     */       {
/* 241 */         String messageRef = ((Element)xmlNode)
/* 241 */           .getAttribute("messageRef");
/*     */ 
/* 243 */         Map messages = (Map)((ProcessBuildData)parser
/* 243 */           .getData()).getMetaData("Messages");
/* 244 */         if (messages == null) {
/* 245 */           throw new IllegalArgumentException("No messages found");
/*     */         }
/* 247 */         Message message = (Message)messages.get(messageRef);
/* 248 */         if (message == null) {
/* 249 */           throw new IllegalArgumentException(new StringBuilder().append("Could not find message ").append(messageRef).toString());
/*     */         }
/*     */ 
/* 253 */         String variable = (String)actionNode
/* 253 */           .getMetaData("MappingVariable");
/*     */ 
/* 254 */         actionNode.setMetaData("MessageType", message.getType());
/* 255 */         actionNode
/* 256 */           .setAction(
/* 271 */           new DroolsConsequenceAction("java", new StringBuilder().append(" Object tVariable = ").append(variable == null ? "null" : variable).append(";org.jbpm.workflow.core.node.Transformation transformation = (org.jbpm.workflow.core.node.Transformation)kcontext.getNodeInstance().getNode().getMetaData().get(\"Transformation\");if (transformation != null) {  tVariable = new org.jbpm.process.core.event.EventTransformerImpl(transformation)  .transformEvent(").append(variable == null ? "null" : variable).append(");}org.drools.core.process.instance.impl.WorkItemImpl workItem = new org.drools.core.process.instance.impl.WorkItemImpl();").append(EOL).append("workItem.setName(\"Send Task\");").append(EOL).append("workItem.setProcessInstanceId(kcontext.getProcessInstance().getId());").append(EOL).append("workItem.setParameter(\"MessageType\", \"")
/* 271 */           .append(message
/* 271 */           .getType()).append("\");").append(EOL).append("workItem.setNodeInstanceId(kcontext.getNodeInstance().getId());").append(EOL).append("workItem.setNodeId(kcontext.getNodeInstance().getNodeId());").append(EOL).append("workItem.setDeploymentId((String) kcontext.getKnowledgeRuntime().getEnvironment().get(\"deploymentId\"));").append(EOL).append(variable == null ? "" : new StringBuilder().append("workItem.setParameter(\"Message\", tVariable);").append(EOL).toString()).append("((org.drools.core.process.instance.WorkItemManager) kcontext.getKnowledgeRuntime().getWorkItemManager()).internalExecuteWorkItem(workItem);").toString()));
/*     */       }
/*     */ 
/* 284 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void handleEscalationNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/* 292 */     ActionNode actionNode = (ActionNode)node;
/* 293 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/* 294 */     while (xmlNode != null) {
/* 295 */       String nodeName = xmlNode.getNodeName();
/* 296 */       if ("dataInputAssociation".equals(nodeName)) {
/* 297 */         readDataInputAssociation(xmlNode, actionNode);
/* 298 */       } else if ("escalationEventDefinition".equals(nodeName))
/*     */       {
/* 300 */         String escalationRef = ((Element)xmlNode)
/* 300 */           .getAttribute("escalationRef");
/*     */ 
/* 301 */         if ((escalationRef != null) && (escalationRef.trim().length() > 0))
/*     */         {
/* 303 */           Map escalations = (Map)((ProcessBuildData)parser
/* 303 */             .getData()).getMetaData("BPMN.Escalations");
/* 304 */           if (escalations == null) {
/* 305 */             throw new IllegalArgumentException("No escalations found");
/*     */           }
/*     */ 
/* 308 */           Escalation escalation = (Escalation)escalations.get(escalationRef);
/* 309 */           if (escalation == null) {
/* 310 */             throw new IllegalArgumentException(new StringBuilder().append("Could not find escalation ").append(escalationRef).toString());
/*     */           }
/*     */ 
/* 313 */           String faultName = escalation.getEscalationCode();
/* 314 */           String variable = (String)actionNode.getMetaData("MappingVariable");
/* 315 */           actionNode
/* 316 */             .setAction(new DroolsConsequenceAction("java", new StringBuilder().append("org.jbpm.process.instance.context.exception.ExceptionScopeInstance scopeInstance = (org.jbpm.process.instance.context.exception.ExceptionScopeInstance) ((org.jbpm.workflow.instance.NodeInstance) kcontext.getNodeInstance()).resolveContextInstance(org.jbpm.process.core.context.exception.ExceptionScope.EXCEPTION_SCOPE, \"").append(faultName).append("\");").append(EOL).append("if (scopeInstance != null) {").append(EOL).append(" Object tVariable = ").append(variable == null ? "null" : variable).append(";org.jbpm.workflow.core.node.Transformation transformation = (org.jbpm.workflow.core.node.Transformation)kcontext.getNodeInstance().getNode().getMetaData().get(\"Transformation\");if (transformation != null) {  tVariable = new org.jbpm.process.core.event.EventTransformerImpl(transformation)  .transformEvent(").append(variable == null ? "null" : variable).append(");}  scopeInstance.handleException(\"").append(faultName).append("\", tVariable);").append(EOL).append("} else {").append(EOL).append("    ((org.jbpm.process.instance.ProcessInstance) kcontext.getProcessInstance()).setState(org.jbpm.process.instance.ProcessInstance.STATE_ABORTED);").append(EOL).append("}").toString()));
/*     */         }
/*     */         else
/*     */         {
/* 339 */           throw new IllegalArgumentException("General escalation is not yet supported");
/*     */         }
/*     */       }
/* 342 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void readDataInputAssociation(org.w3c.dom.Node xmlNode, ActionNode actionNode)
/*     */   {
/* 349 */     org.w3c.dom.Node subNode = xmlNode.getFirstChild();
/* 350 */     String eventVariable = subNode.getTextContent();
/*     */ 
/* 352 */     subNode = subNode.getNextSibling();
/* 353 */     String target = subNode.getTextContent();
/*     */ 
/* 355 */     Transformation transformation = null;
/* 356 */     subNode = subNode.getNextSibling();
/* 357 */     if ((subNode != null) && ("transformation".equals(subNode.getNodeName()))) {
/* 358 */       String lang = subNode.getAttributes().getNamedItem("language").getNodeValue();
/* 359 */       String expression = subNode.getTextContent();
/*     */ 
/* 361 */       DataTransformer transformer = this.transformerRegistry.find(lang);
/* 362 */       if (transformer == null) {
/* 363 */         throw new IllegalArgumentException(new StringBuilder().append("No transformer registered for language ").append(lang).toString());
/*     */       }
/* 365 */       transformation = new Transformation(lang, expression, (String)this.dataInputs.get(target));
/* 366 */       actionNode.setMetaData("Transformation", transformation);
/*     */     }
/*     */ 
/* 369 */     if ((eventVariable != null) && (eventVariable.trim().length() > 0))
/* 370 */       actionNode.setMetaData("MappingVariable", eventVariable);
/*     */   }
/*     */ 
/*     */   public void writeNode(org.jbpm.workflow.core.Node node, StringBuilder xmlDump, int metaDataType)
/*     */   {
/* 375 */     throw new IllegalArgumentException("Writing out should be handled by action node handler");
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.IntermediateThrowEventHandler
 * JD-Core Version:    0.6.0
 */