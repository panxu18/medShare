package com.xp.medshare.util.crypto;

import org.fisco.bcos.web3j.crypto.ECKeyPair;

import java.math.BigInteger;

public class AnonymousUtil {


    /**
     * 发布者生成匿名参数
     *
     * @param userPk       接收者的公钥
     * @param supervisorPk 监管者公钥
     * @return
     */
    public AnonymousParams publish(String userPk, String supervisorPk) {
        return publish(new SimplePublicKey(userPk), new SimplePublicKey(supervisorPk));
    }

    public AnonymousParams publish(SimplePublicKey userPk, SimplePublicKey supervisorPk) {
        ECKeyPair randPair = Sepc256Util.generateSimpleKeyPair();
        SimplePrivateKey c = Sepc256Util.hash(userPk.times(randPair.getPrivateKey()).toString());
        SimplePublicKey pk = Sepc256Util.G.times(c).add(userPk);
        SimplePublicKey idCommitment = Sepc256Util.H.times(c).add(userPk);
        SimplePrivateKey c2 = Sepc256Util.hash(supervisorPk.times(randPair.getPrivateKey()).toString());
        SimplePublicKey supervisionValue = supervisorPk.times(c2).add(userPk);

        // 构造零知识证明
        BigInteger r1 = Sepc256Util.randValue();
        BigInteger r2 = Sepc256Util.randValue();
        SimplePublicKey Q1 = supervisorPk.times(r2).subtract(Sepc256Util.G.times(r1));
        SimplePublicKey Q2 = supervisorPk.times(r2).subtract(Sepc256Util.H.times(r1));
        String raw = String.join("", Sepc256Util.G.toString(), Sepc256Util.H.toString(),
                pk.toString(), supervisorPk.toString(), idCommitment.toString(), supervisionValue.toString(),
                Q1.toString(), Q2.toString());
        SimplePrivateKey h = Sepc256Util.hash(raw);
        SimplePrivateKey w1 = h.times(c).add(r1);
        SimplePrivateKey w2 = h.times(c2).add(r2);
        return new AnonymousParams(randPair.getPublicKey(), pk.key, idCommitment.key, supervisionValue.key,
                h.key, w1.key, w2.key);
    }

    /**
     * 接收者计算私钥
     *
     * @return
     */
    public String recieve(AnonymousParams param, String  userSk) {
        return recieve(param, new SimplePrivateKey(userSk)).toString();
    }

    public SimplePrivateKey recieve(AnonymousParams param, SimplePrivateKey userSk) {
        SimplePrivateKey sharedValue = Sepc256Util.hash(new SimplePublicKey(param.R).times(userSk).toString());
        return sharedValue.add(userSk);
    }

    /**
     * 恢复用户身份
     *
     * @param param 匿名参数
     * @param sk    监管者私钥
     * @param pk    监管者公钥
     */
    public String  recover(AnonymousParams param, String sk, String pk) {
        return recover(param, new SimplePrivateKey(sk), new SimplePublicKey(pk)).toString();
    }

    public SimplePublicKey recover(AnonymousParams param, SimplePrivateKey sk, SimplePublicKey pk) {
        SimplePrivateKey c = Sepc256Util.hash((new SimplePublicKey(param.R).times(sk).toString()));
        return new SimplePublicKey(param.supervisionValue).subtract(pk.times(c));
    }

    public BigInteger computIdCommitment(AnonymousParams param1, AnonymousParams param2, SimplePrivateKey userSk) {

        SimplePrivateKey c1 = Sepc256Util.hash(new SimplePublicKey(param1.R).times(userSk).toString());
        SimplePrivateKey c2 = Sepc256Util.hash(new SimplePublicKey(param2.R).times(userSk).toString());
        return c1.subtract(c2).key;
    }

    public BigInteger computIdCommitment(AnonymousParams param1, AnonymousParams param2, String userSk) {
        return computIdCommitment(param1,param2,new SimplePrivateKey(userSk));
    }

    /**
     * 关联验证
     *
     * @param commitment 关联证明参数
     */
    public boolean verifyIdCommitment(AnonymousParams param1, AnonymousParams param2, BigInteger commitment) {
        BigInteger c1 = Sepc256Util.H.times(commitment).key;
        BigInteger c2 = Sepc256Util.publicKeySubtract(param1.idCommitment, param2.idCommitment);
        return c1.equals(c2);
    }

    /**
     * 验证零知识证明
     */
    public boolean verifyZKP(AnonymousParams param, SimplePublicKey supervicorPk) {
        SimplePublicKey Q1 = new SimplePublicKey(param.pk)
                .subtract(param.supervisionValue)
                .times(param.h)
                .add(supervicorPk.times(param.w2))
                .subtract(Sepc256Util.G.times(param.w1));
        SimplePublicKey Q2 = new SimplePublicKey(param.idCommitment)
                .subtract(param.supervisionValue)
                .times(param.h)
                .add(supervicorPk.times(param.w2))
                .subtract(Sepc256Util.H.times(param.w1));

        String raw = String.join("", Sepc256Util.G.toString(), Sepc256Util.H.toString(),
                param.pk.toString(), supervicorPk.toString(), param.idCommitment.toString(),
                param.supervisionValue.toString(), Q1.toString(), Q2.toString());
        SimplePrivateKey h = Sepc256Util.hash(raw);
        return h.key.equals(param.h);
    }

}
