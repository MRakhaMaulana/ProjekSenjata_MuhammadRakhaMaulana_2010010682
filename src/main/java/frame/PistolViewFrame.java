package frame;

import helpers.Koneksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class PistolViewFrame extends JFrame {
    private JPanel mainPanel;
    private JPanel cariPanel;
    private JScrollPane viewScrollPane;
    private JPanel buttonPanel;
    private JTextField cariTextField;
    private JButton cariButton;
    private JButton tambahButton;
    private JButton ubahButton;
    private JButton hapusButton;
    private JButton batalButton;
    private JButton cetakButton;
    private JButton tutupButton;
    private JTable viewTable;

    public PistolViewFrame() {
        tutupButton.addActionListener(e -> {
            dispose();
        });
        batalButton.addActionListener(e -> {
            isiTable();
        });
        hapusButton.addActionListener(e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if(barisTerpilih < 0){
                JOptionPane.showMessageDialog(
                        null,
                        "Pilih data",
                        "Validasi Pilih Data",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            int pilihan = JOptionPane.showConfirmDialog(
                    null,
                    "Yakin?",
                    "Konfirmasi Hapus",
                    JOptionPane.YES_NO_OPTION
            );
            if(pilihan == 0){ //pilihan YES
                TableModel tm = viewTable.getModel();
                String idString = tm.getValueAt(barisTerpilih,0).toString();
                int id = Integer.parseInt(idString);

                String deleteSQL = "DELETE FROM pistol WHERE Id_pistol = ?";
                Connection c= Koneksi.getConnection();
                PreparedStatement ps;
                try {
                    ps = c.prepareStatement(deleteSQL);
                    ps.setInt(1,id);
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        cariButton.addActionListener(e -> {
            String keyword = "%"+cariTextField.getText()+"%";
            String searchSQL = "SELECT * FROM pistol WHERE nama_pistol like ?";
            Connection c = Koneksi.getConnection();
            try {
                PreparedStatement ps = c.prepareStatement(searchSQL);
                ps.setString(1, keyword);
                ResultSet rs = ps.executeQuery();

                String[] header = {"Id Pistol", "Nama", "Jenis", "Jarak", "Kecepatan", "Amunisi", "Berat", "Warna"};
                DefaultTableModel dtm = new DefaultTableModel(header,0);
                viewTable.setModel(dtm);
                viewTable.getColumnModel().getColumn(0).setWidth(32);
                viewTable.getColumnModel().getColumn(0).setMaxWidth(32);
                viewTable.getColumnModel().getColumn(0).setMinWidth(32);
                viewTable.getColumnModel().getColumn(0).setPreferredWidth(32);
                Object[] row = new Object[8];
                while (rs.next()){
                    row[0] = rs.getInt("Id_pistol");
                    row[1] = rs.getString("nama_pistol");
                    row[2] = rs.getString("jenis_pistol");
                    row[3] = rs.getInt("jarak_pistol");
                    row[4] = rs.getString("kecepatan_pistol");
                    row[5] = rs.getString("amunisi_pistol");
                    row[6] = rs.getString("berat_pistol");
                    row[7] = rs.getString("warna_pistol");
                    dtm.addRow(row);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                isiTable();
            }
        });
        isiTable();
        init();
    }

    public void init(){
        setContentPane(mainPanel);
        pack();
        setTitle("Data Pistol");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    public void isiTable() {
        String selectSQL = "SELECT * FROM pistol";
        Connection c = Koneksi.getConnection();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);

            String[] header = {"Id Pistol", "Nama", "Jenis", "Jarak", "Kecepatan", "Amunisi", "Berat", "Warna"};
            DefaultTableModel dtm = new DefaultTableModel(header,0);
            viewTable.setModel(dtm);
            viewTable.getColumnModel().getColumn(0).setWidth(32);
            viewTable.getColumnModel().getColumn(0).setMaxWidth(32);
            viewTable.getColumnModel().getColumn(0).setMinWidth(32);
            viewTable.getColumnModel().getColumn(0).setPreferredWidth(32);
            Object[] row = new Object[8];
            while (rs.next()){
                row[0] = rs.getInt("Id_pistol");
                row[1] = rs.getString("nama_pistol");
                row[2] = rs.getString("jenis_pistol");
                row[3] = rs.getInt("jarak_pistol");
                row[4] = rs.getString("kecepatan_pistol");
                row[5] = rs.getString("amunisi_pistol");
                row[6] = rs.getString("berat_pistol");
                row[7] = rs.getString("warna_pistol");
                dtm.addRow(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}



