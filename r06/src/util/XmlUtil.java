package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlUtil {

	/**
	 * 将XML文件输出到指定的路径
	 * 
	 * @param doc
	 * @param fileName
	 * @throws Exception
	 */
	public static void outputXml(Document doc, String fileName) throws Exception {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		DOMSource source = new DOMSource(doc);
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
		StreamResult result = new StreamResult(pw);
		transformer.transform(source, result);
		System.out.println("生成XML文件成功!");
	}

	/**
	 * 生成XML
	 * 
	 * @param ip
	 * @return
	 */
	public static Document generateXml(String ip) {
		Document doc = null;
		Element root = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.newDocument();
			root = doc.createElement("errorDevices");
			doc.appendChild(root);
		} catch (Exception e) {
			e.printStackTrace();
			return null;// 如果出现异常，则不再往下执行
		}

		Element element;
		element = doc.createElement("errorDevice");
		element.setAttribute("ip", ip);
		element.setAttribute("date", StringUtil.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss"));
		element.setAttribute("status", "1");
		root.appendChild(element);
		return doc;
	}

	/**
	 * 新增Xml节点
	 * 
	 * @param ip
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 * @throws TransformerException
	 */
	public static void addNode(String filename, String ip) throws FileNotFoundException, TransformerException {
		String date = StringUtil.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(new File(filename));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		NodeList links = doc.getElementsByTagName("errorDevice");
		if (links.getLength() > 0) {
			for (int i = 0; i < links.getLength(); i++) {
				Node nd = links.item(i);
				Node catParent = nd.getParentNode();
				Element ele = (Element) nd;
				String url = ele.getAttribute("ip");
				if (url.equals(ip)) {
					// ele.setAttribute("date", date);
					catParent.removeChild(nd);
				}
			}
		}
		Element element = doc.createElement("errorDevice");
		element.setAttribute("ip", ip);
		element.setAttribute("date", StringUtil.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss"));
		element.setAttribute("status", "1");
		doc.getDocumentElement().appendChild(element);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		DOMSource source = new DOMSource(doc);
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		PrintWriter pw = new PrintWriter(new FileOutputStream(filename));
		StreamResult result = new StreamResult(pw);
		transformer.transform(source, result);
		System.out.println("新增XML节点成功!");
	}
	public static void addNode(String filename, String nodename,String fileString) throws FileNotFoundException, TransformerException {
		//String date = StringUtil.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(new File(filename));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		NodeList links = doc.getElementsByTagName(nodename);
		if (links.getLength() > 0) {
			for (int i = 0; i < links.getLength(); i++) {
				Node nd = links.item(i);
				Node catParent = nd.getParentNode();
				Element ele = (Element) nd;
				String url = ele.getAttribute("path");
				if (url.equals("java/lib/"+fileString)) {
					// ele.setAttribute("date", date);
					catParent.removeChild(nd);
				}
			}
		}
		Element element = doc.createElement(nodename);
		if(fileString.contains(".jar")){
		
		element.setAttribute("kind", "lib");
		element.setAttribute("path", "java/lib/"+fileString);
		}
		else if(fileString.contains("unit")){
			element.setAttribute("kind", "con");
			element.setAttribute("path", "org.eclipse.jdt.junit.JUNIT_CONTAINER/4");
		}
		else if(fileString.contains("drools")){
			element.setAttribute("kind", "con");
			element.setAttribute("path", "DROOLS/Drools");
		}
		else if(fileString.contains("maven")){
			element.setAttribute("kind", "con");
			element.setAttribute("path", "org.eclipse.m2e.MAVEN2_CLASSPATH_CONTAINER");
			
			Element attributes=doc.createElement("attributes"); 
			
			Element attribute=doc.createElement("attribute"); 
			attribute.setAttribute("name", "maven.pomderived");
			attribute.setAttribute("value", "true");
			attributes.appendChild(attribute);
			Element attribute2=doc.createElement("attribute"); 
			attribute2.setAttribute("name", "org.eclipse.jst.component.dependency");
			attribute2.setAttribute("value", "java/lib");
			attributes.appendChild(attribute2);
			element.appendChild(attributes);
		}
		doc.getDocumentElement().appendChild(element);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		DOMSource source = new DOMSource(doc);
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		PrintWriter pw = new PrintWriter(new FileOutputStream(filename));
		StreamResult result = new StreamResult(pw);
		transformer.transform(source, result);
		System.out.println("新增XML节点成功!");
	}

	/**
	 * remove节点
	 * 
	 * @param ip
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 * @throws TransformerException
	 */
	

	public static void delXmlCode(String filename, String ip) {
		Document doc;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		String path=filename;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(path);
			doc.normalize();
			NodeList links = doc.getElementsByTagName("errorDevice");
			if (links.getLength() > 0) {
				for (int i = 0; i < links.getLength(); i++) {
					Node nd = links.item(i);
					//Node catParent = nd.getParentNode();
					Element ele = (Element) nd;
					String url = ele.getAttribute("ip");
					if (url.equals(ip)) {

						//catParent.removeChild(nd);
						//Element elink = ele;
						/*elink.removeChild(elink.getElementsByTagName("date").item(0));
						elink.removeChild(elink.getElementsByTagName("ip").item(0));
						elink.removeChild(elink.getElementsByTagName("1").item(0));*/
						doc.getFirstChild().removeChild(ele);
						

					} 
				}
			}
			
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new java.io.File(path));
			transformer.transform(source, result);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	public static void delXmlCode(String filename,String nodename, String jarfilename) {
		Document doc;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		String path=filename;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(path);
			doc.normalize();
			NodeList links = doc.getElementsByTagName(nodename);
			if (links.getLength() > 0) {
				for (int i = 0; i < links.getLength(); i++) {
					Node nd = links.item(i);
					Node catParent = nd.getParentNode();
					Element ele = (Element) nd;
					String url = ele.getAttribute("path");
					if (url.equals("java/lib/"+jarfilename)) {

						//catParent.removeChild(nd);
						//Element elink = ele;
						/*elink.removeChild(elink.getElementsByTagName("date").item(0));
						elink.removeChild(elink.getElementsByTagName("ip").item(0));
						elink.removeChild(elink.getElementsByTagName("1").item(0));*/
						doc.getFirstChild().removeChild(ele);
						

					} 
				}
			}
			
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new java.io.File(path));
			transformer.transform(source, result);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 读取XML
	 * 
	 * @param filename
	 * @return
	 */
	public static List<Map> readXml(String filename) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(new File(filename));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		NodeList links = doc.getElementsByTagName("errorDevice");
		List<Map> list = new ArrayList<Map>();
		for (int i = 0; i < links.getLength(); i++) {
			Element node = (Element) links.item(i);
			Map map = new HashMap();
			map.put(node.getAttribute("ip"), node.getAttribute("date"));
			list.add(map);
		}
		return list;
	}

	public static void setNodeAttribute(String filename,String nodename, String planName) {
		// TODO Auto-generated method stub
				Document doc;
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder;
				String path=filename;
				try {
					builder = factory.newDocumentBuilder();
					doc = builder.parse(path);
					doc.normalize();
					NodeList links = doc.getElementsByTagName(nodename);
					if (links.getLength() > 0) {
						for (int i = 0; i < links.getLength(); i++) {
							Node nd = links.item(i);
							Element ele = (Element) nd;
							if(nodename.equals("Plan")){
							String url = ele.getAttribute("name");
							if (url.equals(planName)) {
								//Element elink = ele;
								/*elink.removeChild(elink.getElementsByTagName("date").item(0));
								elink.removeChild(elink.getElementsByTagName("ip").item(0));
								elink.removeChild(elink.getElementsByTagName("1").item(0));*/
								//doc.getFirstChild().removeChild(ele);
								ele.removeAttribute("isActive");
								ele.setAttribute("isActive", "true");
								

							} 
							else{
								ele.removeAttribute("isActive");
								ele.setAttribute("isActive", "false");
							}
							}
							if(nodename.equals("kbase")){
									String url = ele.getAttribute("packages");
									ele.removeAttribute("packages");
									ele.setAttribute("packages", planName);
								}
							}
						
					}
					
					TransformerFactory tFactory = TransformerFactory.newInstance();
					Transformer transformer = tFactory.newTransformer();
					DOMSource source = new DOMSource(doc);
					StreamResult result = new StreamResult(new java.io.File(path));
					transformer.transform(source, result);
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (TransformerConfigurationException e) {
					e.printStackTrace();
				} catch (TransformerException e) {
					e.printStackTrace();
				}
	}

	public static void delPlan(String filename,String nodename, String planName) {
		// TODO Auto-generated method stub
		Document doc;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		String path=filename;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(path);
			doc.normalize();
			NodeList links = doc.getElementsByTagName(nodename);
			if (links.getLength() > 0) {
				for (int i = 0; i < links.getLength(); i++) {
					Node nd = links.item(i);
					Node catParent = nd.getParentNode();
					Element ele = (Element) nd;
					String url = ele.getAttribute("name");
					if (url.equals(planName)) {
						 catParent.removeChild(nd);

					} 
				}
			}
			
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new java.io.File(path));
			transformer.transform(source, result);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	public static String getDestDir(String filename, String nodename, String name) {
		// TODO Auto-generated method stub
		Document doc;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		String path=filename;
		String destDir="";
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(path);
			doc.normalize();
			NodeList links = doc.getElementsByTagName(nodename);
			if (links.getLength() > 0) {
				for (int i = 0; i < links.getLength(); i++) {
					Node nd = links.item(i);
					Element ele = (Element) nd;
					String url = ele.getAttribute("name");
					if (url.equals(name)) {
						destDir=ele.getAttribute("value");
						
						

					} 
				}
			}
			
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return destDir;
	}

	public static int getNodeList(String filename, String nodename) {
		// TODO Auto-generated method stub
		Document doc;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		String path=filename;
		int nodeNum=0;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(path);
			doc.normalize();
			NodeList links = doc.getElementsByTagName(nodename);
			nodeNum=links.getLength();
			
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return nodeNum;
	}

	public static String setNodeAttribute(String filename, String nodename) {
		// TODO Auto-generated method stub
		Document doc;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		String path=filename;
		String activePlan = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(path);
			doc.normalize();
			NodeList links = doc.getElementsByTagName(nodename);
			if (links.getLength() > 0) {
				for (int i = 0; i < links.getLength(); i++) {
					Node nd = links.item(i);
					Element ele = (Element) nd;
					
					String url = ele.getAttribute("isActive");
					if (url.equals("false")) {
						
						ele.removeAttribute("isActive");
						ele.setAttribute("isActive", "true");	
						activePlan=ele.getAttribute("name");
						
                        break;
					}
					
					
					
				}
				
			}
			
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new java.io.File(path));
			transformer.transform(source, result);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return activePlan;
	}
	
}
