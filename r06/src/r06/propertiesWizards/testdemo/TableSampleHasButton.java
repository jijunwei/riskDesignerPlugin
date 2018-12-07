package r06.propertiesWizards.testdemo;

import java.util.Hashtable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.ControlEditor;
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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class TableSampleHasButton {


public Shell sShell = null; // @jve:decl-index=0:visual-constraint="10,10"
private ViewForm viewForm = null;
private ToolBar toolBar = null;
private Composite composite = null;
private Table table = null;
private Menu menu = null;
private Hashtable<TableItem, TableItemControls> tablecontrols = new Hashtable<TableItem, TableItemControls>();

//创建ViewForm面板放置工具栏和表格
private void createViewForm() {
   viewForm = new ViewForm(sShell, SWT.NONE);
   viewForm.setTopCenterSeparate(true);
   createToolBar();
   viewForm.setTopLeft(toolBar);
   createComposite();
   viewForm.setContent(composite);
}

//创建工具栏
private void createToolBar() {
   toolBar = new ToolBar(viewForm, SWT.FLAT);
   final ToolItem add = new ToolItem(toolBar, SWT.PUSH);
   add.setText("添加");
   add.setImage( ImageFactory.loadImage( toolBar.getDisplay() , ImageFactory.ADD_OBJ));
   final ToolItem del = new ToolItem(toolBar, SWT.PUSH);
   del.setText("删除");
   del.setImage( ImageFactory.loadImage( toolBar.getDisplay() , ImageFactory.DELETE_OBJ));
  
   final ToolItem save = new ToolItem(toolBar, SWT.PUSH);
   save.setText("保存");
   save.setImage( ImageFactory.loadImage( toolBar.getDisplay() , ImageFactory.SAVE_EDIT));
   //工具栏按钮事件处理
   Listener listener = new Listener(){
    public void handleEvent(Event event) {
     //如果单击添加按钮，添加一行，在实际的项目实现中通常是接收输入的参数，案后添加
     //这里为了简单起见，添加固定的一条记录
     if ( event.widget == add )
     {
      TableItem item = new TableItem(table, SWT.NONE);
      item.setText(new String[] {"", "","","delete-"});
      
     

      //添加可编辑的单元格
      // /******************************************************
      TableItem [] items = table.getItems ();
      for (int i=0; i<items.length; i++) {
       //第一列设置，创建TableEditor对象
       final TableEditor editor = new TableEditor (table);
       //创建一个文本框，用于输入文字
       final Text text = new Text (table, SWT.NONE);
       //将文本框当前值，设置为表格中的值
       text.setText(items[i].getText(0));
       //设置编辑单元格水平填充
       editor.grabHorizontal = true;
       //关键方法，将编辑单元格与文本框绑定到表格的第一列
       editor.setEditor(text, items[i], 0);
       //当文本框改变值时，注册文本框改变事件，该事件改变表格中的数据。
       //否则即使改变的文本框的值，对表格中的数据也不会影响
       text.addModifyListener( new ModifyListener(){
        public void modifyText(ModifyEvent e) {
         editor.getItem().setText(1,text.getText());
        }
       
       });
       
       
     //第二列设置，创建TableEditor对象
       final TableEditor editor1 = new TableEditor (table);
       //创建一个文本框，用于输入文字
       final Text text1 = new Text (table, SWT.NONE);
       //将文本框当前值，设置为表格中的值
       text1.setText(items[i].getText(1));
       //设置编辑单元格水平填充
       editor1.grabHorizontal = true;
       //关键方法，将编辑单元格与文本框绑定到表格的第二列
       editor1.setEditor(text1, items[i], 1);
       //当文本框改变值时，注册文本框改变事件，该事件改变表格中的数据。
       //否则即使改变的文本框的值，对表格中的数据也不会影响
       text1.addModifyListener( new ModifyListener(){
        public void modifyText(ModifyEvent e) {
         editor1.getItem().setText(1,text1.getText());
        }
       
       });
     //第三列设置，创建TableEditor对象
       final TableEditor editor2 = new TableEditor (table);
       //创建一个文本框，用于输入文字
       final Text text2 = new Text (table, SWT.NONE);
       //将文本框当前值，设置为表格中的值
       text2.setText(items[i].getText(2));
       //设置编辑单元格水平填充
       editor2.grabHorizontal = true;
       //关键方法，将编辑单元格与文本框绑定到表格的第二列
       editor2.setEditor(text2, items[i], 2);
       //当文本框改变值时，注册文本框改变事件，该事件改变表格中的数据。
       //否则即使改变的文本框的值，对表格中的数据也不会影响
       text2.addModifyListener( new ModifyListener(){
        public void modifyText(ModifyEvent e) {
         editor1.getItem().setText(1,text2.getText());
        }
       
       });
       
      
       // 保存TableItem与绑定Control的对应关系，删除TableItem时使用
       TableItemControls cons = new TableItemControls(text, text1, text2, editor1);
       tablecontrols.put(items[i], cons);
      
      } 
     }
     //如果单击删除按钮
     else if ( event.widget == del)
     {
      //首先获得表格中所有的行
      TableItem[] items = table.getItems();
      //循环所有行
      for ( int i=items.length-1;i>=0;i-- ){
       //如果该行没有被选中，继续循环
       if ( !items[i].getChecked())
        continue;
       //否则选中，查找该表格中是否有该行
       int index = table.indexOf( items[i]);
       //如果没有该行，继续循环
       if (index<0)
        continue;
       //删除绑定的控件
       TableItemControls cons = tablecontrols.get(items[index]);
       if (cons != null) {
        cons.dispose();
        tablecontrols.remove(items[index]);
        System.out.println("dispose " + index);
       }
       //如果有该行，删除该行
//       items[index].dispose();
       table.remove( index );
       System.out.println("i="+i+", index="+index);
       System.out.println("行数:" + table.getItemCount());
//       table.pack();
      }
     }
    
     //如果单击保存按钮
     else if ( event.widget == save)
     {
      TableItem [] items = table.getItems();
      //保存到文件或数据库中，数据持久化，这里省略
      for ( int i=0;i<items.length;i++)
       for (int j=0;j<table.getColumnCount();j++)
        System.out.println(items[i].getText(j));
     }
    }
   
   };
   //为工具栏的按钮注册事件
   add.addListener( SWT.Selection , listener );
   del.addListener( SWT.Selection, listener );
   
   save.addListener( SWT.Selection, listener );
}
//创建放置表格的面板
private void createComposite() {
   GridLayout gridLayout = new GridLayout();
   gridLayout.numColumns = 1;
   composite = new Composite(viewForm, SWT.NONE);
   composite.setLayout(gridLayout);
   createTable();
}
//创建表格
private void createTable() {
   //表格布局
   GridData gridData = new org.eclipse.swt.layout.GridData();
   gridData.horizontalAlignment = SWT.FILL;
   gridData.grabExcessHorizontalSpace = true;
   gridData.grabExcessVerticalSpace = true;
   gridData.verticalAlignment = SWT.FILL;
  
   //创建表格，使用SWT.FULL_SELECTION样式，可同时选中一行
   table = new Table(composite, SWT.MULTI|SWT.FULL_SELECTION|SWT.CHECK);
   table.setHeaderVisible(true);//设置显示表头
   table.setLayoutData(gridData);//设置表格布局
   table.setLinesVisible(true);//设置显示表格线/*
   //创建表头的字符串数组
   String[] tableHeader = {"key","value","comment","operation"};
   for (int i=0;i<tableHeader.length;i++){
    TableColumn tableColumn = new TableColumn(table, SWT.NONE);
    tableColumn.setText( tableHeader[i]);
    //设置表头可移动，默认为false
    tableColumn.setMoveable(true);
   }
   //添加三行数据
   TableItem item = new TableItem(table, SWT.NONE);
   item.setText(new String[] {"windows.cer", "D:\\Key\\credoo_stg.cer","# windows os keys test environment","delete-"});
   
  

   //添加可编辑的单元格
   // /******************************************************
   TableItem [] items = table.getItems ();
   for (int i=0; i<items.length; i++) {
    //第一列设置，创建TableEditor对象
    final TableEditor editor = new TableEditor (table);
    //创建一个文本框，用于输入文字
    final Text text = new Text (table, SWT.NONE);
    //将文本框当前值，设置为表格中的值
    text.setText(items[i].getText(0));
    //设置编辑单元格水平填充
    editor.grabHorizontal = true;
    //关键方法，将编辑单元格与文本框绑定到表格的第一列
    editor.setEditor(text, items[i], 0);
    //当文本框改变值时，注册文本框改变事件，该事件改变表格中的数据。
    //否则即使改变的文本框的值，对表格中的数据也不会影响
    text.addModifyListener( new ModifyListener(){
     public void modifyText(ModifyEvent e) {
      editor.getItem().setText(1,text.getText());
     }
    
    });
    
    
  //第二列设置，创建TableEditor对象
    final TableEditor editor1 = new TableEditor (table);
    //创建一个文本框，用于输入文字
    final Text text1 = new Text (table, SWT.NONE);
    //将文本框当前值，设置为表格中的值
    text1.setText(items[i].getText(1));
    //设置编辑单元格水平填充
    editor1.grabHorizontal = true;
    //关键方法，将编辑单元格与文本框绑定到表格的第二列
    editor1.setEditor(text1, items[i], 1);
    //当文本框改变值时，注册文本框改变事件，该事件改变表格中的数据。
    //否则即使改变的文本框的值，对表格中的数据也不会影响
    text1.addModifyListener( new ModifyListener(){
     public void modifyText(ModifyEvent e) {
      editor1.getItem().setText(1,text1.getText());
     }
    
    });
  //第三列设置，创建TableEditor对象
    final TableEditor editor2 = new TableEditor (table);
    //创建一个文本框，用于输入文字
    final Text text2 = new Text (table, SWT.NONE);
    //将文本框当前值，设置为表格中的值
    text2.setText(items[i].getText(2));
    //设置编辑单元格水平填充
    editor2.grabHorizontal = true;
    //关键方法，将编辑单元格与文本框绑定到表格的第二列
    editor2.setEditor(text2, items[i], 2);
    //当文本框改变值时，注册文本框改变事件，该事件改变表格中的数据。
    //否则即使改变的文本框的值，对表格中的数据也不会影响
    text2.addModifyListener( new ModifyListener(){
     public void modifyText(ModifyEvent e) {
      editor1.getItem().setText(1,text2.getText());
     }
    
    });
    
   
    // 保存TableItem与绑定Control的对应关系，删除TableItem时使用
    TableItemControls cons = new TableItemControls(text, text1, text2, editor1);
    tablecontrols.put(items[i], cons);
   
   }  
   // *****************************************************/
        // /***************************************************
   //创建TableCursor对象，使用上下左右键可以控制表格
   final TableCursor cursor = new TableCursor(table, SWT.NONE);
   //创建可编辑的控件
   final ControlEditor editor = new ControlEditor(cursor);
   editor.grabHorizontal = true;
   editor.grabVertical = true;
   //为TableCursor对象注册事件
   cursor.addSelectionListener( new SelectionAdapter() {
    //但移动光标，在单元格上单击回车所触发的事件
    public void widgetDefaultSelected(SelectionEvent e) {
     //创建一个文本框控件
     final Text text = new Text(cursor, SWT.NONE);
     //获得当前光标所在的行TableItem对象
     TableItem row = cursor.getRow();
     //获得当前光标所在的列数
     int column = cursor.getColumn();
     //当前光标所在单元格的值赋给文本框
     text.setText(row.getText(column));
     //为文本框注册键盘事件
     text.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
       //此时在文本框上单击回车后，这是表格中的数据为修改后文本框中的数据
       //然后释放文本框资源
       if (e.character == SWT.CR) {
        TableItem row = cursor.getRow();
        int column = cursor.getColumn();
        row.setText(column, text.getText());
        text.dispose();
       }
       //如果在文本框中单击了ESC键，则并不对表格中的数据进行修改
       if (e.character == SWT.ESC) {
        text.dispose();
       }
      }
     });
     //注册焦点事件
     text.addFocusListener(new FocusAdapter() {
      //当该文本框失去焦点时，释放文本框资源
      public void focusLost(FocusEvent e) {
       text.dispose();
      }
     });
     //将该文本框绑定到可编辑的控件上
     editor.setEditor(text);
     //设置文本框的焦点
     text.setFocus();
    }
    //移动光标到一个单元格上所触发的事件
    public void widgetSelected(SelectionEvent e) {
     table.setSelection(new TableItem[] { cursor.getRow()});
    }
   });
   cursor.addMouseListener(new MouseListener() {

    @Override
    public void mouseDoubleClick(MouseEvent e) {
     // TODO Auto-generated method stub
    
    }

    @Override
    public void mouseDown(MouseEvent e) {
     if (e.button==3) { // 右键按下，显示右键菜单
      menu.setVisible(true);
     }
    }

    @Override
    public void mouseUp(MouseEvent e) {
     // TODO Auto-generated method stub
    
    }
   
   });
   // ******************************************************/
   //重新布局表格
   for (int i=0; i<tableHeader.length; i++) {
    table.getColumn (i).pack ();
   }
   // /****************************************************
   //为单元格注册选中事件
   table.addSelectionListener( new SelectionAdapter(){
    public void widgetSelected(SelectionEvent e) {
     //获得所有的行数
     int total = table.getItemCount();
     //循环所有行
     for ( int i=0;i<total;i++){
      TableItem item = table.getItem(i);
      //如果该行为选中状态，改变背景色和前景色，否则颜色设置
      if (table.isSelected( i )){ 
       item.setBackground( sShell.getDisplay().getSystemColor( SWT.COLOR_RED));
       item.setForeground( sShell.getDisplay().getSystemColor( SWT.COLOR_WHITE));
      }else {
       item.setBackground( null );
       item.setForeground( null );
      }
     }
    }
   
   });
   // ******************************************************/
}

