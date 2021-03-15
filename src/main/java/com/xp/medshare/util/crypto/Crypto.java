package com.xp.medshare.util.crypto;

import com.xp.medshare.model.domodel.CryptoDo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigInteger;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Crypto {
    BigInteger c0;
    BigInteger c1;
    BigInteger c2;

    public static Crypto of(CryptoDo cryptoDo) {
        Crypto crypto = new Crypto();
        crypto.setC0(Optional.ofNullable(cryptoDo.getC0()).map(BigInteger::new).orElse(BigInteger.ZERO));
        crypto.setC1(Optional.ofNullable(cryptoDo.getC1()).map(BigInteger::new).orElse(BigInteger.ZERO));
        crypto.setC2(Optional.ofNullable(cryptoDo.getC2()).map(BigInteger::new).orElse(BigInteger.ZERO));
        return crypto;
    }
}
