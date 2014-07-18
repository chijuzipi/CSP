import java.io.*;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GetURLContent 
{
	//for creating id for extracted elements
	private int scriptCount;
	private int cssCount;
	private File inputFile;
	private File output_html;
	private File output_script;
	private File output_css;
	//private File output_policy;
	private BufferedWriter html_out; 
	private BufferedWriter js_out; 
	private BufferedWriter css_out; 
	//private BufferedWriter policy_out; 
	private Document doc;
	private String newFileName;

	public GetURLContent(String input) throws IOException{
		//policy remain un-implemented
		//ArrayList<String> policys = new ArrayList();

		//used to generate id for inline script elements
		scriptCount = 0;
		cssCount = 0;

		//prepare IO
		inputFile = new File(input);
	
		String[] pathList = input.trim().split("/");
		String fileName = pathList[pathList.length -1];
		System.out.println(fileName);

		String[] fileNameList = fileName.split("\\.");
		System.out.println(fileNameList.length);
		
		newFileName = fileNameList[fileNameList.length - 2];
		System.out.println(newFileName);
		output_html = new File(newFileName + ".html"); 
		output_script = new File(newFileName + ".js"); 
		output_css = new File(newFileName + ".css"); 
		html_out = new BufferedWriter(new FileWriter(output_html));
		js_out = new BufferedWriter(new FileWriter(output_script));
		css_out = new BufferedWriter(new FileWriter(output_css));

		//initialize Jsoup document
		doc = Jsoup.parse(inputFile, "UTF-8");
		
	}
	
	public void processHTML() throws IOException {
		//select external script and styles, using them to generate csp header
		Elements external_scripts = doc.select("script[src]");
		Elements external_styles = doc.select("link[href$=.css]");
		String cspHeader = generateCSPHeader(external_scripts, external_styles);
		// this header is at the HTTP request, so no output file, just print out.
		System.out.println(cspHeader);
		
		//select and write internal script to .js file and remove it from HTML
		Elements internal_scripts = doc.select("script").not("script[src]");
		for (Element x : internal_scripts){
			js_out.write(x.data().toString());
			//delete the element
			x.remove();
		}
		
		//select and write inline script to file and remove it from HTML
		Elements inline_scripts;
		String[] script_directive = {
    			// Mouse Events
    		"onclick", "ondblclick", "onmousedown", "onmousemove", "onmouseover", "onmouseout", "onmouseup", 
    			// Keyboard Events
    		"onkeydown", "onkeypress", "onkeyup", 
    			// Frame/Object Events
    		"onabort", "onerror", "onload", "onresize", "onscroll", "onunload",
    			// Form Events
    		"onblur", "onchange", "onfocus", "onreset", "onselect", "onsubmit"};
		
		//write the document ready part
		js_out.write("\r\n");
		js_out.write("document.addEventListener('DOMContentReady', function () {");
		js_out.write("\r\n");
		for (int i = 0; i < script_directive.length; i++){
			inline_scripts = doc.select("[" + script_directive[i] +"]");
			inlineToJs(inline_scripts, script_directive[i]);
			}	
		js_out.write("});");

		//select and write internal style to .css file and remove it from HTML
		Elements internal_styles = doc.select("style");
		for (Element y : internal_styles){
			css_out.write(y.data().toString());
			//delete the element
			y.remove();
		}	

		//select and write inline style to .css file and remove it from HTML
		Elements inline_styles = doc.select("[style]");
		for(Element z : inline_styles){
			String cssId;
			if (z.id() == ""){
				z.attr("id", String.valueOf(cssCount));
				cssId = String.valueOf(cssCount);
				cssCount++;
			}
			else{
				cssId = z.id();
			}
			String cssContent = z.attr("style");
			z.removeAttr("style");
			css_out.write("\r\n");
			css_out.write("#"+cssId+ " {" + cssContent + "}");
			css_out.write("\r\n");

		}
		
		addExternalJsToHtml();
		addExternalCssToHtml();

		// write html into a new file
		html_out.write(doc.toString());
		js_out.close();
		css_out.close();
		html_out.close();
	}
	
	//write inline script to external .js file
	public void inlineToJs(Elements ele, String directive) throws IOException
	{
		// delete front two bits
		String js_directive = directive.substring(2);
		for (Element x : ele)
		{
			// add id check, if exists then use it. otherwise create a new one
			String ele_id = x.id();
			if (ele_id == "")
			{
				//convert to String
				x.attr("id", String.valueOf(scriptCount));
				ele_id = String.valueOf(scriptCount);
				scriptCount++;
			}
			
			String function_content = x.attr(directive);
			
			// delete onclick attribute
			x.removeAttr(directive);
			js_out.write("\r\n");
			js_out.write("var element_" + ele_id + " = document.getElementById(\"" + ele_id + "\");");
			js_out.write("\r\n");
			js_out.write("element_" + ele_id + ".addEventListener(\""+ js_directive +"\", function(){" + function_content + "}, false);" );
		}
	}
	
	public String generateCSPHeader(Elements ele, Elements ele_css)
	{
		String CSPHeader = "Content-Security-Policy: default-src 'self'; script-src 'slef' ";
		for (Element y : ele)
		{
			//System.out.println(y);
			CSPHeader = CSPHeader + y.attr("src") + " ";
		}
		CSPHeader = CSPHeader + newFileName + ".js ";
		// delete the last space 
		CSPHeader = CSPHeader.substring(0, CSPHeader.length() - 1);
		CSPHeader = CSPHeader + "; ";
		CSPHeader = CSPHeader + "style-src 'self' ";
		for (Element x : ele_css)
		{
			CSPHeader = CSPHeader + x.attr("href") + " ";
		}
		CSPHeader = CSPHeader.substring(0, CSPHeader.length() - 1);
		CSPHeader = CSPHeader + "; ";
		return CSPHeader;
	}
	
	public void addExternalJsToHtml()
	{
		doc.head().appendElement("script").attr("src", newFileName + ".js");
	}
	
	public void addExternalCssToHtml()
	{
		doc.head().appendElement("link").attr("rel", "stylesheet").attr("type", "text/css").attr("href", newFileName + ".css");
	}

	/**
	 * **************************************************************
	 * the policy is not implemented yet
	 * **************************************************************
	public void generatePolicy(String webpage) throws IOException
	{
		File policy_file = new File(webpage.trim() + "_policy.txt");
		// add true to enable append content to the file
		BufferedWriter policy_out = new BufferedWriter(new FileWriter(policy_file, true));
		if (!policy_file.exists()) 
		{
			policy_file.createNewFile();
		}
		for (String x : policys)
		{
			policy_out.write(x + "\r\n");
			
		}
		policy_out.close();
	}
	**/
	
	  //static String webadd;
	public static void main(String[] args) throws FileNotFoundException, IOException 
	{
		String htmlFile = "demo/index.html";
		GetURLContent getURL = new GetURLContent(htmlFile);
		getURL.processHTML();
	}
}
