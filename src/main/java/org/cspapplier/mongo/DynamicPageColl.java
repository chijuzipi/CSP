package org.cspapplier.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public class DynamicPageColl extends CSPCollection{
	protected DBCollection DP;

	public DynamicPageColl(DB db){
		super();
		DP = db.getCollection("Dynamic_Page");
	}

	//insert to diffLog collection
	public void insert(String hashKey, String url){	

		String date = CSPMongoDriver.getDate();

		//wrap the document with date and hashKey;
		BasicDBObject docInsert = new BasicDBObject("URLHash", hashKey).
										append("url", url).
										append("count", 0).
										append("date", date);
		DP.insert(docInsert);
	}
	
	public void remove(String hashKey){
		BasicDBObject document = new BasicDBObject();
		document.put("URLHash", hashKey);
		DP.remove(document);
	}
	
	public DBCollection getCollection(){
		return DP; 
	}
	
	public void query(){
		
	}
	
	public void update(String hashKey){
		BasicDBObject newDocument = 
			new BasicDBObject().append("$inc", 
			new BasicDBObject().append("count", 1));
		 
		DP.update(new BasicDBObject().append("URLHash", hashKey), newDocument);
		
	}
}

