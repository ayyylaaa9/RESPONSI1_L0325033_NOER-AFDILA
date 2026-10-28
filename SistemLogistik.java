/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package responsi1;

/**
 *
 * @author Noer Afdila
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SistemLogistik {
    private static final ArrayList<BarangBantuan> daftarBarang =
            new ArrayList<>();

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean selesai = false;

        do {
            System.out.println("\n=== SISTEM LOGISTIK BANTUAN ===");
            System.out.println("1. Tambah Barang");
            System.out.println("2. Selesai dan Cetak Manifes");
            System.out.print("Pilih menu: ");

            String pilihan = input.nextLine();

            if (pilihan.equals("1")) {
                tambahBarang(input);
            } else if (pilihan.equals("2")) {
                cetakManifes();
                selesai = true;
            } else {
                System.out.println(
                    "Pilihan tidak valid. Coba lagi."
                );
            }

        } while (!selesai);

        input.close();
    }

    private static void tambahBarang(Scanner input) {
        try {
            System.out.print("Kode barang (awali EDX-): ");
            String kode = input.nextLine().trim();

            if (!kode.startsWith("EDX-")) {
                throw new IllegalArgumentException(
                    "Kode barang harus diawali EDX-"
                );
            }

            System.out.print("Nama barang: ");
            String nama = input.nextLine().trim();

            System.out.print("Kategori barang: ");
            String kategori = input.nextLine().trim();

            System.out.print("Berat barang (kg): ");
            double berat;

            try {
                berat = Double.parseDouble(
                    input.nextLine().trim()
                );
            } catch (NumberFormatException e) {
                System.out.println(
                    "Berat harus berupa angka."
                );
                return;
            }

            if (berat <= 0) {
                throw new BeratInvalidException(
                    "Berat barang harus lebih dari 0 kg."
                );
            }

            BarangBantuan barang = new BarangBantuan(
                kode, nama, kategori, berat
            );

            daftarBarang.add(barang);

            System.out.println(
                "Barang berhasil ditambahkan."
            );

        } catch (BeratInvalidException |
                 IllegalArgumentException e) {

            System.out.println(
                "Input tidak valid: " + e.getMessage()
            );

            System.out.println(
                "Silakan ulangi input barang."
            );

        } finally {
            System.out.println("Proses input selesai.");
        }
    }

    private static void cetakManifes() {
        int totalBarang = daftarBarang.size();
        double totalBerat = 0;

        for (BarangBantuan barang : daftarBarang) {
            totalBerat += barang.getBeratKg();
        }

        String armada;

        if (totalBerat < 50) {
            armada = "Mobil Pickup";
        } else {
            armada = "Truk Ekspedisi";
        }

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("manifest_pengiriman.txt"))) {

            writer.write("MANIFES PENGIRIMAN BANTUAN");
            writer.newLine();

            writer.write(
                "========================================"
            );
            writer.newLine();

            writer.write(String.format(
                "%-15s %-20s %-15s %10s",
                "Kode", "Nama Barang",
                "Kategori", "Berat (kg)"
            ));
            writer.newLine();

            for (BarangBantuan barang : daftarBarang) {
                writer.write(String.format(
                    "%-15s %-20s %-15s %10.2f",
                    barang.getKodeBarang(),
                    barang.getNamaBarang(),
                    barang.getKategori(),
                    barang.getBeratKg()
                ));

                writer.newLine();
            }

            writer.write(
                "----------------------------------------"
            );
            writer.newLine();

            writer.write("Total barang: " + totalBarang);
            writer.newLine();

            writer.write(String.format(
                "Total berat: %.2f kg", totalBerat
            ));
            writer.newLine();

            writer.write("Label armada: " + armada);
            writer.newLine();

            System.out.println(
                "Data manifes berhasil diekspor ke manifest_pengiriman.txt"
            );

        } catch (IOException e) {
            System.out.println(
                "Gagal menulis manifes: " + e.getMessage()
            );
        }
    }
}
