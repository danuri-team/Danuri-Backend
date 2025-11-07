package org.aing.danurirest.domain.admin.service.excel

import org.aing.danurirest.persistence.usage.entity.UsageHistory
import org.aing.danurirest.persistence.user.Age
import org.aing.danurirest.persistence.user.Sex
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.YearMonth

/**
 * 사용 통계 데이터 집계를 담당하는 컴포넌트
 */
@Component
class UsageStatisticsAggregator(
    private val formDataParser: FormDataParser,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(UsageStatisticsAggregator::class.java)
    }

    /**
     * 일별 사용 통계 집계 결과
     * @property dailyStats 일별 통계 맵: Day -> Age -> Sex -> Count
     * @property processedCount 처리된 이용 내역 수
     * @property skippedCount 건너뛴 이용 내역 수
     */
    data class AggregationResult(
        val dailyStats: Map<Int, Map<Age, Map<Sex, Int>>>,
        val processedCount: Int,
        val skippedCount: Int,
    )

    /**
     * 이용 내역 목록으로부터 일별 통계 집계
     * @param usageHistories 이용 내역 목록
     * @param yearMonth 대상 년월
     * @return 집계 결과
     */
    fun aggregateDailyStatistics(
        usageHistories: List<UsageHistory>,
        yearMonth: YearMonth,
    ): AggregationResult {
        logger.info("통계 집계 시작: yearMonth=$yearMonth, 이용 내역 수=${usageHistories.size}")

        val daysInMonth = yearMonth.lengthOfMonth()
        val dailyStats = initializeDailyStats(daysInMonth)

        var processedCount = 0
        var skippedCount = 0

        usageHistories.forEach { usage ->
            try {
                val day = usage.startAt.dayOfMonth

                if (day < 1 || day > daysInMonth) {
                    logger.warn("유효하지 않은 날짜: day=$day (월의 날짜 수: $daysInMonth)")
                    skippedCount++
                    return@forEach
                }

                // 메인 사용자 통계 집계
                if (aggregateMainUser(usage, dailyStats, day)) {
                    processedCount++
                }

                // 추가 참가자 통계 집계
                aggregateAdditionalParticipants(usage, dailyStats, day)
            } catch (e: Exception) {
                logger.error("이용 내역 처리 중 오류: usageId=${usage.id}", e)
                skippedCount++
            }
        }

        logger.info("통계 집계 완료 - 처리: $processedCount, 건너뜀: $skippedCount")

        return AggregationResult(
            dailyStats = dailyStats,
            processedCount = processedCount,
            skippedCount = skippedCount,
        )
    }

    /**
     * 일별 통계 맵 초기화
     */
    private fun initializeDailyStats(daysInMonth: Int): MutableMap<Int, MutableMap<Age, MutableMap<Sex, Int>>> {
        val dailyStats = mutableMapOf<Int, MutableMap<Age, MutableMap<Sex, Int>>>()

        for (day in 1..daysInMonth) {
            dailyStats[day] = mutableMapOf()
            Age.entries.forEach { age ->
                dailyStats[day]!![age] =
                    mutableMapOf(
                        Sex.MALE to 0,
                        Sex.FEMALE to 0,
                    )
            }
        }

        return dailyStats
    }

    /**
     * 메인 사용자 통계 집계
     * @return 집계 성공 여부
     */
    private fun aggregateMainUser(
        usage: UsageHistory,
        dailyStats: MutableMap<Int, MutableMap<Age, MutableMap<Sex, Int>>>,
        day: Int,
    ): Boolean {
        val formResult = usage.user.signUpForm?.result
        val userAge = formDataParser.extractUserAge(formResult)
        val userSex = formDataParser.extractUserSex(formResult)

        if (userAge == null || userSex == null) {
            logger.debug(
                "Day {}: 사용자 연령/성별 정보 없음 (userId={}, Age={}, Sex={})",
                day,
                usage.user.id,
                userAge,
                userSex,
            )
            return false
        }

        val ageStats = dailyStats[day]?.get(userAge)
        if (ageStats != null) {
            val currentCount = ageStats[userSex] ?: 0
            ageStats[userSex] = currentCount + 1

            logger.debug(
                "Day {}: 사용자 추가 - {}/{} (현재 카운트: {})",
                day,
                userAge,
                userSex,
                currentCount + 1,
            )
            return true
        }

        return false
    }

    /**
     * 추가 참가자 통계 집계
     */
    private fun aggregateAdditionalParticipants(
        usage: UsageHistory,
        dailyStats: MutableMap<Int, MutableMap<Age, MutableMap<Sex, Int>>>,
        day: Int,
    ) {
        usage.additionalParticipants.forEach { participant ->
            try {
                val count = participant.count
                if (count <= 0) {
                    return@forEach
                }

                val participantAge = participant.ageGroup
                val participantSex = participant.sex

                val ageStats = dailyStats[day]?.get(participantAge)
                if (ageStats != null) {
                    val currentCount = ageStats[participantSex] ?: 0
                    ageStats[participantSex] = currentCount + count

                    logger.debug(
                        "Day {}: 추가 참가자 - {}/{} x {}",
                        day,
                        participantAge,
                        participantSex,
                        count,
                    )
                }
            } catch (e: Exception) {
                logger.error("추가 참가자 처리 중 오류: participantId=${participant.id}", e)
            }
        }
    }
}
