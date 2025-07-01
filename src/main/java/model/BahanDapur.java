package model;

import java.io.Serializable;
import java.time.LocalDate;

public class BahanDapur implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String nama;
    private int jumlah;
    private String satuan;
    private LocalDate tanggalKadaluarsa;
    private String kategori;

    public BahanDapur(String id, String nama, int jumlah, String satuan, LocalDate tanggalKadaluarsa, String kategori) {
        this.id = id;
        this.nama = nama;
        this.jumlah = jumlah;
        this.satuan = satuan;
        this.tanggalKadaluarsa = tanggalKadaluarsa;
        this.kategori = kategori;
    }

    // Getter dan Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }

    public String getSatuan() { return satuan; }
    public void setSatuan(String satuan) { this.satuan = satuan; }

    public LocalDate getTanggalKadaluarsa() { return tanggalKadaluarsa; }
    public void setTanggalKadaluarsa(LocalDate tanggalKadaluarsa) { this.tanggalKadaluarsa = tanggalKadaluarsa; }

    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }
}
