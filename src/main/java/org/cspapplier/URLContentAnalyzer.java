package main.java.org.cspapplier;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
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

	private BufferedWriter bufferJS;
	private BufferedWriter bufferCSS;

	public Document inputDOM;
	public String newFileName;

	public URLContentAnalyzer(String input) throws IOException
    {
		//used to generate id for inline script elements
		jsCount = 0;
		cssCount = 0;

		// Prepare IO
		File inputFile = new File(input);

        String[] pathList = input.trim().split("/");
		String fileName = pathList[pathList.length -1];
		System.out.println(fileName);

		String[] fileNameList = fileName.split("\\.");
		System.out.println(fileNameList.length);
		
		newFileName = fileNameList[fileNameList.length - 2];
		System.out.println(newFileName);

        File outputScript = new File(newFileName + ".js");
        File outputCSS = new File(newFileName + ".css");

		bufferJS = new BufferedWriter(new FileWriter(outputScript));
		bufferCSS = new BufferedWriter(new FileWriter(outputCSS));

		// Parse the DOM structure of the input URL content
		inputDOM = Jsoup.parse(inputFile, "UTF-8");
	}

    public void processHTML() throws IOException
    {
        // Select external script and styles, using them to generate csp header
        Elements externalJS = inputDOM.select("script[src]");
        Elements externalCSS = inputDOM.select("link[href$=.css]");

        // Select and write block script to .js file and remove it from HTML
        extractBlockJS();

        // Select and write inline script to file and remove it from HTML
        extractInlineJS();

        //select and write internal style to .css file and remove it from HTML
        extractBlockCSS();

        //select and write inline style to .css file and remove it from HTML
        extractInlineCSS();
    }

    public void extractBlockJS() throws IOException
    {
        Elements blockJS = inputDOM.select("script").not("script[src]");
        for (Element item : blockJS){
            bufferJS.write(item.data());
            // Delete the element
            item.remove();
        }
    }

    public void extractInlineJS() throws IOException
    {
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
            extractEachInlineJS(inlineJS, event);
        }

        bufferJS.write("});");
		bufferJS.close();
    }

	// Write inline script to external .js file
	public void extractEachInlineJS(Elements elements, String event) throws IOException
	{
		// Remove first two characters (e.g. "on" in "onclick")
		String jsTriggerEvent = event.substring(2);
		for (Element ele : elements)
		{
			// add id check, if exists then use it. otherwise create a new one
			String ele_id = ele.id();
			if (ele_id.equals(""))
			{
				// Convert to String
				ele.attr("id", String.valueOf(jsCount));
				ele_id = String.valueOf(jsCount);
				jsCount++;
			}
			
			String function_content = ele.attr(event);
			
			// delete onclick attribute
			ele.removeAttr(event);
			bufferJS.write("\r\n");
			bufferJS.write("var element_" + ele_id + " = document.getElementById(\"" + ele_id + "\");");
			bufferJS.write("\r\n");
			bufferJS.write("element_" + ele_id + ".addEventListener(\"" + jsTriggerEvent
                           + "\", function(){" + function_content + "}, false);" );
		}

	}

    public void extractBlockCSS() throws IOException
    {
        Elements inlineCSS = inputDOM.select("style");
        for (Element item : inlineCSS){
            bufferCSS.write(item.data());
            // Delete the element
            item.remove();
        }
    }
    public void extractInlineCSS() throws IOException
    {
        Elements inlineScript = inputDOM.select("[style]");
        for (Element z : inlineScript) {
            String cssId;
            if (z.id().equals("")) {
                z.attr("id", String.valueOf(cssCount));
                cssId = String.valueOf(cssCount);
                cssCount++;
            } else {
                cssId = z.id();
            }
            String cssContent = z.attr("style");
            z.removeAttr("style");

            bufferCSS.write("\r\n");
            bufferCSS.write("#" + cssId + " {" + cssContent + "}");
            bufferCSS.write("\r\n");
        }
        bufferCSS.close();
    }

	public static void main(String[] args) throws IOException
    {
		String htmlFile = "demo/Twitter.html";
		URLContentAnalyzer getURL = new URLContentAnalyzer(htmlFile);
		getURL.processHTML();
	}
}
