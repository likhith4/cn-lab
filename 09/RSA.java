import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Random;

public class RSA {
    private BigInteger p, q, N, phi, e, d;
    private int bitlength = 1024;
    private Random r;

    public RSA() {
        r = new Random();
        p = BigInteger.probablePrime(bitlength, r);
        q = BigInteger.probablePrime(bitlength, r);
        System.out.println("Prime number p is " + p);
        System.out.println("Prime number q is " + q);
        N = p.multiply(q);
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        
        // Choose e such that 1 < e < phi and gcd(e, phi) = 1
        e = BigInteger.probablePrime(bitlength / 2, r);
        while (phi.gcd(e).compareTo(BigInteger.ONE) != 0) {
            e = e.add(BigInteger.ONE); // Correctly incrementing e
            if (e.compareTo(phi) >= 0) {
                e = BigInteger.probablePrime(bitlength / 2, r); // Reset if out of bounds
            }
        }

        System.out.println("Public key (e) is " + e);
        d = e.modInverse(phi);
        System.out.println("Private key (d) is " + d);
    }

    public RSA(BigInteger e, BigInteger d, BigInteger N) {
        this.e = e;
        this.d = d;
        this.N = N;
    }

    public static void main(String[] args) throws IOException {
        RSA rsa = new RSA();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("Enter the plain text:");
        String testString = reader.readLine();
        
        System.out.println("Encrypting string: " + testString);
        
        byte[] encrypted = rsa.encrypt(testString.getBytes());
        byte[] decrypted = rsa.decrypt(encrypted);
        
        System.out.println("Encrypted Bytes: " + bytesToString(encrypted));
        System.out.println("Decrypted string: " + new String(decrypted));
    }

    private static String bytesToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b)); // Format bytes as hex
        }
        
        return sb.toString().trim(); // Remove trailing space
    }

    public byte[] encrypt(byte[] message) {
        return (new BigInteger(1, message)).modPow(e, N).toByteArray(); // Use unsigned
    }

    public byte[] decrypt(byte[] message) {
        return (new BigInteger(1, message)).modPow(d, N).toByteArray(); // Use unsigned
    }
}
