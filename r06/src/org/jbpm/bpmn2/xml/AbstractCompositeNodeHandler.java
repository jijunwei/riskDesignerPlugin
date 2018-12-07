/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.jbpm.bpmn2.core.Association;
/*    */ import org.jbpm.workflow.core.node.CompositeNode;
/*    */ import org.jbpm.workflow.core.node.CompositeNode.CompositeNodeEnd;
/*    */ import org.jbpm.workflow.core.node.CompositeNode.CompositeNodeStart;
/*    */ import org.kie.api.definition.process.Connection;
/*    */ 
/*    */ public abstract class AbstractCompositeNodeHandler extends AbstractNodeHandler
/*    */ {
/*    */   protected void visitConnectionsAndAssociations(org.jbpm.workflow.core.Node node, StringBuilder xmlDump, int metaDataType)
/*    */   {
/* 32 */     List<Connection> connections = getSubConnections((CompositeNode)node);
/* 33 */     xmlDump.append("    <!-- connections -->" + EOL);
/* 34 */     for (Iterator localIterator = connections.iterator(); localIterator.hasNext(); ) 
               { Connection connection = (Connection)localIterator.next();
/* 35 */       XmlBPMNProcessDumper.INSTANCE.visitConnection(connection, xmlDump, metaDataType);
/*    */     }
/*    */     Connection connection;
/* 38 */     List<Association> associations = (List<Association>) node.getMetaData().get("BPMN.Associations");
/* 39 */     if (associations != null)
/* 40 */       for (Association association : associations)
/* 41 */         XmlBPMNProcessDumper.INSTANCE.visitAssociation(association, xmlDump);
/*    */   }
/*    */ 
/*    */   protected List<Connection> getSubConnections(CompositeNode compositeNode)
/*    */   {
/* 47 */     List connections = new ArrayList();
/* 48 */     for (org.kie.api.definition.process.Node subNode : compositeNode.getNodes())
/*    */     {
/* 50 */       if (!(subNode instanceof CompositeNode.CompositeNodeEnd)) {
/* 51 */         for (Connection connection : subNode.getIncomingConnections("DROOLS_DEFAULT")) {
/* 52 */           if (!(connection.getFrom() instanceof CompositeNode.CompositeNodeStart)) {
/* 53 */             connections.add(connection);
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/* 58 */     return connections;
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.AbstractCompositeNodeHandler
 * JD-Core Version:    0.6.0
 */