public static void main(String[] args) {
   //调用主窗口
   Display display = Display.getDefault();
   TableSampleHasButton thisClass = new TableSampleHasButton();
   thisClass.createSShell();
   thisClass.sShell.open();
   while (!thisClass.sShell.isDisposed()) {
    if (!display.readAndDispatch())
     display.sleep();
   }
   ImageFactory.dispose();
   display.dispose();
}

//创建主窗口
public void createSShell() {
   sShell = new Shell();
   sShell.setText("表格窗口");
   sShell.setLayout(new FillLayout());
   createViewForm();
   createMenu();
   sShell.setImage( ImageFactory.loadImage( sShell.getDisplay(), ImageFactory.SAMPLES));
   sShell.setSize(new org.eclipse.swt.graphics.Point(307,218));
   sShell.pack();
}
//创建上下文菜单
private void createMenu() {
   //创建弹出式菜单
   menu = new Menu (sShell, SWT.POP_UP);
   //设置该菜单为表格菜单
   table.setMenu (menu);
   //创建删除菜单项
   MenuItem del = new MenuItem (menu, SWT.PUSH);
   del.setText ("删除");
   del.setImage( ImageFactory.loadImage( sShell.getDisplay(), ImageFactory.DELETE_EDIT));
   //为删除菜单注册事件，当单击时，删除所选择的行
   del.addListener (SWT.Selection, new Listener () {
    public void handleEvent (Event event) {
     // 此处需添加删除绑定Control的代码
     //table.remove (table.getSelectionIndices ());
          
     TableItem[] items = table.getSelection();
    for (int i=0;i<items.length;i++){
    	
      System.out.print(items[i].getText());
    //查找该表格中是否有该行
      int index = table.indexOf( items[i]);
      
      //删除绑定的控件
      TableItemControls cons = tablecontrols.get(items[index]);
      if (cons != null) {
       cons.dispose();
       tablecontrols.remove(items[index]);
       System.out.println("dispose " + index);
      }
      //如果有该行，删除该行
//    items[index].dispose();
      table.remove( index );
      System.out.println("i="+table.getSelectionIndices ()+", index="+index);
      System.out.println("行数:" + table.getItemCount());
//      table.pack();
      
    }
    }
   });
   
 
   //创建查看菜单项
   MenuItem view = new MenuItem (menu, SWT.PUSH);
   view.setText ("查看");
   view.setImage( ImageFactory.loadImage( sShell.getDisplay(), ImageFactory.SCOPY_EDIT));
   //为查看菜单项注册事件，当单击时打印出所选的姓名
   view.addListener (SWT.Selection, new Listener () {
    public void handleEvent (Event event) {
     TableItem[] items = table.getSelection();
     for (int i=0;i<items.length;i++)
      System.out.print(items[i].getText());
    }
   });
  
   table.setMenu(menu);
}

}
