package frame;

import helpers.ComboBoxItem;
import helpers.Koneksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class SenjataInputFrame extends JFrame {
    private JPanel mainPanel;
    private JTextField idTextField;
    private JTextField jenisTextField;
    private JButton simpanButton;
    private JButton batalButton;

    private int id;
    public void setId(int id_) {
        this.id = id;
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

                    String cekSQL = "SELECT * FROM jenis_senjata WHERE jenis=?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, jenis);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) { // kalau ADA
                        JOptionPane.showMessageDialog(
                                null,
                                "Jenis Senjata sama sudah ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }


                    String insertSQL = "INSERT INTO jenis_senjata (id,jenis) VALUES (NULL, ?, ?)";
                    insertSQL = "INSERT INTO `jenis_senjata` (`id`, `jenis`) VALUES (NULL, ?)";
                    insertSQL = "INSERT INTO `jenis_senjata` VALUES (NULL, ?)";
                    insertSQL = "INSERT INTO jenis_senjata (jenis) VALUES (?)";
                    insertSQL = "INSERT INTO jenis_senjata SET jenis=?";
                    ps = c.prepareStatement(insertSQL);
                    ps.setString(1, jenis);
                    ps.setInt(2, id);
                    ps.executeUpdate();
                    dispose();
                } else {
                    String cekSQL = "SELECT * FROM jenis_senjata WHERE jenis=? AND id!=?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, jenis);
                    ps.setInt(2, id);
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

                    String updateSQL = "UPDATE jenis_senjata SET jenis=? WHERE id=?";
                    ps = c.prepareStatement(updateSQL);
                    ps.setString(1, jenis);
                    ps.setInt(2, id);
                    ps.executeUpdate();
                    dispose();
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
}
