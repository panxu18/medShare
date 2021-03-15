package com.xp.medshare.model;

import lombok.Data;

/**
 * original record
 */
@Data
public class AnonymousUserRecordDto {
    String R; // 盲化因子
    String pk; // 一次性公钥
    String idCommitment; // 身份承诺
    String supervisionValue; // 监管参数
    String h;
    String w1;
    String w2;
    String symptom;
    String type;
    String assessment;
    String prescription;
    
    public static AnonymousUserRecordDto of(AnonymousParamsDto params,RecordEntity record){
        AnonymousUserRecordDto recordDto = new AnonymousUserRecordDto();
        recordDto.setR(params.getR());
        recordDto.setPk(params.getPk());
        recordDto.setIdCommitment(params.getIdCommitment());
        recordDto.setSupervisionValue(params.getSupervisionValue());
        recordDto.setH(params.getH());
        recordDto.setW1(params.getW1());
        recordDto.setW2(params.getW2());
        recordDto.setSymptom(record.getSymptom());
        recordDto.setType(record.getType());
        recordDto.setAssessment(record.getAssessment());
        recordDto.setPrescription(record.getPrescription());
        return recordDto;
    }
}
