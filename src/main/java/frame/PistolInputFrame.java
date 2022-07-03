package frame;

import helpers.ComboBoxItem;
import helpers.Koneksi;

import javax.swing.*;
import java.sql.*;

public class PistolInputFrame extends JFrame{
    private JPanel mainPanel;
    private JTextField idTextField;
    private JTextField namaTextField;
    private JTextField jenisTextField;
    private JTextField tipeTextField;
    private JTextField jarakTextField;
    private JTextField jumlahTextField;
    private JTextField beratTextField;
    private JTextField warnaTextField;
    private JPanel buttonPanel;
    private JButton simpanButton;
    private JButton batalButton;
    private JTextField peluruTextField;
    private JComboBox jenisComboBox;
    private JPanel tipePanel;
    private JRadioButton manualRadioButton;
    private JRadioButton semiOtomatisRadioButton;
    private JRadioButton otomatisRadioButton;

    private ButtonGroup tipeButtonGroup;

    private int id_senjata;
    public void setId_senjata(int id_senjata) {
        this.id_senjata = id_senjata;
    }

    public void isiKomponen() {
        idTextField.setText(String.valueOf(id_senjata));

        String findSQL = "SELECT * FROM senjata WHERE id_senjata = ?";
        Connection c = Koneksi.getConnection();
        PreparedStatement ps;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, id_senjata);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idTextField.setText(String.valueOf(rs.getInt("id_senjata")));
                namaTextField.setText(rs.getString("nama_senjata"));
                jarakTextField.setText(rs.getString("jarak_peluru"));
                peluruTextField.setText(rs.getString("tipe_peluru"));
                jumlahTextField.setText(rs.getString("jumlah_amunisi"));
                beratTextField.setText(rs.getString("berat"));
                warnaTextField.setText(rs.getString("warna"));

