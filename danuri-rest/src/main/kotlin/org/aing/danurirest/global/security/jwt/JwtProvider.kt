package org.aing.danurirest.global.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.Keys
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.dto.JwtDetails
import org.aing.danurirest.global.security.jwt.enum.TokenType
import org.aing.danurirest.global.security.serivce.AuthDetailService
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Base64
import java.util.Date
import java.util.UUID

class JwtProvider(
    @Value("\${jwt.access-token-key}")
    private val accessTokenKey: String,
    @Value("\${jwt.refresh-token-key}")
    private val refreshTokenKey: String,
    @Value("\${jwt.access-token-expires}")
    private val accessTokenExpires: Long,
    @Value("\${jwt.refresh-token-expires}")
    private val refreshTokenExpires: Long,
    private val authDetailsService: AuthDetailService,
) {
    fun getAuthentication(token: String?): UsernamePasswordAuthenticationToken {
        val resolvedToken = resolveToken(token)
        val payload = getPayload(resolvedToken, TokenType.ACCESS_TOKEN)

        val userDetails = authDetailsService.loadUserByUsername(UUID.fromString(payload.subject))

        return UsernamePasswordAuthenticationToken(userDetails, null, mutableListOf(SimpleGrantedAuthority(userDetails.role.toString())))
    }

    private fun resolveToken(token: String?): String? =
        if (token == null || !token.startsWith("Bearer ")) {
            null
        } else {
            token.substring(7)
        }

    fun getIdByRefreshToken(refreshToken: String): String = getPayload(refreshToken, TokenType.REFRESH_TOKEN).subject

    private fun getPayload(
        token: String?,
        tokenType: TokenType,
    ): Claims {
        if (token == null) {
            throw CustomException(CustomErrorCode.VALIDATION_ERROR)
        }

        val tokenKey = if (tokenType == TokenType.ACCESS_TOKEN) accessTokenKey else refreshTokenKey
        val keyBytes = Base64.getEncoder().encode(tokenKey.encodeToByteArray())
        val signingKey = Keys.hmacShaKeyFor(keyBytes)

        try {
            return Jwts
                .parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: ExpiredJwtException) {
            throw CustomException(CustomErrorCode.VALIDATION_ERROR)
        } catch (e: UnsupportedJwtException) {
            throw CustomException(CustomErrorCode.VALIDATION_ERROR)
        } catch (e: MalformedJwtException) {
            throw CustomException(CustomErrorCode.VALIDATION_ERROR)
        } catch (e: RuntimeException) {
            throw CustomException(CustomErrorCode.VALIDATION_ERROR)
        }
    }

    fun generateToken(
        id: UUID,
        tokenType: TokenType,
    ): JwtDetails {
        val tokenExpires = if (tokenType == TokenType.ACCESS_TOKEN) accessTokenExpires else refreshTokenExpires
        val expiredAt =
            Date.from(
                LocalDateTime
                    .now()
                    .plus(Duration.ofMillis(tokenExpires))
                    .atZone(ZoneId.systemDefault())
                    .toInstant(),
            )

        val tokenKey = if (tokenType == TokenType.ACCESS_TOKEN) accessTokenKey else refreshTokenKey
        val keyBytes = Base64.getEncoder().encode(tokenKey.encodeToByteArray())
        val signingKey = Keys.hmacShaKeyFor(keyBytes)

        val token =
            Jwts
                .builder()
                .subject(id.toString())
                .signWith(signingKey)
                .issuedAt(Date())
                .expiration(expiredAt)
                .compact()

        return JwtDetails(
            token = token,
            expiredAt = expiredAt,
        )
    }
}
