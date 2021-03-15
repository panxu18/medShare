package com.xp.medshare.model.domodel;

import com.xp.medshare.util.crypto.Crypto;
import lombok.Data;

import java.math.BigInteger;
import java.util.Optional;

@Data
public class CryptoDo {
    String c0;
    String c1;
    String c2;

    public static CryptoDo of(Crypto crypto) {
        CryptoDo cryptoDo= new CryptoDo();
        cryptoDo.setC0(Optional.ofNullable(crypto.getC0()).map(String::valueOf).orElse(""));
        cryptoDo.setC1(Optional.ofNullable(crypto.getC1()).map(String::valueOf).orElse(""));
        cryptoDo.setC2(Optional.ofNullable(crypto.getC2()).map(String::valueOf).orElse(null));
        return cryptoDo;
    }
}
