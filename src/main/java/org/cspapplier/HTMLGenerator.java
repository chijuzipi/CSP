package main.java.org.cspapplier;

import org.jsoup.nodes.Document;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Shuangping on 7/30/2014.
 */
public class HTMLGenerator {
    private final Document inputDOM;
    private final String newFileName;
    public HTMLGenerator(URLContentAnalyzer getURL) throws IOException {
        this.inputDOM = getURL.inputDOM;
        this.newFileName = getURL.newFileName;

        BufferedWriter bufferHTML;
        File outputHtml = new File(newFileName + ".html");
        bufferHTML = new BufferedWriter(new FileWriter(outputHtml));

        // Add tags in HTMl to include external generated JS/CS
        addExternalJsToHtml();
        addExternalCssToHtml();

        // Write html into a new file
        bufferHTML.write(inputDOM.toString());
        bufferHTML.close();
    }
    public void addExternalJsToHtml() {
        inputDOM.head().appendElement("script").attr("src", newFileName + ".js");
    }

    public void addExternalCssToHtml() {
        inputDOM.head().appendElement("link").attr("rel", "stylesheet")
                .attr("type", "text/css").attr("href", newFileName + ".css");
    }
}
