/*     */ package org.jbpm.bpmn2.xml;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.drools.compiler.compiler.xml.XmlDumper;
/*     */ import org.jbpm.process.core.Work;
/*     */ import org.jbpm.workflow.core.Node;
/*     */ import org.jbpm.workflow.core.node.WorkItemNode;
/*     */ import org.xml.sax.Attributes;
/*     */ 
/*     */ public class WorkItemNodeHandler extends AbstractNodeHandler
/*     */ {
/*     */   protected Node createNode(Attributes attrs)
/*     */   {
/*  29 */     throw new IllegalArgumentException("Reading in should be handled by specific handlers");
/*     */   }
/*     */ 
/*     */   public Class generateNodeFor()
/*     */   {
/*  34 */     return WorkItemNode.class;
/*     */   }
/*     */ 
/*     */   public void writeNode(Node node, StringBuilder xmlDump, int metaDataType) {
/*  38 */     WorkItemNode workItemNode = (WorkItemNode)node;
/*  39 */     String type = workItemNode.getWork().getName();
/*  40 */     if ("Manual Task".equals(type)) {
/*  41 */       writeNode("manualTask", workItemNode, xmlDump, metaDataType);
/*  42 */       xmlDump.append(">" + EOL);
/*  43 */       writeExtensionElements(workItemNode, xmlDump);
/*  44 */       endNode("manualTask", xmlDump);
/*  45 */       return;
/*     */     }
/*  47 */     if ("Service Task".equals(type)) {
/*  48 */       writeNode("serviceTask", workItemNode, xmlDump, metaDataType);
/*  49 */       String impl = "Other";
/*  50 */       if (workItemNode.getWork().getParameter("implementation") != null) {
/*  51 */         impl = (String)workItemNode.getWork().getParameter("implementation");
/*     */       }
/*  53 */       xmlDump.append("operationRef=\"" + 
/*  54 */         XmlBPMNProcessDumper.getUniqueNodeId(workItemNode) + 
/*  54 */         "_ServiceOperation\" implementation=\"" + impl + "\" >" + EOL);
/*  55 */       writeExtensionElements(workItemNode, xmlDump);
/*  56 */       xmlDump.append("      <ioSpecification>" + EOL + "        <dataInput id=\"" + 
/*  58 */         XmlBPMNProcessDumper.getUniqueNodeId(workItemNode) + 
/*  58 */         "_param\" name=\"Parameter\" />" + EOL + "        <dataOutput id=\"" + 
/*  59 */         XmlBPMNProcessDumper.getUniqueNodeId(workItemNode) + 
/*  59 */         "_result\" name=\"Result\" />" + EOL + "        <inputSet>" + EOL + "          <dataInputRefs>" + 
/*  61 */         XmlBPMNProcessDumper.getUniqueNodeId(workItemNode) + 
/*  61 */         "_param</dataInputRefs>" + EOL + "        </inputSet>" + EOL + "        <outputSet>" + EOL + "          <dataOutputRefs>" + 
/*  64 */         XmlBPMNProcessDumper.getUniqueNodeId(workItemNode) + 
/*  64 */         "_result</dataOutputRefs>" + EOL + "        </outputSet>" + EOL + "      </ioSpecification>" + EOL);
/*     */ 
/*  67 */       String inMapping = workItemNode.getInMapping("Parameter");
/*  68 */       if (inMapping != null) {
/*  69 */         xmlDump.append("      <dataInputAssociation>" + EOL + "        <sourceRef>" + 
/*  71 */           XmlDumper.replaceIllegalChars(inMapping) + 
/*  71 */           "</sourceRef>" + EOL + "        <targetRef>" + 
/*  72 */           XmlBPMNProcessDumper.getUniqueNodeId(workItemNode) + 
/*  72 */           "_param</targetRef>" + EOL + "      </dataInputAssociation>" + EOL);
/*     */       }
/*     */ 
/*  75 */       String outMapping = workItemNode.getOutMapping("Result");
/*  76 */       if (outMapping != null) {
/*  77 */         xmlDump.append("      <dataOutputAssociation>" + EOL + "        <sourceRef>" + 
/*  79 */           XmlBPMNProcessDumper.getUniqueNodeId(workItemNode) + 
/*  79 */           "_result</sourceRef>" + EOL + "        <targetRef>" + 
/*  80 */           XmlDumper.replaceIllegalChars(outMapping) + 
/*  80 */           "</targetRef>" + EOL + "      </dataOutputAssociation>" + EOL);
/*     */       }
/*     */ 
/*  83 */       endNode("serviceTask", xmlDump);
/*  84 */       return;
/*     */     }
/*  86 */     if ("Send Task".equals(type)) {
/*  87 */       writeNode("sendTask", workItemNode, xmlDump, metaDataType);
/*  88 */       xmlDump.append("messageRef=\"" + 
/*  89 */         XmlBPMNProcessDumper.getUniqueNodeId(workItemNode) + 
/*  89 */         "_Message\" implementation=\"Other\" >" + EOL);
/*  90 */       writeExtensionElements(workItemNode, xmlDump);
/*  91 */       xmlDump.append("      <ioSpecification>" + EOL + "        <dataInput id=\"" + 
/*  93 */         XmlBPMNProcessDumper.getUniqueNodeId(workItemNode) + 
/*  93 */         "_param\" name=\"Message\" />" + EOL + "        <inputSet>" + EOL + "          <dataInputRefs>" + 
/*  95 */         XmlBPMNProcessDumper.getUniqueNodeId(workItemNode) + 
/*  95 */         "_param</dataInputRefs>" + EOL + "        </inputSet>" + EOL + "        <outputSet/>" + EOL + "      </ioSpecification>" + EOL);
/*     */ 
/*  99 */       String inMapping = workItemNode.getInMapping("Message");
/* 100 */       if (inMapping != null) {
/* 101 */         xmlDump.append("      <dataInputAssociation>" + EOL + "        <sourceRef>" + 
/* 103 */           XmlDumper.replaceIllegalChars(inMapping) + 
/* 103 */           "</sourceRef>" + EOL + "        <targetRef>" + 
/* 104 */           XmlBPMNProcessDumper.getUniqueNodeId(workItemNode) + 
/* 104 */           "_param</targetRef>" + EOL + "      </dataInputAssociation>" + EOL);
/*     */       }
/*     */ 
/* 107 */       endNode("sendTask", xmlDump);
/* 108 */       return;
/*     */     }
/* 110 */     if ("Receive Task".equals(type)) {
/* 111 */       writeNode("receiveTask", workItemNode, xmlDump, metaDataType);
/* 112 */       String messageId = (String)workItemNode.getWork().getParameter("MessageId");
/* 113 */       xmlDump.append("messageRef=\"" + messageId + "\" implementation=\"Other\" >" + EOL);
/*     */ 
/* 115 */       writeExtensionElements(workItemNode, xmlDump);
/* 116 */       xmlDump.append("      <ioSpecification>" + EOL + "        <dataOutput id=\"" + 
/* 118 */         XmlBPMNProcessDumper.getUniqueNodeId(workItemNode) + 
/* 118 */         "_result\" name=\"Message\" />" + EOL + "        <inputSet/>" + EOL + "        <outputSet>" + EOL + "          <dataOutputRefs>" + 
/* 121 */         XmlBPMNProcessDumper.getUniqueNodeId(workItemNode) + 
/* 121 */         "_result</dataOutputRefs>" + EOL + "        </outputSet>" + EOL + "      </ioSpecification>" + EOL);
/*     */ 
/* 124 */       String outMapping = workItemNode.getOutMapping("Message");
/* 125 */       if (outMapping != null) {
/* 126 */         xmlDump.append("      <dataOutputAssociation>" + EOL + "        <sourceRef>" + 
/* 128 */           XmlBPMNProcessDumper.getUniqueNodeId(workItemNode) + 
/* 128 */           "_result</sourceRef>" + EOL + "        <targetRef>" + 
/* 129 */           XmlDumper.replaceIllegalChars(outMapping) + 
/* 129 */           "</targetRef>" + EOL + "      </dataOutputAssociation>" + EOL);
/*     */       }
/*     */ 
/* 132 */       endNode("receiveTask", xmlDump);
/* 133 */       return;
/*     */     }
/* 135 */     writeNode("task", workItemNode, xmlDump, metaDataType);
/* 136 */     Object isForCompensationObject = workItemNode.getMetaData("isForCompensation");
/* 137 */     if ((isForCompensationObject != null) && (((Boolean)isForCompensationObject).booleanValue())) {
/* 138 */       xmlDump.append("isForCompensation=\"true\" ");
/*     */     }
/* 140 */     xmlDump.append("tns:taskName=\"" + XmlBPMNProcessDumper.replaceIllegalCharsAttribute(type) + "\" >" + EOL);
/* 141 */     writeExtensionElements(workItemNode, xmlDump);
/* 142 */     writeIO(workItemNode, xmlDump);
/* 143 */     endNode("task", xmlDump);
/*     */   }
/*     */ 
/*     */   protected void writeIO(WorkItemNode workItemNode, StringBuilder xmlDump) {
/* 147 */     xmlDump.append("      <ioSpecification>" + EOL);
/* 148 */     for (Map.Entry entry : workItemNode.getInMappings().entrySet()) {
/* 149 */       xmlDump.append("        <dataInput id=\"" + XmlBPMNProcessDumper.getUniqueNodeId(workItemNode) + "_" + XmlBPMNProcessDumper.replaceIllegalCharsAttribute((String)entry.getKey()) + "Input\" name=\"" + XmlBPMNProcessDumper.replaceIllegalCharsAttribute((String)entry.getKey()) + "\" />" + EOL);
/*     */     }
/* 151 */     for (Map.Entry entry : workItemNode.getWork().getParameters().entrySet()) {
/* 152 */       if (entry.getValue() != null) {
/* 153 */         xmlDump.append("        <dataInput id=\"" + XmlBPMNProcessDumper.getUniqueNodeId(workItemNode) + "_" + XmlBPMNProcessDumper.replaceIllegalCharsAttribute((String)entry.getKey()) + "Input\" name=\"" + XmlBPMNProcessDumper.replaceIllegalCharsAttribute((String)entry.getKey()) + "\" />" + EOL);
/*     */       }
/*     */     }
/* 156 */     for (Map.Entry entry : workItemNode.getOutMappings().entrySet()) {
/* 157 */       xmlDump.append("        <dataOutput id=\"" + XmlBPMNProcessDumper.getUniqueNodeId(workItemNode) + "_" + XmlBPMNProcessDumper.replaceIllegalCharsAttribute((String)entry.getKey()) + "Output\" name=\"" + XmlBPMNProcessDumper.replaceIllegalCharsAttribute((String)entry.getKey()) + "\" />" + EOL);
/*     */     }
/* 159 */     xmlDump.append("        <inputSet>" + EOL);
/* 160 */     for (Map.Entry entry : workItemNode.getInMappings().entrySet()) {
/* 161 */       xmlDump.append("          <dataInputRefs>" + XmlBPMNProcessDumper.getUniqueNodeId(workItemNode) + "_" + XmlBPMNProcessDumper.replaceIllegalCharsAttribute((String)entry.getKey()) + "Input</dataInputRefs>" + EOL);
/*     */     }
/* 163 */     for (Map.Entry entry : workItemNode.getWork().getParameters().entrySet()) {
/* 164 */       if (entry.getValue() != null) {
/* 165 */         xmlDump.append("          <dataInputRefs>" + XmlBPMNProcessDumper.getUniqueNodeId(workItemNode) + "_" + XmlDumper.replaceIllegalChars((String)entry.getKey()) + "Input</dataInputRefs>" + EOL);
/*     */       }
/*     */     }
/* 168 */     xmlDump.append("        </inputSet>" + EOL);
/*     */ 
/* 170 */     xmlDump.append("        <outputSet>" + EOL);
/* 171 */     for (Map.Entry entry : workItemNode.getOutMappings().entrySet()) {
/* 172 */       xmlDump.append("          <dataOutputRefs>" + XmlBPMNProcessDumper.getUniqueNodeId(workItemNode) + "_" + XmlDumper.replaceIllegalChars((String)entry.getKey()) + "Output</dataOutputRefs>" + EOL);
/*     */     }
/* 174 */     xmlDump.append("        </outputSet>" + EOL);
/*     */ 
/* 176 */     xmlDump.append("      </ioSpecification>" + EOL);
/*     */ 
/* 178 */     writeInputAssociation(workItemNode, xmlDump);
/* 179 */     writeOutputAssociation(workItemNode, xmlDump);
/*     */   }
/*     */ 
/*     */   protected void writeInputAssociation(WorkItemNode workItemNode, StringBuilder xmlDump) {
/* 183 */     for (Map.Entry entry : workItemNode.getInMappings().entrySet()) {
/* 184 */       xmlDump.append("      <dataInputAssociation>" + EOL);
/* 185 */       xmlDump.append("        <sourceRef>" + 
/* 186 */         XmlDumper.replaceIllegalChars((String)entry
/* 186 */         .getValue()) + "</sourceRef>" + EOL + "        <targetRef>" + 
/* 187 */         XmlBPMNProcessDumper.getUniqueNodeId(workItemNode) + 
/* 187 */         "_" + XmlDumper.replaceIllegalChars((String)entry.getKey()) + "Input</targetRef>" + EOL);
/* 188 */       xmlDump.append("      </dataInputAssociation>" + EOL);
/*     */     }
/* 190 */     for (Map.Entry entry : workItemNode.getWork().getParameters().entrySet())
/* 191 */       if (entry.getValue() != null) {
/* 192 */         xmlDump.append("      <dataInputAssociation>" + EOL);
/* 193 */         xmlDump.append("        <targetRef>" + 
/* 194 */           XmlBPMNProcessDumper.getUniqueNodeId(workItemNode) + 
/* 194 */           "_" + XmlDumper.replaceIllegalChars((String)entry.getKey()) + "Input</targetRef>" + EOL + "        <assignment>" + EOL + "          <from xsi:type=\"tFormalExpression\">" + 
/* 196 */           XmlDumper.replaceIllegalChars(entry
/* 196 */           .getValue().toString()) + "</from>" + EOL + "          <to xsi:type=\"tFormalExpression\">" + 
/* 197 */           XmlBPMNProcessDumper.getUniqueNodeId(workItemNode) + 
/* 197 */           "_" + XmlDumper.replaceIllegalChars((String)entry.getKey()) + "Input</to>" + EOL + "        </assignment>" + EOL);
/*     */ 
/* 199 */         xmlDump.append("      </dataInputAssociation>" + EOL);
/*     */       }
/*     */   }
/*     */ 
/*     */   protected void writeOutputAssociation(WorkItemNode workItemNode, StringBuilder xmlDump)
/*     */   {
/* 205 */     for (Map.Entry entry : workItemNode.getOutMappings().entrySet()) {
/* 206 */       xmlDump.append("      <dataOutputAssociation>" + EOL);
/* 207 */       xmlDump.append("        <sourceRef>" + 
/* 208 */         XmlBPMNProcessDumper.getUniqueNodeId(workItemNode) + 
/* 208 */         "_" + XmlDumper.replaceIllegalChars((String)entry.getKey()) + "Output</sourceRef>" + EOL + "        <targetRef>" + 
/* 209 */         XmlDumper.replaceIllegalChars((String)entry
/* 209 */         .getValue()) + "</targetRef>" + EOL);
/* 210 */       xmlDump.append("      </dataOutputAssociation>" + EOL);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.WorkItemNodeHandler
 * JD-Core Version:    0.6.0
 */