package org.cspapplier.mongo;

import com.mongodb.client.*;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CSPMongoDriver {
	protected MongoDatabase db;
	public PageJsonColl pageJson;
	/**
	 *  public DiffLogColl diffLog;
	 *  public DiffURLColl diffURL;
	 *  public DynamicPageColl DPage;
	 */

	public CSPMongoDriver() throws UnknownHostException{
        MongoClient mongoClient = new MongoClient( "localhost", 27017);
        db = mongoClient.getDatabase("csp");

        //#1 collection, store page json
        pageJson = new PageJsonColl(db);

		/*

        //#2 collection, store difference list for each url
        diffLog = new DiffLogColl(db);

        //#3 collection, store difference url and the differnce count
        diffURL = new DiffURLColl(db);

        //#4 collection, store the dynamic page url and hashkey 
        DPage = new DynamicPageColl(db);
        */
	}

	public MongoCollection getCollection(int dbIndex){
			return pageJson.getCollection();
	}

	public static String getDate(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime()); //2014/08/06 16:00:22
	}

}//end of class
