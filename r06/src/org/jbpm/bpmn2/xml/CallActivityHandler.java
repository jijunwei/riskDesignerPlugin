/*     */ package org.jbpm.bpmn2.xml;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.drools.compiler.compiler.xml.XmlDumper;
/*     */ import org.drools.core.xml.ExtensibleXmlParser;
/*     */ import org.jbpm.compiler.xml.ProcessBuildData;
/*     */ import org.jbpm.process.core.impl.DataTransformerRegistry;
/*     */ import org.jbpm.workflow.core.NodeContainer;
/*     */ import org.jbpm.workflow.core.node.ForEachNode;
/*     */ import org.jbpm.workflow.core.node.SubProcessNode;
/*     */ import org.jbpm.workflow.core.node.Transformation;
/*     */ import org.kie.api.runtime.process.DataTransformer;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.Text;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class CallActivityHandler extends AbstractNodeHandler
/*     */ {
/*  42 */   private DataTransformerRegistry transformerRegistry = DataTransformerRegistry.get();
/*     */ 
/*     */   protected org.jbpm.workflow.core.Node createNode(Attributes attrs) {
/*  45 */     return new SubProcessNode();
/*     */   }
/*     */ 
/*     */   public Class generateNodeFor()
/*     */   {
/*  50 */     return SubProcessNode.class;
/*     */   }
/*     */ 
/*     */   protected void handleNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser) throws SAXException
/*     */   {
/*  55 */     super.handleNode(node, element, uri, localName, parser);
/*  56 */     SubProcessNode subProcessNode = (SubProcessNode)node;
/*  57 */     String processId = element.getAttribute("calledElement");
/*  58 */     if ((processId != null) && (processId.length() > 0)) {
/*  59 */       subProcessNode.setProcessId(processId);
/*     */     } else {
/*  61 */       String processName = element.getAttribute("calledElementByName");
/*  62 */       subProcessNode.setProcessName(processName);
/*     */     }
/*  64 */     String waitForCompletion = element.getAttribute("waitForCompletion");
/*  65 */     if ((waitForCompletion != null) && ("false".equals(waitForCompletion))) {
/*  66 */       subProcessNode.setWaitForCompletion(false);
/*     */     }
/*  68 */     String independent = element.getAttribute("independent");
/*  69 */     if ((independent != null) && ("false".equals(independent))) {
/*  70 */       subProcessNode.setIndependent(false);
/*     */     }
/*  72 */     Map dataInputs = new HashMap();
/*  73 */     Map dataOutputs = new HashMap();
/*  74 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/*  75 */     while (xmlNode != null) {
/*  76 */       String nodeName = xmlNode.getNodeName();
/*  77 */       if ("ioSpecification".equals(nodeName))
/*  78 */         readIoSpecification(xmlNode, dataInputs, dataOutputs);
/*  79 */       else if ("dataInputAssociation".equals(nodeName))
/*  80 */         readDataInputAssociation(xmlNode, subProcessNode, dataInputs);
/*  81 */       else if ("dataOutputAssociation".equals(nodeName)) {
/*  82 */         readDataOutputAssociation(xmlNode, subProcessNode, dataOutputs);
/*     */       }
/*  84 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*     */ 
/*  87 */     subProcessNode.setMetaData("DataInputs", dataInputs);
/*  88 */     subProcessNode.setMetaData("DataOutputs", dataOutputs);
/*     */ 
/*  90 */     handleScript(subProcessNode, element, "onEntry");
/*  91 */     handleScript(subProcessNode, element, "onExit");
/*     */   }
/*     */ 
/*     */   public Object end(String uri, String localName, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/*  97 */     Element element = parser.endElementBuilder();
/*  98 */     org.jbpm.workflow.core.Node node = (org.jbpm.workflow.core.Node)parser.getCurrent();
/*  99 */     handleNode(node, element, uri, localName, parser);
/*     */ 
/* 101 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/* 102 */     int uniqueIdGen = 1;
/* 103 */     while (xmlNode != null) {
/* 104 */       String nodeName = xmlNode.getNodeName();
/* 105 */       if ("multiInstanceLoopCharacteristics".equals(nodeName))
/*     */       {
/* 107 */         ForEachNode forEachNode = new ForEachNode();
/* 108 */         forEachNode.setId(node.getId());
/* 109 */         String uniqueId = (String)node.getMetaData().get("UniqueId");
/* 110 */         forEachNode.setMetaData("UniqueId", uniqueId);
/* 111 */         node.setMetaData("UniqueId", uniqueId + ":" + uniqueIdGen++);
/* 112 */         node.setMetaData("hidden", Boolean.valueOf(true));
/* 113 */         forEachNode.addNode(node);
/* 114 */         forEachNode.linkIncomingConnections("DROOLS_DEFAULT", node.getId(), "DROOLS_DEFAULT");
/* 115 */         forEachNode.linkOutgoingConnections(node.getId(), "DROOLS_DEFAULT", "DROOLS_DEFAULT");
/*     */ 
/* 117 */         org.jbpm.workflow.core.Node orignalNode = node;
/* 118 */         node = forEachNode;
/* 119 */         handleForEachNode(node, element, uri, localName, parser);
/*     */ 
/* 121 */         if ((orignalNode instanceof SubProcessNode)) {
/* 122 */           ((SubProcessNode)orignalNode).adjustOutMapping(forEachNode.getOutputCollectionExpression());
/*     */         }
/*     */ 
/* 125 */         Map dataInputs = (Map)orignalNode.getMetaData().remove("DataInputs");
/* 126 */         Map dataOutputs = (Map)orignalNode.getMetaData().remove("DataOutputs");
/*     */ 
/* 128 */         orignalNode.setMetaData("MICollectionOutput", dataOutputs.get(((ForEachNode)node).getMetaData("MICollectionOutput")));
/* 129 */         orignalNode.setMetaData("MICollectionInput", dataInputs.get(((ForEachNode)node).getMetaData("MICollectionInput")));
/*     */ 
/* 131 */         break;
/*     */       }
/* 133 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*     */ 
/* 136 */     NodeContainer nodeContainer = (NodeContainer)parser.getParent();
/* 137 */     nodeContainer.addNode(node);
/* 138 */     ((ProcessBuildData)parser.getData()).addNode(node);
/* 139 */     return node;
/*     */   }
/*     */ 
/*     */   protected void readIoSpecification(org.w3c.dom.Node xmlNode, Map<String, String> dataInputs, Map<String, String> dataOutputs) {
/* 143 */     org.w3c.dom.Node subNode = xmlNode.getFirstChild();
/* 144 */     while ((subNode instanceof Element)) {
/* 145 */       String subNodeName = subNode.getNodeName();
/* 146 */       if ("dataInput".equals(subNodeName)) {
/* 147 */         String id = ((Element)subNode).getAttribute("id");
/* 148 */         String inputName = ((Element)subNode).getAttribute("name");
/* 149 */         dataInputs.put(id, inputName);
/* 150 */       } else if ("dataOutput".equals(subNodeName)) {
/* 151 */         String id = ((Element)subNode).getAttribute("id");
/* 152 */         String outputName = ((Element)subNode).getAttribute("name");
/* 153 */         dataOutputs.put(id, outputName);
/*     */       }
/* 155 */       subNode = subNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void readDataInputAssociation(org.w3c.dom.Node xmlNode, SubProcessNode subProcessNode, Map<String, String> dataInputs)
/*     */   {
/* 162 */     org.w3c.dom.Node subNode = xmlNode.getFirstChild();
/* 163 */     if ("sourceRef".equals(subNode.getNodeName()))
/*     */     {
/* 165 */       String from = subNode.getTextContent();
/*     */ 
/* 167 */       subNode = subNode.getNextSibling();
/* 168 */       String to = subNode.getTextContent();
/*     */ 
/* 170 */       Transformation transformation = null;
/* 171 */       subNode = subNode.getNextSibling();
/* 172 */       if ((subNode != null) && ("transformation".equals(subNode.getNodeName()))) {
/* 173 */         String lang = subNode.getAttributes().getNamedItem("language").getNodeValue();
/* 174 */         String expression = subNode.getTextContent();
/*     */ 
/* 176 */         DataTransformer transformer = this.transformerRegistry.find(lang);
/* 177 */         if (transformer == null) {
/* 178 */           throw new IllegalArgumentException("No transformer registered for language " + lang);
/*     */         }
/* 180 */         transformation = new Transformation(lang, expression);
/*     */ 
/* 182 */         subNode = subNode.getNextSibling();
/*     */       }
/* 184 */       subProcessNode.addInMapping((String)dataInputs.get(to), from, transformation);
/*     */     }
/*     */     else {
/* 187 */       String to = subNode.getTextContent();
/*     */ 
/* 189 */       subNode = subNode.getNextSibling();
/* 190 */       if (subNode != null) {
/* 191 */         org.w3c.dom.Node subSubNode = subNode.getFirstChild();
/* 192 */         NodeList nl = subSubNode.getChildNodes();
/* 193 */         if (nl.getLength() > 1)
/*     */         {
/* 195 */           subProcessNode.addInMapping((String)dataInputs.get(to), subSubNode.getTextContent());
/* 196 */           return;
/* 197 */         }if (nl.getLength() == 0) {
/* 198 */           return;
/*     */         }
/* 200 */         Object result = null;
/* 201 */         Object from = nl.item(0);
/* 202 */         if ((from instanceof Text))
/* 203 */           result = ((Text)from).getTextContent();
/*     */         else {
/* 205 */           result = nl.item(0);
/*     */         }
/* 207 */         subProcessNode.addInMapping((String)dataInputs.get(to), result.toString());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void readDataOutputAssociation(org.w3c.dom.Node xmlNode, SubProcessNode subProcessNode, Map<String, String> dataOutputs)
/*     */   {
/* 215 */     org.w3c.dom.Node subNode = xmlNode.getFirstChild();
/* 216 */     String from = subNode.getTextContent();
/*     */ 
/* 218 */     subNode = subNode.getNextSibling();
/* 219 */     String to = subNode.getTextContent();
/*     */ 
/* 221 */     Transformation transformation = null;
/* 222 */     subNode = subNode.getNextSibling();
/* 223 */     if ((subNode != null) && ("transformation".equals(subNode.getNodeName()))) {
/* 224 */       String lang = subNode.getAttributes().getNamedItem("language").getNodeValue();
/* 225 */       String expression = subNode.getTextContent();
/* 226 */       DataTransformer transformer = this.transformerRegistry.find(lang);
/* 227 */       if (transformer == null) {
/* 228 */         throw new IllegalArgumentException("No transformer registered for language " + lang);
/*     */       }
/* 230 */       transformation = new Transformation(lang, expression, from);
/* 231 */       subNode = subNode.getNextSibling();
/*     */     }
/* 233 */     subProcessNode.addOutMapping((String)dataOutputs.get(from), to, transformation);
/*     */   }
/*     */ 
/*     */   protected void handleForEachNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser) throws SAXException
/*     */   {
/* 238 */     ForEachNode forEachNode = (ForEachNode)node;
/* 239 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/*     */ 
/* 241 */     while (xmlNode != null) {
/* 242 */       String nodeName = xmlNode.getNodeName();
/* 243 */       if ("dataInputAssociation".equals(nodeName))
/* 244 */         readDataInputAssociation(xmlNode, this.inputAssociation);
/* 245 */       else if ("dataOutputAssociation".equals(nodeName))
/* 246 */         readDataOutputAssociation(xmlNode, this.outputAssociation);
/* 247 */       else if ("multiInstanceLoopCharacteristics".equals(nodeName)) {
/* 248 */         readMultiInstanceLoopCharacteristics(xmlNode, forEachNode, parser);
/*     */       }
/* 250 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeNode(org.jbpm.workflow.core.Node node, StringBuilder xmlDump, int metaDataType) {
/* 255 */     SubProcessNode subProcessNode = (SubProcessNode)node;
/* 256 */     writeNode("callActivity", subProcessNode, xmlDump, metaDataType);
/* 257 */     if (subProcessNode.getProcessId() != null) {
/* 258 */       xmlDump.append("calledElement=\"" + XmlBPMNProcessDumper.replaceIllegalCharsAttribute(subProcessNode.getProcessId()) + "\" ");
/*     */     }
/* 260 */     if (!subProcessNode.isWaitForCompletion()) {
/* 261 */       xmlDump.append("tns:waitForCompletion=\"false\" ");
/*     */     }
/* 263 */     if (!subProcessNode.isIndependent()) {
/* 264 */       xmlDump.append("tns:independent=\"false\" ");
/*     */     }
/* 266 */     xmlDump.append(">" + EOL);
/* 267 */     writeExtensionElements(subProcessNode, xmlDump);
/* 268 */     writeIO(subProcessNode, xmlDump);
/* 269 */     endNode("callActivity", xmlDump);
/*     */   }
/*     */ 
/*     */   protected void writeIO(SubProcessNode subProcessNode, StringBuilder xmlDump) {
/* 273 */     xmlDump.append("      <ioSpecification>" + EOL);
/* 274 */     for (Map.Entry entry : subProcessNode.getInMappings().entrySet()) {
/* 275 */       xmlDump.append("        <dataInput id=\"" + XmlBPMNProcessDumper.getUniqueNodeId(subProcessNode) + "_" + XmlBPMNProcessDumper.replaceIllegalCharsAttribute((String)entry.getKey()) + "Input\" name=\"" + XmlBPMNProcessDumper.replaceIllegalCharsAttribute((String)entry.getKey()) + "\" />" + EOL);
/*     */     }
/* 277 */     for (Map.Entry entry : subProcessNode.getOutMappings().entrySet()) {
/* 278 */       xmlDump.append("        <dataOutput id=\"" + XmlBPMNProcessDumper.getUniqueNodeId(subProcessNode) + "_" + XmlBPMNProcessDumper.replaceIllegalCharsAttribute((String)entry.getKey()) + "Output\" name=\"" + XmlBPMNProcessDumper.replaceIllegalCharsAttribute((String)entry.getKey()) + "\" />" + EOL);
/*     */     }
/* 280 */     xmlDump.append("        <inputSet>" + EOL);
/* 281 */     for (Map.Entry entry : subProcessNode.getInMappings().entrySet()) {
/* 282 */       xmlDump.append("          <dataInputRefs>" + XmlBPMNProcessDumper.getUniqueNodeId(subProcessNode) + "_" + XmlDumper.replaceIllegalChars((String)entry.getKey()) + "Input</dataInputRefs>" + EOL);
/*     */     }
/* 284 */     xmlDump.append("        </inputSet>" + EOL);
/* 285 */     xmlDump.append("        <outputSet>" + EOL);
/* 286 */     for (Map.Entry entry : subProcessNode.getOutMappings().entrySet()) {
/* 287 */       xmlDump.append("          <dataOutputRefs>" + XmlBPMNProcessDumper.getUniqueNodeId(subProcessNode) + "_" + XmlDumper.replaceIllegalChars((String)entry.getKey()) + "Output</dataOutputRefs>" + EOL);
/*     */     }
/* 289 */     xmlDump.append("        </outputSet>" + EOL);
/* 290 */     xmlDump.append("      </ioSpecification>" + EOL);
/* 291 */     for (Map.Entry entry : subProcessNode.getInMappings().entrySet()) {
/* 292 */       xmlDump.append("      <dataInputAssociation>" + EOL);
/* 293 */       xmlDump.append("        <sourceRef>" + 
/* 294 */         XmlDumper.replaceIllegalChars((String)entry
/* 294 */         .getValue()) + "</sourceRef>" + EOL + "        <targetRef>" + 
/* 295 */         XmlBPMNProcessDumper.getUniqueNodeId(subProcessNode) + 
/* 295 */         "_" + XmlDumper.replaceIllegalChars((String)entry.getKey()) + "Input</targetRef>" + EOL);
/* 296 */       xmlDump.append("      </dataInputAssociation>" + EOL);
/*     */     }
/* 298 */     for (Map.Entry entry : subProcessNode.getOutMappings().entrySet()) {
/* 299 */       xmlDump.append("      <dataOutputAssociation>" + EOL);
/* 300 */       xmlDump.append("        <sourceRef>" + 
/* 301 */         XmlBPMNProcessDumper.getUniqueNodeId(subProcessNode) + 
/* 301 */         "_" + XmlDumper.replaceIllegalChars((String)entry.getKey()) + "Output</sourceRef>" + EOL + "        <targetRef>" + 
/* 302 */         (String)entry
/* 302 */         .getValue() + "</targetRef>" + EOL);
/* 303 */       xmlDump.append("      </dataOutputAssociation>" + EOL);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.CallActivityHandler
 * JD-Core Version:    0.6.0
 */