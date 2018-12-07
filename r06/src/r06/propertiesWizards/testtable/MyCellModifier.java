package r06.propertiesWizards.testtable;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

public class MyCellModifier implements ICellModifier{
		private TableViewer tv;
		public static String[] NAMES ={"张三","李四","小红","翠花"};
		
		public MyCellModifier(TableViewer tv){
				this.tv = tv;
		}
		public boolean canModify(Object element, String property) {
			return true;
		}

		public Object getValue(Object element, String property) {
			People p = (People)element;
			if(property.equals("name")){
				return new Integer(getNameIndex(p.getName()));
			}else if(property.equals("sex")){
				return new Boolean(p.getSex().equals("男"));
			}else if(property.equals("age")){
				return String.valueOf(p.getAge());
			}
			throw new RuntimeException("error column name : " + property);
		}
		private int getNameIndex(String name){
			for(int i=0;i<NAMES.length;i++){
				if(NAMES[i].equals(name)){
					return i;
				}
			}
			return -1;
		}

		public void modify(Object element, String property, Object value) {
			TableItem item = (TableItem)element;
			People p = (People)item.getData();
			if (property.equals("name")){
				Integer comboIndex = (Integer)value;
				if(comboIndex.intValue() == -1){
					return ;
				}
				String newName = NAMES[comboIndex.intValue()];
				p.setName(newName);
			}else if(property.equals("sex")){
				Boolean newValue = (Boolean)value;
				System.out.println(newValue);
				if(newValue.booleanValue()){
					p.setSex("男");
				}else{
					p.setSex("女");
				}
			}else if (property.equals("age")){
				String newValue = (String)value;
				if(newValue.equals("")){
					return ;
				}
				Integer newAge = new Integer(newValue);
				p.setAge(newAge);
			}else{
				throw new RuntimeException("错误列名:" + property);
			}
			tv.update(p, null);
		}
		
	}