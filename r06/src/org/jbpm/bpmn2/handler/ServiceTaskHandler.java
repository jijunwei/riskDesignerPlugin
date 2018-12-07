/*     */ package org.jbpm.bpmn2.handler;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.kie.api.runtime.process.WorkItem;
/*     */ import org.kie.api.runtime.process.WorkItemHandler;
/*     */ import org.kie.api.runtime.process.WorkItemManager;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class ServiceTaskHandler
/*     */   implements WorkItemHandler
/*     */ {
/*  32 */   private static final Logger logger = LoggerFactory.getLogger(ServiceTaskHandler.class);
/*     */   private String resultVarName;
/*     */ 
/*     */   public ServiceTaskHandler()
/*     */   {
/*  37 */     this("Result");
/*     */   }
/*     */ 
/*     */   public ServiceTaskHandler(String resultVarName) {
/*  41 */     this.resultVarName = resultVarName;
/*     */   }
/*     */ 
/*     */   public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
/*  45 */     String service = (String)workItem.getParameter("Interface");
/*  46 */     String interfaceImplementationRef = (String)workItem.getParameter("interfaceImplementationRef");
/*  47 */     String operation = (String)workItem.getParameter("Operation");
/*  48 */     String parameterType = (String)workItem.getParameter("ParameterType");
/*  49 */     Object parameter = workItem.getParameter("Parameter");
/*     */ 
/*  51 */     String[] services = { service, interfaceImplementationRef };
/*  52 */     Class c = null;
/*     */ 
/*  54 */     for (String serv : services) {
/*     */       try {
/*  56 */         c = Class.forName(serv);
/*     */       }
/*     */       catch (ClassNotFoundException cnfe) {
/*  59 */         if (serv.compareTo(services[(services.length - 1)]) == 0) {
/*  60 */           handleException(cnfe, service, interfaceImplementationRef, operation, parameterType, parameter);
/*     */         }
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/*  66 */       Object instance = c.newInstance();
/*  67 */       Class[] classes = null;
/*  68 */       Object[] params = null;
/*  69 */       if (parameterType != null)
/*     */       {
/*  71 */         classes = new Class[] { 
/*  71 */           Class.forName(parameterType) };
/*     */ 
/*  73 */         params = new Object[] { parameter };
/*     */       }
/*     */ 
/*  77 */       Method method = c.getMethod(operation, classes);
/*  78 */       Object result = method.invoke(instance, params);
/*  79 */       Map results = new HashMap();
/*  80 */       results.put(this.resultVarName, result);
/*  81 */       manager.completeWorkItem(workItem.getId(), results);
/*     */     } catch (ClassNotFoundException cnfe) {
/*  83 */       handleException(cnfe, service, interfaceImplementationRef, operation, parameterType, parameter);
/*     */     } catch (InstantiationException ie) {
/*  85 */       handleException(ie, service, interfaceImplementationRef, operation, parameterType, parameter);
/*     */     } catch (IllegalAccessException iae) {
/*  87 */       handleException(iae, service, interfaceImplementationRef, operation, parameterType, parameter);
/*     */     } catch (NoSuchMethodException nsme) {
/*  89 */       handleException(nsme, service, interfaceImplementationRef, operation, parameterType, parameter);
/*     */     } catch (InvocationTargetException ite) {
/*  91 */       handleException(ite, service, interfaceImplementationRef, operation, parameterType, parameter);
/*     */     } catch (Throwable cause) {
/*  93 */       handleException(cause, service, interfaceImplementationRef, operation, parameterType, parameter);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void handleException(Throwable cause, String service, String interfaceImplementationRef, String operation, String paramType, Object param) {
/*  98 */     logger.debug("Handling exception {} inside service {} or {} and operation {} with param type {} and value {}", new Object[] { cause
/*  99 */       .getMessage(), service, operation, paramType, param });
/*     */     WorkItemHandlerRuntimeException wihRe;
/* 101 */     if ((cause instanceof InvocationTargetException)) {
/* 102 */       Throwable realCause = cause.getCause();
/* 103 */       wihRe = new WorkItemHandlerRuntimeException(realCause);
/* 104 */       wihRe.setStackTrace(realCause.getStackTrace());
/*     */     } else {
/* 106 */       wihRe = new WorkItemHandlerRuntimeException(cause);
/* 107 */       wihRe.setStackTrace(cause.getStackTrace());
/*     */     }
/* 109 */     wihRe.setInformation("Interface", service);
/* 110 */     wihRe.setInformation("InterfaceImplementationRef", interfaceImplementationRef);
/* 111 */     wihRe.setInformation("Operation", operation);
/* 112 */     wihRe.setInformation("ParameterType", paramType);
/* 113 */     wihRe.setInformation("Parameter", param);
/* 114 */     wihRe.setInformation("workItemHandlerType", getClass().getSimpleName());
/* 115 */     throw wihRe;
/*     */   }
/*     */ 
/*     */   public void abortWorkItem(WorkItem workItem, WorkItemManager manager)
/*     */   {
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.handler.ServiceTaskHandler
 * JD-Core Version:    0.6.0
 */