package com.xp.medshare.model.domodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
public class EvidenceDo {
    String id;
    String  type;
    String sharedSecret; // 计算代理重加密秘钥时用于计算共享秘密值的盲化因子
    String index;
    String hash;
    String signature;
    String user;
    String pk;
}
