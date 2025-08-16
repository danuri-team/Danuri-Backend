package org.aing.danurirest.global.third_party.s3.service

import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.third_party.s3.BucketType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.model.S3Exception
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import java.io.IOException
import java.time.Duration
import java.util.UUID

@Service
class S3Service(
    private val s3Client: S3Client,
    private val preSigner: S3Presigner,
) {
    private val log: Logger = LoggerFactory.getLogger(S3Service::class.java)

    private fun uploadToS3(
        bucketType: BucketType,
        fileBytes: ByteArray,
        contentType: String,
    ): String {
        val fileName: String = UUID.randomUUID().toString()

        try {
            s3Client.putObject(
                PutObjectRequest
                    .builder()
                    .bucket(bucketType.bucketName)
                    .key("${bucketType.folder}/$fileName")
                    .contentType(contentType)
                    .build(),
                RequestBody.fromBytes(fileBytes),
            )
            return fileName
        } catch (e: IOException) {
            log.error("S3 업로드 중 I/O 오류 발생 | ${e.message}")
            throw CustomException(CustomErrorCode.UNKNOWN_SERVER_ERROR)
        } catch (e: S3Exception) {
            log.error("S3 업로드 중 외부 종속 오류 발생 | ${e.message}")
            throw CustomException(CustomErrorCode.UNKNOWN_SERVER_ERROR)
        }
    }

    fun uploadFile(
        multipartFile: MultipartFile,
        bucketType: BucketType,
    ): String {
        if (multipartFile.isEmpty) {
            log.error("S3 업로드 중 파일이 빈 오류 발생")
            throw CustomException(CustomErrorCode.UNKNOWN_SERVER_ERROR)
        }

        val fileName: String =
            uploadToS3(
                fileBytes = multipartFile.bytes,
                contentType = multipartFile.contentType ?: "application/octet-stream",
                bucketType = bucketType,
            )

        return s3Client
            .utilities()
            .getUrl { builder ->
                builder
                    .bucket(bucketType.bucketName)
                    .key("${bucketType.folder}/$fileName")
            }.toExternalForm()
    }

    fun uploadQrImage(
        qrBytes: ByteArray,
        bucketType: BucketType = BucketType.QR_LINK,
    ): String =
        uploadToS3(
            bucketType = bucketType,
            fileBytes = qrBytes,
            contentType = "image/jpeg",
        )

    fun generatePreSignedUrl(
        bucketType: BucketType,
        key: String,
    ): String {
        val getObjectRequest =
            GetObjectRequest
                .builder()
                .bucket(bucketType.bucketName)
                .key("${bucketType.folder}/$key")
                .build()

        val preSignRequest =
            GetObjectPresignRequest
                .builder()
                .signatureDuration(Duration.ofHours(1))
                .getObjectRequest(getObjectRequest)
                .build()

        val preSignedUrl = preSigner.presignGetObject(preSignRequest)
        return preSignedUrl.url().toString()
    }
}
