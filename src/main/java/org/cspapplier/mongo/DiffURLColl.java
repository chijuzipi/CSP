package org.cspapplier.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class DiffURLColl extends CSPCollection{
	protected DBCollection diffURL;

	public DiffURLColl(DB db){
		super();
		//NOTE the collection name format
		diffURL = db.getCollection("Diff_URL");
	}

	//insert to diffURL collection
	public void insert(String URLHash, String url){

		BasicDBObject query = new BasicDBObject("URLHash", URLHash);
		DBCursor cursor = diffURL.find(query);
		//if no record in the collection, insert the item
		if(cursor.count() == 0){
			BasicDBObject docInsert = new BasicDBObject("URLHash", URLHash).
										append("url", url).
										append("count", 1);
			diffURL.insert(docInsert);
		}

		//if found existing record, increase count by 1
		else{
			BasicDBObject newDocument = 
					new BasicDBObject().append("$inc", 
					new BasicDBObject().append("count", 1));
			 
			diffURL.update(new BasicDBObject().append("URLHash", URLHash), newDocument);
		}
	}
	
	public DBCollection getCollection(){
		return diffURL;
	}
	
	public void query(){
		
	}
	
	public void update(){
		
	}
}
