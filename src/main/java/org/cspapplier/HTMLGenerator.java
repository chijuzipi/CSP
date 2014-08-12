/**
 * FileGenerator
 *
 * Generate new HTML file and external js file ccording to the three hashmap generated from JSONAnalyzer
 *
**/
package main.java.org.cspapplier;
import main.java.org.cspapplier.util.ElementEventBinder;
import main.java.org.cspapplier.util.SHAHash;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;

//import org.jsoup.select.Elements;


import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;


public class HTMLGenerator {

    //private HashMap<String, Element> externalJSMap;
    private HashMap<String, Element> blockJSMap;
    private HashMap<String, ArrayList<ElementEventBinder>> inlineJSMap;
    
    private String newFileName;

    //input HTML file and its doc element
    private File inputHTML;
    private Document doc; 
    
    //out put css, js and new html file
    private File outputScript;
    private File outputHTML;
    private File outputCss;
    
    private BufferedWriter bufferJS;
    private BufferedWriter bufferCSS;
    private BufferedWriter bufferHTML;
    
    // Initialize counters to generate id for inline JS/CSS
    int inlineJsCount;
    int inlineCssCount;

    public HTMLGenerator(HashMap<String, Element> bl, HashMap<String, ArrayList<ElementEventBinder>> in, String fileName) throws IOException{
        //this.externalJSMap = ex;
        this.blockJSMap = bl;
        this.inlineJSMap = in;

        inputHTML = new File(fileName);
        newFileName = generateFileName(fileName);
        
        inlineJsCount = 0;
        inlineCssCount = 0;
        

        File outputScript = new File(newFileName + ".js");
        File outputCSS = new File(newFileName + ".css");
        File outputHTML = new File(newFileName + ".html");

        // Prepare buffer for file IO
        bufferJS = new BufferedWriter(new FileWriter(outputScript));
        bufferCSS = new BufferedWriter(new FileWriter(outputCSS));
        bufferHTML = new BufferedWriter(new FileWriter(outputHTML));

        doc = Jsoup.parse(inputHTML, "UTF-8");
        generateBlockJS();
        generateInlineJS();
        processHTML();
    }
    
    private String generateFileName(String fileName){
    	String[] pathList = fileName.trim().split("/");
		String input = pathList[pathList.length -1];

		String[] fileNameList = input.split("\\.");
		System.out.println(fileNameList.length);
		
		String newFileName = fileNameList[fileNameList.length - 2];
		System.out.println(newFileName);
		return newFileName;
    }

    private void generateBlockJS() throws IOException{
    	for(Element x : blockJSMap.values()){
    		bufferJS.write(x.data().toString());
    	}
    }
    
    private void generateInlineJS() throws IOException{
		//write the document ready part
		bufferJS.write("\r\n");
		bufferJS.write("document.addEventListener('DOMContentReady', function () {");
		bufferJS.write("\r\n");
    	//using two arraylist to store all the element-event pair 
    	ArrayList<Element> eleList = new ArrayList<Element>();
    	ArrayList<String>  eventList = new ArrayList<String>();
    	
    	//generate the two arraylist
    	for(ArrayList<ElementEventBinder> x : inlineJSMap.values()){
    		for (int i = 0; i < x.size(); i++){
    			Element ele  = x.get(i).getElement();
    			String event = x.get(i).getEvent(); 
    			eleList.add(ele);
    			eventList.add(event);
    		}
    	}
    	
    	for (int j = 0; j < eleList.size(); j++){
    		Element x = eleList.get(j);
    		String event = eventList.get(j);
    		
    		//this is for the event string for external js file
    		String eventExternal = event.substring(2);

    		String ele_id = x.id();
    		if (ele_id == "")
			{
				//convert to String
				x.attr("id", String.valueOf(inlineJsCount));
				ele_id = String.valueOf(inlineJsCount);
				inlineJsCount++;
			}
			
			String function_content = x.attr(event);
			
			bufferJS.write("\r\n");
			bufferJS.write("var element_" + ele_id + " = document.getElementById(\"" + ele_id + "\");");
			bufferJS.write("\r\n");
			bufferJS.write("element_" + ele_id + ".addEventListener(\""+ eventExternal +"\", function(){" + function_content + "}, false);" );
		}
    	bufferJS.write("});");
    }
    
	private void processHTML() throws IOException {
		//select external script and styles, using them to generate csp header
		Elements externalJS = doc.select("script[src]");
		Elements externalCSS = doc.select("link[href$=.css]");
		CSPGenerator cspGen = new CSPGenerator(externalJS, externalCSS, newFileName);
		
		//remve block JS
		Elements blockJS = doc.select("script").not("script[src]");
		for (Element x : blockJS){
			x.remove();
		}
		
		//remove inline JS
		String[] scriptDirective = {
    			// Mouse Events
    		"onclick", "ondblclick", "onmousedown", "onmousemove", "onmouseover", "onmouseout", "onmouseup", 
    			// Keyboard Events
    		"onkeydown", "onkeypress", "onkeyup", 
    			// Frame/Object Events
    		"onabort", "onerror", "onload", "onresize", "onscroll", "onunload",
    			// Form Events
    		"onblur", "onchange", "onfocus", "onreset", "onselect", "onsubmit"};
		
		for (int i = 0; i < scriptDirective.length; i++){
			Elements inlineJS = doc.select("[" + scriptDirective[i] +"]");
			for (Element x : inlineJS){
				x.removeAttr(scriptDirective[i]);
			}
		}	

		//remove block CSS 
		Elements blockCSS = doc.select("style");
		for (Element y : blockCSS){
			y.remove();
		}	

		//remove inline CSS
		Elements inlineCSS = doc.select("[style]");
		for(Element z : inlineCSS){
			z.removeAttr("style");
		}
		
		//add external JS file to html
		doc.head().appendElement("script").attr("src", newFileName + ".js");
		
		//add external CSS file to html
		doc.head().appendElement("link").attr("rel", "stylesheet").attr("type", "text/css").attr("href", newFileName + ".css");

		// write html into a new file
		bufferHTML.write(doc.toString());
	}
}
