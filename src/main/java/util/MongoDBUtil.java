package util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBUtil {
    private static final String URI = "mongodb://localhost:27017";
    private static final String DB_NAME = "kalkulatorDB";

    private static MongoClient client;

    // Singleton pattern: hanya buat client satu kali
    static {
        client = MongoClients.create(URI);
    }

    public static MongoDatabase getDatabase() {
        return client.getDatabase(DB_NAME);
    }

    // Optional: dipanggil saat aplikasi ditutup
    public static void close() {
        if (client != null) {
            client.close();
        }
    }
}
