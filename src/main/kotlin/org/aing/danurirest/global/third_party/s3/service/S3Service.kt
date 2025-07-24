package org.aing.danurirest.global.third_party.s3.service

import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
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
import java.io.FileNotFoundException
import java.io.IOException
import java.time.Duration
import java.util.UUID

@Service
class S3Service(
    private val s3Client: S3Client,
    private val preSigner: S3Presigner,
) {
    private val log: Logger = LoggerFactory.getLogger(S3Service::class.java)

    fun uploadFile(
        multipartFile: MultipartFile,
        bucketName: String,
    ): String {
        if (multipartFile.isEmpty) {
            throw FileNotFoundException("업로드된 파일이 비어 있습니다.")
        }

        val fileName: String = UUID.randomUUID().toString()

        try {
            multipartFile.inputStream.use { inputStream ->
                val fileBytes = inputStream.readAllBytes()

                s3Client.putObject(
                    PutObjectRequest
                        .builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .contentType(multipartFile.contentType)
                        .build(),
                    RequestBody.fromBytes(fileBytes),
                )

                val fileUrl: String =
                    s3Client
                        .utilities()
                        .getUrl { builder ->
                            builder
                                .bucket(
                                    bucketName,
                                ).key(fileName)
                        }.toExternalForm()
                return fileUrl
            }
        } catch (e: IOException) {
            log.error("이미지 업로드 중 I/O 오류 발생 | ${e.message}")
            throw CustomException(CustomErrorCode.UNKNOWN_SERVER_ERROR)
        } catch (e: S3Exception) {
            log.error("이미지 업로드 중 외부 종속 오류 발생 | ${e.message}")
            throw CustomException(CustomErrorCode.UNKNOWN_SERVER_ERROR)
        }
    }

    fun uploadQrImage(
        qrBytes: ByteArray,
        bucketName: String,
    ): String {
        val fileName = UUID.randomUUID().toString()

        try {
            s3Client.putObject(
                PutObjectRequest
                    .builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType("image/jpeg")
                    .build(),
                RequestBody.fromBytes(qrBytes),
            )

            return fileName
        } catch (e: IOException) {
            log.error("QR 업로드 중 I/O 오류 발생 | ${e.message}")
            throw CustomException(CustomErrorCode.UNKNOWN_SERVER_ERROR)
        } catch (e: S3Exception) {
            log.error("QR 업로드 중 외부 종속 오류 발생 | ${e.message}")
            throw CustomException(CustomErrorCode.UNKNOWN_SERVER_ERROR)
        }
    }

    fun generatePreSignedUrl(
        bucketName: String,
        key: String,
    ): String {
        val getObjectRequest =
            GetObjectRequest
                .builder()
                .bucket(bucketName)
                .key(key)
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
