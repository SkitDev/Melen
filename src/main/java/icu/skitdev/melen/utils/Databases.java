package icu.skitdev.melen.utils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import icu.skitdev.melen.utils.useful.Constants;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Databases {
    private final MongoDatabase database;
    private final Logger logger = LoggerFactory.getLogger(Databases.class);

    public Databases(){
        MongoClientURI uri = new MongoClientURI(Constants.MONGODBURI);
        MongoClient mongoClient = new MongoClient(uri);

        database = mongoClient.getDatabase(Constants.MONGODBDATABASE);
    }

    private MongoCollection<Document> getCollection(String name){
        return database.getCollection(name);
    }

    public void insertDocument(String collection, Document document){
        getCollection(collection).insertOne(document);
    }

    public void updateDocument(String collection, String key, long search, Document updatedDocument){
        MongoCollection<Document> col = getCollection(collection);
        Bson filter = new Document(key, search);
        Bson updateOperationDocument = new Document("$set", updatedDocument);
        col.updateMany(filter, updateOperationDocument);
        logger.info("Document Updated");
    }

    public Document getDocument(String collection, String key, long search){
        if(isExists(collection, key, search)){
            MongoCollection<Document> col = getCollection(collection);
            return col.find(new Document(key, search)).first();
        }
        return null;
    }

    public boolean isExists(String collection, String key, long search){
        MongoCollection<Document> col = getCollection(collection);
        Document doc = col.find(new Document(key, search)).first();
        return doc != null;
    }

}