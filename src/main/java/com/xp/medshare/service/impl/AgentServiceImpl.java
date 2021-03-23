package com.xp.medshare.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.webank.weid.protocol.base.Cpt;
import com.webank.weid.protocol.base.CptBaseInfo;
import com.webank.weid.protocol.base.WeIdAuthentication;
import com.webank.weid.protocol.base.WeIdDocument;
import com.webank.weid.protocol.request.CptMapArgs;
import com.webank.weid.protocol.response.CreateWeIdDataResult;
import com.webank.weid.protocol.response.ResponseData;
import com.webank.weid.rpc.CptService;
import com.webank.weid.rpc.CredentialService;
import com.webank.weid.rpc.WeIdService;
import com.xp.medshare.contract.client.RecordClient;
import com.xp.medshare.model.SimpleCredential;
import com.xp.medshare.model.UserAccountDto;
import com.xp.medshare.model.domodel.AnonymousEvidenceDo;
import com.xp.medshare.model.domodel.CryptoDo;
import com.xp.medshare.model.domodel.EvidenceDo;
import com.xp.medshare.model.vomodel.Response;
import com.xp.medshare.model.vomodel.ResponseCode;
import com.xp.medshare.service.AgentService;
import com.xp.medshare.util.CryptoUtil;
import com.xp.medshare.util.FileUtil;
import com.xp.medshare.util.KeyUtil;
import com.xp.medshare.util.PropertiesUtil;
import com.xp.medshare.util.crypto.Crypto;
import com.xp.medshare.util.crypto.ProxyReEncryptionUtil;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class AgentServiceImpl implements AgentService {
    // 默认身份表示符
    private static final String DEFAULT_WEID = "did:weid:1:0x23fd23af356c924cdfba4f98a15b9b6409c0b56f";

    private static final String SUPERVISOR_PATH = PropertiesUtil.getProperty("supervisorAccount.path");

    @Autowired
    RecordClient recordClient;

    @Autowired
    WeIdService weIdService;

    @Autowired
    CptService cptService;

    @Autowired
    CredentialService credentialService;

    @Override
    public String deployRecordContract() throws ContractException, IOException {
        return recordClient.deploy();
    }

    @Override
    public Response createRecord(BigInteger type, List<String> params) throws Exception {
        return recordClient.create(type, params);
    }

    @Override
    public Response<Object> queryRecord(String id) throws Exception {
        return recordClient.query(id);
    }

    @Override
    public Response<Object> recordExist(String id) throws Exception {
        return recordClient.exist(id);
    }

    @Override
    public Response<Object> confirmRecord(String id, String signature) throws Exception {
        return recordClient.confirm(id, signature);
    }

    @Override
    public Response<Object> registerUser() {
        return Response.of(ResponseCode.SUCCESS, createUser());
    }

    private UserAccountDto createUser() {
        ResponseData<CreateWeIdDataResult> result = weIdService.createWeId();
        CreateWeIdDataResult weIdDataResult = result.getResult();
        KeyUtil.savePrivateKey(weIdDataResult.getWeId(), weIdDataResult.getUserWeIdPrivateKey().getPrivateKey());
        UserAccountDto userAccountDto = new UserAccountDto();
        userAccountDto.setId(weIdDataResult.getWeId());
        userAccountDto.setPublicKey(weIdDataResult.getUserWeIdPublicKey().getPublicKey());
        userAccountDto.setPrivateKey(weIdDataResult.getUserWeIdPrivateKey().getPrivateKey());
        return userAccountDto;
    }

    @Override
    public Response<Object> queryDidDocument(String id) {
        return Response.of(ResponseCode.SUCCESS, queryUserDocument(id));
    }

    private WeIdDocument queryUserDocument(String id) {
        ResponseData<WeIdDocument> result = weIdService.getWeIdDocument(id);
        WeIdDocument weIdDataResult = result.getResult();
        return weIdDataResult;
    }

    /**
     * 注册cpt
     * @param cpt
     * @return
     */
    @Override
    public Response<Object> registerCpt(String weId, Map<String, Object> cpt) {
        if (weId== null || weId.isEmpty()) {
            weId = DEFAULT_WEID;
        }
        String weIdPrivKey = KeyUtil.getPrivateKeyByWeId(weId);
        WeIdAuthentication authentication = new WeIdAuthentication(weId, weIdPrivKey);
        CptMapArgs cptMapArgs = new CptMapArgs();
        cptMapArgs.setWeIdAuthentication(authentication);
        cptMapArgs.setCptJsonSchema(cpt);
        ResponseData<CptBaseInfo> result = cptService.registerCpt(cptMapArgs);
        CptBaseInfo info = result.getResult();
        return Response.of(ResponseCode.SUCCESS, info);
    }

    /**
     * 查询cpt
     */
    @Override
    public Response<Object> queryCpt(int cptId) {
        ResponseData<Cpt> responseData = cptService.queryCpt(cptId);
        return Response.of(ResponseCode.SUCCESS, responseData.getResult());
    }

    @Override
    public Response<Object> createAnonymousRecord(AnonymousEvidenceDo data) throws Exception {
        return createRecord(BigInteger.ONE, Arrays.asList(data.getId(), data.getType(), data.getSharedSecret(), data.getIndex(), data.getHash(),
                data.getSignature(),data.getR(), data.getPk(), data.getIdCommitment(), data.getSupervisionValue(),
                data.getH(), data.getW1(), data.getW2()));
    }

    @Override
    public Response<Object> createOriginalRecord(EvidenceDo data) throws Exception {
        return createRecord(BigInteger.ZERO, Arrays.asList(data.getId(), data.getType(), data.getSharedSecret(), data.getIndex(), data.getHash(),
                data.getSignature(),data.getUser(), data.getPk()));
    }


    @Override
    public Response<Object> reEncrypt(SimpleCredential credential) {
        Map<String, Object> authData = credential.getRawData().getClaim();
        String cryptoId = (String) authData.get("id");
        Crypto crypto = CryptoUtil.getCrypto(cryptoId);

        String reKey = (String) authData.get("reKey");
        ProxyReEncryptionUtil encryptionUtil = new ProxyReEncryptionUtil();
        Crypto result = encryptionUtil.reEncrypt(crypto, new BigInteger(reKey));
        CryptoDo cryptoDo = CryptoDo.of(result);
        CryptoUtil.saveReCrypto(cryptoId,cryptoDo);
        return Response.of(ResponseCode.SUCCESS, cryptoDo);
    }

    @Override
    public Response<Object> uploadCrypto(String id, CryptoDo crypto) {
        CryptoUtil.saveCrypto(id, crypto);
        return Response.of(ResponseCode.SUCCESS,id);
    }

    @Override
    public Response<Object> querySupervisor() {
        String path = FileUtil.checkDir(SUPERVISOR_PATH);
        String str = FileUtil.getDataByPath(path);
        UserAccountDto account;
        if (str == null || str.isEmpty()) {
            account = createUser();
            FileUtil.saveFile(path, JSON.toJSONString(account));
        } else {
            account = JSONObject.parseObject(str, UserAccountDto.class);
        }
        return Response.of(ResponseCode.SUCCESS, account);
    }

    @Override
    public Response<Object> queryUserAccount(String id) {
        WeIdDocument result = queryUserDocument(id);
        UserAccountDto userAccountDto = new UserAccountDto();
        userAccountDto.setId(id);
        userAccountDto.setPublicKey(result.getPublicKey().get(0).getPublicKey());
        return Response.of(ResponseCode.SUCCESS, userAccountDto);
    }


}
