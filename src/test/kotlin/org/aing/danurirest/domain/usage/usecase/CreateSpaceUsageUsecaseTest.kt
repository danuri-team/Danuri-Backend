package org.aing.danurirest.domain.usage.usecase

import io.mockk.*
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.global.security.jwt.dto.ContextDto
import org.aing.danurirest.global.third_party.notification.service.NotificationService
import org.aing.danurirest.global.third_party.s3.service.S3Service
import org.aing.danurirest.global.util.GenerateQrCode
import org.aing.danurirest.global.util.PrincipalUtil
import org.aing.danurirest.persistence.space.repository.SpaceJpaRepository
import org.aing.danurirest.persistence.usage.repository.UsageHistoryJpaRepository
import org.aing.danurirest.persistence.usage.repository.UsageHistoryRepository
import org.aing.danurirest.persistence.user.Role
import org.aing.danurirest.persistence.user.repository.UserJpaRepository
import org.aing.danurirest.testfixture.SpaceFixture
import org.aing.danurirest.testfixture.UsageHistoryFixture
import org.aing.danurirest.testfixture.UserFixture
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

class CreateSpaceUsageUsecaseTest {
    private val usageHistoryRepository = mockk<UsageHistoryRepository>()
    private val spaceJpaRepository = mockk<SpaceJpaRepository>()
    private val userJpaRepository = mockk<UserJpaRepository>()
    private val usageHistoryJpaRepository = mockk<UsageHistoryJpaRepository>()
    private val notificationService = mockk<NotificationService>()
    private val s3Service = mockk<S3Service>()

    private val createSpaceUsageUsecase =
        CreateSpaceUsageUsecase(
            usageHistoryRepository = usageHistoryRepository,
            spaceJpaRepository = spaceJpaRepository,
            userJpaRepository = userJpaRepository,
            usageHistoryJpaRepository = usageHistoryJpaRepository,
            notificationService = notificationService,
            s3Service = s3Service,
        )

    private val spaceId = UUID.randomUUID()
    private val userId = UUID.randomUUID()

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        mockkStatic(PrincipalUtil::class)
        mockkStatic(GenerateQrCode::class)
        mockkStatic(SecurityContextHolder::class)

        setupSecurityContext()
    }

    private fun setupSecurityContext() {
        val contextDto =
            ContextDto(
                id = userId,
                role = Role.ROLE_USER,
            )

        val authenticationMock =
            mockk<Authentication> {
                every { principal } returns contextDto
            }

        val securityContext =
            mockk<SecurityContext> {
                every { authentication } returns authenticationMock
            }

        every { SecurityContextHolder.getContext() } returns securityContext
    }

    @Test
    fun `가입 폼을 입력하지 않은 사용자는 예약할 수 없다`() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now() } returns LocalDateTime.of(2024, 1, 1, 10, 0)

        val space = SpaceFixture.createTestSpace()
        val userWithoutSignUp = UserFixture.createTestUser(signUpForm = null)

        every { spaceJpaRepository.findById(spaceId) } returns Optional.of(space)
        every { usageHistoryRepository.findUserCurrentUsageInfo(userId) } returns
            mockk {
                every { isUsingSpace } returns false
            }
        every { usageHistoryJpaRepository.findUsagesBySpaceAndTimeRange(any(), any(), any()) } returns emptyList()
        every { userJpaRepository.findById(userId) } returns Optional.of(userWithoutSignUp)

        val exception =
            assertThrows<CustomException> {
                createSpaceUsageUsecase.execute(spaceId)
            }

        assert(exception.customErrorCode == CustomErrorCode.NOT_SIGNED_UP)

        unmockkStatic(LocalDateTime::class)
    }

    @Test
    fun `존재하지 않는 공간은 예약할 수 없다`() {
        every { spaceJpaRepository.findById(spaceId) } returns Optional.empty()

        val exception =
            assertThrows<CustomException> {
                createSpaceUsageUsecase.execute(spaceId)
            }

        assert(exception.customErrorCode == CustomErrorCode.NOT_FOUND_SPACE)
    }

    @Test
    fun `이미 공간을 사용 중 일 경우 추가 사용할 수 없다`() {
        val space = SpaceFixture.createTestSpace()

        every { spaceJpaRepository.findById(spaceId) } returns Optional.of(space)
        every { usageHistoryRepository.findUserCurrentUsageInfo(userId) } returns
            mockk {
                every { isUsingSpace } returns true
            }

        val exception =
            assertThrows<CustomException> {
                createSpaceUsageUsecase.execute(spaceId)
            }

        assert(exception.customErrorCode == CustomErrorCode.USAGE_CONFLICT_USER)
    }

    @Test
    fun `운영 시간이 아닌 경우 이용할 수 없다`() {
        val space =
            SpaceFixture.createTestSpace(
                startAt = LocalTime.of(9, 0),
                endAt = LocalTime.of(18, 0),
            )

        every { spaceJpaRepository.findById(spaceId) } returns Optional.of(space)
        every { usageHistoryRepository.findUserCurrentUsageInfo(userId) } returns
            mockk {
                every { isUsingSpace } returns false
            }

        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now() } returns LocalDateTime.of(2024, 1, 1, 20, 0)

        val exception =
            assertThrows<CustomException> {
                createSpaceUsageUsecase.execute(spaceId)
            }

        assert(exception.customErrorCode == CustomErrorCode.SPACE_NOT_AVAILABLE)

        unmockkStatic(LocalDateTime::class)
    }

    @Test
    fun `예약 시간이 겹치는 경우 예약할 수 없다`() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now() } returns LocalDateTime.of(2024, 1, 1, 10, 0)

        val space = SpaceFixture.createTestSpace()
        val existingUsage = UsageHistoryFixture.createTestUsageHistory()

        every { spaceJpaRepository.findById(spaceId) } returns Optional.of(space)
        every { usageHistoryRepository.findUserCurrentUsageInfo(userId) } returns
            mockk {
                every { isUsingSpace } returns false
            }
        every { usageHistoryJpaRepository.findUsagesBySpaceAndTimeRange(any(), any(), any()) } returns listOf(existingUsage)

        val exception =
            assertThrows<CustomException> {
                createSpaceUsageUsecase.execute(spaceId)
            }

        assert(exception.customErrorCode == CustomErrorCode.USAGE_CONFLICT_SPACE)

        unmockkStatic(LocalDateTime::class)
    }
}
