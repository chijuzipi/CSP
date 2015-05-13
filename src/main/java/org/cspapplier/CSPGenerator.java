package org.cspapplier;

/**
 * CSPGenerator
 *
 * Generate CSP header according to the results from URL analyzer.
 *
 */

import org.jsoup.nodes.Element;

public class CSPGenerator {
    private String httpPath;
	private String CSPHeader;
	private URLContentAnalyzer URLContent;

    public CSPGenerator(URLContentAnalyzer getURL, String httpPath)
    {
    	this.URLContent = getURL;
        this.CSPHeader = "Content-Security-Policy: script-src 'self' ";
        this.httpPath = URLContentGenerator.generateCompletePath(httpPath);
    }

    public void generateCSPHeader(){
        /**
         * JS Part
         */
        for (Element y : URLContent.getExternalJSElements())
        {
            String srcURL = y.attr("src");
            if (srcURL.startsWith("//"))
                srcURL = "http:" + srcURL;
            CSPHeader = CSPHeader + srcURL + " ";
        }
        CSPHeader += httpPath + " ";
        CSPHeader += "'unsafe-eval'; ";

        /**
         * CSS Part
         */
        CSPHeader = CSPHeader + "style-src 'self' ";
        for (Element y : URLContent.getExternalCSSElements())
        {
            String srcURL = y.attr("href");
            if (srcURL.startsWith("//"))
                srcURL = "http:" + srcURL;
            CSPHeader = CSPHeader + srcURL + " ";
        }
        CSPHeader += httpPath + " ";
        CSPHeader += "'unsafe-inline'";
    }

    public String getCSPHeader() {
        return CSPHeader;
    }
}
