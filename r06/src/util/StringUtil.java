package util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil {
	  
	public static String formatDate(Date date, String f) {
		// TODO Auto-generated method stub
		SimpleDateFormat sformat = new SimpleDateFormat(f);
	      /*Date d = new Date();*/
	      String str = "";
	      str=sformat.format(date);
		  System.out.println("DateTime>>>>>>: " + str);
		return str;
	}
}
