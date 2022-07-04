import frame.AtributViewFrame;
import frame.SenjataViewFrame;
import helpers.Koneksi;

public class Main {
    public static void main(String[] args) {
        Koneksi.getConnection();
        AtributViewFrame viewFrame = new AtributViewFrame();
        viewFrame.setVisible(true);
    }
}
