import frame.PistolViewFrame;
import helpers.Koneksi;

public class Main {
    public static void main(String[] args) {
        Koneksi.getConnection();
        PistolViewFrame viewFrame = new PistolViewFrame();
        viewFrame.setVisible(true);
    }
}
