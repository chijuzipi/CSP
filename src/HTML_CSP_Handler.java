import java.io.*;

import org.joox.Match;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

//import org.w3c.dom.Document;
import static org.joox.JOOX.*;


public class HTML_CSP_Handler {
	public HTML_CSP_Handler(File inputHtml) throws Exception, IOException{
		inputHtml = new File("demo/Twitter.html");
		Document document = $(inputHtml).document();
		Match x2 = $(document).find("script");
		String path = $(x2).xpath();
		System.out.println(x2);
		//System.out.println(path);
	}
	
	public static void main(String[] args) throws IOException, Exception{
		File f = new File("index.html");
		new HTML_CSP_Handler(f);
	}
	
}
