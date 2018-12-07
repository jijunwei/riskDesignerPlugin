/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import org.drools.compiler.compiler.xml.XmlDumper;
/*    */ import org.jbpm.process.core.timer.Timer;
/*    */ import org.jbpm.workflow.core.Node;
/*    */ import org.jbpm.workflow.core.node.TimerNode;
/*    */ import org.xml.sax.Attributes;
/*    */ 
/*    */ public class TimerNodeHandler extends AbstractNodeHandler
/*    */ {
/*    */   protected Node createNode(Attributes attrs)
/*    */   {
/* 28 */     throw new IllegalArgumentException("Reading in should be handled by intermediate catch event handler");
/*    */   }
/*    */ 
/*    */   public Class generateNodeFor()
/*    */   {
/* 33 */     return TimerNode.class;
/*    */   }
/*    */ 
/*    */   public void writeNode(Node node, StringBuilder xmlDump, int metaDataType) {
/* 37 */     TimerNode timerNode = (TimerNode)node;
/* 38 */     writeNode("intermediateCatchEvent", timerNode, xmlDump, metaDataType);
/* 39 */     xmlDump.append(">" + EOL);
/* 40 */     writeExtensionElements(node, xmlDump);
/* 41 */     xmlDump.append("      <timerEventDefinition>" + EOL);
/* 42 */     Timer timer = timerNode.getTimer();
/* 43 */     if ((timer != null) && ((timer.getDelay() != null) || (timer.getDate() != null))) {
/* 44 */       if (timer.getTimeType() == 1)
/* 45 */         xmlDump.append("        <timeDuration xsi:type=\"tFormalExpression\">" + XmlDumper.replaceIllegalChars(timer.getDelay()) + "</timeDuration>" + EOL);
/* 46 */       else if (timer.getTimeType() == 2)
/*    */       {
/* 48 */         if (timer.getPeriod() != null)
/* 49 */           xmlDump.append("        <timeCycle xsi:type=\"tFormalExpression\">" + XmlDumper.replaceIllegalChars(timer.getDelay()) + "###" + XmlDumper.replaceIllegalChars(timer.getPeriod()) + "</timeCycle>" + EOL);
/*    */         else
/* 51 */           xmlDump.append("        <timeCycle xsi:type=\"tFormalExpression\">" + XmlDumper.replaceIllegalChars(timer.getDelay()) + "</timeCycle>" + EOL);
/*    */       }
/* 53 */       else if (timer.getTimeType() == 3) {
/* 54 */         xmlDump.append("        <timeDate xsi:type=\"tFormalExpression\">" + XmlDumper.replaceIllegalChars(timer.getDelay()) + "</timeDate>" + EOL);
/*    */       }
/*    */     }
/* 57 */     xmlDump.append("      </timerEventDefinition>" + EOL);
/* 58 */     endNode("intermediateCatchEvent", xmlDump);
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.TimerNodeHandler
 * JD-Core Version:    0.6.0
 */