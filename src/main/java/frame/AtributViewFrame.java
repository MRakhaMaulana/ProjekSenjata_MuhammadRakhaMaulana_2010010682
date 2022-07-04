package frame;

import helpers.JasperDataSourceBuilder;
import helpers.Koneksi;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.text.NumberFormat;
import java.util.Locale;

public class AtributViewFrame extends JFrame{
    private JPanel mainPanel;
    private JPanel cariPanel;
    private JTextField cariTextField;
    private JButton cariButton;
    private JScrollPane viewScrollPane;
    private JTable viewTable;
    private JPanel buttonPanel;
    private JButton tambahButton;
    private JButton ubahButton;
    private JButton hapusButton;
    private JButton batalButton;
    private JButton cetakButton;
    private JButton tutupButton;

    public AtributViewFrame() {
        tutupButton.addActionListener(e -> {
            dispose();
        });
        batalButton.addActionListener(e -> {
            isiTable();
        });
        tambahButton.addActionListener(e -> {
            AtributInputFrame inputFrame = new AtributInputFrame();
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
            int id = Integer.parseInt(tm.getValueAt(barisTerpilih, 0).toString());
            AtributInputFrame inputFrame = new AtributInputFrame();
            inputFrame.setId(id);
            inputFrame.isiKomponen();
            inputFrame.setVisible(true);
        });
        cetakButton.addActionListener(e -> {
            Connection c = Koneksi.getConnection();
            String selectSQL = "SELECT * FROM atribut";
            Object[][] row;
            try {
                Statement s = c.createStatement(
                        ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = s.executeQuery(selectSQL);
                rs.last();
                int jumlah = rs.getRow();
                row = new Object[jumlah][9];
                int i = 0;
                rs.beforeFirst();
                while (rs.next()){
                    row[i][0] = rs.getInt("id");
                    row[i][1] = rs.getString("nama");
                    row[i][2] = rs.getInt("jenis_id");
                    row[i][3] = rs.getString("tipe");
                    row[i][4] = rs.getInt("jarak_peluru");
                    row[i][5] = rs.getString("tipe_peluru");
                    row[i][6] = rs.getString("jumlah_amunisi");
                    row[i][7] = rs.getString("berat");
                    row[i][8] = rs.getString("warna");
                    i++;
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        hapusButton.addActionListener(e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if(barisTerpilih < 0){
                JOptionPane.showMessageDialog(
                        null,
                        "Pilih data yang ingin dihapus");
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
                int id = Integer.parseInt(tm.getValueAt(barisTerpilih,0).toString());
                String deleteSQL = "DELETE FROM atribut WHERE id = ?";
                Connection c= Koneksi.getConnection();
                try {
                    PreparedStatement ps = c.prepareStatement(deleteSQL);
                    ps.setInt(1,id);
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        cariButton.addActionListener(e -> {
            if(cariTextField.getText().equals("")){
                JOptionPane.showMessageDialog(null,
                        "Isi Kata Kunci Pencarian",
                        "Validasi Kata Kunci Kosong",
                        JOptionPane.WARNING_MESSAGE);
                cariTextField.requestFocus();
                return;
            }
            String keyword = "%"+cariTextField.getText()+"%";
            String searchSQL = "SELECT K.*, B.jenis AS jenis_senjata FROM atribut K LEFT JOIN senjata B ON K.jenis_id = B.id WHERE nama like ? ";
            Connection c = Koneksi.getConnection();
            try {
                PreparedStatement ps = c.prepareStatement(searchSQL);
                ps.setString(1, keyword);

                ResultSet rs = ps.executeQuery();
                DefaultTableModel dtm = (DefaultTableModel) viewTable.getModel();
                dtm.setRowCount(0);
                viewTable.setModel(dtm);
                Object[] row = new Object[9];
                while (rs.next()){
                    row[0] = rs.getInt("id");
                    row[1] = rs.getString("nama");
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

    public void isiTable() {
        String selectSQL = "SELECT K.*, B.jenis AS jenis_senjata FROM atribut K LEFT JOIN senjata B ON K.jenis_id = B.id";
        Connection c = Koneksi.getConnection();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);

            String[] header = {"ID Senjata", "Nama Senjata", "Jenis Senjata", "Tipe Senjata", "Jarak Jangkau", "Tipe Peluru", "Jumlah Amunisi", "Berat", "Warna"};
            DefaultTableModel dtm = new DefaultTableModel(header,0);
            viewTable.setModel(dtm);
            viewTable.getColumnModel().getColumn(0).setMaxWidth(32);
            viewTable.getColumnModel().getColumn(1).setMaxWidth(150);
            viewTable.getColumnModel().getColumn(2).setMaxWidth(150);
            viewTable.getColumnModel().getColumn(3).setMaxWidth(150);

            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            viewTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
            viewTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
            viewTable.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
            viewTable.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
            viewTable.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);
            Object[] row = new Object[9];
            while (rs.next()){
                row[0] = rs.getInt("id");
                row[1] = rs.getString("nama");
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
    public void init() {
        setContentPane(mainPanel);
        pack();
        setTitle("Data Senjata");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
