package dao;

import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;

import model.User;
import org.bson.Document;
import util.MongoDBUtil;
import util.HashUtil;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // Mendaftarkan user baru
    public static boolean signupUser(User user) {
        try {
            MongoCollection<Document> userCol = MongoDBUtil.getDatabase().getCollection("users");

            // Cek apakah username sudah ada
            Document existing = userCol.find(eq("username", user.getUsername())).first();
            if (existing != null) {
                System.out.println("Username already exists: " + user.getUsername());
                return false;
            }

            // Hash password
            String hashedPassword = HashUtil.hashPassword(user.getPasswordHash());

            // Buat dokumen user baru
            Document newUser = new Document("username", user.getUsername())
                    .append("password", hashedPassword)
                    .append("history", new ArrayList<String>());

            // Simpan ke MongoDB
            userCol.insertOne(newUser);
            System.out.println("User inserted!");
            return true;

        } catch (Exception e) {
            System.err.println("Signup error: " + e.getMessage());
            return false;
        }
    }

    // Login user
    public static User login(String username, String password) {
        try {
            MongoCollection<Document> userCol = MongoDBUtil.getDatabase().getCollection("users");
            Document userDoc = userCol.find(eq("username", username)).first();

            if (userDoc != null) {
                String storedHash = userDoc.getString("password");

                if (storedHash.equals(HashUtil.hashPassword(password))) {
                    User user = new User(username, storedHash);

                    List<String> history = userDoc.getList("history", String.class);
                    if (history != null) {
                        user.setHistory(history);
                    }

                    return user;
                } else {
                    System.out.println("Password mismatch for user: " + username);
                }
            } else {
                System.out.println("User not found: " + username);
            }

        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
        }
        return null;
    }

    // Temukan user berdasarkan username
    public static User findUserByUsername(String username) {
        try {
            MongoCollection<Document> userCol = MongoDBUtil.getDatabase().getCollection("users");
            Document userDoc = userCol.find(eq("username", username)).first();

            if (userDoc != null) {
                String hash = userDoc.getString("password");
                List<String> history = userDoc.getList("history", String.class);

                User user = new User(username, hash);
                if (history != null) {
                    user.setHistory(history);
                }

                return user;
            }

        } catch (Exception e) {
            System.err.println("Find user error: " + e.getMessage());
        }
        return null;
    }

    // Tambahkan catatan riwayat BMI ke database
    public static void saveHistory(String username, String result) {
        try {
            MongoCollection<Document> userCol = MongoDBUtil.getDatabase().getCollection("users");
            userCol.updateOne(
                    eq("username", username),
                    new Document("$push", new Document("history", result))
            );
        } catch (Exception e) {
            System.err.println("Save history error: " + e.getMessage());
        }
    }

    // Ambil seluruh riwayat pengukuran BMI user
    public static List<String> getHistory(String username) {
        try {
            MongoCollection<Document> userCol = MongoDBUtil.getDatabase().getCollection("users");
            Document doc = userCol.find(eq("username", username)).first();

            if (doc != null && doc.containsKey("history")) {
                return doc.getList("history", String.class);
            }

        } catch (Exception e) {
            System.err.println("Get history error: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    // Alias agar kompatibel dengan KalkulatorController
    public static List<String> getUserHistory(String username) {
        return getHistory(username);
    }

    public static void updateUserHistory(String username, String record) {
        saveHistory(username, record);
    }
}
