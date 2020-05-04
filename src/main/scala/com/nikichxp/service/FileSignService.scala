package com.nikichxp.service

import java.io.{File, FileInputStream, FileOutputStream}
import java.security.{KeyStore, PrivateKey}
import java.security._
import java.nio.file.Paths

import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureOptions
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature
import java.util.Calendar

import org.apache.pdfbox.pdmodel.PDDocument

class FileSignService {

  def signFileAlt() = {
    val document = PDDocument.load(new File("D:/keys/card.pdf"))
    val signature = new PDSignature()
    signature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE)
    signature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED)
    signature.setName("Example User")
    signature.setLocation("Los Angeles, CA")
    signature.setReason("Testing")
    signature.setSignDate(Calendar.getInstance)
    val signatureOptions = new SignatureOptions
    // Size can vary, but should be enough for purpose.
    signatureOptions.setPreferredSignatureSize(SignatureOptions.DEFAULT_SIGNATURE_SIZE * 2)
    document.addSignature(signature, signatureOptions)
    // write incremental (only for signing purpose)
    document.save(new FileOutputStream(new File("D:/keys/card-sign.pdf")))
  }

  def signFile(file: File, key: String) = {
    val keyStore = KeyStore.getInstance("PKCS12")
    keyStore.load(new FileInputStream("D:/keys/sender_keystore.p12"), "changeit".toCharArray)
    val privateKey = keyStore.getKey("senderKeyPair", "changeit".toCharArray).asInstanceOf[PrivateKey]

    keyStore.load(new FileInputStream("D:/keys/receiver_keystore.p12"), "changeit".toCharArray)
    val certificate = keyStore.getCertificate("receiverKeyPair")
    val publicKey = certificate.getPublicKey

    val signature = Signature.getInstance("SHA256withRSA")
    signature.initSign(privateKey)

    import java.nio.file.Files
    import java.nio.file.Paths
    val messageBytes = Files.readAllBytes(Paths.get("D:/keys/card.pdf"))

    signature.update(messageBytes)
    val digitalSignature = signature.sign

    Files.write(Paths.get("digital_signature_2"), digitalSignature)
  }

}
