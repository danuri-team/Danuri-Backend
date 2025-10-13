package org.aing.danurirest.domain.space.usecase

import org.aing.danurirest.domain.space.dto.GetSpaceStatusByDeviceIdResponse
import org.aing.danurirest.domain.space.dto.SpaceTimeSlot
import org.aing.danurirest.global.util.PrincipalUtil
import org.aing.danurirest.persistence.space.dto.BookedTimeRange
import org.aing.danurirest.persistence.space.entity.Space
import org.aing.danurirest.persistence.space.repository.SpaceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class GetSpaceStatusUsecase(
    private val spaceRepository: SpaceRepository,
) {
    @Transactional(readOnly = true)
    fun execute(): List<GetSpaceStatusByDeviceIdResponse> {
        val deviceContextDto = PrincipalUtil.getContextDto()
        val spacesWithBookings = spaceRepository.findSpacesWithBookingsByDeviceId(deviceContextDto.id!!)
        val currentTime = LocalTime.now()

        return spacesWithBookings
            .filter { it.space.endAt > currentTime }
            .map { dto ->
                val timeSlots = generateTimeSlots(dto.space, dto.bookedRanges)
                val isAvailable = isCurrentlyAvailable(dto.space, dto.bookedRanges, currentTime)

                GetSpaceStatusByDeviceIdResponse.from(
                    entity = dto.space,
                    isSpaceAvailable = isAvailable,
                    timeSlots = timeSlots,
                )
            }
    }

    private fun isCurrentlyAvailable(
        space: Space,
        bookedRanges: List<BookedTimeRange>,
        currentTime: LocalTime,
    ): Boolean {
        if (currentTime !in space.startAt..space.endAt) {
            return false
        }

        if (space.allowOverlap) {
            return true
        }

        val now = LocalDateTime.now()
        return bookedRanges.none {
            now.isAfter(it.startTime) && now.isBefore(it.endTime)
        }
    }

    private fun generateTimeSlots(
        space: Space,
        bookedRanges: List<BookedTimeRange>,
    ): List<SpaceTimeSlot> {
        val slots = mutableListOf<SpaceTimeSlot>()
        val slotDuration = Duration.ofMinutes(30)
        val now = LocalDateTime.now()
        val today = LocalDate.now()
        val currentTimeRounded =
            now
                .toLocalTime()
                .let { LocalTime.of(it.hour, if (it.minute < 30) 0 else 30) }

        var currentTime = maxOf(space.startAt, currentTimeRounded)
        val threeHoursLater = currentTimeRounded.plusHours(3)

        val endTime = minOf(threeHoursLater, space.endAt)

        while (currentTime.plusMinutes(30) <= endTime) {
            val slotEnd = currentTime.plus(slotDuration)

            val isBooked =
                bookedRanges.any { booking ->
                    val slotStart = LocalDateTime.of(today, currentTime)
                    val slotEndTime = LocalDateTime.of(today, slotEnd)

                    slotStart.isBefore(booking.endTime) && slotEndTime.isAfter(booking.startTime)
                }

            slots.add(
                SpaceTimeSlot(
                    startTime = currentTime,
                    endTime = slotEnd,
                    isAvailable = !isBooked || space.allowOverlap,
                ),
            )

            currentTime = slotEnd
        }

        return slots
    }
}
