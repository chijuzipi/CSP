package org.cspapplier.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.util.JSON;

public class DiffLogColl extends CSPCollection{
	protected DBCollection diffLog;

	public DiffLogColl(DB db){
		super();
		//NOTE the collection name format
		diffLog = db.getCollection("Diff_Log");
	}

	//insert to diffLog collection
	public void insert(String key, String url, String cssBlack, String cssWarn, String jsBlack, String jsWarn){	
		BasicDBObject doc1 = (BasicDBObject)JSON.parse(cssBlack);
		//BasicDBObject doc2 = (BasicDBObject)JSON.parse(cssWarn);
		BasicDBObject doc3 = (BasicDBObject)JSON.parse(jsBlack);
		//BasicDBObject doc4 = (BasicDBObject)JSON.parse(jsWarn);
		
		String date = CSPMongoDriver.getDate();
		
		System.out.println(doc3);
		
		BasicDBObject docInsert = new BasicDBObject("URLHash", key).
									append("url", url).
									append("cssBlackList", doc1).
									append("jsBlackList", doc3).
									append("date", date);
		/*

		BasicDBObject docInsert = new BasicDBObject("URLHash", key).
									append("cssBlackList", doc1).
									append("cssWarningList", doc2).
									append("jsBlackList", doc3).
									append("jsWarningList", doc4).
									append("date", date);
		*/
		diffLog.insert(docInsert);	
	}
	
	public DBCollection getCollection(){
		return diffLog;
	}
	
	public void query(){
		
	}
	
	public void update(){
		
	}
}
