package com.xp.medshare.model;

import lombok.Data;

/**
 * original record
 */
@Data
public class UserRecordDto {
    String user;
    String pk;
    String symptom;
    String type;
    String assessment;
    String prescription;

    public static UserRecordDto of(RecordRequestDto requestDto) {
        UserRecordDto dto = new UserRecordDto();
        dto.setUser(requestDto.getUser());
        dto.setPk(requestDto.getUserPk());
        RecordEntity entity = requestDto.getRecord();
        dto.setSymptom(entity.getSymptom());
        dto.setType(entity.getType());
        dto.setAssessment(entity.getAssessment());
        dto.setPrescription(entity.getPrescription());
        return dto;
    }
}
