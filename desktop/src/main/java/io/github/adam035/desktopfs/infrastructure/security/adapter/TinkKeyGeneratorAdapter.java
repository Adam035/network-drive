package io.github.adam035.desktopfs.infrastructure.security.adapter;

import com.google.crypto.tink.InsecureSecretKeyAccess;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.RegistryConfiguration;
import com.google.crypto.tink.TinkProtoKeysetFormat;
import com.google.crypto.tink.hybrid.HpkeParameters;
import com.google.crypto.tink.hybrid.HybridConfig;
import io.github.adam035.desktopfs.application.dto.KeyPair;
import io.github.adam035.desktopfs.application.port.KeyGeneratorPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.GeneralSecurityException;

import static com.google.crypto.tink.hybrid.HpkeParameters.AeadId.AES_256_GCM;
import static com.google.crypto.tink.hybrid.HpkeParameters.KdfId.HKDF_SHA256;
import static com.google.crypto.tink.hybrid.HpkeParameters.KemId.DHKEM_X25519_HKDF_SHA256;
import static com.google.crypto.tink.hybrid.HpkeParameters.Variant.TINK;

@Component
@RequiredArgsConstructor
public class TinkKeyGeneratorAdapter implements KeyGeneratorPort {

    @Override
    public KeyPair generateKeyPair() {
        try {
            HybridConfig.register();

            HpkeParameters parameters = HpkeParameters.builder()
                    .setKemId(DHKEM_X25519_HKDF_SHA256)
                    .setKdfId(HKDF_SHA256)
                    .setAeadId(AES_256_GCM)
                    .setVariant(TINK)
                    .build();

            KeysetHandle privateHandle = KeysetHandle.generateNew(parameters, RegistryConfiguration.get());
            KeysetHandle publicHandle = privateHandle.getPublicKeysetHandle();

            byte[] privateKey = TinkProtoKeysetFormat.serializeKeyset(
                    privateHandle,
                    InsecureSecretKeyAccess.get(),
                    RegistryConfiguration.get()
            );
            byte[] publicKey = TinkProtoKeysetFormat.serializeKeysetWithoutSecret(
                    publicHandle,
                    RegistryConfiguration.get()
            );

            return new KeyPair(privateKey, publicKey);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Could not generate device key pair", e);
        }
    }

}
