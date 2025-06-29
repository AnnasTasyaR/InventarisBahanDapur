package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Model class untuk merepresentasikan data user.
 */
public class User implements Serializable {
    private String username;
    private String passwordHash;
    private List<String> history;

    /**
     * Konstruktor default - dibutuhkan untuk deserialisasi dari MongoDB.
     */
    public User() {
        this.history = new ArrayList<>();
    }

    /**
     * Konstruktor dengan parameter username dan password hash.
     * @param username nama pengguna
     * @param passwordHash hash dari kata sandi
     */
    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.history = new ArrayList<>();
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public List<String> getHistory() {
        return history;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setHistory(List<String> history) {
        this.history = (history != null) ? history : new ArrayList<>();
    }

    /**
     * Menambahkan entri ke dalam riwayat pengguna.
     * @param entry string catatan riwayat
     */
    public void addHistory(String entry) {
        if (entry != null && !entry.trim().isEmpty()) {
            if (this.history == null) this.history = new ArrayList<>();
            this.history.add(entry);
        }
    }

    /**
     * Representasi string dari user, termasuk daftar riwayat.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User {\n")
          .append("  Username: ").append(username).append(",\n")
          .append("  History:\n");

        if (history == null || history.isEmpty()) {
            sb.append("    (belum ada riwayat)\n");
        } else {
            for (String h : history) {
                sb.append("    - ").append(h).append("\n");
            }
        }

        sb.append("}");
        return sb.toString();
    }

    // equals dan hashCode berdasarkan username
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
