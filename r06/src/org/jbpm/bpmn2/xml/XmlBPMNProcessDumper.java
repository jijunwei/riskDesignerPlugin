/*     */ package org.jbpm.bpmn2.xml;
/*     */ 
/*     */ import java.io.StringReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;

/*     */ import org.drools.compiler.compiler.xml.XmlDumper;
/*     */ import org.drools.core.xml.Handler;
/*     */ import org.drools.core.xml.SemanticModule;
/*     */ import org.drools.core.xml.SemanticModules;
/*     */ import org.jbpm.bpmn2.core.Association;
/*     */ import org.jbpm.bpmn2.core.DataStore;
/*     */ import org.jbpm.bpmn2.core.Definitions;
/*     */ import org.jbpm.bpmn2.core.Error;
/*     */ import org.jbpm.bpmn2.core.ItemDefinition;
/*     */ import org.jbpm.compiler.xml.XmlProcessReader;
/*     */ import org.jbpm.process.core.ContextContainer;
/*     */ import org.jbpm.process.core.Work;
/*     */ import org.jbpm.process.core.context.swimlane.Swimlane;
/*     */ import org.jbpm.process.core.context.swimlane.SwimlaneContext;
/*     */ import org.jbpm.process.core.context.variable.Variable;
/*     */ import org.jbpm.process.core.context.variable.VariableScope;
/*     */ import org.jbpm.process.core.datatype.impl.type.ObjectDataType;
/*     */ import org.jbpm.process.core.event.EventTypeFilter;
/*     */ import org.jbpm.process.core.impl.ProcessImpl;
/*     */ import org.jbpm.process.core.impl.XmlProcessDumper;
/*     */ import org.jbpm.ruleflow.core.RuleFlowProcess;
/*     */ import org.jbpm.workflow.core.Constraint;
/*     */ import org.jbpm.workflow.core.impl.ConnectionImpl;
/*     */ import org.jbpm.workflow.core.impl.DroolsConsequenceAction;
/*     */ import org.jbpm.workflow.core.node.ActionNode;
/*     */ import org.jbpm.workflow.core.node.CompositeNode;
/*     */ import org.jbpm.workflow.core.node.EndNode;
/*     */ import org.jbpm.workflow.core.node.EventNode;
/*     */ import org.jbpm.workflow.core.node.EventTrigger;
/*     */ import org.jbpm.workflow.core.node.FaultNode;
/*     */ import org.jbpm.workflow.core.node.ForEachNode;
/*     */ import org.jbpm.workflow.core.node.HumanTaskNode;
/*     */ import org.jbpm.workflow.core.node.Join;
/*     */ import org.jbpm.workflow.core.node.Split;
/*     */ import org.jbpm.workflow.core.node.StartNode;
/*     */ import org.jbpm.workflow.core.node.Trigger;
/*     */ import org.jbpm.workflow.core.node.WorkItemNode;
/*     */ import org.kie.api.definition.process.Connection;
/*     */ import org.kie.api.definition.process.NodeContainer;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class XmlBPMNProcessDumper
/*     */   implements XmlProcessDumper
/*     */ {
/*     */   public static final String JAVA_LANGUAGE = "http://www.java.com/java";
/*     */   public static final String MVEL_LANGUAGE = "http://www.mvel.org/2.0";
/*     */   public static final String RULE_LANGUAGE = "http://www.jboss.org/drools/rule";
/*     */   public static final String XPATH_LANGUAGE = "http://www.w3.org/1999/XPath";
/*     */   public static final String JAVASCRIPT_LANGUAGE = "http://www.javascript.com/javascript";
/*     */   public static final int NO_META_DATA = 0;
/*     */   public static final int META_DATA_AS_NODE_PROPERTY = 1;
/*     */   public static final int META_DATA_USING_DI = 2;
/*  87 */   public static final XmlBPMNProcessDumper INSTANCE = new XmlBPMNProcessDumper();
/*     */ 
/*  89 */   private static final Logger logger = LoggerFactory.getLogger(XmlBPMNProcessDumper.class);
/*     */ 
/*  91 */   private static final String EOL = System.getProperty("line.separator");
/*     */   private SemanticModule semanticModule;
/*  94 */   private int metaDataType = 2;
/*     */   private Set<String> visitedVariables;
/*     */ 
/*     */   private XmlBPMNProcessDumper()
/*     */   {
/*  97 */     this.semanticModule = new BPMNSemanticModule();
/*     */   }
/*     */ 
/*     */   public String dump(org.kie.api.definition.process.WorkflowProcess process) {
/* 101 */     return dump(process, 2);
/*     */   }
/*     */ 
/*     */   public String dump(org.kie.api.definition.process.WorkflowProcess process, boolean includeMeta) {
/* 105 */     return dump(process, 1);
/*     */   }
/*     */ 
/*     */   public String dump(org.kie.api.definition.process.WorkflowProcess process, int metaDataType) {
/* 109 */     StringBuilder xmlDump = new StringBuilder();
/* 110 */     visitProcess(process, xmlDump, metaDataType);
/* 111 */     return xmlDump.toString();
/*     */   }
/*     */ 
/*     */   public int getMetaDataType() {
/* 115 */     return this.metaDataType;
/*     */   }
/*     */ 
/*     */   public void setMetaDataType(int metaDataType) {
/* 119 */     this.metaDataType = metaDataType;
/*     */   }
/*     */ 
/*     */   protected void visitProcess(org.kie.api.definition.process.WorkflowProcess process, StringBuilder xmlDump, int metaDataType)
/*     */   {
/* 125 */     String targetNamespace = (String)process.getMetaData().get("TargetNamespace");
/* 126 */     if (targetNamespace == null) {
/* 127 */       targetNamespace = "http://www.jboss.org/drools";
/*     */     }
/* 129 */     xmlDump.append(new StringBuilder().append("<?xml version=\"1.0\" encoding=\"UTF-8\"?> ").append(EOL).append("<definitions id=\"Definition\"").append(EOL).append("             targetNamespace=\"").append(targetNamespace).append("\"").append(EOL).append("             typeLanguage=\"http://www.java.com/javaTypes\"").append(EOL).append("             expressionLanguage=\"http://www.mvel.org/2.0\"").append(EOL).append("             xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"").append(EOL).append("             xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"").append(EOL).append("             xsi:schemaLocation=\"http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd\"").append(EOL).append("             xmlns:g=\"http://www.jboss.org/drools/flow/gpd\"").append(EOL).append(metaDataType == 2 ? new StringBuilder().append("             xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\"").append(EOL).append("             xmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\"").append(EOL).append("             xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\"").append(EOL).toString() : "").append("             xmlns:tns=\"http://www.jboss.org/drools\">").append(EOL).append(EOL).toString());
/*     */ 
/* 146 */     this.visitedVariables = new HashSet();
/*     */ 
/* 148 */     VariableScope variableScope = (VariableScope)((org.jbpm.process.core.Process)process)
/* 148 */       .getDefaultContext("VariableScope");
/*     */ 
/* 149 */     Set dumpedItemDefs = new HashSet();
/* 150 */     Map itemDefs = (Map)process.getMetaData().get("ItemDefinitions");
/*     */     Iterator localIterator1;
/* 152 */     ItemDefinition def;
				if (itemDefs != null)
/* 153 */       for (localIterator1 = itemDefs.values().iterator(); localIterator1.hasNext(); ) {
	              def = (ItemDefinition)localIterator1.next();
/* 154 */         xmlDump.append(new StringBuilder().append("  <itemDefinition id=\"")
/* 155 */           .append(replaceIllegalCharsAttribute(def
/* 155 */           .getId())).append("\" ").toString());
/* 156 */         if ((def.getStructureRef() != null) && (!"java.lang.Object".equals(def.getStructureRef()))) {
/* 157 */           xmlDump.append(new StringBuilder().append("structureRef=\"").append(replaceIllegalCharsAttribute(def.getStructureRef())).append("\" ").toString());
/*     */         }
/* 159 */         xmlDump.append(new StringBuilder().append("/>").append(EOL).toString());
/* 160 */         dumpedItemDefs.add(def.getId().intern());
/*     */       }
/*     */     
/* 164 */     visitVariableScope(variableScope, "_", xmlDump, dumpedItemDefs);
/* 165 */     visitSubVariableScopes(process.getNodes(), xmlDump, dumpedItemDefs);
/*     */ 
/* 167 */     visitInterfaces(process.getNodes(), xmlDump);
/*     */ 
/* 169 */     visitEscalations(process.getNodes(), xmlDump, new ArrayList());
/* 170 */     Definitions def1 = (Definitions)process.getMetaData().get("Definitions");
/* 171 */     visitErrors(def1, xmlDump);
/*     */ 
/* 174 */     if ((def1 != null) && (def1.getDataStores() != null)) {
/* 175 */       for (DataStore dataStore : def1.getDataStores()) {
/* 176 */         visitDataStore(dataStore, xmlDump);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 181 */     xmlDump.append("  <process processType=\"Private\" isExecutable=\"true\" ");
/* 182 */     if ((process.getId() == null) || (process.getId().trim().length() == 0)) {
/* 183 */       ((ProcessImpl)process).setId("com.sample.bpmn2");
/*     */     }
/* 185 */     xmlDump.append(new StringBuilder().append("id=\"").append(replaceIllegalCharsAttribute(process.getId())).append("\" ").toString());
/* 186 */     if (process.getName() != null) {
/* 187 */       xmlDump.append(new StringBuilder().append("name=\"").append(replaceIllegalCharsAttribute(process.getName())).append("\" ").toString());
/*     */     }
/* 189 */     String packageName = process.getPackageName();
/* 190 */     if ((packageName != null) && (!"org.drools.bpmn2".equals(packageName))) {
/* 191 */       xmlDump.append(new StringBuilder().append("tns:packageName=\"").append(replaceIllegalCharsAttribute(packageName)).append("\" ").toString());
/*     */     }
/* 193 */     if (((org.jbpm.workflow.core.WorkflowProcess)process).isDynamic()) {
/* 194 */       xmlDump.append("tns:adHoc=\"true\" ");
/*     */     }
/* 196 */     String version = process.getVersion();
/* 197 */     if ((version != null) && (!"".equals(version))) {
/* 198 */       xmlDump.append(new StringBuilder().append("tns:version=\"").append(replaceIllegalCharsAttribute(version)).append("\" ").toString());
/*     */     }
/*     */ 
/* 201 */     xmlDump.append(new StringBuilder().append(">").append(EOL).append(EOL).toString());
/* 202 */     visitHeader(process, xmlDump, metaDataType);
/*     */ 
/* 204 */     List processNodes = new ArrayList();
/* 205 */     for (org.kie.api.definition.process.Node procNode : process.getNodes()) {
/* 206 */       processNodes.add((org.jbpm.workflow.core.Node)procNode);
/*     */     }
/* 208 */     visitNodes(processNodes, xmlDump, metaDataType);
/* 209 */     visitConnections(process.getNodes(), xmlDump, metaDataType);
/*     */ 
/* 211 */     List<Association> associations = (List)process.getMetaData().get("BPMN.Associations");
/* 212 */     if (associations != null) {
/* 213 */       for (Association association : associations) {
/* 214 */         visitAssociation(association, xmlDump);
/*     */       }
/*     */     }
/*     */ 
/* 218 */     xmlDump.append(new StringBuilder().append("  </process>").append(EOL).append(EOL).toString());
/* 219 */     if (metaDataType == 2) {
/* 220 */       xmlDump.append(new StringBuilder().append("  <bpmndi:BPMNDiagram>").append(EOL).append("    <bpmndi:BPMNPlane bpmnElement=\"")
/* 222 */         .append(replaceIllegalCharsAttribute(process
/* 222 */         .getId())).append("\" >").append(EOL).toString());
/* 223 */       visitNodesDi(process.getNodes(), xmlDump);
/* 224 */       visitConnectionsDi(process.getNodes(), xmlDump);
/* 225 */       xmlDump.append(new StringBuilder().append("    </bpmndi:BPMNPlane>").append(EOL).append("  </bpmndi:BPMNDiagram>").append(EOL).append(EOL).toString());
/*     */     }
/*     */ 
/* 229 */     xmlDump.append("</definitions>");
/*     */   }
/*     */ 
/*     */   private void visitDataStore(DataStore dataStore, StringBuilder xmlDump) {
/* 233 */     String itemSubjectRef = dataStore.getItemSubjectRef();
/* 234 */     String itemDefId = itemSubjectRef.substring(itemSubjectRef.indexOf(58) + 1);
/* 235 */     xmlDump.append(new StringBuilder().append("  <itemDefinition id=\"").append(itemDefId).append("\" ").toString());
/* 236 */     if ((dataStore.getType() != null) && (!"java.lang.Object".equals(dataStore.getType().getStringType()))) {
/* 237 */       xmlDump.append(new StringBuilder().append("structureRef=\"").append(XmlDumper.replaceIllegalChars(dataStore.getType().getStringType())).append("\" ").toString());
/*     */     }
/* 239 */     xmlDump.append(new StringBuilder().append("/>").append(EOL).toString());
/*     */ 
/* 241 */     xmlDump.append(new StringBuilder().append("  <dataStore name=\"").append(XmlDumper.replaceIllegalChars(dataStore.getName())).append("\"").toString());
/* 242 */     xmlDump.append(new StringBuilder().append(" id=\"").append(XmlDumper.replaceIllegalChars(dataStore.getId())).append("\"").toString());
/* 243 */     xmlDump.append(new StringBuilder().append(" itemSubjectRef=\"").append(XmlDumper.replaceIllegalChars(dataStore.getItemSubjectRef())).append("\"").toString());
/* 244 */     xmlDump.append(new StringBuilder().append("/>").append(EOL).toString());
/*     */   }
/*     */ 
/*     */   public void visitAssociation(Association association, StringBuilder xmlDump) {
/* 248 */     xmlDump.append(new StringBuilder().append("    <association id=\"").append(association.getId()).append("\" ").toString());
/* 249 */     xmlDump.append(new StringBuilder().append(" sourceRef=\"").append(association.getSourceRef()).append("\" ").toString());
/* 250 */     xmlDump.append(new StringBuilder().append(" targetRef=\"").append(association.getTargetRef()).append("\" ").toString());
/* 251 */     xmlDump.append(new StringBuilder().append("/>").append(EOL).toString());
/*     */   }
/*     */ 
/*     */   private void visitVariableScope(VariableScope variableScope, String prefix, StringBuilder xmlDump, Set<String> dumpedItemDefs) {
/* 255 */     if ((variableScope != null) && (!variableScope.getVariables().isEmpty())) {
/* 256 */       int variablesAdded = 0;
/* 257 */       for (Variable variable : variableScope.getVariables()) {
/* 258 */         String itemDefId = (String)variable.getMetaData("ItemSubjectRef");
/* 259 */         if (itemDefId == null) {
/* 260 */           itemDefId = new StringBuilder().append(prefix).append(variable.getName()).toString();
/*     */         }
/* 262 */         if (((itemDefId != null) && (!dumpedItemDefs.add(itemDefId.intern()))) || 
/* 265 */           (!this.visitedVariables.add(variable.getName()))) {
/*     */           continue;
/*     */         }
/* 268 */         variablesAdded++;
/* 269 */         xmlDump.append(new StringBuilder().append("  <itemDefinition id=\"")
/* 270 */           .append(replaceIllegalCharsAttribute(itemDefId))
/* 270 */           .append("\" ").toString());
/* 271 */         if ((variable.getType() != null) && (!"java.lang.Object".equals(variable.getType().getStringType()))) {
/* 272 */           xmlDump.append(new StringBuilder().append("structureRef=\"").append(replaceIllegalCharsAttribute(variable.getType().getStringType())).append("\" ").toString());
/*     */         }
/* 274 */         xmlDump.append(new StringBuilder().append("/>").append(EOL).toString());
/*     */       }
/* 276 */       if (variablesAdded > 0)
/* 277 */         xmlDump.append(EOL);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void visitSubVariableScopes(org.kie.api.definition.process.Node[] nodes, StringBuilder xmlDump, Set<String> dumpedItemDefs)
/*     */   {
/* 283 */     for (org.kie.api.definition.process.Node node : nodes) {
/* 284 */       if ((node instanceof ContextContainer))
/*     */       {
/* 286 */         VariableScope variableScope = (VariableScope)((ContextContainer)node)
/* 286 */           .getDefaultContext("VariableScope");
/*     */ 
/* 287 */         if (variableScope != null) {
/* 288 */           visitVariableScope(variableScope, new StringBuilder().append(getUniqueNodeId(node)).append("-").toString(), xmlDump, dumpedItemDefs);
/*     */         }
/*     */       }
/* 291 */       if ((node instanceof NodeContainer))
/* 292 */         visitSubVariableScopes(((NodeContainer)node).getNodes(), xmlDump, dumpedItemDefs);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void visitLanes(org.kie.api.definition.process.WorkflowProcess process, StringBuilder xmlDump)
/*     */   {
/* 301 */     Collection<Swimlane> swimlanes = ((SwimlaneContext)((org.jbpm.workflow.core.WorkflowProcess)process)
/* 301 */       .getDefaultContext("SwimlaneScope"))
/* 301 */       .getSwimlanes();
/* 302 */     if (!swimlanes.isEmpty()) {
/* 303 */       xmlDump.append(new StringBuilder().append("    <laneSet>").append(EOL).toString());
/* 304 */       for (Swimlane swimlane : swimlanes) {
/* 305 */         xmlDump.append(new StringBuilder().append("      <lane name=\"").append(replaceIllegalCharsAttribute(swimlane.getName())).append("\" >").append(EOL).toString());
/* 306 */         visitLane(process, swimlane.getName(), xmlDump);
/* 307 */         xmlDump.append(new StringBuilder().append("      </lane>").append(EOL).toString());
/*     */       }
/* 309 */       xmlDump.append(new StringBuilder().append("    </laneSet>").append(EOL).toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void visitLane(NodeContainer container, String lane, StringBuilder xmlDump) {
/* 314 */     for (org.kie.api.definition.process.Node node : container.getNodes()) {
/* 315 */       if ((node instanceof HumanTaskNode)) {
/* 316 */         String swimlane = ((HumanTaskNode)node).getSwimlane();
/* 317 */         if (lane.equals(swimlane))
/* 318 */           xmlDump.append(new StringBuilder().append("        <flowNodeRef>").append(getUniqueNodeId(node)).append("</flowNodeRef>").append(EOL).toString());
/*     */       }
/*     */       else {
/* 321 */         String swimlane = (String)node.getMetaData().get("Lane");
/* 322 */         if (lane.equals(swimlane)) {
/* 323 */           xmlDump.append(new StringBuilder().append("        <flowNodeRef>").append(getUniqueNodeId(node)).append("</flowNodeRef>").append(EOL).toString());
/*     */         }
/*     */       }
/* 326 */       if ((node instanceof NodeContainer))
/* 327 */         visitLane((NodeContainer)node, lane, xmlDump);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void visitHeader(org.kie.api.definition.process.WorkflowProcess process, StringBuilder xmlDump, int metaDataType)
/*     */   {
/* 333 */     Map metaData = getMetaData(process.getMetaData());
/* 334 */     Set<String> imports = ((org.jbpm.process.core.Process)process).getImports();
/* 335 */     Map<String,String> globals = ((org.jbpm.process.core.Process)process).getGlobals();
/* 336 */     if (((imports != null) && (!imports.isEmpty())) || ((globals != null) && (globals.size() > 0)) || (!metaData.isEmpty())) {
/* 337 */       xmlDump.append(new StringBuilder().append("    <extensionElements>").append(EOL).toString());
/* 338 */       if (imports != null) {
/* 339 */         for (String s : imports) {
/* 340 */           xmlDump.append(new StringBuilder().append("     <tns:import name=\"").append(s).append("\" />").append(EOL).toString());
/*     */         }
/*     */       }
/* 343 */       if (globals != null) {
/* 344 */         for (Map.Entry global : globals.entrySet()) {
/* 345 */           xmlDump.append(new StringBuilder().append("     <tns:global identifier=\"").append((String)global.getKey()).append("\" type=\"").append((String)global.getValue()).append("\" />").append(EOL).toString());
/*     */         }
/*     */       }
/* 348 */       writeMetaData(getMetaData(process.getMetaData()), xmlDump);
/* 349 */       xmlDump.append(new StringBuilder().append("    </extensionElements>").append(EOL).toString());
/*     */     }
/*     */ 
/* 354 */     VariableScope variableScope = (VariableScope)((org.jbpm.process.core.Process)process)
/* 354 */       .getDefaultContext("VariableScope");
/*     */ 
/* 355 */     if (variableScope != null) {
/* 356 */       visitVariables(variableScope.getVariables(), xmlDump);
/*     */     }
/* 358 */     visitLanes(process, xmlDump);
/*     */   }
/*     */ 
/*     */   public static void visitVariables(List<Variable> variables, StringBuilder xmlDump) {
/* 362 */     if (!variables.isEmpty()) {
/* 363 */       xmlDump.append(new StringBuilder().append("    <!-- process variables -->").append(EOL).toString());
/* 364 */       for (Variable variable : variables) {
/* 365 */         if (variable.getMetaData("DataObject") == null) {
/* 366 */           xmlDump.append(new StringBuilder().append("    <property id=\"").append(replaceIllegalCharsAttribute(variable.getName())).append("\" ").toString());
/* 367 */           if (variable.getType() != null) {
/* 368 */             xmlDump.append(new StringBuilder().append("itemSubjectRef=\"").append(replaceIllegalCharsAttribute((String)variable.getMetaData("ItemSubjectRef"))).append("\"").toString());
/*     */           }
/*     */ 
/* 371 */           Map metaData = getMetaData(variable.getMetaData());
/* 372 */           if (metaData.isEmpty()) {
/* 373 */             xmlDump.append(new StringBuilder().append("/>").append(EOL).toString());
/*     */           } else {
/* 375 */             xmlDump.append(new StringBuilder().append(">").append(EOL).append("      <extensionElements>").append(EOL).toString());
/*     */ 
/* 377 */             writeMetaData(metaData, xmlDump);
/* 378 */             xmlDump.append(new StringBuilder().append("      </extensionElements>").append(EOL).append("    </property>").append(EOL).toString());
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 383 */       for (Variable variable : variables) {
/* 384 */         if (variable.getMetaData("DataObject") != null) {
/* 385 */           xmlDump.append(new StringBuilder().append("    <dataObject id=\"").append(replaceIllegalCharsAttribute(variable.getName())).append("\" ").toString());
/* 386 */           if (variable.getType() != null) {
/* 387 */             xmlDump.append(new StringBuilder().append("itemSubjectRef=\"_").append(replaceIllegalCharsAttribute(variable.getName())).append("\"").toString());
/*     */           }
/*     */ 
/* 390 */           Map metaData = getMetaData(variable.getMetaData());
/* 391 */           if (metaData.isEmpty()) {
/* 392 */             xmlDump.append(new StringBuilder().append("/>").append(EOL).toString());
/*     */           } else {
/* 394 */             xmlDump.append(new StringBuilder().append(">").append(EOL).append("      <extensionElements>").append(EOL).toString());
/*     */ 
/* 396 */             writeMetaData(metaData, xmlDump);
/* 397 */             xmlDump.append(new StringBuilder().append("      </extensionElements>").append(EOL).append("    </property>").append(EOL).toString());
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 402 */       xmlDump.append(EOL);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Map<String, Object> getMetaData(Map<String, Object> input) {
/* 407 */     Map metaData = new HashMap();
/* 408 */     for (Map.Entry entry : input.entrySet()) {
/* 409 */       String name = (String)entry.getKey();
/* 410 */       if ((((String)entry.getKey()).startsWith("custom")) && 
/* 411 */         ((entry
/* 411 */         .getValue() instanceof String))) {
/* 412 */         metaData.put(name, entry.getValue());
/*     */       }
/*     */     }
/* 415 */     return metaData;
/*     */   }
/*     */ 
/*     */   public static void writeMetaData(Map<String, Object> metaData, StringBuilder xmlDump) {
/* 419 */     if (!metaData.isEmpty())
/* 420 */       for (Map.Entry entry : metaData.entrySet()) {
/* 421 */         xmlDump.append(new StringBuilder().append("        <tns:metaData name=\"").append((String)entry.getKey()).append("\">").append(EOL).toString());
/* 422 */         xmlDump.append(new StringBuilder().append("          <tns:metaValue>").append(entry.getValue()).append("</tns:metaValue>").append(EOL).toString());
/* 423 */         xmlDump.append(new StringBuilder().append("        </tns:metaData>").append(EOL).toString());
/*     */       }
/*     */   }
/*     */ 
/*     */   protected void visitInterfaces(org.kie.api.definition.process.Node[] nodes, StringBuilder xmlDump)
/*     */   {
/* 429 */     for (org.kie.api.definition.process.Node node : nodes) {
/* 430 */       if ((node instanceof WorkItemNode)) {
/* 431 */         Work work = ((WorkItemNode)node).getWork();
/* 432 */         if (work != null)
/* 433 */           if ("Service Task".equals(work.getName())) {
/* 434 */             String interfaceName = (String)work.getParameter("Interface");
/* 435 */             if (interfaceName == null) {
/* 436 */               interfaceName = "";
/*     */             }
/* 438 */             String interfaceRef = (String)work.getParameter("interfaceImplementationRef");
/* 439 */             if (interfaceRef == null) {
/* 440 */               interfaceRef = "";
/*     */             }
/* 442 */             String operationName = (String)work.getParameter("Operation");
/* 443 */             if (operationName == null) {
/* 444 */               operationName = "";
/*     */             }
/* 446 */             String operationRef = (String)work.getParameter("operationImplementationRef");
/* 447 */             if (operationRef == null) {
/* 448 */               operationRef = "";
/*     */             }
/* 450 */             String parameterType = (String)work.getParameter("ParameterType");
/* 451 */             if (parameterType == null) {
/* 452 */               parameterType = "";
/*     */             }
/* 454 */             xmlDump.append(new StringBuilder().append("  <itemDefinition id=\"")
/* 455 */               .append(getUniqueNodeId(node))
/* 455 */               .append("_InMessageType\" ")
/* 456 */               .append((""
/* 456 */               .equals(parameterType)) || 
/* 456 */               ("java.lang.Object".equals(parameterType)) ? "" : new StringBuilder().append("structureRef=\"").append(parameterType).append("\" ").toString()).append("/>").append(EOL).append("  <message id=\"")
/* 459 */               .append(getUniqueNodeId(node))
/* 459 */               .append("_InMessage\" itemRef=\"").append(getUniqueNodeId(node)).append("_InMessageType\" />").append(EOL).append("  <interface id=\"")
/* 460 */               .append(getUniqueNodeId(node))
/* 460 */               .append("_ServiceInterface\" name=\"").append(interfaceName).append("\" implementationRef=\"").append(interfaceRef).append("\" >").append(EOL).append("    <operation id=\"")
/* 461 */               .append(getUniqueNodeId(node))
/* 461 */               .append("_ServiceOperation\" name=\"").append(operationName).append("\" implementationRef=\"").append(operationRef).append("\" >").append(EOL).append("      <inMessageRef>")
/* 462 */               .append(getUniqueNodeId(node))
/* 462 */               .append("_InMessage</inMessageRef>").append(EOL).append("    </operation>").append(EOL).append("  </interface>").append(EOL).append(EOL).toString());
/*     */           }
/* 465 */           else if ("Send Task".equals(work.getName())) {
/* 466 */             String messageType = (String)work.getParameter("MessageType");
/* 467 */             if (messageType == null) {
/* 468 */               messageType = "";
/*     */             }
/* 470 */             xmlDump.append(new StringBuilder().append("  <itemDefinition id=\"")
/* 471 */               .append(getUniqueNodeId(node))
/* 471 */               .append("_MessageType\" ")
/* 473 */               .append((""
/* 472 */               .equals(messageType)) || 
/* 472 */               ("java.lang.Object".equals(messageType)) ? "" : new StringBuilder().append("structureRef=\"")
/* 473 */               .append(replaceIllegalCharsAttribute(messageType))
/* 473 */               .append("\" ").toString()).append("/>").append(EOL).append("  <message id=\"")
/* 475 */               .append(getUniqueNodeId(node))
/* 475 */               .append("_Message\" itemRef=\"").append(getUniqueNodeId(node)).append("_MessageType\" />").append(EOL).append(EOL).toString());
/* 476 */           } else if ("Receive Task".equals(work.getName())) {
/* 477 */             String messageId = (String)work.getParameter("MessageId");
/* 478 */             String messageType = (String)work.getParameter("MessageType");
/* 479 */             if (messageType == null) {
/* 480 */               messageType = "";
/*     */             }
/* 482 */             xmlDump.append(new StringBuilder().append("  <itemDefinition id=\"")
/* 483 */               .append(getUniqueNodeId(node))
/* 483 */               .append("_MessageType\" ")
/* 485 */               .append((""
/* 484 */               .equals(messageType)) || 
/* 484 */               ("java.lang.Object".equals(messageType)) ? "" : new StringBuilder().append("structureRef=\"")
/* 485 */               .append(replaceIllegalCharsAttribute(messageType))
/* 485 */               .append("\" ").toString()).append("/>").append(EOL).append("  <message id=\"").append(messageId).append("\" itemRef=\"")
/* 487 */               .append(getUniqueNodeId(node))
/* 487 */               .append("_MessageType\" />").append(EOL).append(EOL).toString());
/*     */           }
/*     */       }
/* 490 */       else if ((node instanceof EndNode)) {
/* 491 */         String messageType = (String)node.getMetaData().get("MessageType");
/* 492 */         if (messageType != null)
/* 493 */           xmlDump.append(new StringBuilder().append("  <itemDefinition id=\"")
/* 494 */             .append(getUniqueNodeId(node))
/* 494 */             .append("_MessageType\" ")
/* 496 */             .append((""
/* 495 */             .equals(messageType)) || 
/* 495 */             ("java.lang.Object".equals(messageType)) ? "" : new StringBuilder().append("structureRef=\"")
/* 496 */             .append(replaceIllegalCharsAttribute(messageType))
/* 496 */             .append("\" ").toString()).append("/>").append(EOL).append("  <message id=\"")
/* 498 */             .append(getUniqueNodeId(node))
/* 498 */             .append("_Message\" itemRef=\"").append(getUniqueNodeId(node)).append("_MessageType\" />").append(EOL).append(EOL).toString());
/*     */       }
/* 500 */       else if ((node instanceof ActionNode)) {
/* 501 */         String messageType = (String)node.getMetaData().get("MessageType");
/* 502 */         if (messageType != null)
/* 503 */           xmlDump.append(new StringBuilder().append("  <itemDefinition id=\"")
/* 504 */             .append(getUniqueNodeId(node))
/* 504 */             .append("_MessageType\" ")
/* 506 */             .append((""
/* 505 */             .equals(messageType)) || 
/* 505 */             ("java.lang.Object".equals(messageType)) ? "" : new StringBuilder().append("structureRef=\"")
/* 506 */             .append(replaceIllegalCharsAttribute(messageType))
/* 506 */             .append("\" ").toString()).append("/>").append(EOL).append("  <message id=\"")
/* 508 */             .append(getUniqueNodeId(node))
/* 508 */             .append("_Message\" itemRef=\"").append(getUniqueNodeId(node)).append("_MessageType\" />").append(EOL).append(EOL).toString());
/*     */       }
/* 510 */       else if ((node instanceof EventNode)) {
/* 511 */         List filters = ((EventNode)node).getEventFilters();
/* 512 */         if (filters.size() > 0) {
/* 513 */           String messageRef = ((EventTypeFilter)filters.get(0)).getType();
/* 514 */           if (messageRef.startsWith("Message-")) {
/* 515 */             messageRef = messageRef.substring(8);
/* 516 */             String messageType = (String)node.getMetaData().get("MessageType");
/* 517 */             xmlDump.append(new StringBuilder().append("  <itemDefinition id=\"")
/* 518 */               .append(replaceIllegalCharsAttribute(messageRef))
/* 518 */               .append("Type\" ")
/* 520 */               .append((""
/* 519 */               .equals(messageType)) || 
/* 519 */               ("java.lang.Object".equals(messageType)) ? "" : new StringBuilder().append("structureRef=\"")
/* 520 */               .append(replaceIllegalCharsAttribute(messageType))
/* 520 */               .append("\" ").toString()).append("/>").append(EOL).append("  <message id=\"")
/* 522 */               .append(replaceIllegalCharsAttribute(messageRef))
/* 522 */               .append("\" itemRef=\"").append(replaceIllegalCharsAttribute(messageRef)).append("Type\" />").append(EOL).append(EOL).toString());
/*     */           }
/*     */         }
/* 525 */       } else if ((node instanceof StartNode)) {
/* 526 */         StartNode startNode = (StartNode)node;
/* 527 */         if ((startNode.getTriggers() != null) && (!startNode.getTriggers().isEmpty())) {
/* 528 */           Trigger trigger = (Trigger)startNode.getTriggers().get(0);
/* 529 */           if ((trigger instanceof EventTrigger)) {
/* 530 */             String eventType = ((EventTypeFilter)((EventTrigger)trigger).getEventFilters().get(0)).getType();
/* 531 */             if (eventType.startsWith("Message-")) {
/* 532 */               eventType = eventType.substring(8);
/* 533 */               String messageType = (String)node.getMetaData().get("MessageType");
/* 534 */               xmlDump.append(new StringBuilder().append("  <itemDefinition id=\"")
/* 535 */                 .append(replaceIllegalCharsAttribute(eventType))
/* 535 */                 .append("Type\" ")
/* 537 */                 .append((""
/* 536 */                 .equals(messageType)) || 
/* 536 */                 ("java.lang.Object".equals(messageType)) ? "" : new StringBuilder().append("structureRef=\"")
/* 537 */                 .append(replaceIllegalCharsAttribute(messageType))
/* 537 */                 .append("\" ").toString()).append("/>").append(EOL).append("  <message id=\"")
/* 539 */                 .append(replaceIllegalCharsAttribute(eventType))
/* 539 */                 .append("\" itemRef=\"").append(replaceIllegalCharsAttribute(eventType)).append("Type\" />").append(EOL).append(EOL).toString());
/*     */             }
/*     */           }
/*     */         }
/* 543 */       } else if ((node instanceof ForEachNode)) {
/* 544 */         ForEachNode forEachNode = (ForEachNode)node;
/* 545 */         String type = null;
/* 546 */         if ((forEachNode.getVariableType() instanceof ObjectDataType)) {
/* 547 */           type = ((ObjectDataType)forEachNode.getVariableType()).getClassName();
/*     */         }
/* 549 */         xmlDump.append(new StringBuilder().append("  <itemDefinition id=\"")
/* 550 */           .append(getUniqueNodeId(forEachNode))
/* 550 */           .append("_multiInstanceItemType\" ")
/* 551 */           .append((type == null) || 
/* 551 */           ("java.lang.Object"
/* 551 */           .equals(type)) ? 
/* 551 */           "" : new StringBuilder().append("structureRef=\"").append(replaceIllegalCharsAttribute(type)).append("\" ").toString()).append("/>").append(EOL).append(EOL).toString());
/*     */       }
/* 553 */       if ((node instanceof CompositeNode))
/* 554 */         visitInterfaces(((CompositeNode)node).getNodes(), xmlDump);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void visitEscalations(org.kie.api.definition.process.Node[] nodes, StringBuilder xmlDump, List<String> escalations)
/*     */   {
/* 560 */     for (org.kie.api.definition.process.Node node : nodes) {
/* 561 */       if ((node instanceof FaultNode)) {
/* 562 */         FaultNode faultNode = (FaultNode)node;
/* 563 */         if (!faultNode.isTerminateParent()) {
/* 564 */           String escalationCode = faultNode.getFaultName();
/* 565 */           if (!escalations.contains(escalationCode)) {
/* 566 */             escalations.add(escalationCode);
/* 567 */             xmlDump.append(new StringBuilder().append("  <escalation id=\"")
/* 568 */               .append(replaceIllegalCharsAttribute(escalationCode))
/* 568 */               .append("\" escalationCode=\"").append(replaceIllegalCharsAttribute(escalationCode)).append("\" />").append(EOL).toString());
/*     */           }
/*     */         }
/* 571 */       } else if ((node instanceof ActionNode)) {
/* 572 */         ActionNode actionNode = (ActionNode)node;
/* 573 */         if ((actionNode.getAction() instanceof DroolsConsequenceAction)) {
/* 574 */           DroolsConsequenceAction action = (DroolsConsequenceAction)actionNode.getAction();
/* 575 */           if (action != null) {
/* 576 */             String s = action.getConsequence();
/* 577 */             if (s.startsWith("org.drools.core.process.instance.context.exception.ExceptionScopeInstance scopeInstance = (org.drools.core.process.instance.context.exception.ExceptionScopeInstance) ((org.drools.workflow.instance.NodeInstance) kcontext.getNodeInstance()).resolveContextInstance(org.drools.core.process.core.context.exception.ExceptionScope.EXCEPTION_SCOPE, \"")) {
/* 578 */               s = s.substring(327);
/* 579 */               String type = s.substring(0, s.indexOf("\""));
/* 580 */               if (!escalations.contains(type)) {
/* 581 */                 escalations.add(type);
/* 582 */                 xmlDump.append(new StringBuilder().append("  <escalation id=\"")
/* 583 */                   .append(replaceIllegalCharsAttribute(type))
/* 583 */                   .append("\" escalationCode=\"").append(replaceIllegalCharsAttribute(type)).append("\" />").append(EOL).toString());
/*     */               }
/*     */             }
/*     */           }
/*     */         } else {
/* 588 */           logger.warn("Cannot serialize custom implementation of the Action interface to XML");
/*     */         }
/* 590 */       } else if ((node instanceof EventNode)) {
/* 591 */         EventNode eventNode = (EventNode)node;
/* 592 */         String type = (String)eventNode.getMetaData("EscalationEvent");
/* 593 */         if ((type != null) && 
/* 594 */           (!escalations.contains(type))) {
/* 595 */           escalations.add(type);
/* 596 */           xmlDump.append(new StringBuilder().append("  <escalation id=\"")
/* 597 */             .append(replaceIllegalCharsAttribute(type))
/* 597 */             .append("\" escalationCode=\"").append(replaceIllegalCharsAttribute(type)).append("\" />").append(EOL).toString());
/*     */         }
/*     */       }
/*     */ 
/* 601 */       if ((node instanceof CompositeNode))
/* 602 */         visitEscalations(((CompositeNode)node).getNodes(), xmlDump, escalations);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void visitErrors(Definitions definitions, StringBuilder xmlDump)
/*     */   {
/* 608 */     if (definitions == null) {
/* 609 */       return;
/*     */     }
/* 611 */     List<Error> errors = definitions.getErrors();
/* 612 */     if ((errors == null) || (errors.isEmpty())) {
/* 613 */       return;
/*     */     }
/* 615 */     for (Error error : errors) {
/* 616 */       String id = replaceIllegalCharsAttribute(error.getId());
/* 617 */       String code = error.getErrorCode();
/* 618 */       xmlDump.append(new StringBuilder().append("  <error id=\"").append(id).append("\"").toString());
/* 619 */       if (error.getErrorCode() != null) {
/* 620 */         code = replaceIllegalCharsAttribute(code);
/* 621 */         xmlDump.append(new StringBuilder().append(" errorCode=\"").append(code).append("\"").toString());
/*     */       }
/* 623 */       String structureRef = error.getStructureRef();
/* 624 */       if (structureRef != null) {
/* 625 */         structureRef = replaceIllegalCharsAttribute(structureRef);
/* 626 */         xmlDump.append(new StringBuilder().append(" structureRef=\"").append(structureRef).append("\"").toString());
/*     */       }
/* 628 */       xmlDump.append(new StringBuilder().append("/>").append(EOL).toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void visitNodes(List<org.jbpm.workflow.core.Node> nodes, StringBuilder xmlDump, int metaDataType) {
/* 633 */     xmlDump.append(new StringBuilder().append("    <!-- nodes -->").append(EOL).toString());
/* 634 */     for (org.kie.api.definition.process.Node node : nodes) {
/* 635 */       visitNode(node, xmlDump, metaDataType);
/*     */     }
/* 637 */     xmlDump.append(EOL);
/*     */   }
/*     */ 
/*     */   private void visitNode(org.kie.api.definition.process.Node node, StringBuilder xmlDump, int metaDataType) {
/* 641 */     Handler handler = this.semanticModule.getHandlerByClass(node.getClass());
/* 642 */     if (handler != null)
/* 643 */       ((AbstractNodeHandler)handler).writeNode((org.jbpm.workflow.core.Node)node, xmlDump, metaDataType);
/*     */     else
/* 645 */       throw new IllegalArgumentException(new StringBuilder().append("Unknown node type: ").append(node).toString());
/*     */   }
/*     */ 
/*     */   private void visitNodesDi(org.kie.api.definition.process.Node[] nodes, StringBuilder xmlDump)
/*     */   {
/* 651 */     for (org.kie.api.definition.process.Node node : nodes) {
/* 652 */       Integer x = (Integer)node.getMetaData().get("x");
/* 653 */       Integer y = (Integer)node.getMetaData().get("y");
/* 654 */       Integer width = (Integer)node.getMetaData().get("width");
/* 655 */       Integer height = (Integer)node.getMetaData().get("height");
/* 656 */       if (x == null) {
/* 657 */         x = Integer.valueOf(0);
/*     */       }
/* 659 */       if (y == null) {
/* 660 */         y = Integer.valueOf(0);
/*     */       }
/* 662 */       if (width == null) {
/* 663 */         width = Integer.valueOf(48);
/*     */       }
/* 665 */       if (height == null) {
/* 666 */         height = Integer.valueOf(48);
/*     */       }
/* 668 */       if (((node instanceof StartNode)) || ((node instanceof EndNode)) || ((node instanceof EventNode)) || ((node instanceof FaultNode))) {
/* 669 */         int offsetX = (width.intValue() - 48) / 2;
/* 670 */         width = Integer.valueOf(48);
/* 671 */         x = Integer.valueOf(x.intValue() + offsetX);
/* 672 */         int offsetY = (height.intValue() - 48) / 2;
/* 673 */         y = Integer.valueOf(y.intValue() + offsetY);
/* 674 */         height = Integer.valueOf(48);
/* 675 */       } else if (((node instanceof Join)) || ((node instanceof Split))) {
/* 676 */         int offsetX = (width.intValue() - 48) / 2;
/* 677 */         width = Integer.valueOf(48);
/* 678 */         x = Integer.valueOf(x.intValue() + offsetX);
/* 679 */         int offsetY = (height.intValue() - 48) / 2;
/* 680 */         y = Integer.valueOf(y.intValue() + offsetY);
/* 681 */         height = Integer.valueOf(48);
/*     */       }
/* 683 */       int parentOffsetX = 0;
/* 684 */       int parentOffsetY = 0;
/* 685 */       NodeContainer nodeContainer = node.getNodeContainer();
/* 686 */       while ((nodeContainer instanceof CompositeNode)) {
/* 687 */         CompositeNode parent = (CompositeNode)nodeContainer;
/* 688 */         Integer parentX = (Integer)parent.getMetaData().get("x");
/* 689 */         if (parentX != null) {
/* 690 */           parentOffsetX += parentX.intValue();
/*     */         }
/* 692 */         Integer parentY = (Integer)parent.getMetaData().get("y");
/* 693 */         if (parentY != null) {
/* 694 */           parentOffsetY += ((Integer)parent.getMetaData().get("y")).intValue();
/*     */         }
/* 696 */         nodeContainer = parent.getNodeContainer();
/*     */       }
/* 698 */       x = Integer.valueOf(x.intValue() + parentOffsetX);
/* 699 */       y = Integer.valueOf(y.intValue() + parentOffsetY);
/* 700 */       xmlDump.append(new StringBuilder().append("      <bpmndi:BPMNShape bpmnElement=\"")
/* 701 */         .append(getUniqueNodeId(node))
/* 701 */         .append("\" >").append(EOL).append("        <dc:Bounds x=\"").append(x).append("\" y=\"").append(y).append("\" width=\"").append(width).append("\" height=\"").append(height).append("\" />").append(EOL).append("      </bpmndi:BPMNShape>").append(EOL).toString());
/*     */ 
/* 705 */       if ((node instanceof CompositeNode))
/* 706 */         visitNodesDi(((CompositeNode)node).getNodes(), xmlDump);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void visitConnections(org.kie.api.definition.process.Node[] nodes, StringBuilder xmlDump, int metaDataType)
/*     */   {
/* 713 */     xmlDump.append(new StringBuilder().append("    <!-- connections -->").append(EOL).toString());
/* 714 */     List connections = new ArrayList();
/* 715 */     for (org.kie.api.definition.process.Node node : nodes) {
/* 716 */       for (List connectionList : node.getIncomingConnections().values()) {
/* 717 */         connections.addAll(connectionList);
/*     */       }
/*     */     }
/* 720 */     for (Iterator it = connections.iterator(); ((Iterator)it).hasNext(); ) { Connection connection = (Connection)((Iterator)it).next();
/* 721 */       visitConnection(connection, xmlDump, metaDataType);
/*     */     }
/* 723 */     xmlDump.append(EOL);
/*     */   }
/*     */ 
/*     */   private boolean isConnectionRepresentingLinkEvent(Connection connection) {
/* 727 */     boolean bValue = connection.getMetaData().get("linkNodeHidden") != null;
/* 728 */     return bValue;
/*     */   }
/*     */ 
/*     */   public void visitConnection(Connection connection, StringBuilder xmlDump, int metaDataType)
/*     */   {
/* 733 */     if (isConnectionRepresentingLinkEvent(connection)) {
/* 734 */       return;
/*     */     }
/*     */ 
/* 737 */     Object hidden = ((ConnectionImpl)connection).getMetaData("hidden");
/* 738 */     if ((hidden != null) && (((Boolean)hidden).booleanValue())) {
/* 739 */       return;
/*     */     }
/*     */ 
/* 742 */     xmlDump.append(new StringBuilder().append("    <sequenceFlow id=\"")
/* 743 */       .append(getUniqueNodeId(connection
/* 743 */       .getFrom())).append("-")
/* 744 */       .append(getUniqueNodeId(connection
/* 744 */       .getTo())).append("\" sourceRef=\"")
/* 745 */       .append(getUniqueNodeId(connection
/* 745 */       .getFrom())).append("\" ").toString());
/*     */ 
/* 747 */     xmlDump.append(new StringBuilder().append("targetRef=\"").append(getUniqueNodeId(connection.getTo())).append("\" ").toString());
/* 748 */     if (metaDataType == 1) {
/* 749 */       String bendpoints = (String)connection.getMetaData().get("bendpoints");
/* 750 */       if (bendpoints != null) {
/* 751 */         xmlDump.append(new StringBuilder().append("g:bendpoints=\"").append(bendpoints).append("\" ").toString());
/*     */       }
/*     */     }
/* 754 */     if ((connection.getFrom() instanceof Split)) {
/* 755 */       Split split = (Split)connection.getFrom();
/* 756 */       if ((split.getType() == 2) || (split.getType() == 3)) {
/* 757 */         Constraint constraint = split.getConstraint(connection);
/* 758 */         if (constraint == null) {
/* 759 */           xmlDump.append(new StringBuilder().append(">").append(EOL).append("      <conditionExpression xsi:type=\"tFormalExpression\" />").toString());
/*     */         }
/*     */         else {
/* 762 */           if ((constraint.getName() != null) && (constraint.getName().trim().length() > 0)) {
/* 763 */             xmlDump.append(new StringBuilder().append("name=\"").append(replaceIllegalCharsAttribute(constraint.getName())).append("\" ").toString());
/*     */           }
/* 765 */           if (constraint.getPriority() != 0) {
/* 766 */             xmlDump.append(new StringBuilder().append("tns:priority=\"").append(constraint.getPriority()).append("\" ").toString());
/*     */           }
/* 768 */           xmlDump.append(new StringBuilder().append(">").append(EOL).append("      <conditionExpression xsi:type=\"tFormalExpression\" ").toString());
/*     */ 
/* 770 */           if ("code".equals(constraint.getType())) {
/* 771 */             if ("java".equals(constraint.getDialect()))
/* 772 */               xmlDump.append("language=\"http://www.java.com/java\" ");
/* 773 */             else if ("XPath".equals(constraint.getDialect()))
/* 774 */               xmlDump.append("language=\"http://www.w3.org/1999/XPath\" ");
/* 775 */             else if ("JavaScript".equals(constraint.getDialect()))
/* 776 */               xmlDump.append("language=\"http://www.javascript.com/javascript\" ");
/*     */           }
/*     */           else {
/* 779 */             xmlDump.append("language=\"http://www.jboss.org/drools/rule\" ");
/*     */           }
/* 781 */           String constraintString = constraint.getConstraint();
/* 782 */           if (constraintString == null) {
/* 783 */             constraintString = "";
/*     */           }
/* 785 */           xmlDump.append(new StringBuilder().append(">").append(XmlDumper.replaceIllegalChars(constraintString)).append("</conditionExpression>").toString());
/*     */         }
/* 787 */         xmlDump.append(new StringBuilder().append(EOL).append("    </sequenceFlow>").append(EOL).toString());
/*     */       }
/*     */       else {
/* 790 */         xmlDump.append(new StringBuilder().append("/>").append(EOL).toString());
/*     */       }
/*     */     } else {
/* 793 */       xmlDump.append(new StringBuilder().append("/>").append(EOL).toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void visitConnectionsDi(org.kie.api.definition.process.Node[] nodes, StringBuilder xmlDump) {
/* 798 */     List connections = new ArrayList();
/* 799 */     for (org.kie.api.definition.process.Node node : nodes) {
/* 800 */       for (List connectionList : node.getIncomingConnections().values()) {
/* 801 */         connections.addAll(connectionList);
/*     */       }
/* 803 */       if ((node instanceof CompositeNode)) {
/* 804 */         visitConnectionsDi(((CompositeNode)node).getNodes(), xmlDump);
/*     */       }
/*     */     }
/* 807 */     for (Iterator it = connections.iterator(); ((Iterator)it).hasNext(); ) { Connection connection = (Connection)((Iterator)it).next();
/* 808 */       String bendpoints = (String)connection.getMetaData().get("bendpoints");
/* 809 */       xmlDump.append(new StringBuilder().append("      <bpmndi:BPMNEdge bpmnElement=\"")
/* 811 */         .append(getUniqueNodeId(connection
/* 811 */         .getFrom())).append("-").append(getUniqueNodeId(connection.getTo())).append("\" >").append(EOL).toString());
/* 812 */       Integer x = (Integer)connection.getFrom().getMetaData().get("x");
/* 813 */       if (x == null) {
/* 814 */         x = Integer.valueOf(0);
/*     */       }
/* 816 */       Integer y = (Integer)connection.getFrom().getMetaData().get("y");
/* 817 */       if (y == null) {
/* 818 */         y = Integer.valueOf(0);
/*     */       }
/* 820 */       Integer width = (Integer)connection.getFrom().getMetaData().get("width");
/* 821 */       if (width == null) {
/* 822 */         width = Integer.valueOf(40);
/*     */       }
/* 824 */       Integer height = (Integer)connection.getFrom().getMetaData().get("height");
/* 825 */       if (height == null) {
/* 826 */         height = Integer.valueOf(40);
/*     */       }
/* 828 */       xmlDump.append(new StringBuilder().append("        <di:waypoint x=\"")
/* 829 */         .append(x
/* 829 */         .intValue() + width.intValue() / 2).append("\" y=\"").append(y.intValue() + height.intValue() / 2).append("\" />").append(EOL).toString());
/* 830 */       if (bendpoints != null) {
/* 831 */         bendpoints = bendpoints.substring(1, bendpoints.length() - 1);
/* 832 */         String[] points = bendpoints.split(";");
/* 833 */         for (String point : points) {
/* 834 */           String[] coords = point.split(",");
/* 835 */           if (coords.length == 2) {
/* 836 */             xmlDump.append(new StringBuilder().append("        <di:waypoint x=\"").append(coords[0]).append("\" y=\"").append(coords[1]).append("\" />").append(EOL).toString());
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 841 */       x = (Integer)connection.getTo().getMetaData().get("x");
/* 842 */       if (x == null) {
/* 843 */         x = Integer.valueOf(0);
/*     */       }
/* 845 */       y = (Integer)connection.getTo().getMetaData().get("y");
/* 846 */       if (y == null) {
/* 847 */         y = Integer.valueOf(0);
/*     */       }
/* 849 */       width = (Integer)connection.getTo().getMetaData().get("width");
/* 850 */       if (width == null) {
/* 851 */         width = Integer.valueOf(40);
/*     */       }
/* 853 */       height = (Integer)connection.getTo().getMetaData().get("height");
/* 854 */       if (height == null) {
/* 855 */         height = Integer.valueOf(40);
/*     */       }
/* 857 */       xmlDump.append(new StringBuilder().append("        <di:waypoint x=\"")
/* 858 */         .append(x
/* 858 */         .intValue() + width.intValue() / 2).append("\" y=\"").append(y.intValue() + height.intValue() / 2).append("\" />").append(EOL).toString());
/* 859 */       xmlDump.append(new StringBuilder().append("      </bpmndi:BPMNEdge>").append(EOL).toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static String getUniqueNodeId(org.kie.api.definition.process.Node node)
/*     */   {
/* 865 */     String result = (String)node.getMetaData().get("UniqueId");
/* 866 */     if (result != null) {
/* 867 */       return result;
/*     */     }
/* 869 */     result = new StringBuilder().append(node.getId()).append("").toString();
/* 870 */     NodeContainer nodeContainer = node.getNodeContainer();
/* 871 */     while ((nodeContainer instanceof CompositeNode)) {
/* 872 */       CompositeNode composite = (CompositeNode)nodeContainer;
/* 873 */       result = new StringBuilder().append(composite.getId()).append("-").append(result).toString();
/* 874 */       nodeContainer = composite.getNodeContainer();
/*     */     }
/* 876 */     return new StringBuilder().append("_").append(result).toString();
/*     */   }
/*     */ 
/*     */   public static String replaceIllegalCharsAttribute(String code) {
/* 880 */     StringBuilder sb = new StringBuilder();
/* 881 */     if (code != null) {
/* 882 */       int n = code.length();
/* 883 */       for (int i = 0; i < n; i++) {
/* 884 */         char c = code.charAt(i);
/* 885 */         switch (c) {
/*     */         case '<':
/* 887 */           sb.append("&lt;");
/* 888 */           break;
/*     */         case '>':
/* 890 */           sb.append("&gt;");
/* 891 */           break;
/*     */         case '&':
/* 893 */           sb.append("&amp;");
/* 894 */           break;
/*     */         case '"':
/* 896 */           sb.append("&quot;");
/* 897 */           break;
/*     */         default:
/* 899 */           sb.append(c);
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 904 */       sb.append("null");
/*     */     }
/* 906 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public String dumpProcess(org.kie.api.definition.process.Process process)
/*     */   {
/* 911 */     return dump((RuleFlowProcess)process, false);
/*     */   }
/*     */ 
/*     */   public org.kie.api.definition.process.Process readProcess(String processXml)
/*     */   {
/* 916 */     SemanticModules semanticModules = new SemanticModules();
/* 917 */     semanticModules.addSemanticModule(new BPMNSemanticModule());
/* 918 */     semanticModules.addSemanticModule(new BPMNExtensionsSemanticModule());
/* 919 */     semanticModules.addSemanticModule(new BPMNDISemanticModule());
/* 920 */     XmlProcessReader xmlReader = new XmlProcessReader(semanticModules, Thread.currentThread().getContextClassLoader());
/*     */     try {
/* 922 */       List processes = xmlReader.read(new StringReader(processXml));
/* 923 */       return (org.kie.api.definition.process.Process)processes.get(0);
/*     */     } catch (Throwable t) {
/* 925 */       t.printStackTrace();
/* 926 */     }return null;
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.XmlBPMNProcessDumper
 * JD-Core Version:    0.6.0
 */