                int senjataId = rs.getInt("jenis_senjata");
                for (int i = 0; i < jenisComboBox.getItemCount(); i++){
                    jenisComboBox.setSelectedIndex(i);
                    ComboBoxItem item = (ComboBoxItem) jenisComboBox.getSelectedItem();
                    if (senjataId == item.getValue()){
                        break;
                    }
                }
                String tipe = rs.getString("Tipe");
                if (tipe != null){
                    if (tipe.equals("Manual")){
                        manualRadioButton.setSelected(true);
                    } else if (tipe.equals("Semi-Otomatis")){
                        semiOtomatisRadioButton.setSelected(true);
                    } else if (tipe.equals("Otomatis")){
                        otomatisRadioButton.setSelected(true);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void kostumisasiKomponen(){
        String selectSQL = "SELECT * FROM jenis_senjata ORDER BY jenis";
        Connection c = Koneksi.getConnection();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);
            jenisComboBox.addItem(new ComboBoxItem(0, "Pilih Jenis Senjata"));
            while (rs.next()){
                jenisComboBox.addItem(new ComboBoxItem(
                        rs.getInt("id"),
                        rs.getString("jenis")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tipeButtonGroup = new ButtonGroup();
        tipeButtonGroup.add(manualRadioButton);
        tipeButtonGroup.add(semiOtomatisRadioButton);
        tipeButtonGroup.add(otomatisRadioButton);
    }

    public PistolInputFrame() {
        simpanButton.addActionListener(e -> {
            Connection c = Koneksi.getConnection();
            PreparedStatement ps;
            String nama_senjata = namaTextField.getText();
            String jarak_peluru = jarakTextField.getText();
            String tipe_peluru = peluruTextField.getText();
            String jumlah_amunisi = jumlahTextField.getText();
            String berat = beratTextField.getText();
            String warna = warnaTextField.getText();

            if (nama_senjata.equals("")) {
                JOptionPane.showMessageDialog(
                        null,
                        "Isi data nama Senjata",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                namaTextField.requestFocus();
                return;
            } else if (jarak_peluru.equals("")) {
                JOptionPane.showMessageDialog(
                        null,
                        "Isi data Jarak Peluru",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                jarakTextField.requestFocus();
                return;
            } else if (tipe_peluru.equals("")) {
                JOptionPane.showMessageDialog(
                        null,
                        "Isi data Tipe Peluru",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                peluruTextField.requestFocus();
                return;
            } else if (jumlah_amunisi.equals("")) {
                JOptionPane.showMessageDialog(
                        null,
                        "Isi data Jumlah Amunisi",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                jumlahTextField.requestFocus();
                return;
            } else if (berat.equals("")) {
                JOptionPane.showMessageDialog(
                        null,
                        "Isi data Berat Senjata",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                beratTextField.requestFocus();
                return;
            } else if (warna.equals("")) {
                JOptionPane.showMessageDialog(
                        null,
                        "Isi data Warna Senjat",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                warnaTextField.requestFocus();
                return;
            }

            ComboBoxItem item = (ComboBoxItem) jenisComboBox.getSelectedItem();
            int senjataId = item.getValue();
            if (senjataId == 0) {
                JOptionPane.showMessageDialog(null,
                        "Pilih Jenis Senjata",
                        "Validasi ComboBox",JOptionPane.WARNING_MESSAGE);
                jenisComboBox.requestFocus();
                return;
            }

            String tipe = "";
            if (manualRadioButton.isSelected()){
                tipe = "Manual";
            } else if (semiOtomatisRadioButton.isSelected()){
                tipe = "Semi-Otomatis";
            } else if (otomatisRadioButton.isSelected()){
                tipe = "Otomatis";
            }else {
                JOptionPane.showMessageDialog(null,
                        "Pilih Tipe",
                        "Validasi Data Kosong", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                if (this.id_senjata == 0) { //jika TAMBAH

                    String cekSQL = "SELECT * FROM senjata WHERE nama_senjata=?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama_senjata);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) { // kalau ADA
                        JOptionPane.showMessageDialog(
                                null,
                                "Nama Senjata sama sudah ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }


                    String insertSQL = "INSERT INTO senjata (id_senjata,nama_senjata,jenis_senjata,tipe,jarak_peluru,tipe_peluru,jumlah_amunisi,berat,warna) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?)";
                    insertSQL = "INSERT INTO `senjata` (`id_senjata`, `nama_senjata`,`jenis_senjata`,`tipe`,`jarak_peluru`,`tipe_peluru`,`jumlah_amunisi`,`berat`,`warna`) VALUES (NULL, ?)";
                    insertSQL = "INSERT INTO `senjata` VALUES (NULL, ?)";
                    insertSQL = "INSERT INTO senjata (nama_senjata,jenis_senjata,tipe,jarak_peluru,tipe_peluru,jumlah_amunisi,berat,warna) VALUES (?)";
                    insertSQL = "INSERT INTO senjata SET nama_senjata=?,jenis_senjata=?,tipe=?,jarak_peluru=?,tipe_peluru=?,jumlah_amunisi=?,berat=?,warna=?";
                    ps = c.prepareStatement(insertSQL);
                    ps.setString(1, nama_senjata);
                    ps.setInt(2, senjataId);
                    ps.setString(3, tipe);
                    ps.setString(4, jarak_peluru);
                    ps.setString(5, tipe_peluru);
                    ps.setString(6, jumlah_amunisi);
                    ps.setString(7, berat);
                    ps.setString(8, warna);
                    ps.executeUpdate();
                    dispose();
                } else {
                    String cekSQL = "SELECT * FROM senjata WHERE nama_senjata=? AND senjataId=? AND tipe=? AND jarak_peluru=? AND tipe_peluru=? AND jumlah_amunisi=? AND berat=? AND warna=? AND id_senjata!=?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama_senjata);
                    ps.setInt(2, senjataId);
                    ps.setString(3, tipe);
                    ps.setString(4, jarak_peluru);
                    ps.setString(5, tipe_peluru);
                    ps.setString(6, jumlah_amunisi);
                    ps.setString(7, berat);
                    ps.setString(8, warna);
                    ps.setInt(9, id_senjata);
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

                    String updateSQL = "UPDATE senjata SET nama_senjata=?,senjataId=?,tipe=?,jarak_peluru=?,tipe_peluru=?,jumlah_amunisi=?,berat=?,warna=? WHERE id_senjata=?";
                    ps = c.prepareStatement(updateSQL);
                    ps.setString(1, nama_senjata);
                    ps.setInt(2, senjataId);
                    ps.setString(3, tipe);
                    ps.setString(4, jarak_peluru);
                    ps.setString(5, tipe_peluru);
                    ps.setString(6, jumlah_amunisi);
                    ps.setString(7, berat);
                    ps.setString(8, warna);
                    ps.setInt(9, id_senjata);
                    ps.executeUpdate();
                    dispose();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        batalButton.addActionListener(e -> dispose());
        kostumisasiKomponen();
        init();
    }

    public void init() {
        setContentPane(mainPanel);
        pack();
        setTitle("Input Senjata");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}