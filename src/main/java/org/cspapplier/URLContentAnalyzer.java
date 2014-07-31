package org.cspapplier;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class URLContentAnalyzer
{
	// For creating id for extracted elements
	private int jsCount;
	private int cssCount;

	// Private File output_policy;
	private BufferedWriter bufferHTML;
	private BufferedWriter bufferJS;
	private BufferedWriter bufferCSS;

	// Private BufferedWriter policy_out;
	private Document inputDOM;
	private String newFileName;

	public URLContentAnalyzer(String input) throws IOException {
		//policy remain un-implemented
		//ArrayList<String> policys = new ArrayList();

		//used to generate id for inline script elements
		jsCount = 0;
		cssCount = 0;

		//prepare IO
		File inputFile = new File(input);

        String[] pathList = input.trim().split("/");
		String fileName = pathList[pathList.length -1];
		System.out.println(fileName);

		String[] fileNameList = fileName.split("\\.");
		System.out.println(fileNameList.length);
		
		newFileName = fileNameList[fileNameList.length - 2];
		System.out.println(newFileName);

        File outputHtml = new File(newFileName + ".html");
        File outputScript = new File(newFileName + ".js");
        File outputCSS = new File(newFileName + ".css");

        bufferHTML = new BufferedWriter(new FileWriter(outputHtml));
		bufferJS = new BufferedWriter(new FileWriter(outputScript));
		bufferCSS = new BufferedWriter(new FileWriter(outputCSS));

		//initialize Jsoup document
		inputDOM = Jsoup.parse(inputFile, "UTF-8");
	}

    public void processHTML() throws IOException
    {
        //select external script and styles, using them to generate csp header
        Elements externalJS = inputDOM.select("script[src]");
        Elements externalCSS = inputDOM.select("link[href$=.css]");
        String cspHeader = generateCSPHeader(externalJS, externalCSS);
        // this header is at the HTTP request, so no output file, just print out.
        System.out.println(cspHeader);

        //select and write internal script to .js file and remove it from HTML
        Elements inlineJS = inputDOM.select("script").not("script[src]");
        for (Element item : inlineJS){
            bufferJS.write(item.data());
            // Delete the element
            item.remove();
        }

        // Select and write inline script to file and remove it from HTML
        Elements inlineJS;
        String[] triggerEvents = {
            // Mouse Events
            "onclick", "ondblclick", "onmousedown", "onmousemove", "onmouseover", "onmouseout", "onmouseup",
            // Keyboard Events
            "onkeydown", "onkeypress", "onkeyup",
            // Frame/Object Events
            "onabort", "onerror", "onload", "onresize", "onscroll", "onunload",
            // Form Events
            "onblur", "onchange", "onfocus", "onreset", "onselect", "onsubmit"
        };

        // Write the document ready part
        bufferJS.write("\r\n");
        bufferJS.write("document.addEventListener('DOMContentReady', function () {");
        bufferJS.write("\r\n");
        for (String event : triggerEvents) {
            inlineJS = inputDOM.select("[" + event +"]");
            extractInlineJS(inlineJS, event);
        }
        bufferJS.write("});");

        //select and write internal style to .css file and remove it from HTML
        Elements inlineCSS = inputDOM.select("style");
        for (Element item : inlineCSS){
            bufferCSS.write(item.data());
            // Delete the element
            item.remove();
        }

        //select and write inline style to .css file and remove it from HTML
        Elements inline_styles = inputDOM.select("[style]");
        for(Element z : inline_styles){
            String cssId;
            if (z.id().equals("")){
                z.attr("id", String.valueOf(cssCount));
                cssId = String.valueOf(cssCount);
                cssCount++;
            }
            else{
                cssId = z.id();
            }
            String cssContent = z.attr("style");
            z.removeAttr("style");
            bufferCSS.write("\r\n");
            bufferCSS.write("#"+cssId+ " {" + cssContent + "}");
            bufferCSS.write("\r\n");

        }

    }
	
	// Write inline script to external .js file
	public void extractInlineJS(Elements ele, String directive) throws IOException
	{
		// delete front two bits
		String js_directive = directive.substring(2);
		for (Element x : ele)
		{
			// add id check, if exists then use it. otherwise create a new one
			String ele_id = x.id();
			if (ele_id.equals(""))
			{
				//convert to String
				x.attr("id", String.valueOf(jsCount));
				ele_id = String.valueOf(jsCount);
				jsCount++;
			}
			
			String function_content = x.attr(directive);
			
			// delete onclick attribute
			x.removeAttr(directive);
			bufferJS.write("\r\n");
			bufferJS.write("var element_" + ele_id + " = document.getElementById(\"" + ele_id + "\");");
			bufferJS.write("\r\n");
			bufferJS.write("element_" + ele_id + ".addEventListener(\""+ js_directive +"\", function(){" + function_content + "}, false);" );
		}
	}

    public void extractInlineCSS(Elements ele, String directive) throws IOException
    {

    }
	public static void main(String[] args) throws FileNotFoundException
	{
		String htmlFile = "demo/Twitter.html";
		URLContentAnalyzer getURL = new URLContentAnalyzer(htmlFile);
		getURL.processHTML();
	}
}
