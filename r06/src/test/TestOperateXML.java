package test;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TestOperateXML extends DefaultHandler {
	public static void main(String[] args)
			throws ParserConfigurationException, SAXException, IOException, JAXBException {
		OutputStream os = new FileOutputStream("d://1.xml");
		JAXBContext jc0 = JAXBContext.newInstance(A.class);
		Marshaller m = jc0.createMarshaller();
		A a = new A();
		a.setId("id for a");
		B b = new B();
		b.setNo(60);
		a.setB(b);
		m.marshal(a, os);
		InputStream is = new FileInputStream("d://1.xml");
		JAXBContext jc = JAXBContext.newInstance(A.class);
		Unmarshaller u = jc.createUnmarshaller();
		A o = (A) u.unmarshal(is);
		System.out.println(o.getB().getNo());
	}
}

@XmlRootElement(name = "A")
class A {
	String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public B getB() {
		return b;
	}

	public void setB(B b) {
		this.b = b;
	}

	B b;
}

class B {
	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	int no;
}
