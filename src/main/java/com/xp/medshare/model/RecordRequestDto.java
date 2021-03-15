package com.xp.medshare.model;

import lombok.Data;

@Data
public class RecordRequestDto {
    String user;
    String userPk;
    int type;
    String supervisorPk;
    RecordEntity record;
}
