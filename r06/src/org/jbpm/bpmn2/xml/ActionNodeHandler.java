/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import org.drools.compiler.compiler.xml.XmlDumper;
/*    */ import org.jbpm.workflow.core.Node;
/*    */ import org.jbpm.workflow.core.impl.DroolsConsequenceAction;
/*    */ import org.jbpm.workflow.core.node.ActionNode;
/*    */ import org.slf4j.Logger;
/*    */ import org.xml.sax.Attributes;
/*    */ 
/*    */ public class ActionNodeHandler extends AbstractNodeHandler
/*    */ {
/*    */   protected Node createNode(Attributes attrs)
/*    */   {
/* 32 */     throw new IllegalArgumentException("Reading in should be handled by specific handlers");
/*    */   }
/*    */ 
/*    */   public Class generateNodeFor()
/*    */   {
/* 37 */     return ActionNode.class;
/*    */   }
/*    */ 
/*    */   public void writeNode(Node node, StringBuilder xmlDump, int metaDataType)
/*    */   {
/* 43 */     ActionNode actionNode = (ActionNode)node;
/* 44 */     DroolsConsequenceAction action = null;
/* 45 */     if ((actionNode.getAction() instanceof DroolsConsequenceAction))
/* 46 */       action = (DroolsConsequenceAction)actionNode.getAction();
/*    */     else {
/* 48 */       logger.warn("Cannot serialize custom implementation of the Action interface to XML");
/*    */     }
/*    */ 
/* 51 */     String eventType = (String)actionNode.getMetaData("EventType");
/* 52 */     String ref = (String)actionNode.getMetaData("Ref");
/* 53 */     String variableRef = (String)actionNode.getMetaData("Variable");
/*    */ 
/* 55 */     if (action != null) {
/* 56 */       String s = action.getConsequence();
/* 57 */       if (s.startsWith("org.drools.core.process.instance.impl.WorkItemImpl workItem = new org.drools.core.process.instance.impl.WorkItemImpl();")) {
/* 58 */         writeNode("intermediateThrowEvent", actionNode, xmlDump, metaDataType);
/*    */ 
/* 62 */         xmlDump.append(new StringBuilder().append(">").append(EOL).toString());
/* 63 */         writeExtensionElements(actionNode, xmlDump);
/*    */ 
/* 65 */         String variable = (String)actionNode.getMetaData("MappingVariable");
/* 66 */         if (variable != null) {
/* 67 */           xmlDump.append(new StringBuilder().append("      <dataInput id=\"")
/* 68 */             .append(XmlBPMNProcessDumper.getUniqueNodeId(actionNode))
/* 68 */             .append("_Input\" />").append(EOL).append("      <dataInputAssociation>").append(EOL).append("        <sourceRef>")
/* 70 */             .append(XmlDumper.replaceIllegalChars(variable))
/* 70 */             .append("</sourceRef>").append(EOL).append("        <targetRef>")
/* 71 */             .append(XmlBPMNProcessDumper.getUniqueNodeId(actionNode))
/* 71 */             .append("_Input</targetRef>").append(EOL).append("      </dataInputAssociation>").append(EOL).append("      <inputSet>").append(EOL).append("        <dataInputRefs>")
/* 74 */             .append(XmlBPMNProcessDumper.getUniqueNodeId(actionNode))
/* 74 */             .append("_Input</dataInputRefs>").append(EOL).append("      </inputSet>").append(EOL).toString());
/*    */         }
/*    */ 
/* 77 */         xmlDump.append(new StringBuilder().append("      <messageEventDefinition messageRef=\"").append(XmlBPMNProcessDumper.getUniqueNodeId(actionNode)).append("_Message\"/>").append(EOL).toString());
/* 78 */         endNode("intermediateThrowEvent", xmlDump);
/*    */       }
/* 80 */       else if ("signal".equals(eventType)) {
/* 81 */         writeNode("intermediateThrowEvent", actionNode, xmlDump, metaDataType);
/*    */ 
/* 85 */         xmlDump.append(new StringBuilder().append(">").append(EOL).toString());
/* 86 */         writeExtensionElements(actionNode, xmlDump);
/*    */ 
/* 89 */         if (!s.startsWith("null"))
/*    */         {
/* 91 */           xmlDump.append(new StringBuilder().append("      <dataInput id=\"")
/* 92 */             .append(XmlBPMNProcessDumper.getUniqueNodeId(actionNode))
/* 92 */             .append("_Input\" />").append(EOL).append("      <dataInputAssociation>").append(EOL).append("        <sourceRef>")
/* 94 */             .append(XmlDumper.replaceIllegalChars(variableRef))
/* 94 */             .append("</sourceRef>").append(EOL).append("        <targetRef>")
/* 95 */             .append(XmlBPMNProcessDumper.getUniqueNodeId(actionNode))
/* 95 */             .append("_Input</targetRef>").append(EOL).append("      </dataInputAssociation>").append(EOL).append("      <inputSet>").append(EOL).append("        <dataInputRefs>")
/* 98 */             .append(XmlBPMNProcessDumper.getUniqueNodeId(actionNode))
/* 98 */             .append("_Input</dataInputRefs>").append(EOL).append("      </inputSet>").append(EOL).toString());
/*    */         }
/*    */ 
/* 101 */         xmlDump.append(new StringBuilder().append("      <signalEventDefinition signalRef=\"").append(XmlBPMNProcessDumper.replaceIllegalCharsAttribute(ref)).append("\"/>").append(EOL).toString());
/* 102 */         endNode("intermediateThrowEvent", xmlDump);
/*    */       }
/* 104 */       else if (s.startsWith("kcontext.getKnowledgeRuntime().signalEvent(")) {
/* 105 */         writeNode("intermediateThrowEvent", actionNode, xmlDump, metaDataType);
/*    */ 
/* 109 */         xmlDump.append(new StringBuilder().append(">").append(EOL).toString());
/* 110 */         writeExtensionElements(actionNode, xmlDump);
/*    */ 
/* 112 */         s = s.substring(44);
/* 113 */         String type = s.substring(0, s
/* 114 */           .indexOf("\""));
/*    */ 
/* 115 */         s = s.substring(s.indexOf(",") + 2);
/* 116 */         String variable = null;
/* 117 */         if (!s.startsWith("null")) {
/* 118 */           variable = s.substring(0, s
/* 119 */             .indexOf(")"));
/*    */ 
/* 120 */           xmlDump.append(new StringBuilder().append("      <dataInput id=\"")
/* 121 */             .append(XmlBPMNProcessDumper.getUniqueNodeId(actionNode))
/* 121 */             .append("_Input\" />").append(EOL).append("      <dataInputAssociation>").append(EOL).append("        <sourceRef>")
/* 123 */             .append(XmlDumper.replaceIllegalChars(variable))
/* 123 */             .append("</sourceRef>").append(EOL).append("        <targetRef>")
/* 124 */             .append(XmlBPMNProcessDumper.getUniqueNodeId(actionNode))
/* 124 */             .append("_Input</targetRef>").append(EOL).append("      </dataInputAssociation>").append(EOL).append("      <inputSet>").append(EOL).append("        <dataInputRefs>")
/* 127 */             .append(XmlBPMNProcessDumper.getUniqueNodeId(actionNode))
/* 127 */             .append("_Input</dataInputRefs>").append(EOL).append("      </inputSet>").append(EOL).toString());
/*    */         }
/*    */ 
/* 130 */         xmlDump.append(new StringBuilder().append("      <signalEventDefinition signalRef=\"").append(XmlBPMNProcessDumper.replaceIllegalCharsAttribute(type)).append("\"/>").append(EOL).toString());
/* 131 */         endNode("intermediateThrowEvent", xmlDump);
/*    */       }
/* 133 */       else if (s.startsWith("kcontext.getProcessInstance().signalEvent(")) {
/* 134 */         writeNode("intermediateThrowEvent", actionNode, xmlDump, metaDataType);
/*    */ 
/* 138 */         xmlDump.append(new StringBuilder().append(">").append(EOL).toString());
/* 139 */         writeExtensionElements(actionNode, xmlDump);
/*    */ 
/* 141 */         s = s.substring(43);
/* 142 */         assert ("Compensation".equals(s.substring(0, s
/* 143 */           .indexOf("\"")))) : new StringBuilder().append("Type is not \"Compensation\" but \"")
/* 144 */           .append(s
/* 144 */           .substring(0, s
/* 145 */           .indexOf("\"")))
/* 144 */           .append("\"").toString();
/*    */ 
/* 147 */         String activityRef = "";
/* 148 */         int begin = 12;
/* 149 */         int end = s.length() - 3;
/* 150 */         String compensationEvent = s.substring(begin, end);
/*    */ 
/* 152 */         if (!compensationEvent.startsWith("implicit:"))
/*    */         {
/* 154 */           activityRef = new StringBuilder().append("activityRef=\"").append(XmlBPMNProcessDumper.replaceIllegalCharsAttribute(activityRef)).append("\" ").toString();
/*    */         }
/* 156 */         xmlDump.append(new StringBuilder().append("      <compensateEventDefinition ").append(activityRef).append("/>").append(EOL).toString());
/* 157 */         endNode("intermediateThrowEvent", xmlDump);
/*    */       }
/* 159 */       else if (s.startsWith("org.drools.core.process.instance.context.exception.ExceptionScopeInstance scopeInstance = (org.drools.core.process.instance.context.exception.ExceptionScopeInstance) ((org.drools.workflow.instance.NodeInstance) kcontext.getNodeInstance()).resolveContextInstance(org.drools.core.process.core.context.exception.ExceptionScope.EXCEPTION_SCOPE, \"")) {
/* 160 */         writeNode("intermediateThrowEvent", actionNode, xmlDump, metaDataType);
/*    */ 
/* 164 */         xmlDump.append(new StringBuilder().append(">").append(EOL).toString());
/* 165 */         writeExtensionElements(actionNode, xmlDump);
/*    */ 
/* 167 */         s = s.substring(327);
/* 168 */         String type = s.substring(0, s
/* 169 */           .indexOf("\""));
/*    */ 
/* 170 */         xmlDump.append(new StringBuilder().append("      <escalationEventDefinition escalationRef=\"").append(XmlBPMNProcessDumper.replaceIllegalCharsAttribute(type)).append("\"/>").append(EOL).toString());
/* 171 */         endNode("intermediateThrowEvent", xmlDump);
/*    */       }
/* 173 */       else if ("IntermediateThrowEvent-None".equals(actionNode.getMetaData("NodeType"))) {
/* 174 */         writeNode("intermediateThrowEvent", actionNode, xmlDump, metaDataType);
/*    */ 
/* 178 */         xmlDump.append(new StringBuilder().append(">").append(EOL).toString());
/* 179 */         writeExtensionElements(actionNode, xmlDump);
/*    */ 
/* 181 */         endNode("intermediateThrowEvent", xmlDump);
/*    */       }
/*    */       else {
/* 184 */         writeNode("scriptTask", actionNode, xmlDump, metaDataType);
/*    */ 
/* 188 */         if ("java".equals(action.getDialect()))
/* 189 */           xmlDump.append("scriptFormat=\"http://www.java.com/java\" ");
/* 190 */         else if ("JavaScript".equals(action.getDialect())) {
/* 191 */           xmlDump.append("scriptFormat=\"http://www.javascript.com/javascript\" ");
/*    */         }
/* 193 */         Object isForCompensationObj = actionNode.getMetaData("isForCompensation");
/* 194 */         if ((isForCompensationObj != null) && (((Boolean)isForCompensationObj).booleanValue())) {
/* 195 */           xmlDump.append("isForCompensation=\"true\" ");
/*    */         }
/* 197 */         xmlDump.append(new StringBuilder().append(">").append(EOL).toString());
/* 198 */         writeExtensionElements(actionNode, xmlDump);
/*    */ 
/* 200 */         if (action.getConsequence() != null) {
/* 201 */           xmlDump.append(new StringBuilder().append("      <script>").append(XmlDumper.replaceIllegalChars(action.getConsequence())).append("</script>").append(EOL).toString());
/*    */         }
/* 203 */         endNode("scriptTask", xmlDump);
/*    */       }
/*    */     }
/*    */     else {
/* 207 */       writeNode("scriptTask", actionNode, xmlDump, metaDataType);
/*    */ 
/* 211 */       xmlDump.append(new StringBuilder().append(">").append(EOL).toString());
/* 212 */       writeExtensionElements(actionNode, xmlDump);
/*    */ 
/* 214 */       endNode("scriptTask", xmlDump);
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.ActionNodeHandler
 * JD-Core Version:    0.6.0
 */