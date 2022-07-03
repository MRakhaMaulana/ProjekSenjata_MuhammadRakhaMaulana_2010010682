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
        tambahButton.addActionListener(e -> {
            PistolInputFrame inputFrame = new PistolInputFrame();
            inputFrame.setVisible(true);
        });
        ubahButton.addActionListener(e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if(barisTerpilih < 0){
                JOptionPane.showMessageDialog(
                        null,
                        "Pilih data dulu"
                );
                return;
            }
            TableModel tm = viewTable.getModel();
            String idString = tm.getValueAt(barisTerpilih,0).toString();
            int id_senjata = Integer.parseInt(idString);

            PistolInputFrame inputFrame = new PistolInputFrame();
            inputFrame.setId_senjata(id_senjata);
            inputFrame.isiKomponen();
            inputFrame.setVisible(true);
        });
        hapusButton.addActionListener(e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if(barisTerpilih < 0){
                JOptionPane.showMessageDialog(
                        null,
                        "Pilih data yang ingin dihapus",
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

                String deleteSQL = "DELETE FROM senjata WHERE id_senjata = ?";
                Connection c= Koneksi.getConnection();
                int id_senjata = Integer.parseInt(idString);
                PreparedStatement ps;
                try {
                    ps = c.prepareStatement(deleteSQL);
                    ps.setInt(1,id_senjata);
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        cariButton.addActionListener(e -> {
            if(cariTextField.getText().equals("")){
                JOptionPane.showMessageDialog(null,
                        "Isi kata kunci pencarian",
                        "Validasi kata kunci kosong",
                        JOptionPane.WARNING_MESSAGE);
                cariTextField.requestFocus();
                return;
            }
            String keyword = "%"+cariTextField.getText()+"%";
            String searchSQL = "SELECT * FROM senjata WHERE nama_senjata like ?";
            Connection c = Koneksi.getConnection();
            try {
                PreparedStatement ps = c.prepareStatement(searchSQL);
                ps.setString(1, keyword);
                ResultSet rs = ps.executeQuery();
                DefaultTableModel dtm = (DefaultTableModel) viewTable.getModel();
                dtm.setRowCount(0);
                viewTable.setModel(dtm);
                viewTable.getColumnModel().getColumn(0).setWidth(32);
                viewTable.getColumnModel().getColumn(0).setMaxWidth(32);
                viewTable.getColumnModel().getColumn(0).setMinWidth(32);
                viewTable.getColumnModel().getColumn(0).setPreferredWidth(32);
                Object[] row = new Object[9];
                while (rs.next()){
                    row[0] = rs.getInt("id_senjata");
                    row[1] = rs.getString("nama_senjata");
                    row[2] = rs.getString("jenis_senjata");
                    row[3] = rs.getString("tipe");
                    row[4] = rs.getString("jarak_peluru");
                    row[5] = rs.getString("tipe_peluru");
                    row[6] = rs.getString("jumlah_amunisi");
                    row[7] = rs.getString("berat");
                    row[8] = rs.getString("warna");
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
        setTitle("Data Senjata");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    public void isiTable() {
        String selectSQL = "SELECT K.*,B.jenis AS jenis_senjata FROM senjata K " +
                "LEFT JOIN jenis_senjata B ON K.jenis_senjata = B.id";
        Connection c = Koneksi.getConnection();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);

            String[] header = {"ID Senjata", "Nama Senjata", "Jenis Senjata", "Tipe Senjata", "Jarak Jangkau", "Tipe Peluru", "Jumlah Amunisi", "Berat", "Warna"};
            DefaultTableModel dtm = new DefaultTableModel(header,0);
            viewTable.setModel(dtm);
            viewTable.getColumnModel().getColumn(0).setWidth(32);
            viewTable.getColumnModel().getColumn(0).setMaxWidth(32);
            viewTable.getColumnModel().getColumn(0).setMinWidth(32);
            viewTable.getColumnModel().getColumn(0).setPreferredWidth(32);
            Object[] row = new Object[9];
            while (rs.next()){
                row[0] = rs.getInt("id_senjata");
                row[1] = rs.getString("nama_senjata");
                row[2] = rs.getString("jenis_senjata");
                row[3] = rs.getString("tipe");
                row[4] = rs.getString("jarak_peluru");
                row[5] = rs.getString("tipe_peluru");
                row[6] = rs.getString("jumlah_amunisi");
                row[7] = rs.getString("berat");
                row[8] = rs.getString("warna");
                dtm.addRow(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}



