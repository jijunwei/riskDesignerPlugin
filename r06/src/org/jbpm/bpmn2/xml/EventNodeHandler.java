/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.drools.compiler.compiler.xml.XmlDumper;
/*    */ import org.jbpm.process.core.event.EventTypeFilter;
/*    */ import org.jbpm.workflow.core.Node;
/*    */ import org.jbpm.workflow.core.node.EventNode;
/*    */ import org.xml.sax.Attributes;
/*    */ 
/*    */ public class EventNodeHandler extends AbstractNodeHandler
/*    */ {
/*    */   protected Node createNode(Attributes attrs)
/*    */   {
/* 34 */     throw new IllegalArgumentException("Reading in should be handled by intermediate catch event handler");
/*    */   }
/*    */ 
/*    */   public Class generateNodeFor()
/*    */   {
/* 39 */     return EventNode.class;
/*    */   }
/*    */ 
/*    */   public void writeNode(Node node, StringBuilder xmlDump, int metaDataType) {
/* 43 */     EventNode eventNode = (EventNode)node;
/* 44 */     String attachedTo = (String)eventNode.getMetaData("AttachedTo");
/* 45 */     if (attachedTo == null) {
/* 46 */       writeNode("intermediateCatchEvent", eventNode, xmlDump, metaDataType);
/* 47 */       xmlDump.append(">" + EOL);
/* 48 */       writeExtensionElements(eventNode, xmlDump);
/* 49 */       writeVariableName(eventNode, xmlDump);
/* 50 */       if (eventNode.getEventFilters().size() > 0) {
/* 51 */         String type = ((EventTypeFilter)eventNode.getEventFilters().get(0)).getType();
/* 52 */         if (type.startsWith("Message-")) {
/* 53 */           type = type.substring(8);
/* 54 */           xmlDump.append("      <messageEventDefinition messageRef=\"" + XmlBPMNProcessDumper.replaceIllegalCharsAttribute(type) + "\"/>" + EOL);
/*    */         } else {
/* 56 */           xmlDump.append("      <signalEventDefinition signalRef=\"" + XmlBPMNProcessDumper.replaceIllegalCharsAttribute(type) + "\"/>" + EOL);
/*    */         }
/*    */       }
/* 59 */       endNode("intermediateCatchEvent", xmlDump);
/*    */     } else {
/* 61 */       String type = ((EventTypeFilter)eventNode.getEventFilters().get(0)).getType();
/* 62 */       if (type.startsWith("Escalation-")) {
/* 63 */         type = type.substring(attachedTo.length() + 12);
/* 64 */         boolean cancelActivity = ((Boolean)eventNode.getMetaData("CancelActivity")).booleanValue();
/* 65 */         writeNode("boundaryEvent", eventNode, xmlDump, metaDataType);
/* 66 */         xmlDump.append("attachedToRef=\"" + attachedTo + "\" ");
/* 67 */         if (!cancelActivity) {
/* 68 */           xmlDump.append("cancelActivity=\"false\" ");
/*    */         }
/* 70 */         xmlDump.append(">" + EOL);
/* 71 */         xmlDump.append("      <escalationEventDefinition escalationRef=\"" + XmlBPMNProcessDumper.replaceIllegalCharsAttribute(type) + "\" />" + EOL);
/* 72 */         endNode("boundaryEvent", xmlDump);
/* 73 */       } else if (type.startsWith("Error-")) {
/* 74 */         type = type.substring(attachedTo.length() + 7);
/* 75 */         writeNode("boundaryEvent", eventNode, xmlDump, metaDataType);
/* 76 */         xmlDump.append("attachedToRef=\"" + attachedTo + "\" ");
/* 77 */         xmlDump.append(">" + EOL);
/* 78 */         String errorId = getErrorIdForErrorCode(type, eventNode);
/* 79 */         xmlDump.append("      <errorEventDefinition errorRef=\"" + XmlBPMNProcessDumper.replaceIllegalCharsAttribute(errorId) + "\" />" + EOL);
/* 80 */         endNode("boundaryEvent", xmlDump);
/* 81 */       } else if (type.startsWith("Timer-")) {
/* 82 */         type = type.substring(attachedTo.length() + 7);
/* 83 */         boolean cancelActivity = ((Boolean)eventNode.getMetaData("CancelActivity")).booleanValue();
/* 84 */         writeNode("boundaryEvent", eventNode, xmlDump, metaDataType);
/* 85 */         xmlDump.append("attachedToRef=\"" + attachedTo + "\" ");
/* 86 */         if (!cancelActivity) {
/* 87 */           xmlDump.append("cancelActivity=\"false\" ");
/*    */         }
/* 89 */         xmlDump.append(">" + EOL);
/* 90 */         String duration = (String)eventNode.getMetaData("TimeDuration");
/* 91 */         String cycle = (String)eventNode.getMetaData("TimeCycle");
/*    */ 
/* 93 */         if ((duration != null) && (cycle != null)) {
/* 94 */           xmlDump.append("      <timerEventDefinition>" + EOL + "        <timeDuration xsi:type=\"tFormalExpression\">" + 
/* 96 */             XmlDumper.replaceIllegalChars(duration) + 
/* 96 */             "</timeDuration>" + EOL + "        <timeCycle xsi:type=\"tFormalExpression\">" + 
/* 97 */             XmlDumper.replaceIllegalChars(cycle) + 
/* 97 */             "</timeCycle>" + EOL + "      </timerEventDefinition>" + EOL);
/*    */         }
/* 99 */         else if (duration != null) {
/* 100 */           xmlDump.append("      <timerEventDefinition>" + EOL + "        <timeDuration xsi:type=\"tFormalExpression\">" + 
/* 102 */             XmlDumper.replaceIllegalChars(duration) + 
/* 102 */             "</timeDuration>" + EOL + "      </timerEventDefinition>" + EOL);
/*    */         }
/*    */         else {
/* 105 */           xmlDump.append("      <timerEventDefinition>" + EOL + "        <timeCycle xsi:type=\"tFormalExpression\">" + 
/* 107 */             XmlDumper.replaceIllegalChars(cycle) + 
/* 107 */             "</timeCycle>" + EOL + "      </timerEventDefinition>" + EOL);
/*    */         }
/*    */ 
/* 110 */         endNode("boundaryEvent", xmlDump);
/* 111 */       } else if (node.getMetaData().get("SignalName") != null)
/*    */       {
/* 113 */         boolean cancelActivity = ((Boolean)eventNode.getMetaData("CancelActivity")).booleanValue();
/* 114 */         writeNode("boundaryEvent", eventNode, xmlDump, metaDataType);
/* 115 */         xmlDump.append("attachedToRef=\"" + attachedTo + "\" ");
/* 116 */         if (!cancelActivity) {
/* 117 */           xmlDump.append("cancelActivity=\"false\" ");
/*    */         }
/* 119 */         xmlDump.append(">" + EOL);
/* 120 */         xmlDump.append("      <signalEventDefinition signalRef=\"" + type + "\"/>" + EOL);
/* 121 */         endNode("boundaryEvent", xmlDump);
/* 122 */       } else if (node.getMetaData().get("Condition") != null)
/*    */       {
/* 124 */         boolean cancelActivity = ((Boolean)eventNode.getMetaData("CancelActivity")).booleanValue();
/* 125 */         writeNode("boundaryEvent", eventNode, xmlDump, metaDataType);
/* 126 */         xmlDump.append("attachedToRef=\"" + attachedTo + "\" ");
/* 127 */         if (!cancelActivity) {
/* 128 */           xmlDump.append("cancelActivity=\"false\" ");
/*    */         }
/* 130 */         xmlDump.append(">" + EOL);
/* 131 */         xmlDump.append("      <conditionalEventDefinition>" + EOL);
/* 132 */         xmlDump.append("        <condition xsi:type=\"tFormalExpression\" language=\"http://www.jboss.org/drools/rule\">" + eventNode.getMetaData("Condition") + "</condition>" + EOL);
/* 133 */         xmlDump.append("      </conditionalEventDefinition>" + EOL);
/* 134 */         endNode("boundaryEvent", xmlDump);
/* 135 */       } else if (type.startsWith("Message-")) {
/* 136 */         type = type.substring(8);
/* 137 */         writeNode("boundaryEvent", eventNode, xmlDump, metaDataType);
/* 138 */         xmlDump.append("attachedToRef=\"" + attachedTo + "\" ");
/* 139 */         xmlDump.append(">" + EOL);
/* 140 */         xmlDump.append("      <messageEventDefinition messageRef=\"" + type + "\"/>" + EOL);
/* 141 */         endNode("boundaryEvent", xmlDump);
/*    */       } else {
/* 143 */         throw new IllegalArgumentException("Unknown boundary event type: \"" + type + "\"");
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.EventNodeHandler
 * JD-Core Version:    0.6.0
 */