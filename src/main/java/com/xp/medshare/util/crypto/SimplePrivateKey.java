package com.xp.medshare.util.crypto;

import java.math.BigInteger;

public class SimplePrivateKey {
    BigInteger key;

    public BigInteger getKey() {
        return key;
    }

    public SimplePrivateKey(BigInteger key) {
        this.key = key;
    }

    public SimplePrivateKey(String  key) {
        this(new BigInteger(key));
    }

    public SimplePrivateKey add(SimplePrivateKey other) {
        return add(other.key);
    }

    public SimplePrivateKey add(BigInteger other) {
        return new SimplePrivateKey(key.add(other).mod(Sepc256Util.N));
    }

    public SimplePrivateKey subtract(SimplePrivateKey other) {
        return new SimplePrivateKey(key.subtract(other.key).mod(Sepc256Util.N));
    }

    public SimplePrivateKey times(SimplePrivateKey other) {
        return times(other.key);
    }

    public SimplePrivateKey times(BigInteger other) {
        return new SimplePrivateKey(key.multiply(other).mod(Sepc256Util.N));
    }

    @Override
    public String toString() {
        return key.toString();
    }


}
