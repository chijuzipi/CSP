package main.java.org.cspapplier;
import org.jsoup.nodes.Document;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * HTMLGenerator
 *
 * Generate new HTML file according to the analysis of JS and CSS of the original
 * web page.
 *
 */

public class HTMLGenerator {
    private final Document inputDOM;
    private final String newFileName;
    private BufferedWriter bufferHTML;

    public HTMLGenerator(URLContentAnalyzer URLAna, JSAnalyzer JSAna ) throws IOException {
        this.inputDOM = URLAna.inputDOM;
        this.newFileName = URLAna.newFileName;

        File outputHtml = new File(newFileName + ".html");
        bufferHTML = new BufferedWriter(new FileWriter(outputHtml));
    }

    public void generateHTML() throws IOException {
        // Add tags in HTMl to include external generated JS/CS
        addExternalJSToHtml();
        addExternalCSSToHtml();

        // Write html into a new file
        bufferHTML.write(inputDOM.toString());
        bufferHTML.close();
    }

    public void addExternalJSToHtml() {
        inputDOM.head().appendElement("script").attr("src", newFileName + ".js");
    }

    public void addExternalCSSToHtml() {
        inputDOM.head().appendElement("link").attr("rel", "stylesheet")
                .attr("type", "text/css").attr("href", newFileName + ".css");
    }
}
