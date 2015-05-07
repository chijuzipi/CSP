package org.cspapplier.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import com.mongodb.DBObject;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.cspapplier.json.HashMapInJson;

import com.google.gson.Gson;

import java.util.*;

public class PageJsonColl extends CSPCollection{

	protected MongoCollection<Document> pageJson;

	public PageJsonColl(MongoDatabase db){
		super();
		//NOTE the collection name format
		pageJson = db.getCollection("Page_Json");
	}
	
	//insert to pageJson collection
	public void insert(String key, String json){
		BasicDBObject doc = (BasicDBObject)JSON.parse(json);

		String date = CSPMongoDriver.getDate();

		//wrap the document with date and hashKey;
		Document docInsert = new Document("URLHash", key)
				 .append("content", doc)
				 .append("date", date);
		pageJson.insertOne(docInsert);
	}
	
	public MongoCollection getCollection(){
		return pageJson;
	}
	
	public void query(){
		//not implemented
	}

	public void update(HashMapInJson localJson, String hashURL){

		Gson gson = new Gson();
		
		//combine the existing record with the css black list
		//REF: http://stackoverflow.com/questions/7713753/mongodb-merging-two-dbobjects
		DBObject newContentCss = new BasicDBObject(localJson.getCss());

		//combine the existing record with the js black list
		DBObject newContentJs = new BasicDBObject(localJson.getJs());


		BasicDBObject newContent = new BasicDBObject().append("js", newContentJs).append("css", newContentCss);
		String newContentString  = gson.toJson(newContent);
		BasicDBObject newContentJson = (BasicDBObject)JSON.parse(newContentString);

		BasicDBObject newDoc = new BasicDBObject();
		newDoc.append("$set", new BasicDBObject().append("content", newContentJson));
			
		//construct the query
		BasicDBObject searchQuery = new BasicDBObject().append("URLHash", hashURL);

		//update
		pageJson.updateOne(searchQuery, newDoc);

	}
}

