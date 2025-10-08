package com.example.chatserver.common.auth

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtTokenProvider: JwtTokenProvider
) : OncePerRequestFilter() {

    companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
        const val TOKEN_PREFIX = "Bearer "
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (request.requestURI.contains("/member/create") ||
            request.requestURI.contains("/member/doLogin") ||
            request.requestURI.contains("/connect")
        ) {
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

            jwtTokenProvider.validateToken(token)
            val email = jwtTokenProvider.getEmailFromToken(token)
            val role = jwtTokenProvider.getRoleFromToken(token)
            val authorities = listOf(SimpleGrantedAuthority("ROLE_$role"))
            val authentication = UsernamePasswordAuthenticationToken(email, null, authorities)
            SecurityContextHolder.getContext().authentication = authentication

        } catch (e: Exception) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.writer.write("{\"message\":\"token is invalid\"}")
            return
        }

        filterChain.doFilter(request, response)
    }
}