package com.xp.medshare.util.crypto;

import java.math.BigInteger;
import java.util.Arrays;

public class ProxyReEncryptionUtil {

    public Crypto encrypt(BigInteger pk, String rawData) {
        SimplePublicKey userPk = new SimplePublicKey(pk);
        // C0
        BigInteger rand = Sepc256Util.randValue();
        BigInteger c0 = rand;
        // C1
        BigInteger alpha = Sepc256Util.randValue();
        BigInteger c1 = Sepc256Util.G.times(alpha).key;
        // c2
        BigInteger hash0 = Sepc256Util.hash(userPk.times(alpha).toString()).key;
        BigInteger hash1 = Sepc256Util.hash(userPk.times(rand,hash0).toString()).key;
        BigInteger c2 = sliceEncrypt(rawData.getBytes(), hash1);
        return new Crypto(c0, c1, c2);
    }

    public String decrypt(Crypto crypto, BigInteger pk, BigInteger sk) {
        SimplePublicKey userPk = new SimplePublicKey(pk);
        SimplePrivateKey userSk = new SimplePrivateKey(sk);
        BigInteger c0 = crypto.c0;
        SimplePublicKey c1 = new SimplePublicKey(crypto.c1);
        BigInteger c2 = crypto.c2;
        BigInteger hash0 = Sepc256Util.hash(c1.times(userSk).toString()).key;
        BigInteger hash1 = Sepc256Util.hash(userPk.times(c0,hash0).toString()).key;
        byte[] result = sliceDecrypt(c2,hash1);
        return new String(result);
    }

    /**
     * 将原始数据分块
     */
    private BigInteger slice(byte[] byteSrc, int start, int length, byte[] byteDes) {
        if (byteSrc.length < length) {
            Arrays.fill(byteDes, (byte)0);
        }
        System.arraycopy(byteSrc, start, byteDes, 0, Math.min(byteSrc.length, length));
        return new BigInteger(byteDes);
    }

    public BigInteger generateReKey(Crypto crypto, BigInteger userSk, BigInteger otherPk) {
        SimplePublicKey c1 = new SimplePublicKey(crypto.c1);
        BigInteger hash0 = Sepc256Util.hash(c1.times(userSk).toString()).key;
        return new SimplePublicKey(otherPk).times(hash0,userSk).key;
    }

    public Crypto reEncrypt(Crypto crypto, BigInteger reKey) {
        crypto.c0 = new SimplePublicKey(reKey).times(crypto.c0).key;
        crypto.c1 = crypto.c2;
        crypto.c2 = null;
        return crypto;
    }

    public String reDecrypt(Crypto crypto, BigInteger sk) {
        BigInteger c0 = crypto.c0;
        BigInteger c1 = crypto.c1;
        BigInteger invSk = Sepc256Util.inverse(sk);
        BigInteger hash1 = Sepc256Util.hash(new SimplePublicKey(c0).times(invSk).toString()).key;
        byte[] result = sliceDecrypt(c1,hash1);
        return new String(result);
    }

    private BigInteger sliceEncrypt(byte[] rawBytes, BigInteger r) {
        int sliceLength = r.toByteArray().length;
        int round = rawBytes.length / sliceLength;
        int rest = rawBytes.length % sliceLength;
        int total = sliceLength * (round + Math.min(1, rest));
        byte[] result = new byte[total];
        for (int i = 0; i < rawBytes.length; i+=sliceLength) {
            byte[] tem = Arrays.copyOfRange(rawBytes, i, i + sliceLength);
            BigInteger slice = r.xor(new BigInteger(tem));
            System.arraycopy(slice.toByteArray(),0, result, i, sliceLength);
        }
        return new BigInteger(result);
    }

    private byte[] sliceDecrypt(BigInteger c, BigInteger r) {
        byte[] rawBytes = c.toByteArray();
        int sliceLength = r.toByteArray().length;
        int round = rawBytes.length / sliceLength;
        byte[] result = new byte[round * sliceLength];
        for (int i = 0; i < rawBytes.length; i+=sliceLength) {
            byte[] tem = Arrays.copyOfRange(rawBytes, i, i + sliceLength);
            BigInteger slice = r.xor(new BigInteger(tem));
            System.arraycopy(slice.toByteArray(),0, result, i, sliceLength);
        }
        return result;
    }
}
