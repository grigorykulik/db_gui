package hospital;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class SecurityManager {
    private static final Map<String, String> passwords = new HashMap<>();

    static {
        passwords.put("user1", "a722c63db8ec8625af6cf71cb8c2d939");
        passwords.put("user2", "c1572d05424d0ecb2a65ec6a82aeacbf");
    }

    public boolean logInSuccessful(String username, String password){
        String passwordHash = passwords.get(username);
        boolean loginSuccessful = false;

        try {
            loginSuccessful=passwordHash.equals(getHash(password));
            System.out.println(passwordHash.equals(getHash(password)));
            System.out.println(loginSuccessful);
            System.out.println(getHash(password));
        } catch (NullPointerException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return loginSuccessful;
    }

    public String getHash(String toHash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(toHash.getBytes());
        byte[] digest = md.digest();
        String myHash = DatatypeConverter.printHexBinary(digest).toLowerCase();
        System.out.println(myHash);
        return myHash;
    }
}
