package r06.propertiesWizards;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.StringTokenizer;

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
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
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
import r06.drlEditor.JavaEditor;
import r06.propertiesWizards.testdemo.ImageFactory;
import r06.propertiesWizards.testdemo.TableItemControls;

/**
 * An example showing how to create a multi-page editor. This example has 3
 * pages:
 * <ul>
 * <li>page 0 contains a nested text editor.
 * <li>page 1 allows you to change the font used in page 2
 * <li>page 2 shows the words in page 0 in sorted order
 * </ul>
 */
public class MultiPageEditor extends MultiPageEditorPart implements IResourceChangeListener {

	/** The text editor used in page 0. */
	private JavaEditor editor;

	/** The font chosen in page 1. */
	private Font font;

	/** The text widget used in page 2. */
	private StyledText text;
	/**
	 * Creates a multi-page editor example.
	 * 
	 * 
	 */

	private ViewForm viewForm = null;
	private ToolBar toolBar = null;
	private Composite composite = null;
	private Table table = null;
	private Menu menu = null;
	private boolean dirty=false;
	private String editorText1="";
	private String editorText="";
	private Hashtable<TableItem, TableItemControls> tablecontrols = new Hashtable<TableItem, TableItemControls>();

	public MultiPageEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	/**
	 * Creates page 0 of the multi-page editor, which contains a text editor.
	 */
	void createPage0() {
		try {
			editor = new JavaEditor();
			int index = addPage(editor, getEditorInput());
			setPageText(index, editor.getTitle());
			editorText1 = editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
			System.err.println("old editorText:"+editorText1);
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), "Error creating nested text editor", null, e.getStatus());
		}
	}

	/**
	 * Creates page 1 of the multi-page editor, which allows you to change the
	 * font used in page 2.
	 */
	void createPage1() {

		Composite composite = new Composite(getContainer(), SWT.NONE);
		FillLayout layout = new FillLayout();
		composite.setLayout(layout);

		// 创建ViewForm面板放置工具栏和表格
		viewForm = new ViewForm(composite, SWT.NONE);
		viewForm.setTopCenterSeparate(true);
		createToolBar();
		viewForm.setTopLeft(toolBar);
		createComposite();

		createMenu();

		

		int index = addPage(composite);
		setPageText(index, "propertiesModifyWindow");
	}

	// 创建上下文菜单
	private void createMenu() {
		// 创建弹出式菜单
		menu = new Menu(getSite().getShell(), SWT.POP_UP);
		// 设置该菜单为表格菜单
		table.setMenu(menu);
		/*// 创建删除菜单项
		MenuItem del = new MenuItem(menu, SWT.PUSH);
		del.setText("删除");
		del.setImage(ImageFactory.loadImage(getSite().getShell().getDisplay(), ImageFactory.DELETE_EDIT));
		// 为删除菜单注册事件，当单击时，删除所选择的行
		del.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				// 此处需添加删除绑定Control的代码
				// table.remove (table.getSelectionIndices ());

			    TableItem[] items = table.getSelection();
			    for (int i=0;i<items.length;i++){
			    	
			      System.out.println(items[i].getText());
			    //查找该表格中是否有该行
			      int index = table.indexOf( items[i]);
			      System.out.println("index:"+index);
			      //删除绑定的控件
			      TableItemControls cons = tablecontrols.get(items[index]);
			      if (cons != null) {
			       cons.dispose();
			       tablecontrols.remove(items[index]);
			       System.out.println("dispose " + index);
			      }
			      //如果有该行，删除该行
//			      items[index].dispose();
			      table.remove( index );
			      System.out.println("i="+table.getSelectionIndices ()+", index="+index);
			      System.out.println("行数:" + table.getItemCount());
			     // table.pack();
			      
			    }
			}
		});*/

		// 创建查看菜单项
		MenuItem view = new MenuItem(menu, SWT.PUSH);
		view.setText("查看");
		view.setImage(ImageFactory.loadImage(getSite().getShell().getDisplay(), ImageFactory.SCOPY_EDIT));
		// 为查看菜单项注册事件，当单击时打印出所选的
		view.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				TableItem[] items = table.getSelection();

				StringBuffer s = new StringBuffer();

				//
				for (int i = 0; i < items.length; i++)
					/*for (int j = 0; j < table.getColumnCount() - 1; j++) {
						System.out.println(items[i].getText(j));
						s.append(items[i].getText(j) + "\n");
					}*/
					s.append(items[i].getText(2) + "\n"+items[i].getText(0)+"="+items[i].getText(1)+"\n");

				/*
				 * for (int i=0;i<items.length;i++){
				 * s.append(items[i].getText()); }
				 */
				// System.out.print(items[i].getText());
				MessageBox messageBox = new MessageBox(getSite().getShell(), SWT.OK |

				SWT.ICON_WARNING);

				messageBox.setMessage(s.toString());
				if (messageBox.open() == SWT.OK) {
					System.out.println("Ok is pressed.");

				}
				

			}
		});

		table.setMenu(menu);
	}

	// 创建工具栏
	private void createToolBar() {
		toolBar = new ToolBar(viewForm, SWT.FLAT);
		final ToolItem add = new ToolItem(toolBar, SWT.PUSH);
		add.setText("添加");
		add.setImage(ImageFactory.loadImage(toolBar.getDisplay(), ImageFactory.ADD_OBJ));
		final ToolItem del = new ToolItem(toolBar, SWT.PUSH);
		del.setText("删除");
		del.setImage(ImageFactory.loadImage(toolBar.getDisplay(), ImageFactory.DELETE_OBJ));

		final ToolItem save = new ToolItem(toolBar, SWT.PUSH);
		save.setText("保存");
		save.setImage(ImageFactory.loadImage(toolBar.getDisplay(), ImageFactory.SAVE_EDIT));
		// 工具栏按钮事件处理
		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				// 如果单击添加按钮，添加一行，在实际的项目实现中通常是接收输入的参数，案后添加
				// 这里为了简单起见，添加固定的一条记录
				if (event.widget == add) {
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(new String[] { "", "", "", "PopupMenu" });

				
						// 第一列设置，创建TableEditor对象
						final TableEditor editor = new TableEditor(table);
						// 创建一个文本框，用于输入文字
						final Text text = new Text(table, SWT.NONE);
						// 将文本框当前值，设置为表格中的值
						text.setText(item.getText(0));
						// 设置编辑单元格水平填充
						editor.grabHorizontal = true;
						// 关键方法，将编辑单元格与文本框绑定到表格的第一列
						editor.setEditor(text, item, 0);
						// 当文本框改变值时，注册文本框改变事件，该事件改变表格中的数据。
						// 否则即使改变的文本框的值，对表格中的数据也不会影响
						text.addModifyListener(new ModifyListener() {
							public void modifyText(ModifyEvent e) {
								editor.getItem().setText(0, text.getText());
							}

						});

						// 第二列设置，创建TableEditor对象
						final TableEditor editor1 = new TableEditor(table);
						// 创建一个文本框，用于输入文字
						final Text text1 = new Text(table, SWT.NONE);
						// 将文本框当前值，设置为表格中的值
						text1.setText(item.getText(1));
						// 设置编辑单元格水平填充
						editor1.grabHorizontal = true;
						// 关键方法，将编辑单元格与文本框绑定到表格的第二列
						editor1.setEditor(text1, item, 1);
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
						text2.setText(item.getText(2));
						// 设置编辑单元格水平填充
						editor2.grabHorizontal = true;
						// 关键方法，将编辑单元格与文本框绑定到表格的第二列
						editor2.setEditor(text2, item, 2);
						// 当文本框改变值时，注册文本框改变事件，该事件改变表格中的数据。
						// 否则即使改变的文本框的值，对表格中的数据也不会影响
						text2.addModifyListener(new ModifyListener() {
							public void modifyText(ModifyEvent e) {
								editor2.getItem().setText(2, text2.getText());
							}

						});

						// 保存TableItem与绑定Control的对应关系，删除TableItem时使用
						TableItemControls cons = new TableItemControls(text, text1, text2, editor1);
						tablecontrols.put(item, cons);

				
				}
				// 如果单击删除按钮
				else if (event.widget == del) {
					// 首先获得表格中所有的行
					TableItem[] items = table.getItems();
					// 循环所有行
					for (int i = items.length - 1; i >= 0; i--) {
						// 如果该行没有被选中，继续循环
						if (!items[i].getChecked())
							continue;
						// 否则选中，查找该表格中是否有该行
						int index = table.indexOf(items[i]);
						// 如果没有该行，继续循环
						if (index < 0)
							continue;
						// 删除绑定的控件
						TableItemControls cons = tablecontrols.get(items[index]);
						if (cons != null) {
							cons.dispose();
							tablecontrols.remove(items[index]);
							System.out.println("dispose " + index);
						}
						// 如果有该行，删除该行
						// items[index].dispose();
						table.remove(index);
						System.out.println("i=" + i + ", index=" + index);
						System.out.println("行数:" + table.getItemCount());
						//table.pack();
					}
				}

				// 如果单击保存按钮
				else if (event.widget == save) {
					TableItem[] items = table.getItems();
					// 保存到文件或数据库中，数据持久化，这里省略
					/*
					 * for ( int i=0;i<items.length;i++) for (int
					 * j=0;j<table.getColumnCount();j++)
					 * System.out.println(items[i].getText(j));
					 */

					//String editorText = editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
					//System.out.println(editorText);
                    String comment="";
                    String newComment="";
					StringBuffer s = new StringBuffer();

					for (int i = 0; i < items.length; i++){
						
						/*for (int j = 0; j < table.getColumnCount() - 1; j++) {
							System.out.println(items[i].getText(j));
							s.append(items[i].getText(j) + "\n");
						}*/
						if(!items[i].getText(2).startsWith("#")){
							newComment="#"+items[i].getText(2);
						}else{
							newComment=items[i].getText(2);
						}
						if(!comment.equals(newComment)){
							comment=newComment;
							System.out.println("items[i].getText(0):"+items[i].getText(0));
							System.out.println("items[i].getText(1):"+items[i].getText(1));
							System.out.println("items[i].getText(2):"+items[i].getText(2));
							System.out.println("items[i].getText(3):"+items[i].getText(3));
							s.append(comment + "\n"+items[i].getText(0)+"="+items[i].getText(1)+"\n");
						}else{
							s.append(items[i].getText(0)+"="+items[i].getText(1)+"\n");
						}
						
						
						
						
						
					}
					

					editorText = s.toString();
					MessageBox messageBox = new MessageBox(getSite().getShell(), SWT.OK |

							SWT.ICON_WARNING);
					messageBox.setMessage(editorText);
					if (messageBox.open() == SWT.OK) {
						System.out.println("Ok is pressed.");
						

					}
					editorText1=editorText;
					editor.getDocumentProvider().getDocument(editor.getEditorInput()).set(editorText);
					
					//可以通过获取IWorkbenchPage，利用IWorkbenchPage作为管理工具进行EditorPart中doSave的调用。
					
					IWorkbenchPage page = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
					page.saveEditor(page.getActiveEditor(), true);
					
					
					
					
				}
			};
		};

		// 为工具栏的按钮注册事件
		add.addListener(SWT.Selection, listener);
		del.addListener(SWT.Selection, listener);

		save.addListener(SWT.Selection, listener);

	}

	// 创建放置表格的面板
	private void createComposite() {

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		composite = new Composite(viewForm, SWT.NONE);
		composite.setLayout(gridLayout);
		viewForm.setContent(composite);
		createTable(composite);
	}

	// 创建表格
	private void createTable(Composite composite) {
		// 表格布局
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = SWT.FILL;

		// 创建表格，使用SWT.FULL_SELECTION样式，可同时选中一行
		table = new Table(composite, SWT.MULTI | SWT.FULL_SELECTION | SWT.CHECK);
		table.setHeaderVisible(true);// 设置显示表头
		table.setLayoutData(gridData);// 设置表格布局
		table.setLinesVisible(true);// 设置显示表格线/*
		// 创建表头的字符串数组
		String[] tableHeader = { "key", "value", "comment", "operation" };
		for (int i = 0; i < tableHeader.length; i++) {
			TableColumn tableColumn = new TableColumn(table, SWT.NONE);
			tableColumn.setText(tableHeader[i]);
			// 设置表头可移动，默认为false
			tableColumn.setMoveable(true);
		}
		// 添加三行数据
		/*TableItem item = new TableItem(table, SWT.NONE);
		item.setText(new String[] { "windows.cer", "D:\\Key\\credoo_stg.cer", "# windows os keys test environment",
				"delete-" });*/
		String editorText = editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
		System.err.println("init:"+editorText);
		
			
			//解析editorText
		   
			 BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(editorText.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));  
			 String line;  
			 StringBuffer strbuf=new StringBuffer();
			 String note = "";
			 try{
				 
			 while ( (line = br.readLine()) != null ) {  
			     if(!line.trim().equals("")){
			    	 /*line="<br>"+line;//每行可以做加工
			    	 strbuf.append(line+"\r\n");*/
			    	 
			    	 
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
		     System.out.println(strbuf.toString());
		// 添加可编辑的单元格
		// /******************************************************
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
					editor.getItem().setText(0, text.getText());
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
					editor2.getItem().setText(2, text2.getText());
				}

			});

			// 保存TableItem与绑定Control的对应关系，删除TableItem时使用
			TableItemControls cons = new TableItemControls(text, text1, text2, editor1);
			tablecontrols.put(items[i], cons);

		}
		// *****************************************************/
		// /***************************************************
		// 创建TableCursor对象，使用上下左右键可以控制表格
		final TableCursor cursor = new TableCursor(table, SWT.NONE);
		// 创建可编辑的控件
		final ControlEditor editor = new ControlEditor(cursor);
		editor.grabHorizontal = true;
		editor.grabVertical = true;
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
				editor.setEditor(text);
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

		});
		// ******************************************************/
		// 重新布局表格
		for (int i = 0; i < tableHeader.length; i++) {
			table.getColumn(i).pack();
		}
		// /****************************************************
		// 为单元格注册选中事件
		table.addSelectionListener(new SelectionAdapter() {
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
		// ******************************************************/
		
		
		     
	
	}

	/**
	 * Creates page 2 of the multi-page editor, which shows the sorted text.
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
		// createPage2();
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
		dirty=true;
	}

	/**
	 * Saves the multi-page editor's document as another file. Also updates the
	 * text for page 0's tab, and updates this multi-page editor's input to
	 * correspond to the nested editor's.
	 */
	public void doSaveAs() {
		JavaEditor editor = (JavaEditor) getEditor(0);
		editor.doSaveAs();
		dirty=true;
		setPageText(0, editor.getTitle());
		setInput(editor.getEditorInput());
	}

	/*
	 * (non-Javadoc) Method declared on IEditorPart
	 */
	public void gotoMarker(IMarker marker) {
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}

	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method
	 * checks that the input is an instance of <code>IFileEditorInput</code>.
	 */
	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		if (!(editorInput instanceof IFileEditorInput))
			throw new PartInitException("Invalid Input: Must be IFileEditorInput");
		super.init(site, editorInput);
	}

	/*
	 * (non-Javadoc) Method declared on IEditorPart.
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
	      if(!editorText.equals(editorText1)){
	    	  dirty=true;
	      }else{
	    	  dirty=false;
	      }
	      System.err.println("dirty:"+dirty);
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
				    	 /*line="<br>"+line;//每行可以做加工
				    	 strbuf.append(line+"\r\n");*/
				    	 
				    	 
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

						});
						// ******************************************************/
						
						// /****************************************************
						// 为单元格注册选中事件
				 table.addSelectionListener(new SelectionAdapter() {
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
					
			}
	  
		  
	  } 
	}
	 
	/**
	 * Closes all project files on project close.
	 */
	public void resourceChanged(final IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
					for (int i = 0; i < pages.length; i++) {
						if (((FileEditorInput) editor.getEditorInput()).getFile().getProject()
								.equals(event.getResource())) {
							IEditorPart editorPart = pages[i].findEditor(editor.getEditorInput());
							pages[i].closeEditor(editorPart, true);
						}
					}
				}
			});
		}
	}

	/**
	 * Sets the font related data to be applied to the text in page 2.
	 */
	void setFont() {
		FontDialog fontDialog = new FontDialog(getSite().getShell());
		fontDialog.setFontList(text.getFont().getFontData());
		FontData fontData = fontDialog.open();
		if (fontData != null) {
			if (font != null)
				font.dispose();
			font = new Font(text.getDisplay(), fontData);
			text.setFont(font);
		}
	}

	/**
	 * Sorts the words in page 0, and shows them in page 2.
	 */
	void sortWords() {

		String editorText = editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();

		StringTokenizer tokenizer = new StringTokenizer(editorText, " \t\n\r\f!@#\u0024%^&*()-_=+`~[]{};:'\",.<>/?|\\");
		ArrayList editorWords = new ArrayList();
		while (tokenizer.hasMoreTokens()) {
			editorWords.add(tokenizer.nextToken());
		}

		Collections.sort(editorWords, Collator.getInstance());
		StringWriter displayText = new StringWriter();
		for (int i = 0; i < editorWords.size(); i++) {
			displayText.write(((String) editorWords.get(i)));
			displayText.write(System.getProperty("line.separator"));
		}
		text.setText(displayText.toString());
	}
}
