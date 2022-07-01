package frame;

import helpers.Koneksi;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PistolInputFrame extends JFrame{
    private JPanel mainPanel;
    private JTextField idTextField;
    private JTextField namaTextField;
    private JTextField tipeTextField;
    private JTextField jarakTextField;
    private JTextField jenisTextField;
    private JTextField amunisiTextField;
    private JTextField beratTextField;
    private JTextField warnaTextField;
    private JPanel buttonPanel;
    private JButton simpanButton;
    private JButton batalButton;

    private int id_pistol;
    public void setId_pistol(int id_pistol) {
    this.id_pistol = id_pistol;
    }

    public void isiKomponen() {
        idTextField.setText(String.valueOf(id_pistol));

        String findSQL = "SELECT * FROM pistol WHERE id_pistol = ?";
        Connection c = Koneksi.getConnection();
        PreparedStatement ps;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, id_pistol);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                namaTextField.setText(rs.getString("nama_pistol"));
                tipeTextField.setText(rs.getString("tipe_pistol"));
                jarakTextField.setText(rs.getString("jarak_pistol"));
                jenisTextField.setText(rs.getString("jenis_peluru"));
                amunisiTextField.setText(rs.getString("amunisi_pistol"));
                beratTextField.setText(rs.getString("berat_pistol"));
                warnaTextField.setText(rs.getString("warna_pistol"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PistolInputFrame() {
        simpanButton.addActionListener(e -> {
            Connection c = Koneksi.getConnection();
            PreparedStatement ps;
            String nama_pistol = namaTextField.getText();
            String tipe_pistol = tipeTextField.getText();
            String jarak_pistol = jarakTextField.getText();
            String jenis_peluru = jenisTextField.getText();
            String amunisi_pistol = amunisiTextField.getText();
            String berat_pistol = beratTextField.getText();
            String warna_pistol = warnaTextField.getText();

            if (nama_pistol.equals("")) {
                JOptionPane.showMessageDialog(
                        null,
                        "Isi data nama Pistol",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                namaTextField.requestFocus();
                return;
            } else if (tipe_pistol.equals("")) {
                JOptionPane.showMessageDialog(
                        null,
                        "Isi data Tipe Pistol",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                tipeTextField.requestFocus();
                return;
            } else if (jarak_pistol.equals("")) {
                JOptionPane.showMessageDialog(
                        null,
                        "Isi data Jarak Jangkauan Pistol",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                jarakTextField.requestFocus();
                return;
            } else if (jenis_peluru.equals("")) {
                JOptionPane.showMessageDialog(
                        null,
                        "Isi data Jenis Peluru",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                jenisTextField.requestFocus();
                return;
            } else if (amunisi_pistol.equals("")) {
                JOptionPane.showMessageDialog(
                        null,
                        "Isi data Amunisi Pistol",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                amunisiTextField.requestFocus();
                return;
            } else if (berat_pistol.equals("")) {
                JOptionPane.showMessageDialog(
                        null,
                        "Isi data Berat Pistol",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                beratTextField.requestFocus();
                return;
            } else if (warna_pistol.equals("")) {
                JOptionPane.showMessageDialog(
                        null,
                        "Isi data Warna Pistol",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                warnaTextField.requestFocus();
                return;
            }
            try {
                if (this.id_pistol == 0) { //jika TAMBAH

                    String cekSQL = "SELECT * FROM pistol WHERE nama_pistol=?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama_pistol);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) { // kalau ADA
                        JOptionPane.showMessageDialog(
                                null,
                                "Nama Pistol sama sudah ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }


                    String insertSQL = "INSERT INTO pistol (id_pistol,nama_pistol,tipe_pistol,jarak_pistol,jenis_peluru,amunisi_pistol,berat_pistol,warna_pistol) VALUES (NULL,?)";
                    insertSQL = "INSERT INTO `pistol` (`id_pistol`, `nama_pistol`,`tipe_pistol`,`jarak_pistol`,`jenis_peluru`,`amunisi_pistol`,`berat_pistol`,`warna_pistol`) VALUES (NULL, ?)";
                    insertSQL = "INSERT INTO `pistol` VALUES (NULL, ?)";
                    insertSQL = "INSERT INTO pistol (nama_pistol,tipe_pistol,jarak_pistol,jenis_peluru,amunisi_pistol,berat_pistol,warna_pistol) VALUES (?)";
                    insertSQL = "INSERT INTO pistol SET nama_pistol=?,tipe_pistol=?,jarak_pistol=?,jenis_peluru=?,amunisi_pistol=?,berat_pistol=?,warna_pistol=?";
                    ps = c.prepareStatement(insertSQL);
                    ps.setString(1, nama_pistol);
                    ps.setString(2, tipe_pistol);
                    ps.setString(3, jarak_pistol);
                    ps.setString(4, jenis_peluru);
                    ps.setString(5, amunisi_pistol);
                    ps.setString(6, berat_pistol);
                    ps.setString(7, warna_pistol);
                    ps.executeUpdate();
                    dispose();
                } else {
                    String cekSQL = "SELECT * FROM pistol WHERE nama_pistol=? AND tipe_pistol=? AND jarak_pistol=? AND jenis_peluru=? AND amunisi_pistol=? AND berat_pistol=? AND warna_pistol=? AND id_pistol!=?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama_pistol);
                    ps.setString(2, tipe_pistol);
                    ps.setString(3, jarak_pistol);
                    ps.setString(4, jenis_peluru);
                    ps.setString(5, amunisi_pistol);
                    ps.setString(6, berat_pistol);
                    ps.setString(7, warna_pistol);
                    ps.setInt(8, id_pistol);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) { // kalau ADA
                        JOptionPane.showMessageDialog(
                                null,
                                "Data sama sudah ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }

                    String updateSQL = "UPDATE pistol SET nama_pistol=?,tipe_pistol=?,jarak_pistol=?,jenis_peluru=?,amunisi_pistol=?,berat_pistol=?,warna_pistol=? WHERE id_pistol=?";
                    ps = c.prepareStatement(updateSQL);
                    ps.setString(1, nama_pistol);
                    ps.setString(2, tipe_pistol);
                    ps.setString(3, jarak_pistol);
                    ps.setString(4, jenis_peluru);
                    ps.setString(5, amunisi_pistol);
                    ps.setString(6, berat_pistol);
                    ps.setString(7, warna_pistol);
                    ps.setInt(8,id_pistol);
                    ps.executeUpdate();
                    dispose();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        batalButton.addActionListener(e -> {
            dispose();
        });
        init();
    }

            public void init() {
                setContentPane(mainPanel);
                pack();
                setTitle("Input Pistol");
                setLocationRelativeTo(null);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        }