package com.xp.medshare.util.crypto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class AnonymousParams {
    BigInteger R; // 盲化因子
    BigInteger pk; // 一次性公钥
    BigInteger idCommitment; // 身份承诺
    BigInteger supervisionValue; // 监管参数
    BigInteger h;
    BigInteger w1;
    BigInteger w2;

    public AnonymousParams(BigInteger r, BigInteger pk, BigInteger idCommitment, BigInteger supervisionValue,
                           BigInteger h, BigInteger w1, BigInteger w2) {
        this.R = r;
        this.pk = pk;
        this.idCommitment = idCommitment;
        this.supervisionValue = supervisionValue;
        this.h = h;
        this.w1 = w1;
        this.w2 = w2;
    }
}
