package com.example.chatserver.common.auth

import com.example.chatserver.domain.Role
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.Date
import javax.crypto.spec.SecretKeySpec

@Component
class JwtTokenProvider(
    @Value("\${jwt.secretKey}")
    private val secretKey: String,
    @Value("\${jwt.expiration}")
    private val expiration: Int,
) {

    companion object {
        const val ROLE_CLAIM = "role"
    }

    private val key: Key by lazy {
        SecretKeySpec(java.util.Base64.getDecoder().decode(secretKey), SignatureAlgorithm.HS256.jcaName)
    }

    fun generateToken(email: String, role: Role): String {
        val now = Date()
        val expirationDate = Date(now.time + expiration * 60 * 1000L)

        return Jwts.builder()
            .setSubject(email)
            .claim(ROLE_CLAIM, role.name)
            .setIssuedAt(now)
            .setExpiration(expirationDate)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
            true
        } catch (e: Exception) {
            println("token invalid: ${e.message}")
            false
        }
    }

    fun getEmailFromToken(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
            .subject ?: throw IllegalArgumentException("email is null")
    }

    fun getRoleFromToken(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body[ROLE_CLAIM] as? String ?: throw IllegalArgumentException("role is null")
    }
}