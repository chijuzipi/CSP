package org.cspapplier;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Shuangping on 7/30/2014.
 */
public class HTMLGenerator {
    public void processHTML() throws IOException {

        addExternalJsToHtml();
        addExternalCssToHtml();

        // Write html into a new file
        bufferHTML.write(inputDOM.toString());
        bufferJS.close();
        bufferCSS.close();
        bufferHTML.close();
    }
    public void addExternalJsToHtml()
    {
        inputDOM.head().appendElement("script").attr("src", newFileName + ".js");
    }

    public void addExternalCssToHtml()
    {
        inputDOM.head().appendElement("link").attr("rel", "stylesheet").attr("type", "text/css").attr("href", newFileName + ".css");
    }
}
