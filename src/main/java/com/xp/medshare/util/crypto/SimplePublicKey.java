package com.xp.medshare.util.crypto;

import java.math.BigInteger;
import java.util.Arrays;

public class SimplePublicKey {
    BigInteger key;


    public SimplePublicKey(BigInteger key) {
        this.key = key;
    }

    public SimplePublicKey(String key) {
        this(new BigInteger(key));
    }

    public SimplePublicKey add(SimplePublicKey other) {
        return add(other.key);
    }

    public SimplePublicKey add(BigInteger other) {
        return new SimplePublicKey(Sepc256Util.publicKeyAdd(key, other));
    }

    public SimplePublicKey subtract(SimplePublicKey other) {
        return subtract(other.key);
    }

    public SimplePublicKey subtract(BigInteger other) {
        return new SimplePublicKey(Sepc256Util.publicKeySubtract(key, other));
    }

    public SimplePublicKey times(BigInteger... values) {
        return new SimplePublicKey(Sepc256Util.times(key, values));
    }

    public SimplePublicKey times(SimplePrivateKey... values) {
        BigInteger[] temp = Arrays.stream(values).map(SimplePrivateKey::getKey).toArray(BigInteger[]::new);
        return new SimplePublicKey(Sepc256Util.times(key, temp));
    }

    @Override
    public String toString() {
        return key.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimplePublicKey that = (SimplePublicKey) o;

        return key != null ? key.equals(that.key) : that.key == null;
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }
}
