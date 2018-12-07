package r06.propertiesWizards.testtable;
import java.util.Date;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

public class Sorter extends ViewerSorter {
		private static final int ID = 1;
		private static final int NAME = 2;
		private static final int SEX = 3;
		private static final int AGE = 4;
		private static final int CREATE_DATE = 5;
		
		public static final Sorter ID_ASC = new Sorter(ID);
		public static final Sorter ID_DESC = new Sorter(-ID);
		public static final Sorter NAME_ASC = new Sorter(NAME);
		public static final Sorter NAME_DESC = new Sorter(-NAME);
		public static final Sorter SEX_ASC = new Sorter(SEX);
		public static final Sorter SEX_DESC = new Sorter(-SEX);
		public static final Sorter AGE_ASC = new Sorter(AGE);
		public static final Sorter AGE_DESC = new Sorter(-AGE);
		public static final Sorter CREATE_DATE_ASC = new Sorter(CREATE_DATE);
		public static final Sorter CREATE_DATE_DESC = new Sorter(-CREATE_DATE);
		
		private int sortType ;
		private Sorter(int sortType){
			this.sortType = sortType;
		}
		public int compare(Viewer viewer, Object e1, Object e2) {
			People p1 = (People)e1;
			People p2 = (People)e2;
			switch(sortType){
				case ID:{
					Long l1 = p1.getId();
					Long l2 = p2.getId();
					return l1.compareTo(l2);
				}
				case -ID:{
					Long l1 = p1.getId();
					Long l2 = p2.getId();
					return l2.compareTo(l1);
				}
				case NAME:{
					String s1 = p1.getName();
					String s2 = p2.getName();
					return s1.compareTo(s2);
				}
				case -NAME:{
					String s1 = p1.getName();
					String s2 = p2.getName();
					return s2.compareTo(s1);
				}
				case SEX:{
					String s1 = p1.getSex();
					String s2 = p2.getSex();
					return s1.compareTo(s2);
				}
				case -SEX:{
					String s1 = p1.getSex();
					String s2 = p2.getSex();
					return s2.compareTo(s1);
				}
				case AGE:{
					Integer i1 = p1.getAge();
					Integer i2 = p2.getAge();
					return i1.compareTo(i2);
				}
				case -AGE:{
					Integer i1 = p1.getAge();
					Integer i2 = p2.getAge();
					return i2.compareTo(i1);
				}
				case CREATE_DATE:{
					Date d1 = p1.getCreateDate();
					Date d2 = p2.getCreateDate();
					d1.compareTo(d2);
				}
				case -CREATE_DATE:{
					Date d1 = p1.getCreateDate();
					Date d2 = p2.getCreateDate();
					d2.compareTo(d1);
				}
			}
			return 0;
		}
	}