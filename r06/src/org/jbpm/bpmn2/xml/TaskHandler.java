/*     */ package org.jbpm.bpmn2.xml;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.drools.core.xml.ExtensibleXmlParser;
/*     */ import org.jbpm.bpmn2.core.ItemDefinition;
/*     */ import org.jbpm.compiler.xml.ProcessBuildData;
/*     */ import org.jbpm.process.core.Work;
/*     */ import org.jbpm.process.core.impl.DataTransformerRegistry;
/*     */ import org.jbpm.process.core.impl.WorkImpl;
/*     */ import org.jbpm.workflow.core.NodeContainer;
/*     */ import org.jbpm.workflow.core.node.Assignment;
/*     */ import org.jbpm.workflow.core.node.DataAssociation;
/*     */ import org.jbpm.workflow.core.node.ForEachNode;
/*     */ import org.jbpm.workflow.core.node.MilestoneNode;
/*     */ import org.jbpm.workflow.core.node.Transformation;
/*     */ import org.jbpm.workflow.core.node.WorkItemNode;
/*     */ import org.kie.api.runtime.process.DataTransformer;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.Text;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class TaskHandler extends AbstractNodeHandler
/*     */ {
/*  48 */   private DataTransformerRegistry transformerRegistry = DataTransformerRegistry.get();
/*     */   private Map<String, ItemDefinition> itemDefinitions;
/*  51 */   Map<String, String> dataTypeInputs = new HashMap();
/*  52 */   Map<String, String> dataTypeOutputs = new HashMap();
/*     */ 
/*     */   protected org.jbpm.workflow.core.Node createNode(Attributes attrs) {
/*  55 */     return new WorkItemNode();
/*     */   }
/*     */ 
/*     */   public Class<?> generateNodeFor() {
/*  59 */     return org.jbpm.workflow.core.Node.class;
/*     */   }
/*     */ 
/*     */   protected void handleNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser) throws SAXException
/*     */   {
/*  64 */     super.handleNode(node, element, uri, localName, parser);
/*     */ 
/*  66 */     this.itemDefinitions = ((Map)((ProcessBuildData)parser.getData()).getMetaData("ItemDefinitions"));
/*  67 */     this.dataTypeInputs.clear();
/*  68 */     this.dataTypeOutputs.clear();
/*     */ 
/*  70 */     WorkItemNode workItemNode = (WorkItemNode)node;
/*  71 */     String name = getTaskName(element);
/*  72 */     Work work = new WorkImpl();
/*  73 */     work.setName(name);
/*  74 */     workItemNode.setWork(work);
/*  75 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/*  76 */     while (xmlNode != null) {
/*  77 */       String nodeName = xmlNode.getNodeName();
/*  78 */       if ("ioSpecification".equals(nodeName))
/*  79 */         readIoSpecification(xmlNode, this.dataInputs, this.dataOutputs);
/*  80 */       else if ("dataInputAssociation".equals(nodeName))
/*  81 */         readDataInputAssociation(xmlNode, workItemNode, this.dataInputs);
/*  82 */       else if ("dataOutputAssociation".equals(nodeName)) {
/*  83 */         readDataOutputAssociation(xmlNode, workItemNode, this.dataOutputs);
/*     */       }
/*  85 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*  87 */     workItemNode.setMetaData("DataInputs", new HashMap(this.dataTypeInputs));
/*  88 */     workItemNode.setMetaData("DataOutputs", new HashMap(this.dataTypeOutputs));
/*  89 */     handleScript(workItemNode, element, "onEntry");
/*  90 */     handleScript(workItemNode, element, "onExit");
/*     */ 
/*  92 */     String compensation = element.getAttribute("isForCompensation");
/*  93 */     if (compensation != null) {
/*  94 */       boolean isForCompensation = Boolean.parseBoolean(compensation);
/*  95 */       if (isForCompensation)
/*  96 */         workItemNode.setMetaData("isForCompensation", Boolean.valueOf(isForCompensation));
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String getTaskName(Element element)
/*     */   {
/* 102 */     return element.getAttribute("taskName");
/*     */   }
/*     */ 
/*     */   protected void readIoSpecification(org.w3c.dom.Node xmlNode, Map<String, String> dataInputs, Map<String, String> dataOutputs)
/*     */   {
/* 107 */     this.dataTypeInputs.clear();
/* 108 */     this.dataTypeOutputs.clear();
/*     */ 
/* 110 */     org.w3c.dom.Node subNode = xmlNode.getFirstChild();
/* 111 */     while ((subNode instanceof Element)) {
/* 112 */       String subNodeName = subNode.getNodeName();
/* 113 */       if ("dataInput".equals(subNodeName)) {
/* 114 */         String id = ((Element)subNode).getAttribute("id");
/* 115 */         String inputName = ((Element)subNode).getAttribute("name");
/* 116 */         dataInputs.put(id, inputName);
/*     */ 
/* 118 */         String itemSubjectRef = ((Element)subNode).getAttribute("itemSubjectRef");
/* 119 */         if ((itemSubjectRef == null) || (itemSubjectRef.isEmpty())) {
/* 120 */           String dataType = ((Element)subNode).getAttribute("dtype");
/* 121 */           if ((dataType == null) || (dataType.isEmpty())) {
/* 122 */             dataType = "java.lang.String";
/*     */           }
/* 124 */           this.dataTypeInputs.put(inputName, dataType);
/* 125 */         } else if (this.itemDefinitions.get(itemSubjectRef) != null) {
/* 126 */           this.dataTypeInputs.put(inputName, ((ItemDefinition)this.itemDefinitions.get(itemSubjectRef)).getStructureRef());
/*     */         } else {
/* 128 */           this.dataTypeInputs.put(inputName, "java.lang.Object");
/*     */         }
/*     */       }
/* 131 */       if ("dataOutput".equals(subNodeName)) {
/* 132 */         String id = ((Element)subNode).getAttribute("id");
/* 133 */         String outputName = ((Element)subNode).getAttribute("name");
/* 134 */         dataOutputs.put(id, outputName);
/*     */ 
/* 136 */         String itemSubjectRef = ((Element)subNode).getAttribute("itemSubjectRef");
/*     */ 
/* 138 */         if ((itemSubjectRef == null) || (itemSubjectRef.isEmpty())) {
/* 139 */           String dataType = ((Element)subNode).getAttribute("dtype");
/* 140 */           if ((dataType == null) || (dataType.isEmpty())) {
/* 141 */             dataType = "java.lang.String";
/*     */           }
/* 143 */           this.dataTypeOutputs.put(outputName, dataType);
/* 144 */         } else if (this.itemDefinitions.get(itemSubjectRef) != null) {
/* 145 */           this.dataTypeOutputs.put(outputName, ((ItemDefinition)this.itemDefinitions.get(itemSubjectRef)).getStructureRef());
/*     */         } else {
/* 147 */           this.dataTypeOutputs.put(outputName, "java.lang.Object");
/*     */         }
/*     */       }
/* 150 */       subNode = subNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void readDataInputAssociation(org.w3c.dom.Node xmlNode, WorkItemNode workItemNode, Map<String, String> dataInputs)
/*     */   {
/* 156 */     org.w3c.dom.Node subNode = xmlNode.getFirstChild();
/* 157 */     if ("sourceRef".equals(subNode.getNodeName())) {
/* 158 */       String source = subNode.getTextContent();
/*     */ 
/* 160 */       subNode = subNode.getNextSibling();
/* 161 */       String target = subNode.getTextContent();
/*     */ 
/* 163 */       Transformation transformation = null;
/* 164 */       subNode = subNode.getNextSibling();
/* 165 */       if ((subNode != null) && ("transformation".equals(subNode.getNodeName()))) {
/* 166 */         String lang = subNode.getAttributes().getNamedItem("language").getNodeValue();
/* 167 */         String expression = subNode.getTextContent();
/*     */ 
/* 169 */         DataTransformer transformer = this.transformerRegistry.find(lang);
/* 170 */         if (transformer == null) {
/* 171 */           throw new IllegalArgumentException("No transformer registered for language " + lang);
/*     */         }
/* 173 */         transformation = new Transformation(lang, expression);
/*     */ 
/* 176 */         subNode = subNode.getNextSibling();
/*     */       }
/*     */ 
/* 179 */       List assignments = new LinkedList();
/* 180 */       while (subNode != null) {
/* 181 */         org.w3c.dom.Node ssubNode = subNode.getFirstChild();
/* 182 */         String from = ssubNode.getTextContent();
/* 183 */         String to = ssubNode.getNextSibling().getTextContent();
/* 184 */         assignments.add(new Assignment("XPath", from, to));
/* 185 */         subNode = subNode.getNextSibling();
/*     */       }
/*     */ 
/* 188 */       workItemNode.addInAssociation(
/* 190 */         new DataAssociation(source, 
/* 190 */         (String)dataInputs
/* 190 */         .get(target), 
/* 190 */         assignments, transformation));
/*     */     }
/*     */     else {
/* 193 */       String to = subNode.getTextContent();
/*     */ 
/* 195 */       subNode = subNode.getNextSibling();
/* 196 */       if (subNode != null) {
/* 197 */         org.w3c.dom.Node subSubNode = subNode.getFirstChild();
/* 198 */         NodeList nl = subSubNode.getChildNodes();
/* 199 */         if (nl.getLength() > 1)
/*     */         {
/* 201 */           workItemNode.getWork().setParameter((String)dataInputs.get(to), subSubNode.getTextContent());
/* 202 */           return;
/* 203 */         }if (nl.getLength() == 0) {
/* 204 */           return;
/*     */         }
/* 206 */         Object result = null;
/* 207 */         Object from = nl.item(0);
/* 208 */         if ((from instanceof Text)) {
/* 209 */           String text = ((Text)from).getTextContent();
/* 210 */           if ((text.startsWith("\"")) && (text.endsWith("\"")))
/* 211 */             result = text.substring(1, text.length() - 1);
/*     */           else
/* 213 */             result = text;
/*     */         }
/*     */         else {
/* 216 */           result = nl.item(0);
/*     */         }
/* 218 */         workItemNode.getWork().setParameter((String)dataInputs.get(to), result);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void readDataOutputAssociation(org.w3c.dom.Node xmlNode, WorkItemNode workItemNode, Map<String, String> dataOutputs)
/*     */   {
/* 225 */     org.w3c.dom.Node subNode = xmlNode.getFirstChild();
/* 226 */     String source = subNode.getTextContent();
/*     */ 
/* 228 */     subNode = subNode.getNextSibling();
/* 229 */     String target = subNode.getTextContent();
/*     */ 
/* 231 */     Transformation transformation = null;
/* 232 */     subNode = subNode.getNextSibling();
/* 233 */     if ((subNode != null) && ("transformation".equals(subNode.getNodeName()))) {
/* 234 */       String lang = subNode.getAttributes().getNamedItem("language").getNodeValue();
/* 235 */       String expression = subNode.getTextContent();
/* 236 */       DataTransformer transformer = this.transformerRegistry.find(lang);
/* 237 */       if (transformer == null) {
/* 238 */         throw new IllegalArgumentException("No transformer registered for language " + lang);
/*     */       }
/* 240 */       transformation = new Transformation(lang, expression, source);
/*     */ 
/* 242 */       subNode = subNode.getNextSibling();
/*     */     }
/*     */ 
/* 245 */     List assignments = new LinkedList();
/* 246 */     while (subNode != null) {
/* 247 */       org.w3c.dom.Node ssubNode = subNode.getFirstChild();
/* 248 */       String from = ssubNode.getTextContent();
/* 249 */       String to = ssubNode.getNextSibling().getTextContent();
/* 250 */       assignments.add(new Assignment("XPath", from, to));
/* 251 */       subNode = subNode.getNextSibling();
/*     */     }
/* 253 */     workItemNode.addOutAssociation(new DataAssociation((String)dataOutputs.get(source), target, assignments, transformation));
/*     */   }
/*     */ 
/*     */   public void writeNode(org.jbpm.workflow.core.Node node, StringBuilder xmlDump, int metaDataType)
/*     */   {
/* 258 */     throw new IllegalArgumentException("Writing out should be handled by the WorkItemNodeHandler");
/*     */   }
/*     */ 
/*     */   public Object end(String uri, String localName, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/* 264 */     Element element = parser.endElementBuilder();
/* 265 */     org.jbpm.workflow.core.Node node = (org.jbpm.workflow.core.Node)parser.getCurrent();
/*     */ 
/* 267 */     handleNode(node, element, uri, localName, parser);
/*     */ 
/* 269 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/* 270 */     int uniqueIdGen = 1;
/* 271 */     while (xmlNode != null) {
/* 272 */       String nodeName = xmlNode.getNodeName();
/* 273 */       if ("multiInstanceLoopCharacteristics".equals(nodeName))
/*     */       {
/* 275 */         ForEachNode forEachNode = new ForEachNode();
/* 276 */         forEachNode.setId(node.getId());
/* 277 */         String uniqueId = (String)node.getMetaData().get("UniqueId");
/* 278 */         forEachNode.setMetaData("UniqueId", uniqueId);
/* 279 */         node.setMetaData("UniqueId", uniqueId + ":" + uniqueIdGen++);
/* 280 */         forEachNode.addNode(node);
/* 281 */         forEachNode.linkIncomingConnections("DROOLS_DEFAULT", node.getId(), "DROOLS_DEFAULT");
/* 282 */         forEachNode.linkOutgoingConnections(node.getId(), "DROOLS_DEFAULT", "DROOLS_DEFAULT");
/*     */ 
/* 284 */         org.jbpm.workflow.core.Node orignalNode = node;
/* 285 */         node = forEachNode;
/* 286 */         handleForEachNode(node, element, uri, localName, parser);
/*     */ 
/* 288 */         if (!(orignalNode instanceof WorkItemNode)) break;
/* 289 */         ((WorkItemNode)orignalNode).adjustOutMapping(forEachNode.getOutputCollectionExpression()); break;
/*     */       }
/*     */ 
/* 294 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*     */ 
/* 297 */     if (((node instanceof WorkItemNode)) && (((WorkItemNode)node).getWork().getName().equals("Milestone"))) {
/* 298 */       WorkItemNode workItemNode = (WorkItemNode)node;
/*     */ 
/* 300 */       String milestoneCondition = (String)((WorkItemNode)node).getWork().getParameter("Condition");
/* 301 */       if (milestoneCondition == null) {
/* 302 */         milestoneCondition = "";
/*     */       }
/*     */ 
/* 305 */       MilestoneNode milestoneNode = new MilestoneNode();
/* 306 */       milestoneNode.setId(workItemNode.getId());
/* 307 */       milestoneNode.setConstraint(milestoneCondition);
/* 308 */       milestoneNode.setMetaData(workItemNode.getMetaData());
/* 309 */       milestoneNode.setName(workItemNode.getName());
/* 310 */       milestoneNode.setNodeContainer(workItemNode.getNodeContainer());
/*     */ 
/* 312 */       node = milestoneNode;
/*     */     }
/*     */ 
/* 315 */     NodeContainer nodeContainer = (NodeContainer)parser.getParent();
/* 316 */     nodeContainer.addNode(node);
/* 317 */     ((ProcessBuildData)parser.getData()).addNode(node);
/*     */ 
/* 319 */     return node;
/*     */   }
/*     */ 
/*     */   protected void handleForEachNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser) throws SAXException
/*     */   {
/* 324 */     ForEachNode forEachNode = (ForEachNode)node;
/* 325 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/*     */ 
/* 327 */     while (xmlNode != null) {
/* 328 */       String nodeName = xmlNode.getNodeName();
/* 329 */       if ("dataInputAssociation".equals(nodeName))
/* 330 */         readDataInputAssociation(xmlNode, this.inputAssociation);
/* 331 */       else if ("dataOutputAssociation".equals(nodeName))
/* 332 */         readDataOutputAssociation(xmlNode, this.outputAssociation);
/* 333 */       else if ("multiInstanceLoopCharacteristics".equals(nodeName)) {
/* 334 */         readMultiInstanceLoopCharacteristics(xmlNode, forEachNode, parser);
/*     */       }
/* 336 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.TaskHandler
 * JD-Core Version:    0.6.0
 */