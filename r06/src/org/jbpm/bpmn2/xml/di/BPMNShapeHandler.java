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
/*     */ public class BPMNShapeHandler extends BaseAbstractHandler
/*     */   implements Handler
/*     */ {
/*     */   public BPMNShapeHandler()
/*     */   {
/*  33 */     initValidParents();
/*  34 */     initValidPeers();
/*  35 */     this.allowNesting = true;
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
/*  46 */     this.validPeers.add(NodeInfo.class);
/*  47 */     this.validPeers.add(BPMNEdgeHandler.ConnectionInfo.class);
/*     */   }
/*     */ 
/*     */   public Object start(String uri, String localName, Attributes attrs, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/*  53 */     parser.startElementBuilder(localName, attrs);
/*  54 */     String elementRef = attrs.getValue("bpmnElement");
/*  55 */     NodeInfo nodeInfo = new NodeInfo(elementRef);
/*  56 */     BPMNPlaneHandler.ProcessInfo processInfo = (BPMNPlaneHandler.ProcessInfo)parser.getParent();
/*  57 */     processInfo.addNodeInfo(nodeInfo);
/*  58 */     return nodeInfo;
/*     */   }
/*     */ 
/*     */   public Object end(String uri, String localName, ExtensibleXmlParser parser) throws SAXException
/*     */   {
/*  63 */     Element element = parser.endElementBuilder();
/*  64 */     NodeInfo nodeInfo = (NodeInfo)parser.getCurrent();
/*  65 */     Node xmlNode = element.getFirstChild();
/*  66 */     while ((xmlNode instanceof Element)) {
/*  67 */       String nodeName = xmlNode.getNodeName();
/*  68 */       if ("Bounds".equals(nodeName))
/*     */       {
/*  70 */         String x = ((Element)xmlNode).getAttribute("x");
/*  71 */         String y = ((Element)xmlNode).getAttribute("y");
/*  72 */         String width = ((Element)xmlNode).getAttribute("width");
/*  73 */         String height = ((Element)xmlNode).getAttribute("height");
/*     */         try {
/*  75 */           int xValue = 0;
/*  76 */           if ((x != null) && (x.trim().length() != 0)) {
/*  77 */             xValue = new Float(x).intValue();
/*     */           }
/*  79 */           int yValue = 0;
/*  80 */           if ((y != null) && (y.trim().length() != 0)) {
/*  81 */             yValue = new Float(y).intValue();
/*     */           }
/*  83 */           int widthValue = 20;
/*  84 */           if ((width != null) && (width.trim().length() != 0)) {
/*  85 */             widthValue = new Float(width).intValue();
/*     */           }
/*  87 */           int heightValue = 20;
/*  88 */           if ((height != null) && (height.trim().length() != 0)) {
/*  89 */             heightValue = new Float(height).intValue();
/*     */           }
/*  91 */           nodeInfo.setX(Integer.valueOf(xValue));
/*  92 */           nodeInfo.setY(Integer.valueOf(yValue));
/*  93 */           nodeInfo.setWidth(Integer.valueOf(widthValue));
/*  94 */           nodeInfo.setHeight(Integer.valueOf(heightValue));
/*     */         } catch (NumberFormatException e) {
/*  96 */           throw new IllegalArgumentException("Invalid bounds for node " + nodeInfo.getNodeRef(), e);
/*     */         }
/*     */       }
/*  99 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/* 101 */     return parser.getCurrent();
/*     */   }
/*     */ 
/*     */   public Class<?> generateNodeFor() {
/* 105 */     return NodeInfo.class; } 
/*     */   public static class NodeInfo { private String nodeRef;
/*     */     private Integer x;
/*     */     private Integer y;
/*     */     private Integer width;
/*     */     private Integer height;
/*     */ 
/* 117 */     public NodeInfo(String nodeRef) { this.nodeRef = nodeRef; }
/*     */ 
/*     */     public String getNodeRef()
/*     */     {
/* 121 */       return this.nodeRef;
/*     */     }
/*     */ 
/*     */     public Integer getX() {
/* 125 */       return this.x;
/*     */     }
/*     */ 
/*     */     public void setX(Integer x) {
/* 129 */       this.x = x;
/*     */     }
/*     */ 
/*     */     public Integer getY() {
/* 133 */       return this.y;
/*     */     }
/*     */ 
/*     */     public void setY(Integer y) {
/* 137 */       this.y = y;
/*     */     }
/*     */ 
/*     */     public Integer getWidth() {
/* 141 */       return this.width;
/*     */     }
/*     */ 
/*     */     public void setWidth(Integer width) {
/* 145 */       this.width = width;
/*     */     }
/*     */ 
/*     */     public Integer getHeight() {
/* 149 */       return this.height;
/*     */     }
/*     */ 
/*     */     public void setHeight(Integer height) {
/* 153 */       this.height = height;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.di.BPMNShapeHandler
 * JD-Core Version:    0.6.0
 */