package com.xp.medshare.model;

import lombok.Data;

import java.util.Map;

@Data
public class CredentialRequestDto {
    String user;
    String userPk;
    int type; // 添加匿名参数
    String supervisorPk;
    Map<String,Object> rawData;
    int cptId;
    String publisher;
    String publisherSk;
}
