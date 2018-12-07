/*     */ package org.jbpm.bpmn2.handler;
/*     */ 
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import org.kie.api.runtime.process.WorkItem;
/*     */ import org.kie.api.runtime.process.WorkItemHandler;
/*     */ import org.kie.api.runtime.process.WorkItemManager;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class LoggingTaskHandlerDecorator extends AbstractExceptionHandlingTaskHandler
/*     */ {
/*  54 */   private static final Logger logger = LoggerFactory.getLogger(LoggingTaskHandlerDecorator.class);
/*  55 */   private int loggedExceptionsLimit = 100;
/*  56 */   private Queue<WorkItemExceptionInfo> exceptionInfoList = new ArrayDeque(this.loggedExceptionsLimit);
/*     */ 
/*  58 */   private String configuredMessage = "{0} thrown when work item {1} ({2}) was {3}ed in process instance {4}.";
/*  59 */   private List<InputParameter> configuredInputList = new ArrayList();
/*  60 */   private boolean printStackTrace = true;
/*     */ 
/*     */   public LoggingTaskHandlerDecorator(Class<? extends WorkItemHandler> originalTaskHandlerClass, int logLimit)
/*     */   {
/*  72 */     super(originalTaskHandlerClass);
/*  73 */     initializeExceptionInfoList(logLimit);
/*     */   }
/*     */ 
/*     */   public LoggingTaskHandlerDecorator(Class<? extends WorkItemHandler> originalTaskHandlerClass)
/*     */   {
/*  86 */     super(originalTaskHandlerClass);
/*     */   }
/*     */ 
/*     */   public LoggingTaskHandlerDecorator(WorkItemHandler originalTaskHandler)
/*     */   {
/*  97 */     super(originalTaskHandler);
/*     */   }
/*     */ 
/*     */   public synchronized void setLoggedMessageFormat(String logMessageFormat)
/*     */   {
/* 139 */     this.configuredMessage = logMessageFormat;
/*     */   }
/*     */ 
/*     */   public synchronized void setLoggedMessageInput(List<InputParameter> inputParameterList)
/*     */   {
/* 154 */     this.configuredInputList = inputParameterList;
/*     */   }
/*     */ 
/*     */   public synchronized void setLoggedExceptionInfoListSize(int loggedExceptionInfoListSize) {
/* 158 */     initializeExceptionInfoList(loggedExceptionInfoListSize);
/*     */   }
/*     */ 
/*     */   public synchronized void setPrintStackTrace(boolean printStackTrace) {
/* 162 */     this.printStackTrace = printStackTrace;
/*     */   }
/*     */ 
/*     */   private void initializeExceptionInfoList(int listSize) {
/* 166 */     this.loggedExceptionsLimit = listSize;
/* 167 */     Queue newExceptionInfoList = new ArrayDeque(this.loggedExceptionsLimit + 1);
/* 168 */     newExceptionInfoList.addAll(this.exceptionInfoList);
/* 169 */     this.exceptionInfoList = newExceptionInfoList;
/*     */   }
/*     */ 
/*     */   public synchronized List<WorkItemExceptionInfo> getWorkItemExceptionInfoList() {
/* 173 */     return new ArrayList(this.exceptionInfoList);
/*     */   }
/*     */ 
/*     */   public synchronized void handleExecuteException(Throwable cause, WorkItem workItem, WorkItemManager manager)
/*     */   {
/* 178 */     if (this.exceptionInfoList.size() == this.loggedExceptionsLimit) {
/* 179 */       this.exceptionInfoList.poll();
/*     */     }
/* 181 */     this.exceptionInfoList.add(new WorkItemExceptionInfo(workItem, cause, true));
/* 182 */     logMessage(true, workItem, cause);
/*     */   }
/*     */ 
/*     */   public synchronized void handleAbortException(Throwable cause, WorkItem workItem, WorkItemManager manager)
/*     */   {
/* 187 */     if (this.exceptionInfoList.size() == this.loggedExceptionsLimit) {
/* 188 */       this.exceptionInfoList.poll();
/*     */     }
/* 190 */     this.exceptionInfoList.add(new WorkItemExceptionInfo(workItem, cause, false));
/* 191 */     logMessage(false, workItem, cause);
/*     */   }
/*     */ 
/*     */   private void logMessage(boolean onExecute, WorkItem workItem, Throwable cause) {
/* 195 */     String handlerMethodStem = "execut";
/* 196 */     if (!onExecute) {
/* 197 */       handlerMethodStem = "abort";
/*     */     }
/*     */ 
/* 200 */     if ((cause instanceof WorkItemHandlerRuntimeException)) {
/* 201 */       cause = cause.getCause();
/*     */     }
/*     */ 
/* 204 */     List inputList = new ArrayList();
/* 205 */     if (this.configuredInputList.isEmpty())
/*     */     {
/* 207 */       if (workItem.getParameter("Interface") != null) {
/* 208 */         this.configuredMessage = "{0}.{1} threw {2} when {3}ing work item {4} in process instance {5}.";
/* 209 */         inputList.add((String)workItem.getParameter("Interface"));
/* 210 */         inputList.add((String)workItem.getParameter("Operation"));
/* 211 */         inputList.add(cause.getClass().getSimpleName());
/* 212 */         inputList.add(handlerMethodStem);
/* 213 */         inputList.add(String.valueOf(workItem.getId()));
/* 214 */         inputList.add(String.valueOf(workItem.getProcessInstanceId()));
/*     */       }
/*     */       else
/*     */       {
/* 218 */         inputList.add(cause.getClass().getSimpleName());
/* 219 */         inputList.add(String.valueOf(workItem.getId()));
/* 220 */         inputList.add(workItem.getName());
/* 221 */         inputList.add(handlerMethodStem);
/* 222 */         inputList.add(String.valueOf(workItem.getProcessInstanceId()));
/*     */       }
/*     */     }
/*     */     else {
/* 226 */       for (InputParameter inputType : this.configuredInputList) {
/* 227 */         switch (inputType.ordinal()) {
/*     */         case 1:
/* 229 */           inputList.add(cause.getClass().getSimpleName());
/* 230 */           break;
/*     */         case 2:
/* 232 */           inputList.add(getOriginalTaskHandler().getClass().getSimpleName());
/* 233 */           break;
/*     */         case 3:
/* 235 */           inputList.add(onExecute ? "execut" : "abort");
/* 236 */           break;
/*     */         case 4:
/* 238 */           inputList.add(String.valueOf(workItem.getId()));
/* 239 */           break;
/*     */         case 5:
/* 241 */           inputList.add(workItem.getName());
/* 242 */           break;
/*     */         case 6:
/* 244 */           StringBuilder parameters = new StringBuilder();
/* 245 */           for (String param : workItem.getParameters().keySet()) {
/* 246 */             parameters.append(new StringBuilder().append(param).append(" : ").append(workItem.getParameters().get(param)).append(", ").toString());
/*     */           }
/* 248 */           inputList.add(parameters.substring(0, parameters.length() - 2));
/* 249 */           break;
/*     */         case 7:
/* 251 */           inputList.add(String.valueOf(workItem.getProcessInstanceId()));
/* 252 */           break;
/*     */         case 8:
/* 254 */           inputList.add((String)workItem.getParameter("Interface"));
/* 255 */           break;
/*     */         case 9:
/* 257 */           inputList.add((String)workItem.getParameter("Operation"));
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 263 */     String message = MessageFormat.format(this.configuredMessage, inputList.toArray());
/*     */ 
/* 265 */     if (this.printStackTrace)
/* 266 */       logger.warn(message, cause);
/*     */     else
/* 268 */       logger.warn(message);
/*     */   }
/*     */ 
/*     */   public static enum InputParameter
/*     */   {
/* 362 */     WORK_ITEM_ID, WORK_ITEM_NAME, 
/* 363 */     WORK_ITEM_METHOD, WORK_ITEM_HANDLER_TYPE, 
/* 364 */     WORK_ITEM_PARAMETERS, 
/*     */ 
/* 366 */     SERVICE, OPERATION, 
/*     */ 
/* 368 */     PROCESS_INSTANCE_ID, EXCEPTION_CLASS;
/*     */   }
/*     */ 
/*     */   public class WorkItemExceptionInfo
/*     */   {
/*     */     private final Throwable cause;
/*     */     private final Date timeThrown;
/*     */     private final boolean onExecute;
/*     */     private final long processInstanceId;
/*     */     private final long workItemId;
/*     */     private final String workItemName;
/*     */     private final Map<String, Object> workItemParameters;
/*     */ 
/*     */     public WorkItemExceptionInfo(WorkItem workItem, Throwable cause, boolean onExecute)
/*     */     {
/* 284 */       this.timeThrown = new Date();
/* 285 */       this.cause = cause;
/* 286 */       this.onExecute = onExecute;
/*     */ 
/* 288 */       this.processInstanceId = workItem.getProcessInstanceId();
/*     */ 
/* 290 */       this.workItemId = workItem.getId();
/* 291 */       this.workItemName = workItem.getName();
/* 292 */       this.workItemParameters = Collections.unmodifiableMap(workItem.getParameters());
/*     */     }
/*     */ 
/*     */     public Throwable getException() {
/* 296 */       return this.cause;
/*     */     }
/*     */ 
/*     */     public Date getTimeThrown() {
/* 300 */       return this.timeThrown;
/*     */     }
/*     */ 
/*     */     public boolean onExecute() {
/* 304 */       return this.onExecute;
/*     */     }
/*     */ 
/*     */     public long getProcessInstanceId() {
/* 308 */       return this.processInstanceId;
/*     */     }
/*     */ 
/*     */     public long getWorkItemId() {
/* 312 */       return this.workItemId;
/*     */     }
/*     */ 
/*     */     public String getWorkItemName() {
/* 316 */       return this.workItemName;
/*     */     }
/*     */ 
/*     */     public Map<String, Object> getWorkItemParameters() {
/* 320 */       return this.workItemParameters;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.handler.LoggingTaskHandlerDecorator
 * JD-Core Version:    0.6.0
 */