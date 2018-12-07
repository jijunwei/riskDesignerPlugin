/*     */ package org.jbpm.bpmn2.xml.di;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.drools.core.xml.BaseAbstractHandler;
/*     */ import org.drools.core.xml.ExtensibleXmlParser;
/*     */ import org.drools.core.xml.Handler;
/*     */ import org.jbpm.bpmn2.core.Definitions;
/*     */ import org.jbpm.compiler.xml.ProcessBuildData;
/*     */ import org.jbpm.ruleflow.core.RuleFlowProcess;
/*     */ import org.jbpm.workflow.core.impl.ConnectionImpl;
/*     */ import org.kie.api.definition.process.Connection;
/*     */ import org.kie.api.definition.process.NodeContainer;
/*     */ import org.kie.api.definition.process.Process;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class BPMNPlaneHandler extends BaseAbstractHandler
/*     */   implements Handler
/*     */ {
/*     */   public BPMNPlaneHandler()
/*     */   {
/*  42 */     initValidParents();
/*  43 */     initValidPeers();
/*  44 */     this.allowNesting = false;
/*     */   }
/*     */ 
/*     */   protected void initValidParents() {
/*  48 */     this.validParents = new HashSet();
/*  49 */     this.validParents.add(Definitions.class);
/*     */   }
/*     */ 
/*     */   protected void initValidPeers() {
/*  53 */     this.validPeers = new HashSet();
/*  54 */     this.validPeers.add(null);
/*  55 */     this.validPeers.add(Process.class);
/*     */   }
/*     */ 
/*     */   public Object start(String uri, String localName, Attributes attrs, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/*  61 */     parser.startElementBuilder(localName, attrs);
/*     */ 
/*  63 */     String processRef = attrs.getValue("bpmnElement");
/*  64 */     ProcessInfo info = new ProcessInfo(processRef);
/*  65 */     return info;
/*     */   }
/*     */ 
/*     */   public Object end(String uri, String localName, ExtensibleXmlParser parser) throws SAXException
/*     */   {
/*  70 */     parser.endElementBuilder();
/*  71 */     ProcessInfo processInfo = (ProcessInfo)parser.getCurrent();
/*  72 */     List<Process> processes = ((ProcessBuildData)parser.getData()).getProcesses();
/*  73 */     RuleFlowProcess process = null;
/*  74 */     for (Process p : processes) {
/*  75 */       if ((p.getId() != null) && (p.getId().equals(processInfo.getProcessRef()))) {
/*  76 */         process = (RuleFlowProcess)p;
/*  77 */         break;
/*     */       }
/*     */     }
/*  80 */     if (process != null) {
/*  81 */       for (BPMNShapeHandler.NodeInfo nodeInfo : processInfo.getNodeInfos()) {
/*  82 */         processNodeInfo(nodeInfo, process.getNodes());
/*     */       }
/*  84 */       postProcessNodeOffset(process.getNodes(), 0, 0);
/*  85 */       for (BPMNEdgeHandler.ConnectionInfo connectionInfo : processInfo.getConnectionInfos()) {
/*  86 */         if (connectionInfo.getBendpoints() != null) {
/*  87 */           processConnectionInfo(connectionInfo, process.getNodes());
/*     */         }
/*     */       }
/*     */     }
/*  91 */     return processInfo;
/*     */   }
/*     */ 
/*     */   private boolean processNodeInfo(BPMNShapeHandler.NodeInfo nodeInfo, org.kie.api.definition.process.Node[] nodes) {
/*  95 */     if ((nodeInfo == null) || (nodeInfo.getNodeRef() == null)) {
/*  96 */       return false;
/*     */     }
/*  98 */     for (org.kie.api.definition.process.Node node : nodes) {
/*  99 */       String id = (String)node.getMetaData().get("UniqueId");
/* 100 */       if (nodeInfo.getNodeRef().equals(id)) {
/* 101 */         ((org.jbpm.workflow.core.Node)node).setMetaData("x", nodeInfo.getX());
/* 102 */         ((org.jbpm.workflow.core.Node)node).setMetaData("y", nodeInfo.getY());
/* 103 */         ((org.jbpm.workflow.core.Node)node).setMetaData("width", nodeInfo.getWidth());
/* 104 */         ((org.jbpm.workflow.core.Node)node).setMetaData("height", nodeInfo.getHeight());
/* 105 */         return true;
/*     */       }
/* 107 */       if ((node instanceof NodeContainer)) {
/* 108 */         boolean found = processNodeInfo(nodeInfo, ((NodeContainer)node).getNodes());
/* 109 */         if (found) {
/* 110 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 114 */     return false;
/*     */   }
/*     */ 
/*     */   private void postProcessNodeOffset(org.kie.api.definition.process.Node[] nodes, int xOffset, int yOffset) {
/* 118 */     for (org.kie.api.definition.process.Node node : nodes) {
/* 119 */       Integer x = (Integer)node.getMetaData().get("x");
/* 120 */       if (x != null) {
/* 121 */         ((org.jbpm.workflow.core.Node)node).setMetaData("x", Integer.valueOf(x.intValue() - xOffset));
/*     */       }
/* 123 */       Integer y = (Integer)node.getMetaData().get("y");
/* 124 */       if (y != null) {
/* 125 */         ((org.jbpm.workflow.core.Node)node).setMetaData("y", Integer.valueOf(y.intValue() - yOffset));
/*     */       }
/* 127 */       if ((node instanceof NodeContainer))
/* 128 */         postProcessNodeOffset(((NodeContainer)node).getNodes(), xOffset + (x == null ? 0 : x.intValue()), yOffset + (y == null ? 0 : y.intValue()));
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean processConnectionInfo(BPMNEdgeHandler.ConnectionInfo connectionInfo, org.kie.api.definition.process.Node[] nodes)
/*     */   {
/* 134 */     for (org.kie.api.definition.process.Node node : nodes) {
/* 135 */       for (List<Connection> connections : node.getOutgoingConnections().values()) {
/* 136 */         for (Connection connection : connections) {
/* 137 */           String id = (String)connection.getMetaData().get("UniqueId");
/* 138 */           if ((id != null) && (id.equals(connectionInfo.getElementRef()))) {
/* 139 */             ((ConnectionImpl)connection).setMetaData("bendpoints", connectionInfo
/* 140 */               .getBendpoints());
/* 141 */             return true;
/*     */           }
/*     */         }
/*     */       }
/* 145 */       if ((node instanceof NodeContainer)) {
/* 146 */         boolean found = processConnectionInfo(connectionInfo, ((NodeContainer)node).getNodes());
/* 147 */         if (found) {
/* 148 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 152 */     return false;
/*     */   }
/*     */ 
/*     */   public Class<?> generateNodeFor() {
/* 156 */     return ProcessInfo.class;
/*     */   }
/*     */ 
/*     */   public static class ProcessInfo
/*     */   {
/*     */     private String processRef;
/* 162 */     private List<BPMNShapeHandler.NodeInfo> nodeInfos = new ArrayList();
/* 163 */     private List<BPMNEdgeHandler.ConnectionInfo> connectionInfos = new ArrayList();
/*     */ 
/*     */     public ProcessInfo(String processRef) {
/* 166 */       this.processRef = processRef;
/*     */     }
/*     */ 
/*     */     public String getProcessRef() {
/* 170 */       return this.processRef;
/*     */     }
/*     */ 
/*     */     public void addNodeInfo(BPMNShapeHandler.NodeInfo nodeInfo) {
/* 174 */       this.nodeInfos.add(nodeInfo);
/*     */     }
/*     */ 
/*     */     public List<BPMNShapeHandler.NodeInfo> getNodeInfos() {
/* 178 */       return this.nodeInfos;
/*     */     }
/*     */ 
/*     */     public void addConnectionInfo(BPMNEdgeHandler.ConnectionInfo connectionInfo) {
/* 182 */       this.connectionInfos.add(connectionInfo);
/*     */     }
/*     */ 
/*     */     public List<BPMNEdgeHandler.ConnectionInfo> getConnectionInfos() {
/* 186 */       return this.connectionInfos;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.di.BPMNPlaneHandler
 * JD-Core Version:    0.6.0
 */