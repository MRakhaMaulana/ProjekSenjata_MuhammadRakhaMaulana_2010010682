package frame;

import helpers.Koneksi;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PistolInputFrame extends JFrame{
    private JPanel mainPanel;
    private JTextField idTextField;
    private JTextField namaTextField;
    private JTextField jenisTextField;
    private JTextField jarakTextField;
    private JTextField kecepatanTextField;
    private JTextField amunisiTextField;
    private JTextField beratTextField;
    private JTextField warnaTextField;
    private JPanel buttonPanel;
    private JButton simpanButton;
    private JButton batalButton;

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public PistolInputFrame(){
        batalButton.addActionListener(e->{
            dispose();
        });
        init();

        simpanButton.addActionListener(e-> {
            String nama_pistol = namaTextField.getText();
            Connection c = Koneksi.getConnection();
            PreparedStatement ps;
            try{
                String insertSQL = "INSERT INTO pistol VALUES (NULL, ?)";
                ps = c.prepareStatement(insertSQL);
                ps.setString(1,nama_pistol);
                ps.executeUpdate();
                dispose();
            } catch (SQLException ex){
                throw new RuntimeException(ex);
            }
        });
    }

    private void init() {
        setContentPane(mainPanel);
        pack();
        setTitle("Input Pistol");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
