package org.aing.danurirest.global.third_party.discord.client

import org.aing.danurirest.global.third_party.discord.dto.DiscordMessage
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "\${discord.name}", url = "\${discord.webhook-url}")
interface DiscordFeignClient {
    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun sendMessage(
        @RequestBody discordMessage: DiscordMessage?,
    )
}
