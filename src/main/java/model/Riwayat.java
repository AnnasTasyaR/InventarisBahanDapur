package model;

import java.time.LocalDateTime;

public class Riwayat {
    private String username;
    private String aktivitas;
    private LocalDateTime timestamp;

    public Riwayat(String username, String aktivitas, LocalDateTime timestamp) {
        this.username = username;
        this.aktivitas = aktivitas;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public String getAktivitas() {
        return aktivitas;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + aktivitas;
    }
}
