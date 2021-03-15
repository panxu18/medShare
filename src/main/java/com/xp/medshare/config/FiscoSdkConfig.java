package com.xp.medshare.config;

import com.moandjiezana.toml.Toml;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.BcosSDKException;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.config.ConfigOption;
import org.fisco.bcos.sdk.config.exceptions.ConfigException;
import org.fisco.bcos.sdk.config.model.ConfigProperty;
import org.fisco.bcos.sdk.config.model.CryptoMaterialConfig;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Configuration
public class FiscoSdkConfig {
    @Bean
    public BcosSDK initSDK() {
        try {
            ClassPathResource classPathResource = new ClassPathResource("fisco-config.toml");
            File configFile = classPathResource.getFile();
            ConfigProperty configProperty = new Toml().read(configFile).to(ConfigProperty.class);
            Map<String, Object> cryptoMaterialConfig = configProperty.getCryptoMaterial();
            classPathResource = new ClassPathResource((String) cryptoMaterialConfig.get("certPath"));
            String path = classPathResource.getFile().getAbsolutePath();
            cryptoMaterialConfig.put("certPath", path);

            ConfigOption configOption = new ConfigOption(configProperty, CryptoType.ECDSA_TYPE);

            return new BcosSDK(configOption);
        } catch (ConfigException | IOException e ) {
            throw new BcosSDKException("create BcosSDK failed, error info: " + e.getMessage(), e);
        }
    }

    @Bean
    public Client initClient(BcosSDK bcosSDK) {
        Client client = bcosSDK.getClient(1);
        CryptoKeyPair cryptoKeyPair = client.getCryptoSuite().createKeyPair();
        client.getCryptoSuite().setCryptoKeyPair(cryptoKeyPair);
        return client;
    }

    @Bean
    public CryptoKeyPair initCryptoKeyPair(Client client) {
        return client.getCryptoSuite().getCryptoKeyPair();
    }


}
