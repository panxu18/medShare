package com.xp.medshare.model;

import lombok.Data;

@Data
public class ReKeyRequestDto {
    String userPk;
    String userSk;
    String otherPk;
    String c1;
    String id;
}
