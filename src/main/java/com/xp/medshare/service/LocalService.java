package com.xp.medshare.service;

import com.xp.medshare.model.*;
import com.xp.medshare.model.domodel.CryptoDo;
import com.xp.medshare.model.vomodel.Response;
import com.xp.medshare.util.crypto.AnonymousParams;

public interface LocalService {

    Response<Object> createAnonymousParams(String userPk, String superivorPk);

    Response<Object> computeStealSk(AnonymousParams param, String userSk);

    Response<Object> recoverAnonymous(AnonymousParams param, String supervisorSk, String supervisorPk);

    Response<Object> encrypt(String userPk, String rawData);

    Response<Object> decrypt(String userPk, String userSk, CryptoDo cryptoDo);

    Response<Object> generateReKey(ReKeyRequestDto requestDto);

    Response<Object> generateReKeyCredential(ReKeyRequestDto requestDto);

    Response<Object> reDecrypt(String userSk, CryptoDo cryptoDo);

    Response<Object> reDecrypt2Credential(String userSk, CryptoDo cryptoDo);

    Response<Object> createRawData(CredentialRequestDto requestDto);

    Response<Object> createCredential(CredentialRequestDto requestDto);

    Response<Object> createEncryptCredential(CredentialRequestDto requestDto);

    Response<Object> decrypt2Credentail(String userPk, String userSk, CryptoDo cryptoDo);

    Response<Object> computeIdCommit(IdCommitmentReuest request);

    Response<Object> verifyCommit(IdCommitmentReuest request);

    Response<Object> sign(SignRequst request);

    Response<Object> verifySignature(CredentialVerifyDto request);
}
