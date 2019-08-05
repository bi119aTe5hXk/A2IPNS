package net.bi119ate5hxk.a2ipns

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

internal object CryptoHelper {
    fun decrypt(encryptedText: String, tagText: String, ivText: String, keyText: String): String? {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val cipherData = Base64.decode(encryptedText, Base64.DEFAULT)
        val tag = Base64.decode(tagText, Base64.DEFAULT)
        val iv = Base64.decode(ivText, Base64.DEFAULT)
        val aad = "A2IPNS"

        cipher.init(
            Cipher.DECRYPT_MODE,
            SecretKeySpec(keyText.toByteArray(), "AES"),
            GCMParameterSpec(128, iv)
        )

        cipher.updateAAD(aad.toByteArray())
        cipher.update(cipherData)
        val decryptedData = cipher.doFinal(tag)

        return decryptedData.toString()
    }
}