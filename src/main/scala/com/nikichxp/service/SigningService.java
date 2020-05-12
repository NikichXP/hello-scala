package com.nikichxp.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureInterface;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.*;
import java.nio.file.Files;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Calendar;

public class SigningService {

    private static final String KEY_STORE_TYPE = "jks";
    private final String keyStorePath;
    private final String keyStorePassword;
    private final String certificateAlias;
    private final String tsaUrl;

    private BouncyCastleProvider provider = new BouncyCastleProvider();

    public SigningService(String keyStorePath, String keyStorePassword, String certificateAlias, String tsaUrl) {
        this.keyStorePath = keyStorePath;
        this.keyStorePassword = keyStorePassword;
        this.certificateAlias = certificateAlias;
        this.tsaUrl = tsaUrl;
    }

    public boolean signPdf(File file) {
        try {
            if (!file.exists() || !file.isFile() || file.length() > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("File is empty, directory or larger than 2GB");
            }
            KeyStore keyStore = this.getKeyStore();
            Signature signature = new Signature(keyStore, this.keyStorePassword.toCharArray(), certificateAlias, tsaUrl);
            this.signDetached(signature, file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private KeyStore getKeyStore() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        char[] password = keyStorePassword.toCharArray();
        KeyStore keystore = KeyStore.getInstance("PKCS12", provider);
        keystore.load(new FileInputStream(keyStorePath), password);
//        keystore.load(new FileInputStream("D:/keys/keystore.p12"), password);
        return keystore;
    }

    private void signDetached(SignatureInterface signature, File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file); PDDocument doc = PDDocument.load(file)) {
            signDetached(signature, doc, fos);
        }
    }

    private void signDetached(SignatureInterface signature, PDDocument document, OutputStream output) throws IOException {
        PDSignature pdSignature = new PDSignature();
        pdSignature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);
        pdSignature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED);
        pdSignature.setName("Nikita R");
        pdSignature.setReason("Example signature");
        pdSignature.setSignDate(Calendar.getInstance());
        document.addSignature(pdSignature, signature);
        document.saveIncremental(output);
    }
}
