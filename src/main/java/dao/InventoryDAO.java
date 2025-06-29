package dao;

import com.mongodb.client.*;
import model.BahanDapur;
import org.bson.Document;
import util.MongoDBUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class InventoryDAO {
    private MongoCollection<Document> collection;

    public InventoryDAO() {
        MongoDatabase db = MongoDBUtil.getDatabase();
        collection = db.getCollection("bahan_dapur");
    }

    // Tambah bahan baru
    public void tambahBahan(BahanDapur bahan) {
        Document doc = new Document("id", bahan.getId())
                .append("nama", bahan.getNama())
                .append("jumlah", bahan.getJumlah())
                .append("satuan", bahan.getSatuan())
                .append("tanggalKadaluarsa", bahan.getTanggalKadaluarsa().toString())
                .append("kategori", bahan.getKategori());
        collection.insertOne(doc);
    }

    // Ambil semua bahan
    public List<BahanDapur> getSemuaBahan() {
        List<BahanDapur> daftar = new ArrayList<>();
        FindIterable<Document> docs = collection.find();
        for (Document doc : docs) {
            BahanDapur bahan = new BahanDapur(
                    doc.getString("id"),
                    doc.getString("nama"),
                    doc.getInteger("jumlah"),
                    doc.getString("satuan"),
                    LocalDate.parse(doc.getString("tanggalKadaluarsa")),
                    doc.getString("kategori")
            );
            daftar.add(bahan);
        }
        return daftar;
    }

    // Hapus bahan berdasarkan ID
    public void hapusBahan(String id) {
        collection.deleteOne(new Document("id", id));
    }

    // Update bahan (untuk fitur Edit)
    public void updateBahan(BahanDapur bahan) {
        Document updated = new Document("$set", new Document()
                .append("nama", bahan.getNama())
                .append("jumlah", bahan.getJumlah())
                .append("satuan", bahan.getSatuan())
                .append("tanggalKadaluarsa", bahan.getTanggalKadaluarsa().toString())
                .append("kategori", bahan.getKategori()));

        collection.updateOne(eq("id", bahan.getId()), updated);
    }
}
