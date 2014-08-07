package org.cspapplier.json;

import org.cspapplier.util.ElementEventBinder;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Shuangping on 8/1/2014.
 */

public class JsonGenerator {
    private ArrayList<JSinJson> external;
    private ArrayList<JSinJson> block;
    private ArrayList<JSinJson> inline;

    public JsonGenerator(HashMap<String, Elements> externalJS,
                         HashMap<String, Elements> blockJS,
                         HashMap<String, ArrayList<ElementEventBinder>> inlineJS) {
        external = new ArrayList<JSinJson>();
        block = new ArrayList<JSinJson>();
        inline = new ArrayList<JSinJson>();

        JSinJson externalJSinJson;
        for (String identity : externalJS.keySet()) {
            externalJSinJson = new JSinJson(identity, externalJS.get(identity));
            external.add(externalJSinJson);
        }

        JSinJson blockJSinJson;
        for (String identity : blockJS.keySet()) {
            blockJSinJson = new JSinJson(identity, blockJS.get(identity));
            block.add(blockJSinJson);
        }

        JSinJson inlineJSinJson;
        for (String identity : inlineJS.keySet()) {
            inlineJSinJson = new JSinJson(identity, inlineJS.get(identity));
            inline.add(inlineJSinJson);
        }
    }

    public ArrayList<JSinJson> getExternal() {
        return external;
    }

    public void setExternal(ArrayList<JSinJson> external) {
        this.external = external;
    }

    public ArrayList<JSinJson> getBlock() {
        return block;
    }

    public void setBlock(ArrayList<JSinJson> block) {
        this.block = block;
    }

    public ArrayList<JSinJson> getInline() {
        return inline;
    }

    public void setInline(ArrayList<JSinJson> inline) {
        this.inline = inline;
    }
}
