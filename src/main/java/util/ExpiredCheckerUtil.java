package util;

import model.BahanDapur;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utilitas untuk mengecek bahan kadaluarsa
 * + Membungkus hasilnya menggunakan generic class DataWrapper<List<BahanDapur>>
 */
public class ExpiredCheckerUtil {

    // Mengecek apakah satu bahan sudah kadaluarsa
    public static boolean isExpired(BahanDapur bahan) {
        return bahan.getTanggalKadaluarsa().isBefore(LocalDate.now());
    }

    // Menampilkan pesan peringatan jika kadaluarsa
    public static String getWarningMessage(BahanDapur bahan) {
        if (isExpired(bahan)) {
            return "⚠️ Bahan \"" + bahan.getNama() + "\" sudah kadaluarsa pada " + bahan.getTanggalKadaluarsa() + ".";
        }
        return null;
    }

    // ✅ Generic: Membungkus daftar bahan yang sudah kadaluarsa
    public static DataWrapper<List<BahanDapur>> getExpiredWrapper(List<BahanDapur> semuaBahan) {
        List<BahanDapur> expiredList = semuaBahan.stream()
                .filter(ExpiredCheckerUtil::isExpired)
                .collect(Collectors.toList());

        return new DataWrapper<>(expiredList);
    }
}
