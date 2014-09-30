package org.cspapplier;

/**
 * Created by Shuangping on 7/30/2014.
 */

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CSPGenerator {
	private String CSPHeader;
	private URLContentAnalyzer URLContent;
    public CSPGenerator(URLContentAnalyzer getURL)
    {
    	this.URLContent = getURL;

    }
    public String generateCSPHeader(){
        CSPHeader = "Content-Security-Policy: default-src 'self'; script-src 'self' ";
        for (Element y : URLContent.getExternalJSElements())
        {
            //System.out.println(y);
            CSPHeader = CSPHeader + y.attr("src") + " ";
        }
        CSPHeader = CSPHeader + URLContent.getHashURL() + ".js ";
        // delete the last space
        CSPHeader = CSPHeader.substring(0, CSPHeader.length() - 1);
        CSPHeader = CSPHeader + "; ";
        CSPHeader = CSPHeader + "style-src 'self' ";
        for (Element x : URLContent.getExternalCSSElements())
        {
            CSPHeader = CSPHeader + x.attr("href") + " ";
        }
        CSPHeader = CSPHeader.substring(0, CSPHeader.length() - 1);
        CSPHeader = CSPHeader + "; ";
        return CSPHeader;
    	
    }
}
