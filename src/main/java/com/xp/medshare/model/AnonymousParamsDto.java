package com.xp.medshare.model;

import com.xp.medshare.util.crypto.AnonymousParams;
import lombok.Data;


@Data
public class AnonymousParamsDto {
    String R; // 盲化因子
    String pk; // 一次性公钥
    String idCommitment; // 身份承诺
    String supervisionValue; // 监管参数
    String h;
    String w1;
    String w2;

    public static AnonymousParamsDto of(AnonymousParams params) {
        AnonymousParamsDto dto = new AnonymousParamsDto();
        dto.setR(params.getR().toString());
        dto.setPk(params.getPk().toString());
        dto.setIdCommitment(params.getIdCommitment().toString());
        dto.setSupervisionValue(params.getSupervisionValue().toString());
        dto.setH(params.getH().toString());
        dto.setW1(params.getW1().toString());
        dto.setW2(params.getW2().toString());
        return dto;
    }
}
