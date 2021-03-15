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
        BigInteger c2 = hash1.xor(new BigInteger(rawData.getBytes()));
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
        byte[] result = hash1.xor(c2).toByteArray();
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
        return new String(c1.xor(hash1).toByteArray());
    }
}
