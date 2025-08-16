package org.aing.danurirest.global.third_party.s3.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import java.net.URI

@Configuration
class S3ClientConfiguration(
    @Value("\${s3.endpoint}")
    private val endpoint: String,
    @Value("\${s3.access-key}")
    private val accessKey: String,
    @Value("\${s3.secret-key}")
    private val secretKey: String,
) {
    val credentials: AwsCredentials = AwsBasicCredentials.create(accessKey, secretKey)

    @Bean
    fun s3Client(): S3Client =
        S3Client
            .builder()
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .region(Region.of("apac"))
            .endpointOverride(URI.create(endpoint))
            .build()

    @Bean
    fun preSigner(): S3Presigner =
        S3Presigner
            .builder()
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .region(Region.of("apac"))
            .endpointOverride(URI.create(endpoint))
            .build()
}
