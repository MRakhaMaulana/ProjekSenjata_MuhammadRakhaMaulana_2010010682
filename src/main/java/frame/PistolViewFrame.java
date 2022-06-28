package frame;

import helpers.Koneksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    public PistolViewFrame() {
        Init();
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

            String[] header = {"Id", "Nama Pistol"};
            DefaultTableModel dtm = new DefaultTableModel(header,0);
            viewTable.setModel(dtm);
            viewTable.getColumnModel().getColumn(0).setWidth(32);
            viewTable.getColumnModel().getColumn(0).setMaxWidth(32);
            viewTable.getColumnModel().getColumn(0).setMinWidth(32);
            viewTable.getColumnModel().getColumn(0).setPreferredWidth(32);
            Object[] row = new Object[2];
            while (rs.next()){
                row[0] = rs.getInt("id");
                row[1] = rs.getString("nama");
                dtm.addRow(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}



