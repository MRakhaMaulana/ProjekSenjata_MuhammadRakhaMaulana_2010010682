package frame;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import helpers.Koneksi;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SenjataInputFrame extends JFrame {
    private JPanel mainPanel;
    private JTextField idTextField;
    private JTextField jenisTextField;
    private JButton simpanButton;
    private JButton batalButton;
    private JPanel buttonPanel;

    private int id;
    public void setId(int id) {this.id = id;
    }

    public SenjataInputFrame() {
        simpanButton.addActionListener(e -> {
            Connection c = Koneksi.getConnection();
            PreparedStatement ps;
            String jenis = jenisTextField.getText();

            if (jenis.equals("")) {
                JOptionPane.showMessageDialog(
                        null,
                        "Isi data Jenis Senjata",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                jenisTextField.requestFocus();
                return;
            }
            try {
                if (this.id == 0) { //jika TAMBAH

                    String cekSQL = "SELECT * FROM senjata WHERE jenis=?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, jenis);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) { // kalau ADA
                        JOptionPane.showMessageDialog(
                                null,
                                "Jenis Senjata sama sudah ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE
                        );
                    } else {
                        String insertSQL = "INSERT INTO senjata (id,jenis) VALUES (NULL, ?, ?)";
                        insertSQL = "INSERT INTO `senjata` (`id`, `jenis`) VALUES (NULL, ?)";
                        insertSQL = "INSERT INTO `senjata` VALUES (NULL, ?)";
                        insertSQL = "INSERT INTO senjata (jenis) VALUES (?)";
                        insertSQL = "INSERT INTO senjata SET jenis=?";
                        ps = c.prepareStatement(insertSQL);
                        ps.setString(1, jenis);
                        ps.setInt(2, id);
                        ps.executeUpdate();
                        dispose();
                    }
                } else {
                    String cekSQL = "SELECT * FROM senjata WHERE jenis=? AND id!=?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, jenis);
                    ps.setInt(2, id);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) { // kalau ADA
                        JOptionPane.showMessageDialog(
                                null,
                                "Data sama sudah ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    } else {
                        String updateSQL = "UPDATE senjata SET jenis=? WHERE id=?";
                        ps = c.prepareStatement(updateSQL);
                        ps.setString(1, jenis);
                        ps.setInt(2, id);
                        ps.executeUpdate();
                        dispose();
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        batalButton.addActionListener(e -> dispose());
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
        Connection c = Koneksi.getConnection();
        String findSQL = "SELECT * FROM senjata WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idTextField.setText(String.valueOf(rs.getInt("id")));
               jenisTextField.setText(rs.getString("jenis"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
