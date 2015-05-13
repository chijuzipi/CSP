package org.cspapplier;

import org.cspapplier.json.JsonAnalyzer;

import java.io.*;

/**
 * Created by Yu on 5/12/15.
 */
public class ReportGenerator {
    private String filePath;
    private String hashURL;
    private JsonAnalyzer jsonAnalyzer;

    public ReportGenerator(String filePath, String hashURL, JsonAnalyzer jsonAnalyzer){
        this.filePath = filePath;
        this.hashURL  = hashURL;
        this.jsonAnalyzer = jsonAnalyzer;
    }
    public void generateReport(){
        String path = filePath + "/"+hashURL + "/";
        File output= new File(path + hashURL+".report");
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(output, true)));
            out.println("TIME: " + System.currentTimeMillis() + "----------------------------------->");

            for (String jsBlackID : jsonAnalyzer.getJsComparisonResult().getBlackList().keySet()) {

                out.println("########### JS BLACK ###########");
                out.println(jsBlackID);
                out.println();
            }

            for (String cssBlackID : jsonAnalyzer.getCssComparisonResult().getBlackList().keySet()) {

                out.println("########### CSS BLACK ###########");
                out.println(cssBlackID);
                out.println();
            }

            for (String jsWarnID : jsonAnalyzer.getJsComparisonResult().getWarningList().keySet()) {

                out.println("########### JS WARN ###########");
                out.println(jsWarnID);
                out.println();
            }

            for (String cssWarnID : jsonAnalyzer.getCssComparisonResult().getWarningList().keySet()) {

                out.println("########### CSS WARN ###########");
                out.println(cssWarnID);
                out.println();
            }
            out.println();
            out.println();
            out.println();
            out.close();

        }
        catch (IOException ex){
            System.out.println (ex.toString());
        }

    }
}
