package net.bi119ate5hxk.a2ipns

import android.util.Base64
import io.jsonwebtoken.Jwts
import org.json.JSONObject
import java.nio.charset.Charset
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.interfaces.ECPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*
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
            SecretKeySpec(keyText.toByteArray(Charsets.US_ASCII), "AES"),
            GCMParameterSpec(128, iv)
        )

        cipher.updateAAD(aad.toByteArray())
        cipher.update(cipherData)
        val decryptedData = cipher.doFinal(tag)

        // Must specify Charset, or toString() returns internal object name
        return decryptedData.toString(Charset.defaultCharset())
    }

    fun getAPNSBearerToken(authTokenPackage: JSONObject?): String? {
        if (authTokenPackage != null) {
            val key = getPrivateKeyFromString(authTokenPackage.getString("authKey"))
            val jwt = Jwts.builder()
                .setHeaderParam("kid", authTokenPackage.getString("authkeyid"))
                .setIssuer(authTokenPackage.getString("teamid"))
                .setIssuedAt(Calendar.getInstance(TimeZone.getTimeZone("UTC")).time)
                .signWith(key)
                .compact()

            return jwt
        }

        return null
    }

    fun getPrivateKeyFromString(keyPEM: String): PrivateKey {
        val bytes = Base64.decode(
            Base64.decode(keyPEM, Base64.DEFAULT)
                .toString(Charset.defaultCharset())
                .replace("-----BEGIN PRIVATE KEY-----\n", "")
                .replace("-----END PRIVATE KEY-----", ""),
            Base64.DEFAULT
        )

        // ES256 = ECDSA using P-256 curve and SHA-256 hash algorithm
        val keyFactory = KeyFactory.getInstance("EC")

        return keyFactory.generatePrivate(PKCS8EncodedKeySpec(bytes)) as ECPrivateKey
    }
}