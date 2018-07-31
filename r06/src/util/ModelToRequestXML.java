package util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ModelToRequestXML {
	public static String parseToXml(Object model) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"GBK\"?>").append("\r\n").append("<conditions>").append("\r\n");
		Field[] fields = model.getClass().getDeclaredFields();
		Field field1 = model.getClass().getDeclaredField("queryType");
		field1.setAccessible(true); 
		Field.setAccessible(fields,true); 
		Integer queryType = (Integer) field1.get(model);
		sb.append("<condition queryType=").append(queryType+">\r\n");
		Map<String,Object> dataMap = new HashMap<String,Object>();
		for (Field field :fields) {
			 String methodName = field.getName();
		    // 如果类型是String
		    if (field.getGenericType().toString().equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
		     // 拿到该属性的gettet方法
		     Method m = (Method) model.getClass().getMethod("get" + getMethodName(methodName));
		     String val = (String) m.invoke(model);// 调用getter方法获取属性值
		     dataMap.put(methodName, val);
		    }

		    // 如果类型是Integer
		    if (field.getGenericType().toString().equals("class java.lang.Integer")) {
		     Method m = (Method) model.getClass().getMethod(
		       "get" + getMethodName(field.getName()));
		     Integer val = (Integer) m.invoke(model);
		     dataMap.put(methodName, val);
		    }

		    // 如果类型是Double
		    if (field.getGenericType().toString().equals("class java.lang.Double")) {
		     Method m = (Method) model.getClass().getMethod(
		       "get" + getMethodName(field.getName()));
		     Double val = (Double) m.invoke(model);
		     dataMap.put(methodName, val);
		    }

		    // 如果类型是Boolean 是封装类
		    if (field.getGenericType().toString().equals("class java.lang.Boolean")) {
		     Method m = (Method) model.getClass().getMethod(
		       field.getName());
		     Boolean val = (Boolean) m.invoke(model);
		     dataMap.put(methodName, val);
		    }

		    // 如果类型是boolean 基本数据类型不一样 这里有点说名如果定义名是 isXXX的 那就全都是isXXX的
		    // 反射找不到getter的具体名
		    if (field.getGenericType().toString().equals("boolean")) {
		     Method m = (Method) model.getClass().getMethod(
		       field.getName());
		     Boolean val = (Boolean) m.invoke(model);
		     dataMap.put(methodName, val);
		    }
		    // 如果类型是Date
		    if (field.getGenericType().toString().equals("class java.util.Date")) {
		     Method m = (Method) model.getClass().getMethod(
		       "get" + getMethodName(field.getName()));
		     Date val = (Date) m.invoke(model);
		     dataMap.put(methodName, val);
		    }
		    // 如果类型是Short
		    if (field.getGenericType().toString().equals("class java.lang.Short")) {
		     Method m = (Method) model.getClass().getMethod(
		       "get" + getMethodName(field.getName()));
		     Short val = (Short) m.invoke(model);
		     dataMap.put(methodName, val);
		    }
		    // 如果还需要其他的类型请自己做扩展
		
		}
		for(Entry<String, Object> entry:dataMap.entrySet()){
			if(!"queryType".equals(entry.getKey())){
				sb.append("<item>"+"\r\n")
				.append("<name>"+entry.getKey()+"</name>\r\n")
				.append("<value>"+entry.getValue()+"</value>\r\n")
				.append("</item>\r\n");
			}
		}
		sb.append("</condition>\r\n").append("</conditions>");
		return sb.toString();
	}
	 // 把一个字符串的第一个字母大写、效率是最高的、
	 private static String getMethodName(String fildeName) throws Exception{
	  byte[] items = fildeName.getBytes();
	  items[0] = (byte) ((char) items[0] - 'a' + 'A');
	  return new String(items);
	 }
}
