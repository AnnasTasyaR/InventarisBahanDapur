package util;

import model.BahanDapur;
import java.time.LocalDate;

public class ExpiredCheckerUtil {
    public static boolean isExpired(BahanDapur bahan) {
        return bahan.getTanggalKadaluarsa().isBefore(LocalDate.now());
    }

    public static String getWarningMessage(BahanDapur bahan) {
        if (isExpired(bahan)) {
            return "⚠️ Bahan \"" + bahan.getNama() + "\" sudah kadaluarsa pada " + bahan.getTanggalKadaluarsa() + ".";
        }
        return null;
    }
}
