package main.java.org.cspapplier.json;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;

/**
 * JsonWriter.java
 *
 * Convert the HashMapInJson object to json string and write to file.
 */

public class JsonWriter {
    private String json;
    private String fileName;

    public JsonWriter(HashMapInJson hashMapInJson, String hashURL) {
        Gson gson = new Gson();
        this.json = gson.toJson(hashMapInJson);
        this.fileName = hashURL + ".json";
    }

    public void write() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(new File(this.fileName));
        OutputStreamWriter outputStream = new OutputStreamWriter(fileOutputStream);

        try {
            outputStream.append(this.json);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            outputStream.close();
            fileOutputStream.close();
        }
    }

}
