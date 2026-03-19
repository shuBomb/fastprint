import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.Base64;

public class PrintHash {
    public static void main(String[] args) throws Exception {
        String keystorePath = System.getProperty("user.home") + "/.android/debug.keystore";
        FileInputStream is = new FileInputStream(keystorePath);
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(is, "android".toCharArray());
        Certificate cert = keystore.getCertificate("androiddebugkey");
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(cert.getEncoded());
        System.out.println("HASH: " + Base64.getEncoder().encodeToString(md.digest()));
    }
}
