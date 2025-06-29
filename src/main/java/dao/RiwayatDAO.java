package dao;

import com.mongodb.client.*;
import model.Riwayat;
import org.bson.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RiwayatDAO {
    private final MongoCollection<Document> collection;

    public RiwayatDAO() {
        MongoClient mongoClient = MongoClients.create(); // default localhost:27017
    MongoDatabase database = mongoClient.getDatabase("kalkulatorDB");

        collection = database.getCollection("riwayat");
    }

    public void simpan(Riwayat riwayat) {
        Document doc = new Document()
                .append("username", riwayat.getUsername())
                .append("aktivitas", riwayat.getAktivitas())
                .append("timestamp", riwayat.getTimestamp().toString());
        collection.insertOne(doc);
    }

    public List<Riwayat> getByUser(String username) {
        List<Riwayat> list = new ArrayList<>();
        FindIterable<Document> documents = collection.find(new Document("username", username));

        for (Document doc : documents) {
            String aktivitas = doc.getString("aktivitas");
            LocalDateTime waktu = LocalDateTime.parse(doc.getString("timestamp"));
            list.add(new Riwayat(username, aktivitas, waktu));
        }

        return list;
    }
}
