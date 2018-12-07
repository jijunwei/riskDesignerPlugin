/*     */ package org.jbpm.bpmn2.xml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.drools.core.xml.BaseAbstractHandler;
/*     */ import org.drools.core.xml.ExtensibleXmlParser;
/*     */ import org.drools.core.xml.Handler;
/*     */ import org.jbpm.bpmn2.core.Association;
/*     */ import org.jbpm.bpmn2.core.DataStore;
/*     */ import org.jbpm.bpmn2.core.Definitions;
/*     */ import org.jbpm.bpmn2.core.Error;
/*     */ import org.jbpm.bpmn2.core.Escalation;
/*     */ import org.jbpm.bpmn2.core.Interface;
/*     */ import org.jbpm.bpmn2.core.IntermediateLink;
/*     */ import org.jbpm.bpmn2.core.ItemDefinition;
/*     */ import org.jbpm.bpmn2.core.Lane;
/*     */ import org.jbpm.bpmn2.core.Message;
/*     */ import org.jbpm.bpmn2.core.SequenceFlow;
/*     */ import org.jbpm.bpmn2.core.Signal;
/*     */ import org.jbpm.compiler.xml.ProcessBuildData;
/*     */ import org.jbpm.process.core.ContextContainer;
/*     */ import org.jbpm.process.core.context.exception.ActionExceptionHandler;
/*     */ import org.jbpm.process.core.context.exception.CompensationHandler;
/*     */ import org.jbpm.process.core.context.exception.CompensationScope;
/*     */ import org.jbpm.process.core.context.exception.ExceptionScope;
/*     */ import org.jbpm.process.core.context.swimlane.Swimlane;
/*     */ import org.jbpm.process.core.context.swimlane.SwimlaneContext;
/*     */ import org.jbpm.process.core.event.EventFilter;
/*     */ import org.jbpm.process.core.event.EventTypeFilter;
/*     */ import org.jbpm.process.core.timer.Timer;
/*     */ import org.jbpm.process.instance.impl.CancelNodeInstanceAction;
/*     */ import org.jbpm.ruleflow.core.RuleFlowProcess;
/*     */ import org.jbpm.workflow.core.Constraint;
/*     */ import org.jbpm.workflow.core.impl.ConnectionImpl;
/*     */ import org.jbpm.workflow.core.impl.ConnectionRef;
/*     */ import org.jbpm.workflow.core.impl.ConstraintImpl;
/*     */ import org.jbpm.workflow.core.impl.DroolsConsequenceAction;
/*     */ import org.jbpm.workflow.core.impl.ExtendedNodeImpl;
/*     */ import org.jbpm.workflow.core.impl.NodeImpl;
/*     */ import org.jbpm.workflow.core.node.ActionNode;
/*     */ import org.jbpm.workflow.core.node.BoundaryEventNode;
/*     */ import org.jbpm.workflow.core.node.CompositeContextNode;
/*     */ import org.jbpm.workflow.core.node.CompositeNode;
/*     */ import org.jbpm.workflow.core.node.ConstraintTrigger;
/*     */ import org.jbpm.workflow.core.node.DataAssociation;
/*     */ import org.jbpm.workflow.core.node.EndNode;
/*     */ import org.jbpm.workflow.core.node.EventNode;
/*     */ import org.jbpm.workflow.core.node.EventSubProcessNode;
/*     */ import org.jbpm.workflow.core.node.EventTrigger;
/*     */ import org.jbpm.workflow.core.node.FaultNode;
/*     */ import org.jbpm.workflow.core.node.HumanTaskNode;
/*     */ import org.jbpm.workflow.core.node.RuleSetNode;
/*     */ import org.jbpm.workflow.core.node.Split;
/*     */ import org.jbpm.workflow.core.node.StartNode;
/*     */ import org.jbpm.workflow.core.node.StateBasedNode;
/*     */ import org.jbpm.workflow.core.node.StateNode;
/*     */ import org.jbpm.workflow.core.node.SubProcessNode;
/*     */ import org.jbpm.workflow.core.node.Trigger;
/*     */ import org.jbpm.workflow.core.node.WorkItemNode;
import org.kie.api.definition.process.Connection;
/*     */ import org.kie.api.definition.process.Node;
/*     */ import org.kie.api.definition.process.NodeContainer;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class ProcessHandler extends BaseAbstractHandler
/*     */   implements Handler
/*     */ {
/*     */   private static final Logger logger;
/*     */   public static final String CONNECTIONS = "BPMN.Connections";
/*     */   public static final String LINKS = "BPMN.ThrowLinks";
/*     */   public static final String ASSOCIATIONS = "BPMN.Associations";
/*     */   public static final String ERRORS = "BPMN.Errors";
/*     */   public static final String ESCALATIONS = "BPMN.Escalations";
/*     */   static final String PROCESS_INSTANCE_SIGNAL_EVENT = "kcontext.getProcessInstance().signalEvent(\"";
/*     */   static final String RUNTIME_SIGNAL_EVENT = "kcontext.getKnowledgeRuntime().signalEvent(\"";
/*     */   static final String RUNTIME_MANAGER_SIGNAL_EVENT = "((org.kie.api.runtime.manager.RuntimeManager)kcontext.getKnowledgeRuntime().getEnvironment().get(\"RuntimeManager\")).signalEvent(\"";
/*     */ 
/*     */   public ProcessHandler()
/*     */   {
/* 105 */     if ((this.validParents == null) && (this.validPeers == null)) {
/* 106 */       this.validParents = new HashSet();
/* 107 */       this.validParents.add(Definitions.class);
/*     */ 
/* 109 */       this.validPeers = new HashSet();
/* 110 */       this.validPeers.add(null);
/* 111 */       this.validPeers.add(ItemDefinition.class);
/* 112 */       this.validPeers.add(Message.class);
/* 113 */       this.validPeers.add(Interface.class);
/* 114 */       this.validPeers.add(Escalation.class);
/* 115 */       this.validPeers.add(Error.class);
/* 116 */       this.validPeers.add(Signal.class);
/* 117 */       this.validPeers.add(DataStore.class);
/* 118 */       this.validPeers.add(RuleFlowProcess.class);
/*     */ 
/* 120 */       this.allowNesting = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object start(String uri, String localName, Attributes attrs, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/* 127 */     parser.startElementBuilder(localName, attrs);
/*     */ 
/* 129 */     String id = attrs.getValue("id");
/* 130 */     String name = attrs.getValue("name");
/* 131 */     String packageName = attrs.getValue("http://www.jboss.org/drools", "packageName");
/* 132 */     String dynamic = attrs.getValue("http://www.jboss.org/drools", "adHoc");
/* 133 */     String version = attrs.getValue("http://www.jboss.org/drools", "version");
/*     */ 
/* 135 */     RuleFlowProcess process = new RuleFlowProcess();
/* 136 */     process.setAutoComplete(true);
/* 137 */     process.setId(id);
/* 138 */     if (name == null) {
/* 139 */       name = id;
/*     */     }
/* 141 */     process.setName(name);
/* 142 */     process.setType("RuleFlow");
/* 143 */     if (packageName == null) {
/* 144 */       packageName = "org.drools.bpmn2";
/*     */     }
/* 146 */     process.setPackageName(packageName);
/* 147 */     if ("true".equals(dynamic)) {
/* 148 */       process.setDynamic(true);
/* 149 */       process.setAutoComplete(false);
/*     */     }
/* 151 */     if (version != null) {
/* 152 */       process.setVersion(version);
/*     */     }
/*     */ 
/* 155 */     ((ProcessBuildData)parser.getData()).addProcess(process);
/*     */ 
/* 157 */     process.setMetaData("Definitions", parser.getParent());
/*     */ 
/* 159 */     Object typedImports = ((ProcessBuildData)parser.getData()).getMetaData("Bpmn2Imports");
/* 160 */     if (typedImports != null) {
/* 161 */       process.setMetaData("Bpmn2Imports", typedImports);
/*     */     }
/*     */ 
/* 164 */     Object itemDefinitions = ((ProcessBuildData)parser.getData()).getMetaData("ItemDefinitions");
/* 165 */     if (itemDefinitions != null) {
/* 166 */       process.setMetaData("ItemDefinitions", itemDefinitions);
/*     */     }
/*     */ 
/* 170 */     parser.getMetaData().put("idGen", new AtomicInteger(1));
/*     */ 
/* 172 */     return process;
/*     */   }
/*     */ 
/*     */   public Object end(String uri, String localName, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/* 178 */     parser.endElementBuilder();
/*     */ 
/* 180 */     RuleFlowProcess process = (RuleFlowProcess)parser.getCurrent();
/*     */ 
/* 182 */     List throwLinks = (List)process
/* 182 */       .getMetaData("BPMN.ThrowLinks");
/*     */ 
/* 183 */     linkIntermediateLinks(process, throwLinks);
/*     */ 
/* 185 */     List connections = (List)process.getMetaData("BPMN.Connections");
/* 186 */     linkConnections(process, connections);
/* 187 */     linkBoundaryEvents(process);
/*     */ 
/* 191 */     List associations = (List)process.getMetaData("BPMN.Associations");
/* 192 */     linkAssociations((Definitions)process.getMetaData("Definitions"), process, associations);
/*     */ 
/* 195 */     List lanes = (List)process
/* 195 */       .getMetaData("BPMN.Lanes");
/*     */ 
/* 196 */     assignLanes(process, lanes);
/* 197 */     postProcessNodes(process, process);
/* 198 */     return process;
/*     */   }
/*     */ 
/*     */   public static void linkIntermediateLinks(NodeContainer process, List<IntermediateLink> links)
/*     */   {
/* 204 */     if (null != links)
/*     */     {
/* 207 */       ArrayList<IntermediateLink> throwLinks = new ArrayList();
/* 208 */       for (IntermediateLink aLinks : links) {
/* 209 */         if (aLinks.isThrowLink()) {
/* 210 */           throwLinks.add(aLinks);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 215 */       for (IntermediateLink throwLink : throwLinks)
/*     */       {
/* 217 */         ArrayList<IntermediateLink> linksWithSharedNames = new ArrayList();
					IntermediateLink aLink;
/* 218 */         for (Iterator localIterator2 = links.iterator(); localIterator2.hasNext(); ) { 
						aLink = (IntermediateLink)localIterator2.next();
/* 219 */           if (throwLink.getName().equals(aLink.getName()))
/* 220 */             linksWithSharedNames.add(aLink);
/*     */         }
/*     */         
/* 224 */         if (linksWithSharedNames.size() < 2) {
/* 225 */           throw new IllegalArgumentException("There should be at least 2 link events to make a connection");
/*     */         }
/*     */ 
/* 229 */         linksWithSharedNames.remove(throwLink);
/*     */ 
/* 232 */         Node t = findNodeByIdOrUniqueIdInMetadata(process, throwLink
/* 233 */           .getUniqueId());
/*     */ 
/* 236 */         for (IntermediateLink catchLink : linksWithSharedNames)
/*     */         {
/* 238 */           Node c = findNodeByIdOrUniqueIdInMetadata(process, catchLink
/* 239 */             .getUniqueId());
/* 240 */           if ((t != null) && (c != null)) {
/* 241 */             org.jbpm.workflow.core.Connection result = new ConnectionImpl(t, "DROOLS_DEFAULT", c, "DROOLS_DEFAULT");
/*     */ 
/* 244 */             result.setMetaData("linkNodeHidden", "yes");
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 249 */         links.remove(throwLink);
/* 250 */         links.removeAll(linksWithSharedNames);
/*     */       }
/*     */ 
/* 253 */       if (links.size() > 0)
/* 254 */         throw new IllegalArgumentException(new StringBuilder().append(links.size()).append(" links were not processed").toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Object findNodeOrDataStoreByUniqueId(Definitions definitions, NodeContainer nodeContainer, String nodeRef, String errorMsg)
/*     */   {
/* 262 */     if (definitions != null) {
/* 263 */       List<DataStore> dataStores = definitions.getDataStores();
/* 264 */       if (dataStores != null) {
/* 265 */         for (DataStore dataStore : dataStores) {
/* 266 */           if (nodeRef.equals(dataStore.getId())) {
/* 267 */             return dataStore;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 272 */     return findNodeByIdOrUniqueIdInMetadata(nodeContainer, nodeRef, errorMsg);
/*     */   }
/*     */ 
/*     */   private static Node findNodeByIdOrUniqueIdInMetadata(NodeContainer nodeContainer, String targetRef)
/*     */   {
/* 277 */     return findNodeByIdOrUniqueIdInMetadata(nodeContainer, targetRef, new StringBuilder().append("Could not find target node for connection:").append(targetRef).toString());
/*     */   }
/*     */ 
/*     */   private static Node findNodeByIdOrUniqueIdInMetadata(NodeContainer nodeContainer, String nodeRef, String errorMsg) {
/* 281 */     Node node = null;
/*     */ 
/* 283 */     for (Node containerNode : nodeContainer.getNodes()) {
/* 284 */       if (nodeRef.equals(containerNode.getMetaData().get("UniqueId"))) {
/* 285 */         node = containerNode;
/* 286 */         break;
/*     */       }
/*     */     }
/* 289 */     if (node == null) {
/* 290 */       throw new IllegalArgumentException(errorMsg);
/*     */     }
/* 292 */     return node;
/*     */   }
/*     */ 
/*     */   public Class<?> generateNodeFor() {
/* 296 */     return RuleFlowProcess.class;
/*     */   }
/*     */ 
/*     */   public static void linkConnections(NodeContainer nodeContainer, List<SequenceFlow> connections) {
/* 300 */     if (connections != null)
/* 301 */       for (SequenceFlow connection : connections) {
/* 302 */         String sourceRef = connection.getSourceRef();
/* 303 */         Node source = findNodeByIdOrUniqueIdInMetadata(nodeContainer, sourceRef, new StringBuilder().append("Could not find source node for connection:").append(sourceRef).toString());
/*     */ 
/* 305 */         if ((source instanceof EventNode)) {
/* 306 */           for (EventFilter eventFilter : ((EventNode)source).getEventFilters()) {
/* 307 */             if (((eventFilter instanceof EventTypeFilter)) && 
/* 308 */               ("Compensation".equals(((EventTypeFilter)eventFilter).getType())))
/*     */             {
/* 311 */               throw new IllegalArgumentException("A Compensation Boundary Event can only be *associated* with a compensation activity via an Association, not via a Sequence Flow element.");
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 318 */         String targetRef = connection.getTargetRef();
/* 319 */         Node target = findNodeByIdOrUniqueIdInMetadata(nodeContainer, targetRef, new StringBuilder().append("Could not find target node for connection:").append(targetRef).toString());
/*     */ 
/* 322 */         org.jbpm.workflow.core.Connection result = new ConnectionImpl(source, "DROOLS_DEFAULT", target, "DROOLS_DEFAULT");
/*     */ 
/* 325 */         result.setMetaData("bendpoints", connection.getBendpoints());
/* 326 */         result.setMetaData("UniqueId", connection.getId());
/*     */ 
/* 328 */         if ("true".equals(System.getProperty("jbpm.enable.multi.con"))) {
/* 329 */           NodeImpl nodeImpl = (NodeImpl)source;
/* 330 */           Constraint constraint = buildConstraint(connection, nodeImpl);
/* 331 */           if (constraint != null) {
/* 332 */             nodeImpl.addConstraint(new ConnectionRef(target.getId(), "DROOLS_DEFAULT"), constraint);
/*     */           }
/*     */ 
/*     */         }
/* 336 */         else if ((source instanceof Split)) {
/* 337 */           Split split = (Split)source;
/* 338 */           Constraint constraint = buildConstraint(connection, split);
/* 339 */           split.addConstraint(
/* 340 */             new ConnectionRef(target
/* 340 */             .getId(), "DROOLS_DEFAULT"), constraint);
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   public static void linkBoundaryEvents(NodeContainer nodeContainer)
/*     */   {
/*     */     String attachedTo;
/* 349 */     for (Node node : nodeContainer.getNodes())
/* 350 */       if ((node instanceof EventNode)) {
/* 351 */         attachedTo = (String)node.getMetaData().get("AttachedTo");
/* 352 */         if (attachedTo != null)
/* 353 */           for (EventFilter filter : ((EventNode)node).getEventFilters()) {
/* 354 */             String type = ((EventTypeFilter)filter).getType();
/* 355 */             Node attachedNode = findNodeByIdOrUniqueIdInMetadata(nodeContainer, attachedTo, new StringBuilder().append("Could not find node to attach to: ").append(attachedTo).toString());
/*     */ 
/* 358 */             if ((!(attachedNode instanceof StateBasedNode)) && (!type.equals("Compensation")))
/*     */             {
/* 360 */               throw new IllegalArgumentException(new StringBuilder().append("Boundary events are supported only on StateBasedNode, found node: ")
/* 360 */                 .append(attachedNode
/* 360 */                 .getClass().getName()).append(" [").append(attachedNode.getMetaData().get("UniqueId")).append("]").toString());
/*     */             }
/*     */ 
/* 363 */             if (type.startsWith("Escalation"))
/* 364 */               linkBoundaryEscalationEvent(nodeContainer, node, attachedTo, attachedNode);
/* 365 */             else if (type.startsWith("Error-"))
/* 366 */               linkBoundaryErrorEvent(nodeContainer, node, attachedTo, attachedNode);
/* 367 */             else if (type.startsWith("Timer-"))
/* 368 */               linkBoundaryTimerEvent(nodeContainer, node, attachedTo, attachedNode);
/* 369 */             else if (type.equals("Compensation"))
/* 370 */               linkBoundaryCompensationEvent(nodeContainer, node, attachedTo, attachedNode);
/* 371 */             else if ((node.getMetaData().get("SignalName") != null) || (type.startsWith("Message-")))
/* 372 */               linkBoundarySignalEvent(nodeContainer, node, attachedTo, attachedNode);
/* 373 */             else if (type.startsWith("Condition-"))
/* 374 */               linkBoundaryConditionEvent(nodeContainer, node, attachedTo, attachedNode);
/*     */           }
/*     */       }
/*     */   }
/*     */ 
/*     */   private static void linkBoundaryEscalationEvent(NodeContainer nodeContainer, Node node, String attachedTo, Node attachedNode)
/*     */   {
/* 383 */     boolean cancelActivity = ((Boolean)node.getMetaData().get("CancelActivity")).booleanValue();
/* 384 */     String escalationCode = (String)node.getMetaData().get("EscalationEvent");
/* 385 */     String escalationStructureRef = (String)node.getMetaData().get("EscalationStructureRef");
/*     */ 
/* 387 */     ContextContainer compositeNode = (ContextContainer)attachedNode;
/*     */ 
/* 389 */     ExceptionScope exceptionScope = (ExceptionScope)compositeNode
/* 389 */       .getDefaultContext("ExceptionScope");
/*     */ 
/* 390 */     if (exceptionScope == null) {
/* 391 */       exceptionScope = new ExceptionScope();
/* 392 */       compositeNode.addContext(exceptionScope);
/* 393 */       compositeNode.setDefaultContext(exceptionScope);
/*     */     }
/*     */ 
/* 396 */     String variable = ((EventNode)node).getVariableName();
/* 397 */     ActionExceptionHandler exceptionHandler = new ActionExceptionHandler();
/* 398 */     DroolsConsequenceAction action = new DroolsConsequenceAction("java", new StringBuilder().append("kcontext.getProcessInstance().signalEvent(\"Escalation-").append(attachedTo).append("-").append(escalationCode).append("\", kcontext.getVariable(\"").append(variable).append("\"));").toString());
/*     */ 
/* 401 */     exceptionHandler.setAction(action);
/* 402 */     exceptionHandler.setFaultVariable(variable);
/* 403 */     exceptionScope.setExceptionHandler(escalationCode, exceptionHandler);
/* 404 */     if (escalationStructureRef != null) {
/* 405 */       exceptionScope.setExceptionHandler(escalationStructureRef, exceptionHandler);
/*     */     }
/*     */ 
/* 408 */     if (cancelActivity) {
/* 409 */       List actions = ((EventNode)node).getActions("onExit");
/* 410 */       if (actions == null) {
/* 411 */         actions = new ArrayList();
/*     */       }
/* 413 */       DroolsConsequenceAction cancelAction = new DroolsConsequenceAction("java", null);
/* 414 */       cancelAction.setMetaData("Action", new CancelNodeInstanceAction(attachedTo));
/* 415 */       actions.add(cancelAction);
/* 416 */       ((EventNode)node).setActions("onExit", actions);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void linkBoundaryErrorEvent(NodeContainer nodeContainer, Node node, String attachedTo, Node attachedNode) {
/* 421 */     ContextContainer compositeNode = (ContextContainer)attachedNode;
/*     */ 
/* 423 */     ExceptionScope exceptionScope = (ExceptionScope)compositeNode
/* 423 */       .getDefaultContext("ExceptionScope");
/*     */ 
/* 424 */     if (exceptionScope == null) {
/* 425 */       exceptionScope = new ExceptionScope();
/* 426 */       compositeNode.addContext(exceptionScope);
/* 427 */       compositeNode.setDefaultContext(exceptionScope);
/*     */     }
/* 429 */     String errorCode = (String)node.getMetaData().get("ErrorEvent");
/* 430 */     boolean hasErrorCode = ((Boolean)node.getMetaData().get("HasErrorEvent")).booleanValue();
/* 431 */     String errorStructureRef = (String)node.getMetaData().get("ErrorStructureRef");
/* 432 */     ActionExceptionHandler exceptionHandler = new ActionExceptionHandler();
/*     */ 
/* 434 */     String variable = ((EventNode)node).getVariableName();
/*     */ 
/* 436 */     DroolsConsequenceAction action = new DroolsConsequenceAction("java", new StringBuilder().append("kcontext.getProcessInstance().signalEvent(\"Error-").append(attachedTo).append("-").append(errorCode).append("\", kcontext.getVariable(\"").append(variable).append("\"));").toString());
/*     */ 
/* 439 */     exceptionHandler.setAction(action);
/* 440 */     exceptionHandler.setFaultVariable(variable);
/* 441 */     exceptionScope.setExceptionHandler(hasErrorCode ? errorCode : null, exceptionHandler);
/* 442 */     if (errorStructureRef != null) {
/* 443 */       exceptionScope.setExceptionHandler(errorStructureRef, exceptionHandler);
/*     */     }
/*     */ 
/* 446 */     List actions = ((EventNode)node).getActions("onExit");
/* 447 */     if (actions == null) {
/* 448 */       actions = new ArrayList();
/*     */     }
/* 450 */     DroolsConsequenceAction cancelAction = new DroolsConsequenceAction("java", null);
/* 451 */     cancelAction.setMetaData("Action", new CancelNodeInstanceAction(attachedTo));
/* 452 */     actions.add(cancelAction);
/* 453 */     ((EventNode)node).setActions("onExit", actions);
/*     */   }
/*     */ 
/*     */   private static void linkBoundaryTimerEvent(NodeContainer nodeContainer, Node node, String attachedTo, Node attachedNode) {
/* 457 */     boolean cancelActivity = ((Boolean)node.getMetaData().get("CancelActivity")).booleanValue();
/* 458 */     StateBasedNode compositeNode = (StateBasedNode)attachedNode;
/* 459 */     String timeDuration = (String)node.getMetaData().get("TimeDuration");
/* 460 */     String timeCycle = (String)node.getMetaData().get("TimeCycle");
/* 461 */     String timeDate = (String)node.getMetaData().get("TimeDate");
/* 462 */     Timer timer = new Timer();
/* 463 */     if (timeDuration != null) {
/* 464 */       timer.setDelay(timeDuration);
/* 465 */       timer.setTimeType(1);
/* 466 */       compositeNode.addTimer(timer, 
/* 467 */         new DroolsConsequenceAction("java", new StringBuilder().append("kcontext.getProcessInstance().signalEvent(\"Timer-").append(attachedTo).append("-").append(timeDuration).append("-")
/* 467 */         .append(node
/* 467 */         .getId()).append("\", kcontext.getNodeInstance().getId());").toString()));
/* 468 */     } else if (timeCycle != null) {
/* 469 */       int index = timeCycle.indexOf("###");
/* 470 */       if (index != -1) {
/* 471 */         String period = timeCycle.substring(index + 3);
/* 472 */         timeCycle = timeCycle.substring(0, index);
/* 473 */         timer.setPeriod(period);
/*     */       }
/* 475 */       timer.setDelay(timeCycle);
/* 476 */       timer.setTimeType(2);
/* 477 */       compositeNode.addTimer(timer, 
/* 478 */         new DroolsConsequenceAction("java", new StringBuilder().append("kcontext.getProcessInstance().signalEvent(\"Timer-").append(attachedTo).append("-").append(timeCycle)
/* 478 */         .append(timer
/* 478 */         .getPeriod() == null ? "" : new StringBuilder().append("###").append(timer.getPeriod()).toString()).append("-").append(node.getId()).append("\", kcontext.getNodeInstance().getId());").toString()));
/* 479 */     } else if (timeDate != null) {
/* 480 */       timer.setDate(timeDate);
/* 481 */       timer.setTimeType(3);
/* 482 */       compositeNode.addTimer(timer, 
/* 483 */         new DroolsConsequenceAction("java", new StringBuilder().append("kcontext.getProcessInstance().signalEvent(\"Timer-").append(attachedTo).append("-").append(timeDate).append("-")
/* 483 */         .append(node
/* 483 */         .getId()).append("\", kcontext.getNodeInstance().getId());").toString()));
/*     */     }
/*     */ 
/* 486 */     if (cancelActivity) {
/* 487 */       List actions = ((EventNode)node).getActions("onExit");
/* 488 */       if (actions == null) {
/* 489 */         actions = new ArrayList();
/*     */       }
/* 491 */       DroolsConsequenceAction cancelAction = new DroolsConsequenceAction("java", null);
/* 492 */       cancelAction.setMetaData("Action", new CancelNodeInstanceAction(attachedTo));
/* 493 */       actions.add(cancelAction);
/* 494 */       ((EventNode)node).setActions("onExit", actions);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void linkBoundaryCompensationEvent(NodeContainer nodeContainer, Node node, String attachedTo, Node attachedNode)
/*     */   {
/* 509 */     String activityRef = (String)node.getMetaData().get("ActivityRef");
/* 510 */     if (activityRef != null)
/* 511 */       logger.warn("Attribute activityRef={} will be IGNORED since this is a Boundary Compensation Event.", activityRef);
/*     */   }
/*     */ 
/*     */   private static void linkBoundarySignalEvent(NodeContainer nodeContainer, Node node, String attachedTo, Node attachedNode)
/*     */   {
/* 518 */     boolean cancelActivity = ((Boolean)node.getMetaData().get("CancelActivity")).booleanValue();
/* 519 */     if (cancelActivity) {
/* 520 */       List actions = ((EventNode)node).getActions("onExit");
/* 521 */       if (actions == null) {
/* 522 */         actions = new ArrayList();
/*     */       }
/* 524 */       DroolsConsequenceAction action = new DroolsConsequenceAction("java", null);
/* 525 */       action.setMetaData("Action", new CancelNodeInstanceAction(attachedTo));
/* 526 */       actions.add(action);
/* 527 */       ((EventNode)node).setActions("onExit", actions);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void linkBoundaryConditionEvent(NodeContainer nodeContainer, Node node, String attachedTo, Node attachedNode) {
/* 532 */     String processId = ((RuleFlowProcess)nodeContainer).getId();
/* 533 */     String eventType = new StringBuilder().append("RuleFlowStateEvent-").append(processId).append("-").append(((EventNode)node).getUniqueId()).append("-").append(attachedTo).toString();
/* 534 */     ((EventTypeFilter)((EventNode)node).getEventFilters().get(0)).setType(eventType);
/* 535 */     boolean cancelActivity = ((Boolean)node.getMetaData().get("CancelActivity")).booleanValue();
/* 536 */     if (cancelActivity) {
/* 537 */       List actions = ((EventNode)node).getActions("onExit");
/* 538 */       if (actions == null) {
/* 539 */         actions = new ArrayList();
/*     */       }
/* 541 */       DroolsConsequenceAction action = new DroolsConsequenceAction("java", null);
/* 542 */       action.setMetaData("Action", new CancelNodeInstanceAction(attachedTo));
/* 543 */       actions.add(action);
/* 544 */       ((EventNode)node).setActions("onExit", actions);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void linkAssociations(Definitions definitions, NodeContainer nodeContainer, List<Association> associations) {
/* 549 */     if (associations != null)
/* 550 */       for (Association association : associations) {
/* 551 */         String sourceRef = association.getSourceRef();
/* 552 */         Object source = null;
/*     */         try {
/* 554 */           source = findNodeOrDataStoreByUniqueId(definitions, nodeContainer, sourceRef, new StringBuilder().append("Could not find source [").append(sourceRef).append("] for association ")
/* 555 */             .append(association
/* 555 */             .getId()).append("]").toString());
/*     */         }
/*     */         catch (IllegalArgumentException localIllegalArgumentException) {
/*     */         }
/* 559 */         String targetRef = association.getTargetRef();
/* 560 */         Object target = null;
/*     */         try {
/* 562 */           target = findNodeOrDataStoreByUniqueId(definitions, nodeContainer, targetRef, new StringBuilder().append("Could not find target [").append(targetRef).append("] for association [")
/* 563 */             .append(association
/* 563 */             .getId()).append("]").toString());
/*     */         }
/*     */         catch (IllegalArgumentException localIllegalArgumentException1) {
/*     */         }
/* 567 */         if ((source != null) && (target != null))
/*     */         {
/* 569 */           if ((!(target instanceof DataStore)) && (!(source instanceof DataStore)))
/*     */           {
/* 571 */             if ((source instanceof EventNode)) {
/* 572 */               EventNode sourceNode = (EventNode)source;
/* 573 */               Node targetNode = (Node)target;
/* 574 */               checkBoundaryEventCompensationHandler(association, sourceNode, targetNode);
/*     */ 
/* 577 */               NodeImpl targetNodeImpl = (NodeImpl)target;
/* 578 */               String isForCompensation = "isForCompensation";
/* 579 */               Object compensationObject = targetNodeImpl.getMetaData(isForCompensation);
/* 580 */               if (compensationObject == null) {
/* 581 */                 targetNodeImpl.setMetaData(isForCompensation, Boolean.valueOf(true));
/* 582 */                 logger.warn("Setting {} attribute to true for node {}", isForCompensation, targetRef);
/* 583 */               } else if (!Boolean.parseBoolean(compensationObject.toString())) {
/* 584 */                 throw new IllegalArgumentException(new StringBuilder().append(isForCompensation).append(" attribute [").append(compensationObject).append("] should be true for Compensation Activity [").append(targetRef).append("]").toString());
/*     */               }
/*     */ 
/* 588 */               NodeContainer sourceParent = sourceNode.getNodeContainer();
/* 589 */               NodeContainer targetParent = targetNode.getNodeContainer();
/* 590 */               if (!sourceParent.equals(targetParent)) {
/* 591 */                 throw new IllegalArgumentException("Compensation Associations may not cross (sub-)process boundaries,");
/*     */               }
/*     */ 
/* 595 */               ConnectionImpl connection = new ConnectionImpl(sourceNode, "DROOLS_DEFAULT", targetNode, "DROOLS_DEFAULT");
/* 596 */               connection.setMetaData("UniqueId", null);
/* 597 */               connection.setMetaData("hidden", Boolean.valueOf(true));
/* 598 */               connection.setMetaData("association", Boolean.valueOf(true));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   private static void checkBoundaryEventCompensationHandler(Association association, Node source, Node target)
/*     */   {
/* 628 */     if (!(source instanceof BoundaryEventNode))
/*     */     {
/* 630 */       throw new IllegalArgumentException(new StringBuilder().append("(Compensation) activities may only be associated with Boundary Event Nodes (not with")
/* 630 */         .append(source
/* 630 */         .getClass().getSimpleName()).append(" nodes [node ").append((String)source.getMetaData().get("UniqueId")).append("].").toString());
/*     */     }
/* 632 */     BoundaryEventNode eventNode = (BoundaryEventNode)source;
/*     */ 
/* 635 */     List<EventFilter> eventFilters = eventNode.getEventFilters();
/* 636 */     boolean compensationCheckPassed = false;
/* 637 */     if (eventFilters != null)
/* 638 */       for (EventFilter filter : eventFilters)
/* 639 */         if ((filter instanceof EventTypeFilter)) {
/* 640 */           String type = ((EventTypeFilter)filter).getType();
/* 641 */           if ((type != null) && (type.equals("Compensation")))
/* 642 */             compensationCheckPassed = true;
/*     */         }
/*     */     String type;
/* 648 */     if (!compensationCheckPassed)
/*     */     {
/* 650 */       throw new IllegalArgumentException(new StringBuilder().append("An Event [").append((String)eventNode.getMetaData("UniqueId")).append("] linked from an association [")
/* 650 */         .append(association
/* 650 */         .getId()).append("] must be a (Boundary) Compensation Event.").toString());
/*     */     }
/*     */ 
/* 666 */     String attachedToId = eventNode.getAttachedToNodeId();
/* 667 */     Node attachedToNode = null;
/*     */     
/* 668 */     for (Node node : eventNode.getNodeContainer().getNodes()) {
/* 669 */       if (attachedToId.equals(node.getMetaData().get("UniqueId"))) {
/* 670 */         attachedToNode = node;
/* 671 */         break;
/*     */       }
/*     */     }
/* 674 */     if (attachedToNode == null) {
/* 675 */       throw new IllegalArgumentException(new StringBuilder().append("Boundary Event [").append((String)eventNode.getMetaData("UniqueId")).append("] is not attached to a node [").append(attachedToId).append("] that can be found.").toString());
/*     */     }
/*     */ 
/* 678 */     if ((!(attachedToNode instanceof RuleSetNode)) && (!(attachedToNode instanceof WorkItemNode)) && (!(attachedToNode instanceof ActionNode)) && (!(attachedToNode instanceof HumanTaskNode)) && (!(attachedToNode instanceof CompositeNode)) && (!(attachedToNode instanceof SubProcessNode)))
/*     */     {
/* 684 */       throw new IllegalArgumentException(new StringBuilder().append("Compensation Boundary Event [").append((String)eventNode.getMetaData("UniqueId")).append("] must be attached to a task or sub-process.").toString());
/*     */     }
/*     */ 
/* 689 */     compensationCheckPassed = false;
/* 690 */     if (((target instanceof WorkItemNode)) || ((target instanceof HumanTaskNode)) || ((target instanceof CompositeContextNode)) || ((target instanceof SubProcessNode)))
/*     */     {
/* 692 */       compensationCheckPassed = true;
/* 693 */     } else if ((target instanceof ActionNode)) {
/* 694 */       Object nodeTypeObj = ((ActionNode)target).getMetaData("NodeType");
/* 695 */       if ((nodeTypeObj != null) && (nodeTypeObj.equals("ScriptTask"))) {
/* 696 */         compensationCheckPassed = true;
/*     */       }
/*     */     }
/* 699 */     if (!compensationCheckPassed)
/*     */     {
/* 701 */       throw new IllegalArgumentException(new StringBuilder().append("An Activity [")
/* 701 */         .append((String)((NodeImpl)target)
/* 701 */         .getMetaData("UniqueId"))
/* 701 */         .append("] associated with a Boundary Compensation Event must be a Task or a (non-Event) Sub-Process").toString());
/*     */     }
/*     */ 
/* 706 */     compensationCheckPassed = true;
/* 707 */     NodeImpl targetNode = (NodeImpl)target;
/* 708 */     Map<String, List<Connection>>  connectionsMap = targetNode.getOutgoingConnections();
/* 709 */     ConnectionImpl outgoingConnection = null;
/* 710 */     for (String connectionType : connectionsMap.keySet()) {
/* 711 */       List<Connection> connections = (List)connectionsMap.get(connectionType);
/* 712 */       if ((connections != null) && (!connections.isEmpty())) {
/* 713 */         for (Connection connection : connections) {
/* 714 */           Object hiddenObj = connection.getMetaData().get("hidden");
/* 715 */           if ((hiddenObj != null) && (((Boolean)hiddenObj).booleanValue())) {
/*     */             continue;
/*     */           }
/* 718 */           outgoingConnection = (ConnectionImpl)connection;
/* 719 */           compensationCheckPassed = false;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 724 */     if (!compensationCheckPassed)
/*     */     {
/* 728 */       throw new IllegalArgumentException(new StringBuilder().append("A Compensation Activity [")
/* 726 */         .append((String)targetNode
/* 726 */         .getMetaData("UniqueId"))
/* 726 */         .append("] may not have any outgoing connection [")
/* 728 */         .append((String)outgoingConnection
/* 728 */         .getMetaData("UniqueId"))
/* 728 */         .append("]").toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void assignLanes(RuleFlowProcess process, List<Lane> lanes) {
/* 733 */     List laneNames = new ArrayList();
/* 734 */     Map laneMapping = new HashMap();
/* 735 */     if (lanes != null)
/* 736 */       for (Lane lane : lanes) {
/* 737 */         String name = lane.getName();
/* 738 */         if (name != null) {
/* 739 */           Swimlane swimlane = new Swimlane();
/* 740 */           swimlane.setName(name);
/* 741 */           process.getSwimlaneContext().addSwimlane(swimlane);
/* 742 */           laneNames.add(name);
/* 743 */           for (String flowElementRef : lane.getFlowElements())
/* 744 */             laneMapping.put(flowElementRef, name);
/*     */         }
/*     */       }
/*     */     String name;
/* 749 */     assignLanes(process, laneMapping);
/*     */   }
/*     */ 
/*     */   private void postProcessNodes(RuleFlowProcess process, NodeContainer container) {
/* 753 */     List eventSubProcessHandlers = new ArrayList();
/* 754 */     for (Node node : container.getNodes())
/*     */     {
/*     */       StateNode stateNode;
/*     */       Constraint constraint;
/*     */       Iterator localIterator1;
/*     */       Connection connection;
/* 756 */       if ((node instanceof StateNode)) {
/* 757 */         stateNode = (StateNode)node;
/* 758 */         String condition = (String)stateNode.getMetaData("Condition");
/* 759 */         constraint = new ConstraintImpl();
/* 760 */         constraint.setConstraint(condition);
/* 761 */         constraint.setType("rule");
/* 762 */         for (localIterator1 = stateNode.getDefaultOutgoingConnections().iterator(); localIterator1.hasNext(); ) { connection = (org.kie.api.definition.process.Connection)localIterator1.next();
/* 763 */           stateNode.setConstraint(connection, constraint); }
/*     */       }
/* 765 */       else if ((node instanceof NodeContainer))
/*     */       {
/* 767 */         if ((node instanceof EventSubProcessNode)) {
/* 768 */           EventSubProcessNode eventSubProcessNode = (EventSubProcessNode)node;
/*     */ 
/* 770 */           Node[] nodes = eventSubProcessNode.getNodes();
                    int localConnection2 = nodes.length;
/* 771 */           Node[] constraint1 = nodes; 
/*     */           Node subNode;
/* 771 */           for (int conn = 0; conn < localConnection2; conn++) { 
	                 subNode = constraint1[conn];
/*     */ 
/* 773 */             if ((subNode == null) || (!(subNode instanceof StartNode))) {
/*     */               continue;
/*     */             }
/* 776 */             List<Trigger> triggers = ((StartNode)subNode).getTriggers();
/* 777 */             if (triggers == null) {
/*     */               continue;
/*     */             }
/* 780 */             for (Trigger trigger : triggers) {
/* 781 */               if ((trigger instanceof EventTrigger)) {
/* 782 */                 List<EventFilter> filters = ((EventTrigger)trigger).getEventFilters();
/*     */ 
/* 784 */                 for (EventFilter filter : filters)
/* 785 */                   if ((filter instanceof EventTypeFilter)) {
/* 786 */                     eventSubProcessNode.addEvent((EventTypeFilter)filter);
/*     */ 
/* 788 */                     String type = ((EventTypeFilter)filter).getType();
/* 789 */                     if ((type.startsWith("Error-")) || (type.startsWith("Escalation"))) {
/* 790 */                       String faultCode = (String)subNode.getMetaData().get("FaultCode");
/* 791 */                       String replaceRegExp = "Error-|Escalation-";
/* 792 */                       String signalType = type;
/*     */ 
/* 794 */                       ExceptionScope exceptionScope = (ExceptionScope)((ContextContainer)eventSubProcessNode.getNodeContainer()).getDefaultContext("ExceptionScope");
/* 795 */                       if (exceptionScope == null) {
/* 796 */                         exceptionScope = new ExceptionScope();
/* 797 */                         ((ContextContainer)eventSubProcessNode.getNodeContainer()).addContext(exceptionScope);
/* 798 */                         ((ContextContainer)eventSubProcessNode.getNodeContainer()).setDefaultContext(exceptionScope);
/*     */                       }
/* 800 */                       String faultVariable = null;
/* 801 */                       if ((trigger.getInAssociations() != null) && (!trigger.getInAssociations().isEmpty())) {
/* 802 */                         faultVariable = ((DataAssociation)trigger.getInAssociations().get(0)).getTarget();
/*     */                       }
/*     */ 
/* 805 */                       ActionExceptionHandler exceptionHandler = new ActionExceptionHandler();
/* 806 */                       DroolsConsequenceAction action = new DroolsConsequenceAction("java", new StringBuilder().append("kcontext.getProcessInstance().signalEvent(\"").append(signalType).append("\", ").append(faultVariable == null ? "null" : new StringBuilder().append("kcontext.getVariable(\"").append(faultVariable).append("\")").toString()).append(");").toString());
/*     */ 
/* 808 */                       exceptionHandler.setAction(action);
/* 809 */                       exceptionHandler.setFaultVariable(faultVariable);
/* 810 */                       if (faultCode != null) {
/* 811 */                         String trimmedType = type.replaceFirst(replaceRegExp, "");
/* 812 */                         exceptionScope.setExceptionHandler(trimmedType, exceptionHandler);
/* 813 */                         eventSubProcessHandlers.add(trimmedType);
/*     */                       } else {
/* 815 */                         exceptionScope.setExceptionHandler(faultCode, exceptionHandler);
/*     */                       }
/* 817 */                     } else if (type.equals("Compensation"))
/*     */                     {
/* 820 */                       NodeContainer subProcess = eventSubProcessNode.getNodeContainer();
/* 821 */                       Object isForCompensationObj = eventSubProcessNode.getMetaData("isForCompensation");
/* 822 */                       if (isForCompensationObj == null) {
/* 823 */                         eventSubProcessNode.setMetaData("isForCompensation", Boolean.valueOf(true));
/* 824 */                         logger.warn(new StringBuilder().append("Overriding empty or false value of \"isForCompensation\" attribute on Event Sub-Process [")
/* 825 */                           .append(eventSubProcessNode
/* 825 */                           .getMetaData("UniqueId"))
/* 825 */                           .append("] and setting it to true.").toString());
/*     */                       }
/* 827 */                       if ((subProcess instanceof RuleFlowProcess))
/*     */                       {
/* 830 */                         throw new IllegalArgumentException("Compensation Event Sub-Processes at the process level are not supported.");
/*     */                       }
/* 832 */                       NodeContainer parentSubProcess = ((Node)subProcess).getNodeContainer();
/*     */ 
/* 837 */                       String compensationHandlerId = (String)((CompositeNode)subProcess).getMetaData("UniqueId");
/* 838 */                       addCompensationScope(process, eventSubProcessNode, parentSubProcess, compensationHandlerId);
/*     */                     }
/*     */                   }
/*     */               }
/* 842 */               else if ((trigger instanceof ConstraintTrigger)) {
/* 843 */                 ConstraintTrigger constraintTrigger = (ConstraintTrigger)trigger;
/*     */ 
/* 845 */                 if (constraintTrigger.getConstraint() != null) {
/* 846 */                   String processId = ((RuleFlowProcess)container).getId();
/* 847 */                   String type = new StringBuilder().append("RuleFlowStateEventSubProcess-Event-").append(processId).append("-").append(eventSubProcessNode.getUniqueId()).toString();
/* 848 */                   EventTypeFilter eventTypeFilter = new EventTypeFilter();
/* 849 */                   eventTypeFilter.setType(type);
/* 850 */                   eventSubProcessNode.addEvent(eventTypeFilter);
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 856 */         postProcessNodes(process, (NodeContainer)node);
/* 857 */       } else if ((node instanceof EndNode)) {
/* 858 */         handleIntermediateOrEndThrowCompensationEvent((EndNode)node);
/* 859 */       } else if ((node instanceof ActionNode)) {
/* 860 */         handleIntermediateOrEndThrowCompensationEvent((ActionNode)node);
/* 861 */       } else if ((node instanceof EventNode)) {
/* 862 */         EventNode eventNode = (EventNode)node;
/* 863 */         if ((!(eventNode instanceof BoundaryEventNode)) && (eventNode.getDefaultIncomingConnections().size() == 0)) {
/* 864 */           throw new IllegalArgumentException(new StringBuilder().append("Event node '").append(node.getName()).append("' [").append(node.getId()).append("] has no incoming connection").toString());
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 870 */     for (Node node : container.getNodes())
/* 871 */       if ((node instanceof FaultNode)) {
/* 872 */         FaultNode faultNode = (FaultNode)node;
/* 873 */         if (eventSubProcessHandlers.contains(faultNode.getFaultName()))
/* 874 */           faultNode.setTerminateParent(false);
/*     */       }
/*     */   }
/*     */ 
/*     */   private void assignLanes(NodeContainer nodeContainer, Map<String, String> laneMapping)
/*     */   {
/* 881 */     for (Node node : nodeContainer.getNodes()) {
/* 882 */       String lane = null;
/* 883 */       String uniqueId = (String)node.getMetaData().get("UniqueId");
/* 884 */       if (uniqueId != null)
/* 885 */         lane = (String)laneMapping.get(uniqueId);
/*     */       else {
/* 887 */         lane = (String)laneMapping.get(XmlBPMNProcessDumper.getUniqueNodeId(node));
/*     */       }
/* 889 */       if (lane != null) {
/* 890 */         ((NodeImpl)node).setMetaData("Lane", lane);
/* 891 */         if ((node instanceof HumanTaskNode)) {
/* 892 */           ((HumanTaskNode)node).setSwimlane(lane);
/*     */         }
/*     */       }
/* 895 */       if ((node instanceof NodeContainer))
/* 896 */         assignLanes((NodeContainer)node, laneMapping);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Constraint buildConstraint(SequenceFlow connection, NodeImpl node)
/*     */   {
/* 902 */     if (connection.getExpression() == null) {
/* 903 */       return null;
/*     */     }
/*     */ 
/* 906 */     Constraint constraint = new ConstraintImpl();
/* 907 */     String defaultConnection = (String)node.getMetaData("Default");
/* 908 */     if ((defaultConnection != null) && (defaultConnection.equals(connection.getId()))) {
/* 909 */       constraint.setDefault(true);
/*     */     }
/* 911 */     if (connection.getName() != null)
/* 912 */       constraint.setName(connection.getName());
/*     */     else {
/* 914 */       constraint.setName("");
/*     */     }
/* 916 */     if (connection.getType() != null)
/* 917 */       constraint.setType(connection.getType());
/*     */     else {
/* 919 */       constraint.setType("code");
/*     */     }
/* 921 */     if (connection.getLanguage() != null) {
/* 922 */       constraint.setDialect(connection.getLanguage());
/*     */     }
/* 924 */     if (connection.getExpression() != null) {
/* 925 */       constraint.setConstraint(connection.getExpression());
/*     */     }
/* 927 */     constraint.setPriority(connection.getPriority());
/*     */ 
/* 929 */     return constraint;
/*     */   }
/*     */ 
/*     */   protected static void addCompensationScope(RuleFlowProcess process, Node node, NodeContainer parentContainer, String compensationHandlerId)
/*     */   {
/* 934 */     process.getMetaData().put("Compensation", Boolean.valueOf(true));
/*     */ 
/* 936 */     assert ((parentContainer instanceof ContextContainer)) : new StringBuilder().append("Expected parent node to be a CompositeContextNode, not a ")
/* 937 */       .append(parentContainer
/* 937 */       .getClass().getSimpleName()).toString();
/*     */ 
/* 939 */     ContextContainer contextContainer = (ContextContainer)parentContainer;
/* 940 */     CompensationScope scope = null;
/* 941 */     boolean addScope = false;
/* 942 */     if (contextContainer.getContexts("CompensationScope") == null) {
/* 943 */       addScope = true;
/*     */     } else {
/* 945 */       scope = (CompensationScope)contextContainer.getContexts("CompensationScope").get(0);
/* 946 */       if (scope == null) {
/* 947 */         addScope = true;
/*     */       }
/*     */     }
/* 950 */     if (addScope) {
/* 951 */       scope = new CompensationScope();
/* 952 */       contextContainer.addContext(scope);
/* 953 */       contextContainer.setDefaultContext(scope);
/* 954 */       scope.setContextContainer(contextContainer);
/*     */     }
/*     */ 
/* 957 */     CompensationHandler handler = new CompensationHandler();
/* 958 */     handler.setNode(node);
/* 959 */     if (scope.getExceptionHandler(compensationHandlerId) != null) {
/* 960 */       throw new IllegalArgumentException(new StringBuilder().append("More than one compensation handler per node (").append(compensationHandlerId).append(") is not supported!").toString());
/*     */     }
/*     */ 
/* 963 */     scope.setExceptionHandler(compensationHandlerId, handler);
/*     */   }
/*     */ 
/*     */   protected void handleIntermediateOrEndThrowCompensationEvent(ExtendedNodeImpl throwEventNode) {
/* 967 */     if (throwEventNode.getMetaData("compensation-activityRef") != null) {
/* 968 */       String activityRef = (String)throwEventNode.getMetaData().remove("compensation-activityRef");
/*     */ 
/* 970 */       NodeContainer nodeParent = throwEventNode.getNodeContainer();
/* 971 */       if ((nodeParent instanceof EventSubProcessNode)) {
/* 972 */         boolean compensationEventSubProcess = false;
/* 973 */         List<Trigger> startTriggers = ((EventSubProcessNode)nodeParent).findStartNode().getTriggers();
/* 974 */         for (Trigger trigger : startTriggers) {
/* 975 */           if ((trigger instanceof EventTrigger)) {
/* 976 */             for (EventFilter filter : ((EventTrigger)trigger).getEventFilters()) {
/* 977 */               if (((EventTypeFilter)filter).getType().equals("Compensation")) {
/* 978 */                 compensationEventSubProcess = true;
/* 979 */                 break ;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 984 */         label157: if (compensationEventSubProcess)
/*     */         {
/* 986 */           nodeParent = ((NodeImpl)nodeParent).getNodeContainer();
/*     */         }
/*     */       }
/*     */       String parentId;
/* 990 */       if ((nodeParent instanceof RuleFlowProcess))
/* 991 */         parentId = ((RuleFlowProcess)nodeParent).getId();
/*     */       else
/* 993 */         parentId = (String)((NodeImpl)nodeParent).getMetaData("UniqueId");
/*     */       String compensationEvent;
/* 997 */       if (activityRef.length() == 0)
/*     */       {
/* 999 */         compensationEvent = new StringBuilder().append("implicit:").append(parentId).toString();
/*     */       }
/*     */       else {
/* 1002 */         compensationEvent = activityRef;
/*     */       }
/*     */ 
/* 1005 */       DroolsConsequenceAction compensationAction = new DroolsConsequenceAction("java", new StringBuilder().append("kcontext.getProcessInstance().signalEvent(\"Compensation\", \"").append(compensationEvent).append("\");").toString());
/*     */ 
/* 1008 */       if ((throwEventNode instanceof ActionNode)) {
/* 1009 */         ((ActionNode)throwEventNode).setAction(compensationAction);
/* 1010 */       } else if ((throwEventNode instanceof EndNode)) {
/* 1011 */         List actions = new ArrayList();
/* 1012 */         actions.add(compensationAction);
/* 1013 */         ((EndNode)throwEventNode).setActions("onEntry", actions);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  91 */     logger = LoggerFactory.getLogger(ProcessHandler.class);
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.ProcessHandler
 * JD-Core Version:    0.6.0
 */