package org.aing.danurirest.domain.admin.usecase

import org.aing.danurirest.domain.admin.dto.SignInDeviceResponse
import org.aing.danurirest.domain.auth.admin.usecase.GetAdminCompanyIdUsecase
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.third_party.s3.BucketType
import org.aing.danurirest.global.third_party.s3.service.S3Service
import org.aing.danurirest.global.util.GenerateQrCode
import org.aing.danurirest.global.util.GenerateRandomCode
import org.aing.danurirest.persistence.device.entity.VerificationCode
import org.aing.danurirest.persistence.device.repository.DeviceJpaRepository
import org.aing.danurirest.persistence.device.repository.VerificationCodeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class SignInDeviceUsecase(
    private val verificationCodeRepository: VerificationCodeRepository,
    private val deviceJpaRepository: DeviceJpaRepository,
    private val s3Service: S3Service,
    private val getAdminCompanyIdUsecase: GetAdminCompanyIdUsecase,
) {
    @Transactional
    fun execute(deviceId: UUID): SignInDeviceResponse {
        val companyId = getAdminCompanyIdUsecase.execute()
        deviceJpaRepository.findByIdAndCompanyId(deviceId, companyId)
            ?: throw CustomException(CustomErrorCode.NOT_FOUND_DEVICE)

        var verifyCode: String

        do {
            verifyCode = GenerateRandomCode.execute()
        } while (verificationCodeRepository.existsById(verifyCode))

        verificationCodeRepository.save(
            VerificationCode(
                id = deviceId,
                code = verifyCode,
            ),
        )

        val qr =
            GenerateQrCode.execute(
                """{"code":"$verifyCode"}""",
            )

        val fileName =
            s3Service.uploadQrImage(
                qr.getOrElse {
                    throw CustomException(CustomErrorCode.UNKNOWN_SERVER_ERROR)
                },
                BucketType.QR_LINK,
            )

        val qrLink =
            s3Service.generatePreSignedUrl(
                BucketType.QR_LINK,
                fileName,
            )

        return SignInDeviceResponse(
            qrLink,
            verifyCode,
            LocalDateTime.now().plusMinutes(3),
        )
    }
}
