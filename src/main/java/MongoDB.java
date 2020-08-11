import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class MongoDB{
    public static void main(String args[] ){
        try {
            //连接到MongoDB服务 如果是远程连接可以替换“localhost”为服务器所在IP地址
            MongoClient mongoClient = new MongoClient("localhost",27018);
            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase("databaseName");
            System.out.println("Connect to database successfully");

            final MongoCollection<Document> collection = mongoDatabase.getCollection("test");
            System.out.println("集合 test 选择成功");

            //插入文档
            for(int i=0;i<1;i++){
                new Thread(){
                    @Override
                    public void run(){
                        int num=0;
                        while (true){
                            num++;
                            Document document = new Document("title", "MongoDB"+num).
                                    append("description", "database").
                                    append("likes", num+100).
                                    append("by", Math.random());
                            List<Document> documents = new ArrayList<Document>();
                            documents.add(document);
                            collection.insertMany(documents);
                            if(num==200){break;}
                            try{
                                TimeUnit.MILLISECONDS.sleep(100);}catch(InterruptedException e){e.printStackTrace();}
                        }
                    }
                }.start();
            }

            //检索所有文档
            FindIterable<Document> findIterable = collection.find();
            MongoCursor<Document> mongoCursor = findIterable.iterator();
            while(mongoCursor.hasNext()){
                System.out.println(mongoCursor.next());
            }

            System.out.println("第二次提交...");

        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
}
