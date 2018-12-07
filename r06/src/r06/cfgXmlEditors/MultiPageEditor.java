package r06.cfgXmlEditors;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Hashtable;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ControlEditor;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

import r06.Activator;
import r06.propertiesWizards.testdemo.TableItemControls;

/**
 * An example showing how to create a multi-page editor.
 * This example has 3 pages:
 * <ul>
 * <li>page 0 contains a nested text editor.
 * <li>page 1 allows you to change the font used in page 2
 * <li>page 2 shows the words in page 0 in sorted order
 * </ul>
 */
public class MultiPageEditor extends MultiPageEditorPart implements IResourceChangeListener{

	/** The text editor used in page 0. */
	private CFGEditor editor;

	/** The font chosen in page 1. */
	private Font font;

	/** The text widget used in page 2. */
	private StyledText text;
	/*private boolean dirty=false;
	private String editorText1="";
	private String editorText="";
	private ViewForm viewForm = null;
	private ToolBar toolBar = null;
	private Composite composite = null;
	private Table table = null;
	private Menu menu = null;
	private Hashtable<TableItem, TableItemControls> tablecontrols = new Hashtable<TableItem, TableItemControls>();
	*/
	/**
	 * Creates a multi-page editor example.
	 */
	public MultiPageEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}
	/**
	 * Creates page 0 of the multi-page editor,
	 * which contains a text editor.
	 */
	void createPage0() {
		try {
			editor = new CFGEditor();
			int index = addPage(editor, getEditorInput());
			setPageText(index, editor.getTitle());
		} catch (PartInitException e) {
			ErrorDialog.openError(
				getSite().getShell(),
				"Error creating nested text editor",
				null,
				e.getStatus());
		}
	}
	/**
	 * Creates page 1 of the multi-page editor,
	 * which allows you to change the font used in page 2.
	 */
	void createPage1() {

		Composite composite = new Composite(getContainer(), SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		layout.numColumns = 5;
		
		Group group1 = new Group(composite, SWT.SHADOW_ETCHED_OUT);
		group1.setLayout(layout);
		group1.setText("设置plan：");
		/*final Label salience = new Label(group1, SWT.LEFT);
		salience.setText("salience");		   
		   // 创建可编辑Text  
        final Text text = new Text(group1, SWT.BORDER);  
        // 创建不可编辑Text组件，用于输出提示信息  
        final Text hintText = new Text(group1, SWT.READ_ONLY);
        final Text oldText1 = new Text(group1, SWT.READ_ONLY);
        final Text oldTextrow1 = new Text(group1, SWT.READ_ONLY);
        oldText1.setVisible(false);
        oldTextrow1.setVisible(false);*/
        
        //hintText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));  
        // 为text组件加入文本修改事件监听器 
        //salience 作用是用来设置规则执行的优先级，salience 属性的值是一个数字，数字越大执行优先级越高，同时它的值可以是一个负数。默认情况下，规则的 salience 默认值为 0，所以如果我们不手动设置规则的 salience 属性，那么它的执行顺序是随机的。
        /*text.addModifyListener(new ModifyListener() {  
            public void modifyText(ModifyEvent e) {  
                String string = text.getText();  
                int index = 0;  
                // 遍历文本内容，检查是否为字母或数字  
                while (index < string.length()) {  
                    char ch = string.charAt(index);  
                    if (!Character.isDigit(ch))  
                        break;  
                    index++;  
                }  
                // 若text中输入不合法，则输出错误提示信息  
                if (index != string.length()) {  
                    hintText.setText("invalid input");  
                } else {  
                    hintText.setText("");  
                }  
            }  
        }); 
        
        Label lockOnActive =new Label(group1, SWT.LEFT);
        lockOnActive.setText("lock-on-active");
        final Text oldText2 = new Text(group1, SWT.READ_ONLY);
        final Text oldTextrow2 = new Text(group1, SWT.READ_ONLY);
        oldText2.setVisible(false);
        oldTextrow2.setVisible(false);
        final Combo combo = new Combo(group1, SWT.SIMPLE);
        String[] items = new String[2];
        for (int i = 0; i < items.length; i++)
         items[i] = "选项" + i;
        
        items[0]="true";
        items[1]="false";
        combo.setItems(items);
        // 注册键盘事件
        combo.addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent e) {
          // 如果单击了向左的箭头按键，则选中上一个选项
          if (e.keyCode == SWT.ARROW_LEFT)
           combo.select(combo.getSelectionIndex() - 1);
          // 如果单击了向右的箭头按键，则选中下一个选项
          else if (e.keyCode == SWT.ARROW_RIGHT)
           combo.select(combo.getSelectionIndex() + 1);
         }
        });*/
        
        /*String editorText =
				editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
        MessageBox messageBox = 
				  new MessageBox(getSite().getShell(), SWT.OK| 
						    
						    SWT.ICON_WARNING); 
				
				
				messageBox.setMessage(editorText);*/ 
        
    	
    	
      
		Button okButton = new Button(composite, SWT.PUSH | SWT.LEFT);
		GridData gd = new GridData(GridData.BEGINNING);
		gd.horizontalSpan = 2;
		okButton.setLayoutData(gd);
		okButton.setText("confirm");
		okButton.setToolTipText("make it effective"); //悬浮提示
		
		okButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				//setFont();
				
				//modify cfg content
				
				String editorText =
						editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
				//System.out.println(editorText);
				
				
				
		    	
		    	System.out.println("new Contents:\n"+editorText);
		    	
				//SWT有不同类型的对话框。有些对话框具有特殊的属性。
				MessageBox messageBox = 
				  new MessageBox(getSite().getShell(), SWT.OK| 
						    
						    SWT.ICON_WARNING); 
				
				
				messageBox.setMessage(editorText); 
				editor.getDocumentProvider().getDocument(editor.getEditorInput()).set(editorText);
				if (messageBox.open() == SWT.OK) 
				{ 
				  System.out.println("Ok is pressed."); 
				  //doSave(monitor);
				  
				}
				IWorkbenchPage page = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
				  page.saveEditor(page.getActiveEditor(), true);
				
				
				
			}
		});		
		
		int index = addPage(composite);
		setPageText(index, "planSetWindow");
	}
	/**
	 * Creates page 2 of the multi-page editor,
	 * which shows the sorted text.
	 */
	void createPage2() {
		Composite composite = new Composite(getContainer(), SWT.NONE);
		FillLayout layout = new FillLayout();
		composite.setLayout(layout);
		text = new StyledText(composite, SWT.H_SCROLL | SWT.V_SCROLL);
		text.setEditable(false);

		int index = addPage(composite);
		setPageText(index, "Preview");
	}
	/**
	 * Creates the pages of the multi-page editor.
	 */
	protected void createPages() {
		createPage0();
		createPage1();
		//createPage2();
	}
	/**
	 * The <code>MultiPageEditorPart</code> implementation of this 
	 * <code>IWorkbenchPart</code> method disposes all nested editors.
	 * Subclasses may extend.
	 */
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}
	/**
	 * Saves the multi-page editor's document.
	 */
	public void doSave(IProgressMonitor monitor) {
		getEditor(0).doSave(monitor);
	}
	/**
	 * Saves the multi-page editor's document as another file.
	 * Also updates the text for page 0's tab, and updates this multi-page editor's input
	 * to correspond to the nested editor's.
	 */
	public void doSaveAs() {
		CFGEditor editor = (CFGEditor) getEditor(0);
		editor.doSaveAs();
		setPageText(0, editor.getTitle());
		setInput(editor.getEditorInput());
	}
	/* (non-Javadoc)
	 * Method declared on IEditorPart
	 */
	public void gotoMarker(IMarker marker) {
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}
	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method
	 * checks that the input is an instance of <code>IFileEditorInput</code>.
	 */
	public void init(IEditorSite site, IEditorInput editorInput)
		throws PartInitException {
		if (!(editorInput instanceof IFileEditorInput))
			throw new PartInitException("Invalid Input: Must be IFileEditorInput");
		super.init(site, editorInput);
	}
	/* (non-Javadoc)
	 * Method declared on IEditorPart.
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}
	/**
	 * Calculates the contents of page 2 when the it is activated.
	 */
	 protected void pageChange(int newPageIndex) {
		  super.pageChange(newPageIndex); 
		  if (newPageIndex == 1) 
		  {   
			  String editorText = editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
		      System.err.println("new editorText:\n"+editorText);
		      /*if(!editorText.equals(editorText1)){
		    	  dirty=true;
		      }else{
		    	  dirty=false;
		      }
		      System.err.println("dirty:"+dirty);
		      StringBuffer strbuf=new StringBuffer();
			  if(dirty){
				  
				// 首先获得表格中所有的行
					TableItem[] items0 = table.getItems();
					// 循环所有行
					for (int i = items0.length - 1; i >= 0; i--) {
						items0[i].setChecked(true);
						// 如果该行没有被选中，继续循环
						if (!items0[i].getChecked())
							continue;
						// 否则选中，查找该表格中是否有该行
						int index = table.indexOf(items0[i]);
						// 如果没有该行，继续循环
						if (index < 0)
							continue;
						// 删除绑定的控件
						TableItemControls cons = tablecontrols.get(items0[index]);
						if (cons != null) {
							cons.dispose();
							tablecontrols.remove(items0[index]);
							System.out.println("dispose " + index);
						}
						// 如果有该行，删除该行
						// items[index].dispose();
						table.remove(index);
						System.out.println("i=" + i + ", index=" + index);
						System.out.println("行数:" + table.getItemCount());
						
						Rectangle  rec=table.getBounds();
						table.pack();
						table.setHeaderVisible(true);
						table.setBounds(rec);
						
					}
					//解析editorText
				   
					 BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(editorText.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));  
					 String line;  
					 //StringBuffer strbuf=new StringBuffer();
					 String note = "";
					 try{
						 
					 while ( (line = br.readLine()) != null ) {  
					     if(!line.trim().equals("")){
					    	 line="<br>"+line;//每行可以做加工
					    	 strbuf.append(line+"\r\n");
					    	 
					    	 
					    	 if(line.startsWith("#")){
					    		 note=line;
					    		 
					    	 }
					    	 if(line.contains("=")){
					    		String[] s=new String[4];
					    		TableItem item = new TableItem(table, SWT.NONE);
					    		String[] s1=line.trim().split("=");
					 			s[0]=s1[0];
					 			s[1]=s1[1];
					 			s[2]=note;
					 			s[3]="PopupMenu";
					 			item.setText(s);
						    }
					    	 
					    	 
					     }       		    	 
					 } 
					 }catch(Exception e){
						 System.out.println(e.getMessage());
						 
					 }
				    // System.out.println(strbuf.toString());
				     
					
					 
					
						TableItem[] items = table.getItems();
						for (int i = 0; i < items.length; i++) {
							// 第一列设置，创建TableEditor对象
							final TableEditor editor = new TableEditor(table);
							// 创建一个文本框，用于输入文字
							final Text text = new Text(table, SWT.NONE);
							// 将文本框当前值，设置为表格中的值
							text.setText(items[i].getText(0));
							// 设置编辑单元格水平填充
							editor.grabHorizontal = true;
							// 关键方法，将编辑单元格与文本框绑定到表格的第一列
							editor.setEditor(text, items[i], 0);
							// 当文本框改变值时，注册文本框改变事件，该事件改变表格中的数据。
							// 否则即使改变的文本框的值，对表格中的数据也不会影响
							text.addModifyListener(new ModifyListener() {
								public void modifyText(ModifyEvent e) {
									editor.getItem().setText(1, text.getText());
								}

							});

							// 第二列设置，创建TableEditor对象
							final TableEditor editor1 = new TableEditor(table);
							// 创建一个文本框，用于输入文字
							final Text text1 = new Text(table, SWT.NONE);
							// 将文本框当前值，设置为表格中的值
							text1.setText(items[i].getText(1));
							// 设置编辑单元格水平填充
							editor1.grabHorizontal = true;
							// 关键方法，将编辑单元格与文本框绑定到表格的第二列
							editor1.setEditor(text1, items[i], 1);
							// 当文本框改变值时，注册文本框改变事件，该事件改变表格中的数据。
							// 否则即使改变的文本框的值，对表格中的数据也不会影响
							text1.addModifyListener(new ModifyListener() {
								public void modifyText(ModifyEvent e) {
									editor1.getItem().setText(1, text1.getText());
								}

							});
							// 第三列设置，创建TableEditor对象
							final TableEditor editor2 = new TableEditor(table);
							// 创建一个文本框，用于输入文字
							final Text text2 = new Text(table, SWT.NONE);
							// 将文本框当前值，设置为表格中的值
							text2.setText(items[i].getText(2));
							// 设置编辑单元格水平填充
							editor2.grabHorizontal = true;
							// 关键方法，将编辑单元格与文本框绑定到表格的第二列
							editor2.setEditor(text2, items[i], 2);
							// 当文本框改变值时，注册文本框改变事件，该事件改变表格中的数据。
							// 否则即使改变的文本框的值，对表格中的数据也不会影响
							text2.addModifyListener(new ModifyListener() {
								public void modifyText(ModifyEvent e) {
									editor1.getItem().setText(1, text2.getText());
								}

							});

							// 保存TableItem与绑定Control的对应关系，删除TableItem时使用
							TableItemControls cons = new TableItemControls(text, text1, text2, editor1);
							tablecontrols.put(items[i], cons);
							final TableCursor cursor = new TableCursor(table, SWT.NONE);
							// 创建可编辑的控件
							final ControlEditor editor3 = new ControlEditor(cursor);
							editor3.grabHorizontal = true;
							editor3.grabVertical = true;
							// 为TableCursor对象注册事件
							cursor.addSelectionListener(new SelectionAdapter() {
								// 但移动光标，在单元格上单击回车所触发的事件
								public void widgetDefaultSelected(SelectionEvent e) {
									// 创建一个文本框控件
									final Text text = new Text(cursor, SWT.NONE);
									// 获得当前光标所在的行TableItem对象
									TableItem row = cursor.getRow();
									// 获得当前光标所在的列数
									int column = cursor.getColumn();
									// 当前光标所在单元格的值赋给文本框
									text.setText(row.getText(column));
									// 为文本框注册键盘事件
									text.addKeyListener(new KeyAdapter() {
										public void keyPressed(KeyEvent e) {
											// 此时在文本框上单击回车后，这是表格中的数据为修改后文本框中的数据
											// 然后释放文本框资源
											if (e.character == SWT.CR) {
												TableItem row = cursor.getRow();
												int column = cursor.getColumn();
												row.setText(column, text.getText());
												text.dispose();
											}
											// 如果在文本框中单击了ESC键，则并不对表格中的数据进行修改
											if (e.character == SWT.ESC) {
												text.dispose();
											}
										}
									});
									// 注册焦点事件
									text.addFocusListener(new FocusAdapter() {
										// 当该文本框失去焦点时，释放文本框资源
										public void focusLost(FocusEvent e) {
											text.dispose();
										}
									});
									// 将该文本框绑定到可编辑的控件上
									editor3.setEditor(text);
									// 设置文本框的焦点
									text.setFocus();
								}

								// 移动光标到一个单元格上所触发的事件
								public void widgetSelected(SelectionEvent e) {
									table.setSelection(new TableItem[] { cursor.getRow() });
								}
							});
							cursor.addMouseListener(new MouseListener() {

								@Override
								public void mouseDoubleClick(MouseEvent e) {
									// TODO Auto-generated method stub

								}

								@Override
								public void mouseDown(MouseEvent e) {
									if (e.button == 3) { // 右键按下，显示右键菜单
										menu.setVisible(true);
									}
									if (e.button == 1) { // 左键按下，显示右键菜单
										menu.setVisible(true);
										
									}
									
								}

								@Override
								public void mouseUp(MouseEvent e) {
									// TODO Auto-generated method stub

								}

							});*/
							// ******************************************************/
							
							// /****************************************************
							// 为单元格注册选中事件
	                        /*table.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent e) {
									// 获得所有的行数
									int total = table.getItemCount();
									// 循环所有行
									for (int i = 0; i < total; i++) {
										TableItem item = table.getItem(i);
										// 如果该行为选中状态，改变背景色和前景色，否则颜色设置
										if (table.isSelected(i)) {
											item.setBackground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_RED));
											item.setForeground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_WHITE));
										} else {
											item.setBackground(null);
											item.setForeground(null);
										}
									}
								}

							});	
					 
					//table.pack();
							

						}	
						
				}*/
		  
			  
		  } 
		}
	/**
	 * Closes all project files on project close.
	 */
	public void resourceChanged(final IResourceChangeEvent event){
		if(event.getType() == IResourceChangeEvent.PRE_CLOSE){
			Display.getDefault().asyncExec(new Runnable(){
				public void run(){
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
					for (int i = 0; i<pages.length; i++){
						if(((FileEditorInput)editor.getEditorInput()).getFile().getProject().equals(event.getResource())){
							IEditorPart editorPart = pages[i].findEditor(editor.getEditorInput());
							pages[i].closeEditor(editorPart,true);
						}
					}
				}            
			});
		}
	}
	
}
