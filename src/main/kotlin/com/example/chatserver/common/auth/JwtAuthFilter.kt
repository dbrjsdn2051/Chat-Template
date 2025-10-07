package com.example.chatserver.common.auth

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.nio.charset.StandardCharsets
import java.security.Key
import javax.crypto.spec.SecretKeySpec

@Component
class JwtAuthFilter(
    @Value("\${jwt.secretKey}")
    private val secretKey: String
) : OncePerRequestFilter() {

    companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
        const val TOKEN_PREFIX = "Bearer "
        const val EMAIL = "email"
        const val ROLE = "role"
    }

    private val key: Key by lazy {
        SecretKeySpec(
            java.util.Base64.getDecoder().decode(secretKey),
            SignatureAlgorithm.HS256.jcaName
        )
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (request.requestURI.contains("/member/create") || request.requestURI.contains("/member/doLogin")) {
            filterChain.doFilter(request, response)
            return
        }

        try {
            val header = request.getHeader(AUTHORIZATION_HEADER)

            if (header.isNullOrBlank() || !header.startsWith(TOKEN_PREFIX)) {
                filterChain.doFilter(request, response)
                return
            }

            val token = header.substring(TOKEN_PREFIX.length)

            val claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .body

            val email = claims.subject
            val role = claims[ROLE] as? String

            if (email != null && role != null) {
                val authorities = listOf(SimpleGrantedAuthority("ROLE_$role"))
                val authentication = UsernamePasswordAuthenticationToken(email, null, authorities)
                SecurityContextHolder.getContext().authentication = authentication
            }

        } catch (e: Exception) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.writer.write("{\"message\":\"token is invalid\"}")
            return
        }

        filterChain.doFilter(request, response)
    }
}