package com.nikichxp.service

import java.io.{File, FileInputStream, FileOutputStream}

class FileSignService {

  private val userName = "Nikita Ru"
  private val location = "Kiev"
  private val reason = "Testing"

  private val signingService = new SigningService("D:/keys/keystore.p12", "changeit", "nikichxp",
    "")

  //  private val provider = new BouncyCastleProvider

  def signFileAlt(file: File, target: File) = {


    val result = signingService.signPdf(file)

    target.createNewFile()
    val stream = new FileOutputStream(target)
    stream.write(result)
    stream.close()

    //    val document = PDDocument.load(input)
    //    val signature = new PDSignature()
    //    signature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE)
    //    signature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED)
    //    signature.setName(userName)
    //    signature.setLocation(location)
    //    signature.setReason(reason)
    //    signature.setSignDate(Calendar.getInstance)
    //    val signatureOptions = new SignatureOptions
    //    signatureOptions.setPreferredSignatureSize(SignatureOptions.DEFAULT_SIGNATURE_SIZE * 2)
    //    document.addSignature(signature, signatureOptions)
    //    document.save(new FileOutputStream(input))
    //
    //    val password = "changeit".toCharArray
    //
    //    val keystore = KeyStore.getInstance("PKCS12")
    //    keystore.load(new FileInputStream("D:/keys/sender_keystore.p12"), password)
    //
    //    Security.addProvider(provider)
    ////    provider.
    //
    //    val aliases = keystore.aliases
    //    var alias: String = aliases.nextElement
    //    if (!aliases.hasMoreElements) {
    //      throw new KeyStoreException("Keystore is empty")
    //    }
    //
    //    CreateSignature.privateKey = keystore.getKey(alias, password).asInstanceOf[PrivateKey]
    //    val certificateChain = keystore.getCertificateChain(alias)
    //    CreateSignature.certificate = certificateChain(0)
    //
    //    val inFile = new Nothing("src/test/resources/mkl/testarea/pdfbox1/sign/test.pdf ")
    //    val outFile = new Nothing(RESULT_FOLDER, filename)
    //    val createSignature = new Nothing
    //    CreateSignature.fixed = fixed
    //    CreateSignature.der = der
    //    createSignature.signPdf(inFile, outFile)
  }

}
