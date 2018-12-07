/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import org.jbpm.process.core.impl.XmlProcessDumper;
/*    */ import org.jbpm.process.core.impl.XmlProcessDumperFactoryService;
/*    */ 
/*    */ public class XmlProcessDumperFactoryServiceImpl
/*    */   implements XmlProcessDumperFactoryService
/*    */ {
/*    */   public XmlProcessDumper newXmlProcessDumper()
/*    */   {
/* 25 */     return XmlBPMNProcessDumper.INSTANCE;
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.XmlProcessDumperFactoryServiceImpl
 * JD-Core Version:    0.6.0
 */