import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAHelper {

    private static final String KeyAlgorithm = "RSA";
    private static final String PublicKey =
                    "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC7kscwWjjHY3moqWu8E4jZ84H0" +
                    "BA41gvHkQ4GiXAIj3/tyshoG2LOgTIrL6EOPS2g+/C6ePRDDPygdODBzsqA24Clc" +
                    "vM2AwjQ7GnnZrmprGxtdgcJSWcRdb7yOqhYffYHZJiklAxMOtVVFz++KJmn4+gwe" +
                    "LFChxhbOyE2BjPEAUQIDAQAB";
    private static final String PrivateKey =
                    "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALuSxzBaOMdjeaip" +
                    "a7wTiNnzgfQEDjWC8eRDgaJcAiPf+3KyGgbYs6BMisvoQ49LaD78Lp49EMM/KB04" +
                    "MHOyoDbgKVy8zYDCNDsaedmuamsbG12BwlJZxF1vvI6qFh99gdkmKSUDEw61VUXP" +
                    "74omafj6DB4sUKHGFs7ITYGM8QBRAgMBAAECgYAgvJdDGxv5wKTFCPyikI4768Hs" +
                    "gqCwOjBZdGa1nXVKKZdLB0Z/l2aSPYmj2N+hrLl+9Kh2OZYpXWZ04w+hwbUwUhtW" +
                    "1YQAo3aenFPHNARvw5XAguyA1dy6tK+H63D5J/9omnsyIyIb5AiM0CmpN4Rbl/dS" +
                    "PLY+p7GbpMBDA8SPWQJBAOJdAf9y027HSjlpGH9Ub4pJNVDJ8AgLY/gwMcQBDIOz" +
                    "pWKvTc/rQy1BJWQkST+aYl6TiUwD3jtyeAlxqjBf6dsCQQDUIabczc3hbjzGsPjf" +
                    "ryqzY4uYTjND2xI76yhTPU2BERwKp1nPpEIbEhfmbyUfRELCQpe9ghc/+KR9r5Np" +
                    "HCRDAkEA1bRZHoGZewxK1siRiCR+6V8UJqF/KxkV2EonsZQL7Iq9qN036QOC+EZ4" +
                    "toJLZPCrzf4bsNg2BombKc9VjTX/4QJBAIk+kHexC2+7Y8TlQM9vxD+1ut46Uf6n" +
                    "4N81YKyiL9++UxctePTXgBGFnkLvS+QzxMFsVkI8xZAU7U+H5eaOV4MCQQC/JBMa" +
                    "31ArX0x7z5ULHS5NKWsabTQ+UKW3svQPkuizErMFbLuAhr09zrWIPUvqeP0YxsLR" +
                    "5jWE5Kpp7KM6+VbP";
    private static final BASE64Decoder decoder = new BASE64Decoder();

    public static byte[] Decrypt(byte[] data) {
        try {
            byte[] keyBytes = decoder.decodeBuffer(PrivateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KeyAlgorithm);
            RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(KeyAlgorithm);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            int keyLength = privateKey.getModulus().bitLength() / 8;
            byte[][] byteArrays = SplitArray(data, keyLength);
            byte[] result = new byte[0];
            boolean isFirst = true;
            for (byte[] array : byteArrays) {
                if(isFirst) {
                    result = cipher.doFinal(array);
                    isFirst = false;
                }
                else {
                    result = ByteMerge(result, cipher.doFinal(array));
                }
            }
            return result;
        } catch (Exception e) {
            Main.MainChatta.console.append("[错误] RSA解密失败：" + e.getLocalizedMessage() + "\n");
            return data;
        }
    }

    public static byte[] Encrypt(byte[] data) {
        try {
            byte[] keyBytes = decoder.decodeBuffer(PublicKey);
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KeyAlgorithm);
            RSAPublicKey publicKey = (RSAPublicKey)keyFactory.generatePublic(x509EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(KeyAlgorithm);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            int keyLength = publicKey.getModulus().bitLength() / 8;
            byte[][] byteArrays = SplitArray(data, keyLength - 11);
            byte[] result = new byte[0];
            boolean isFirst = true;
            for (byte[] array : byteArrays) {
                if(isFirst) {
                    result = cipher.doFinal(array);
                    isFirst = false;
                }
                else {
                    result = ByteMerge(result, cipher.doFinal(array));
                }
            }
            return result;
        } catch (Exception e) {
            Main.MainChatta.console.append("[错误] RSA加密失败：" + e.getLocalizedMessage() + "\n");
            return data;
        }
    }

    private static byte[][] SplitArray(byte[] data, int length) {
        int arrayCount = data.length / length;
        int arrayLeft = data.length % length;
        int arrayOffset = 0;
        if (arrayLeft != 0) {
            arrayOffset = 1;
        }
        byte[][] arrays = new byte[arrayCount + arrayOffset][];
        byte[] array;
        for (int index = 0; index < arrayCount + arrayOffset; index++) {
            if (index == arrayCount + arrayOffset - 1 && arrayLeft != 0) {
                array = new byte[arrayLeft];
                System.arraycopy(data, index * length, array, 0, arrayLeft);
            } else {
                array = new byte[length];
                System.arraycopy(data, index * length, array, 0, length);
            }
            arrays[index] = array;
        }
        return arrays;
    }

    public static byte[] ByteMerge(byte[] byte1, byte[] byte2) {
        byte[] newByte = new byte[byte1.length + byte2.length];
        System.arraycopy(byte1, 0, newByte, 0, byte1.length);
        System.arraycopy(byte2, 0, newByte, byte1.length, byte2.length);
        return newByte;
    }

}
