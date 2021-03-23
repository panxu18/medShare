package com.xp.medshare.model.domodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnonymousEvidenceDo {
    String id;
    String type;
    String sharedSecret; // 计算代理重加密秘钥时用于计算共享秘密值的盲化因子
    String index;
    String hash;
    String signature;
    String R; // 盲化因子
    String pk; // 一次性公钥
    String idCommitment; // 身份承诺
    String supervisionValue; // 监管参数
    String h;
    String w1;
    String w2;
}
