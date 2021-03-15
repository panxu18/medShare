package com.xp.medshare.util.crypto;

import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECPoint;
import org.fisco.bcos.web3j.crypto.ECKeyPair;
import org.fisco.bcos.web3j.crypto.Hash;
import org.fisco.bcos.web3j.crypto.Sign;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

public class Sepc256Util {
    static final X9ECParameters CURVE_PARAMS = CustomNamedCurves.getByName("secp256k1");

    static final ECDomainParameters CURVE =
            new ECDomainParameters(
                    CURVE_PARAMS.getCurve(),
                    CURVE_PARAMS.getG(),
                    CURVE_PARAMS.getN(),
                    CURVE_PARAMS.getH());
    static SecureRandom secureRandom = new SecureRandom();
    static ECKeyGenerationParameters ecKeyGenerationParameters = new ECKeyGenerationParameters(CURVE, secureRandom);
    static final ECKeyPairGenerator eCKeyPairGenerator = new ECKeyPairGenerator();
    static {
        eCKeyPairGenerator.init(ecKeyGenerationParameters);
    }
    public static final BigInteger N = CURVE_PARAMS.getN();
    public static final SimplePublicKey G = new SimplePublicKey(point2PulicKey(CURVE_PARAMS.getG()));
    // 11243312043295020936268534984365667970470776440010476321705585644569702878133
    public static final SimplePublicKey H= G.times(
            new BigInteger("11243312043295020936268534984365667970470776440010476321705585644569702878133")); // 身份承诺生成元





    public static BigInteger point2PulicKey(ECPoint point) {
        byte[] bits = point.getEncoded(false);
        BigInteger publicKey = new BigInteger(1, Arrays.copyOfRange(bits, 1, bits.length));
        return publicKey;
    }

    public static BigInteger publicKeyAdd(BigInteger pk1, BigInteger pk2) {
        ECPoint point = publicKey2EcPoint(pk1).add(publicKey2EcPoint(pk2));
        return point2PulicKey(point);
    }

    /**
     * 将公钥转为椭圆曲线上的点
     *
     * @param publicKey 压缩形式的点
     * @return
     */
    public static ECPoint publicKey2EcPoint(BigInteger publicKey) {
        byte[] bits = publicKey.toByteArray();
        if (bits.length == 65) {
            bits[0] = 0x04;
        } else if (bits.length == 64) {
            byte[] encoded = new byte[bits.length + 1];
            encoded[0] = 0x04;
            System.arraycopy(bits, 0, encoded, 1, bits.length);
            bits = encoded;
        }
        ECPoint decodePoint = CURVE.getCurve().decodePoint(bits);
        return decodePoint;
    }

    public static ECKeyPair generateSimpleKeyPair() {
        AsymmetricCipherKeyPair asymmetricCipherKeyPair = eCKeyPairGenerator.generateKeyPair();
        ECPublicKeyParameters publicKeyParameters = (ECPublicKeyParameters) asymmetricCipherKeyPair.getPublic();
        ECPrivateKeyParameters privateKeyParameters = (ECPrivateKeyParameters) asymmetricCipherKeyPair.getPrivate();
        ECPoint publicPoint = publicKeyParameters.getQ();
        BigInteger publicKey = Sepc256Util.point2PulicKey(publicPoint);
        BigInteger privateKey = privateKeyParameters.getD();
        ECKeyPair keyPair = new ECKeyPair(privateKey, publicKey);
        return keyPair;
    }

    public static boolean isKeypairMatch(BigInteger privateKey, BigInteger publicKey) {
        return Sign.publicKeyFromPrivate(privateKey).equals(publicKey);
    }

    /**
     * 两个公钥相减，将公钥转为椭圆曲线上的点计算后，将结果转为整数类型
     */
    public static BigInteger publicKeySubtract(BigInteger firstKey, BigInteger secondKey) {
        ECPoint firstPoint = publicKey2EcPoint(firstKey);
        ECPoint secondPoint = publicKey2EcPoint(secondKey);
        return Sepc256Util.point2PulicKey(firstPoint.subtract(secondPoint));
    }

    public static BigInteger randValue() {
        BigInteger rand = generateSimpleKeyPair().getPrivateKey();
        return rand;
    }

    public static boolean validPublicKey(BigInteger key) {
        try {
            return CURVE.getCurve().isValidFieldElement(key);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 在椭圆曲线的有限域上进行散列
     */
    public static SimplePrivateKey hash(String rawData) {
        byte[] hashBytes = Hash.sha256(rawData.getBytes());
        BigInteger result =  new BigInteger(hashBytes).add(Sepc256Util.N).mod(Sepc256Util.N);
        return new SimplePrivateKey(result);
    }

    /**
     *
     * @param generator
     * @param values
     * @return
     */
    public static BigInteger times(ECPoint generator, BigInteger... values) {
        for (BigInteger v: values) {
            generator = generator.multiply(v);
        }
        return point2PulicKey(generator);
    }

    public static BigInteger times(BigInteger generator, BigInteger... values) {
        return times(publicKey2EcPoint(generator), values);
    }

    /**
     * 使用费马小定理计算逆元
     * @return
     */
    public static BigInteger inverse(BigInteger a) {
        return a.modPow(N.subtract(BigInteger.valueOf(2)), N);
    }

    public static void main(String[] args) {
        randValue();
        randValue();
        randValue();
    }
}

