package org.aing.danurirest.global.third_party.shortUrl.client

import org.aing.danurirest.global.third_party.shortUrl.dto.ShortUrlRequestDto
import org.aing.danurirest.global.third_party.shortUrl.dto.ShortUrlResponseDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "\${short-url.name}", url = "\${short-url.maker-url}")
interface ShortUrlFeignClient {
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getShortUrl(
        @RequestBody url: ShortUrlRequestDto,
    ): ShortUrlResponseDto
}
