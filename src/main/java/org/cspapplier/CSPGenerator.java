package main.java.org.cspapplier;

/**
 * Created by Shuangping on 7/30/2014.
 */

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CSPGenerator {
	public String CSPHeader;
    public CSPGenerator(Elements externalJS, Elements externalCSS, String newFileName)
    {

        CSPHeader = "Content-Security-Policy: default-src 'self'; script-src 'slef' ";
        for (Element y : externalJS)
        {
            //System.out.println(y);
            CSPHeader = CSPHeader + y.attr("src") + " ";
        }
        CSPHeader = CSPHeader + newFileName + ".js ";
        // delete the last space
        CSPHeader = CSPHeader.substring(0, CSPHeader.length() - 1);
        CSPHeader = CSPHeader + "; ";
        CSPHeader = CSPHeader + "style-src 'self' ";
        for (Element x : externalCSS)
        {
            CSPHeader = CSPHeader + x.attr("href") + " ";
        }
        CSPHeader = CSPHeader.substring(0, CSPHeader.length() - 1);
        CSPHeader = CSPHeader + "; ";
    }
}
