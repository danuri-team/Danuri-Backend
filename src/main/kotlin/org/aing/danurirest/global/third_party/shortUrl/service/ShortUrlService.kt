package org.aing.danurirest.global.third_party.shortUrl.service

import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.third_party.shortUrl.client.ShortUrlFeignClient
import org.aing.danurirest.global.third_party.shortUrl.dto.ShortUrlRequestDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ShortUrlService(
    private val shortUrlFeignClient: ShortUrlFeignClient,
    @Value("\${short-url.maker-url}")
    private val shortUrlMakerUrl: String,
) {
    fun execute(targetUrl: String): String {
        if (targetUrl.isEmpty()) {
            throw CustomException(CustomErrorCode.UNKNOWN_SERVER_ERROR)
        }
        return shortUrlMakerUrl + shortUrlFeignClient.getShortUrl(ShortUrlRequestDto(targetUrl)).id
    }
}
