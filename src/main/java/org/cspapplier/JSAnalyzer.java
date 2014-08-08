package main.java.org.cspapplier;

import java.io.File;
import com.google.gson.*;

public class JSAnalyzer {
	private JsonObject json;
    public JSAnalyzer(JsonObject jsonObj) {
    	this.json = jsonObj;
    }
    
    public void jsJSONGenerator() {}
    public void jsJSONParser() {}
    public void jsJSONCompare() {}
    public String getXpath() {return "result";}
}
