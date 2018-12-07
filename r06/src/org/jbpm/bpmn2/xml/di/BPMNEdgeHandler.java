/*     */ package org.jbpm.bpmn2.xml.di;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.drools.core.xml.BaseAbstractHandler;
/*     */ import org.drools.core.xml.ExtensibleXmlParser;
/*     */ import org.drools.core.xml.Handler;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class BPMNEdgeHandler extends BaseAbstractHandler
/*     */   implements Handler
/*     */ {
/*     */   public BPMNEdgeHandler()
/*     */   {
/*  33 */     initValidParents();
/*  34 */     initValidPeers();
/*  35 */     this.allowNesting = false;
/*     */   }
/*     */ 
/*     */   protected void initValidParents() {
/*  39 */     this.validParents = new HashSet();
/*  40 */     this.validParents.add(BPMNPlaneHandler.ProcessInfo.class);
/*     */   }
/*     */ 
/*     */   protected void initValidPeers() {
/*  44 */     this.validPeers = new HashSet();
/*  45 */     this.validPeers.add(null);
/*  46 */     this.validPeers.add(BPMNShapeHandler.NodeInfo.class);
/*  47 */     this.validPeers.add(ConnectionInfo.class);
/*     */   }
/*     */ 
/*     */   public Object start(String uri, String localName, Attributes attrs, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/*  53 */     parser.startElementBuilder(localName, attrs);
/*     */ 
/*  55 */     String elementRef = attrs.getValue("bpmnElement");
/*  56 */     ConnectionInfo info = new ConnectionInfo(elementRef);
/*  57 */     BPMNPlaneHandler.ProcessInfo processInfo = (BPMNPlaneHandler.ProcessInfo)parser.getParent();
/*  58 */     processInfo.addConnectionInfo(info);
/*  59 */     return info;
/*     */   }
/*     */ 
/*     */   public Object end(String uri, String localName, ExtensibleXmlParser parser) throws SAXException
/*     */   {
/*  64 */     Element element = parser.endElementBuilder();
/*     */ 
/*  66 */     String bendpoints = null;
/*  67 */     Node xmlNode = element.getFirstChild();
/*  68 */     while ((xmlNode instanceof Element)) {
/*  69 */       String nodeName = xmlNode.getNodeName();
/*  70 */       if ("waypoint".equals(nodeName))
/*     */       {
/*  72 */         String x = ((Element)xmlNode).getAttribute("x");
/*  73 */         String y = ((Element)xmlNode).getAttribute("y");
/*     */         try {
/*  75 */           int xValue = new Float(x).intValue();
/*  76 */           int yValue = new Float(y).intValue();
/*  77 */           if (bendpoints == null) {
/*  78 */             bendpoints = "[";
/*  79 */           } else if (xmlNode.getNextSibling() != null) {
/*  80 */             bendpoints = bendpoints + xValue + "," + yValue;
/*  81 */             bendpoints = bendpoints + ";";
/*     */           }
/*     */         } catch (NumberFormatException e) {
/*  84 */           throw new IllegalArgumentException("Invalid bendpoint value", e);
/*     */         }
/*     */       }
/*  87 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*  89 */     ConnectionInfo connectionInfo = (ConnectionInfo)parser.getCurrent();
/*  90 */     if ((bendpoints != null) && (bendpoints.length() > 1)) {
/*  91 */       connectionInfo.setBendpoints(bendpoints + "]");
/*     */     }
/*  93 */     return connectionInfo;
/*     */   }
/*     */ 
/*     */   public Class<?> generateNodeFor() {
/*  97 */     return ConnectionInfo.class;
/*     */   }
/*     */   public static class ConnectionInfo {
/*     */     private String elementRef;
/*     */     private String bendpoints;
/*     */ 
/*     */     public ConnectionInfo(String elementRef) {
/* 106 */       this.elementRef = elementRef;
/*     */     }
/*     */ 
/*     */     public String getElementRef() {
/* 110 */       return this.elementRef;
/*     */     }
/*     */ 
/*     */     public String getBendpoints() {
/* 114 */       return this.bendpoints;
/*     */     }
/*     */ 
/*     */     public void setBendpoints(String bendpoints) {
/* 118 */       this.bendpoints = bendpoints;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.di.BPMNEdgeHandler
 * JD-Core Version:    0.6.0
 */