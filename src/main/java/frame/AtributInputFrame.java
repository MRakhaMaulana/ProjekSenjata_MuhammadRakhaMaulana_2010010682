package frame;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import helpers.ComboBoxItem;
import helpers.Koneksi;

import javax.swing.*;
import java.sql.*;

public class AtributInputFrame extends JFrame{
    private JTextField idTextField;
    private JTextField namaTextField;
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
    private JPanel mainPanel;
    private DatePicker tglDibuatDatePicker;
    private ButtonGroup tipeButtonGroup;

    private int id;
    public void setId(int id) {
        this.id = id;
    }

    public AtributInputFrame() {
        //BATAL BUTTON
        batalButton.addActionListener(e -> {
            dispose();
        });

        //SIMPAN BUTTON
        simpanButton.addActionListener(e -> {
            Connection c = Koneksi.getConnection();
            PreparedStatement ps;
            String nama = namaTextField.getText();
            String jarak_peluru = jarakTextField.getText();
            String tipe_peluru = peluruTextField.getText();
            String jumlah_amunisi = jumlahTextField.getText();
            String berat = beratTextField.getText();
            String warna = warnaTextField.getText();
            String tglDibuat = tglDibuatDatePicker.getText();

            if (nama.equals("")) {
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

            } else if (tglDibuat.equals("")){
                JOptionPane.showMessageDialog(null,
                        "Isi Tanggal Dibuat",
                        "Validasi Data Kosong",JOptionPane.WARNING_MESSAGE);
                tglDibuatDatePicker.requestFocus();
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
                if (this.id == 0) { //jika TAMBAH

                    String cekSQL = "SELECT * FROM atribut WHERE nama=?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
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

                    String insertSQL = "INSERT INTO atribut (id,nama,jenis_id,tipe,jarak_peluru,tipe_peluru,jumlah_amunisi,berat,warna,tgldibuat) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    insertSQL = "INSERT INTO `atribut` (`id`, `nama`,`jenis_id`,`tipe`,`jarak_peluru`,`tipe_peluru`,`jumlah_amunisi`,`berat`,`warna`,`tgldibuat`) VALUES (NULL, ?)";
                    insertSQL = "INSERT INTO `atribut` VALUES (NULL, ?)";
                    insertSQL = "INSERT INTO atribut (nama,jenis_id,tipe,jarak_peluru,tipe_peluru,jumlah_amunisi,berat,warna,tgldibuat) VALUES (?)";
                    insertSQL = "INSERT INTO atribut SET nama=?,jenis_id=?,tipe=?,jarak_peluru=?,tipe_peluru=?,jumlah_amunisi=?,berat=?,warna=?,tgldibuat=?";
                    ps = c.prepareStatement(insertSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, senjataId);
                    ps.setString(3, tipe);
                    ps.setString(4, jarak_peluru);
                    ps.setString(5, tipe_peluru);
                    ps.setString(6, jumlah_amunisi);
                    ps.setString(7, berat);
                    ps.setString(8, warna);
                    ps.setString(9,tglDibuat);
                    ps.executeUpdate();
                    dispose();
                } else {
                    String cekSQL = "SELECT * FROM atribut WHERE nama=? AND jenis_id=? AND tipe=? AND jarak_peluru=? AND tipe_peluru=? AND jumlah_amunisi=? AND berat=? AND warna=? AND tgldibuat=? AND id!=?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, senjataId);
                    ps.setString(3, tipe);
                    ps.setString(4, jarak_peluru);
                    ps.setString(5, tipe_peluru);
                    ps.setString(6, jumlah_amunisi);
                    ps.setString(7, berat);
                    ps.setString(8, warna);
                    ps.setString(9, tglDibuat);
                    ps.setInt(10, id);
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

                    String updateSQL = "UPDATE atribut SET nama=?,jenis_id=?,tipe=?,jarak_peluru=?,tipe_peluru=?,jumlah_amunisi=?,berat=?,warna=? tgldibuat=? WHERE id=?";
                    ps = c.prepareStatement(updateSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, senjataId);
                    ps.setString(3, tipe);
                    ps.setString(4, jarak_peluru);
                    ps.setString(5, tipe_peluru);
                    ps.setString(6, jumlah_amunisi);
                    ps.setString(7, berat);
                    ps.setString(9, tglDibuat);
                    ps.setInt(10, id);
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
    public void isiKomponen() {
        idTextField.setText(String.valueOf(id));

        String findSQL = "SELECT * FROM atribut WHERE id = ?";
        Connection c = Koneksi.getConnection();
        PreparedStatement ps;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idTextField.setText(String.valueOf(rs.getInt("id")));
                namaTextField.setText(rs.getString("nama"));
                jarakTextField.setText(rs.getString("jarak_peluru"));
                peluruTextField.setText(rs.getString("tipe_peluru"));
                jumlahTextField.setText(rs.getString("jumlah_amunisi"));
                beratTextField.setText(rs.getString("berat"));
                warnaTextField.setText(rs.getString("warna"));
                tglDibuatDatePicker.setText(rs.getString("tgldibuat"));

                int senjataId = rs.getInt("jenis_id");
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
        String selectSQL = "SELECT * FROM senjata ORDER BY jenis";
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

        DatePickerSettings dps = new DatePickerSettings();
        dps.setFormatForDatesCommonEra("yyyy-MM-dd");
        tglDibuatDatePicker.setSettings(dps);

    }
}
