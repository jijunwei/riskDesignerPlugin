/*     */ package org.jbpm.bpmn2.xml;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.drools.core.xml.BaseAbstractHandler;
/*     */ import org.drools.core.xml.ExtensibleXmlParser;
/*     */ import org.drools.core.xml.Handler;
/*     */ import org.jbpm.bpmn2.core.Definitions;
/*     */ import org.jbpm.bpmn2.core.Interface;
/*     */ import org.jbpm.bpmn2.core.Interface.Operation;
/*     */ import org.jbpm.bpmn2.core.ItemDefinition;
/*     */ import org.jbpm.bpmn2.core.Message;
/*     */ import org.jbpm.compiler.xml.ProcessBuildData;
/*     */ import org.jbpm.process.core.ContextContainer;
/*     */ import org.jbpm.process.core.Work;
/*     */ import org.jbpm.process.core.context.variable.Variable;
/*     */ import org.jbpm.process.core.context.variable.VariableScope;
/*     */ import org.jbpm.process.core.datatype.DataType;
/*     */ import org.jbpm.process.core.datatype.impl.type.BooleanDataType;
/*     */ import org.jbpm.process.core.datatype.impl.type.FloatDataType;
/*     */ import org.jbpm.process.core.datatype.impl.type.IntegerDataType;
/*     */ import org.jbpm.process.core.datatype.impl.type.ObjectDataType;
/*     */ import org.jbpm.process.core.datatype.impl.type.StringDataType;
/*     */ import org.jbpm.process.core.datatype.impl.type.UndefinedDataType;
/*     */ import org.jbpm.ruleflow.core.RuleFlowProcess;
/*     */ import org.jbpm.workflow.core.NodeContainer;
/*     */ import org.jbpm.workflow.core.node.ForEachNode;
/*     */ import org.jbpm.workflow.core.node.WorkItemNode;
/*     */ import org.kie.api.definition.process.Node;
/*     */ import org.kie.api.definition.process.Process;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class DefinitionsHandler extends BaseAbstractHandler
/*     */   implements Handler
/*     */ {
/*     */   public DefinitionsHandler()
/*     */   {
/*  55 */     if ((this.validParents == null) && (this.validPeers == null)) {
/*  56 */       this.validParents = new HashSet();
/*  57 */       this.validParents.add(null);
/*     */ 
/*  59 */       this.validPeers = new HashSet();
/*  60 */       this.validPeers.add(null);
/*     */ 
/*  62 */       this.allowNesting = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object start(String uri, String localName, Attributes attrs, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/*  69 */     parser.startElementBuilder(localName, attrs);
/*  70 */     return new Definitions();
/*     */   }
/*     */ 
/*     */   public Object end(String uri, String localName, ExtensibleXmlParser parser) throws SAXException
/*     */   {
/*  75 */     Element element = parser.endElementBuilder();
/*  76 */     Definitions definitions = (Definitions)parser.getCurrent();
/*  77 */     String namespace = element.getAttribute("targetNamespace");
/*  78 */     List<Process> processes = ((ProcessBuildData)parser.getData()).getProcesses();
/*     */ 
/*  80 */     Map itemDefinitions = (Map)((ProcessBuildData)parser
/*  80 */       .getData()).getMetaData("ItemDefinitions");
/*     */ 
/*  82 */     List interfaces = (List)((ProcessBuildData)parser.getData()).getMetaData("Interfaces");
/*     */ 
/*  84 */     for (Process process : processes) {
/*  85 */       RuleFlowProcess ruleFlowProcess = (RuleFlowProcess)process;
/*  86 */       ruleFlowProcess.setMetaData("TargetNamespace", namespace);
/*  87 */       postProcessItemDefinitions(ruleFlowProcess, itemDefinitions, parser.getClassLoader());
/*  88 */       postProcessInterfaces(ruleFlowProcess, interfaces);
/*     */     }
/*  90 */     definitions.setTargetNamespace(namespace);
/*  91 */     return definitions;
/*     */   }
/*     */ 
/*     */   public Class<?> generateNodeFor() {
/*  95 */     return Definitions.class;
/*     */   }
/*     */ 
/*     */   private void postProcessInterfaces(NodeContainer nodeContainer, List<Interface> interfaces)
/*     */   {
/* 100 */     for (Node node : nodeContainer.getNodes()) {
/* 101 */       if ((node instanceof NodeContainer)) {
/* 102 */         postProcessInterfaces((NodeContainer)node, interfaces);
/*     */       }
/* 104 */       if (((node instanceof WorkItemNode)) && ("Service Task".equals(((WorkItemNode)node).getMetaData("Type")))) {
/* 105 */         WorkItemNode workItemNode = (WorkItemNode)node;
/* 106 */         if (interfaces == null) {
/* 107 */           throw new IllegalArgumentException("No interfaces found");
/*     */         }
/* 109 */         String operationRef = (String)workItemNode.getMetaData("OperationRef");
/* 110 */         String implementation = (String)workItemNode.getMetaData("Implementation");
/* 111 */         Interface.Operation operation = null;
/* 112 */         for (Interface i : interfaces) {
/* 113 */           operation = i.getOperation(operationRef);
/* 114 */           if (operation != null) {
/*     */             break;
/*     */           }
/*     */         }
/* 118 */         if (operation == null) {
/* 119 */           throw new IllegalArgumentException("Could not find operation " + operationRef);
/*     */         }
/*     */ 
/* 122 */         if (workItemNode.getWork().getParameter("Interface") == null) {
/* 123 */           workItemNode.getWork().setParameter("Interface", operation.getInterface().getName());
/*     */         }
/* 125 */         if (workItemNode.getWork().getParameter("Operation") == null) {
/* 126 */           workItemNode.getWork().setParameter("Operation", operation.getName());
/*     */         }
/* 128 */         if (workItemNode.getWork().getParameter("ParameterType") == null) {
/* 129 */           workItemNode.getWork().setParameter("ParameterType", operation.getMessage().getType());
/*     */         }
/*     */ 
/* 132 */         if (implementation != null) {
/* 133 */           workItemNode.getWork().setParameter("interfaceImplementationRef", operation.getInterface().getImplementationRef());
/* 134 */           workItemNode.getWork().setParameter("operationImplementationRef", operation.getImplementationRef());
/* 135 */           workItemNode.getWork().setParameter("implementation", implementation);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void postProcessItemDefinitions(NodeContainer nodeContainer, Map<String, ItemDefinition> itemDefinitions, ClassLoader cl) {
/* 142 */     if ((nodeContainer instanceof ContextContainer)) {
/* 143 */       setVariablesDataType((ContextContainer)nodeContainer, itemDefinitions, cl);
/*     */     }
/*     */ 
/* 146 */     if ((nodeContainer instanceof ForEachNode)) {
/* 147 */       setVariablesDataType(((ForEachNode)nodeContainer).getCompositeNode(), itemDefinitions, cl);
/*     */     }
/* 149 */     for (Node node : nodeContainer.getNodes()) {
/* 150 */       if ((node instanceof NodeContainer)) {
/* 151 */         postProcessItemDefinitions((NodeContainer)node, itemDefinitions, cl);
/*     */       }
/* 153 */       if ((node instanceof ContextContainer))
/* 154 */         setVariablesDataType((ContextContainer)node, itemDefinitions, cl);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setVariablesDataType(ContextContainer container, Map<String, ItemDefinition> itemDefinitions, ClassLoader cl)
/*     */   {
/* 160 */     VariableScope variableScope = (VariableScope)container.getDefaultContext("VariableScope");
/* 161 */     if (variableScope != null)
/* 162 */       for (Variable variable : variableScope.getVariables())
/* 163 */         setVariableDataType(variable, itemDefinitions, cl);
/*     */   }
/*     */ 
/*     */   private void setVariableDataType(Variable variable, Map<String, ItemDefinition> itemDefinitions, ClassLoader cl)
/*     */   {
/* 171 */     String itemSubjectRef = (String)variable.getMetaData("ItemSubjectRef");
/* 172 */     if ((UndefinedDataType.getInstance().equals(variable.getType())) && (itemDefinitions != null) && (itemSubjectRef != null)) {
/* 173 */       DataType dataType = new ObjectDataType();
/* 174 */       ItemDefinition itemDefinition = (ItemDefinition)itemDefinitions.get(itemSubjectRef);
/* 175 */       if (itemDefinition != null) {
/* 176 */         String structureRef = itemDefinition.getStructureRef();
/*     */ 
/* 178 */         if (("java.lang.Boolean".equals(structureRef)) || ("Boolean".equals(structureRef))) {
/* 179 */           dataType = new BooleanDataType();
/*     */         }
/* 181 */         else if (("java.lang.Integer".equals(structureRef)) || ("Integer".equals(structureRef))) {
/* 182 */           dataType = new IntegerDataType();
/*     */         }
/* 184 */         else if (("java.lang.Float".equals(structureRef)) || ("Float".equals(structureRef))) {
/* 185 */           dataType = new FloatDataType();
/*     */         }
/* 187 */         else if (("java.lang.String".equals(structureRef)) || ("String".equals(structureRef))) {
/* 188 */           dataType = new StringDataType();
/*     */         }
/* 190 */         else if (("java.lang.Object".equals(structureRef)) || ("Object".equals(structureRef)))
/*     */         {
/* 192 */           dataType = new ObjectDataType("java.lang.Object");
/*     */         }
/*     */         else {
/* 195 */           dataType = new ObjectDataType(structureRef, cl);
/*     */         }
/*     */       }
/*     */ 
/* 199 */       variable.setType(dataType);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.DefinitionsHandler
 * JD-Core Version:    0.6.0
 */