package util;

/**
 * Utility class untuk menyimpan session user yang sedang login.
 */
public class Session {
    // Menyimpan username user yang sedang login
    private static String currentUsername;

    /**
     * Set username dari user yang login.
     * @param username Username yang sedang login
     */
    public static void setCurrentUsername(String username) {
        if (username != null && !username.trim().isEmpty()) {
            currentUsername = username;
        }
    }

    /**
     * Ambil username dari user yang sedang login.
     * @return Username atau null jika belum login
     */
    public static String getCurrentUsername() {
        return currentUsername;
    }

    /**
     * Hapus session saat logout.
     */
    public static void clear() {
        currentUsername = null;
    }

    /**
     * Cek apakah user sudah login.
     * @return true jika sudah login
     */
    public static boolean isLoggedIn() {
        return currentUsername != null;
    }
}
