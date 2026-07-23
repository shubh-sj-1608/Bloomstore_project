import java.security.MessageDigest;
import java.util.Base64;

public class GenerateHash {
    public static void main(String[] args) throws Exception {
        String password = "bloom123";
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes("UTF-8"));
        String encoded = Base64.getEncoder().encodeToString(hash);
        System.out.println("SHA256(bloom123) in Base64: " + encoded);
    }
}
