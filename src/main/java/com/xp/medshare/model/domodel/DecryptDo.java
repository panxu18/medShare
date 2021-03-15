package com.xp.medshare.model.domodel;

import lombok.Data;

@Data
public class DecryptDo {
    String pk;
    String sk;
    CryptoDo crypto;
}
