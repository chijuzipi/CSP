import java.io.*;
import org.custommonkey.xmlunit.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import java.io.ByteArrayInputStream;

public class Test {
	private String html1;
	private String html2;
	public Test() throws Exception{
		String xml1 ="<struct><int>3</int><boolean>false</boolean></struct>";
		String xml2 = "<struct><boolean>false</boolean><int>3</int></struct>";
		Document doc1 = loadXMLFromString(xml1);
		Document doc2 = loadXMLFromString(xml2);
		BufferedReader in1 = new BufferedReader(new FileReader("demo/Twitter.html"));
		BufferedReader in2 = new BufferedReader(new FileReader("demo/Twitter2.html"));
		Diff diff = new Diff(doc1, doc2);
		if (diff.similar()){
			System.out.println("they are similar");
		}
		else {
			System.out.println("they are not similar");
			System.out.println(diff.toString());
		}
		if (diff.identical()){
			System.out.println("they are identical");
		}
		else {
			System.out.println("they are not identical");
			System.out.println(diff.toString());
		}
	}
	
	public Document loadXMLFromString(String xml) throws Exception
	{
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

	    factory.setNamespaceAware(true);
	    DocumentBuilder builder = factory.newDocumentBuilder();

	    return builder.parse(new ByteArrayInputStream(xml.getBytes()));
	}
	
	public static void main(String[] args) throws Exception{
		Test test1 = new Test();
	}
}
