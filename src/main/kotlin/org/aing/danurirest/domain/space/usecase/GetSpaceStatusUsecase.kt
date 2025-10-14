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
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class GetSpaceStatusUsecase(
    private val spaceRepository: SpaceRepository,
) {
    @Transactional(readOnly = true)
    fun execute(): List<GetSpaceStatusByDeviceIdResponse> {
        val now = LocalDateTime.now()
        val deviceContextDto = PrincipalUtil.getContextDto()
        val spacesWithBookings = spaceRepository.findSpacesWithBookingsByDeviceId(deviceContextDto.id!!)
        val currentTime = now.toLocalTime()

        return spacesWithBookings
            .filter { it.space.endAt > currentTime }
            .map { dto ->
                val timeSlots = generateTimeSlots(dto.space, dto.bookedRanges, now)
                val isAvailable = isCurrentlyAvailable(dto.space, dto.bookedRanges, now)

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
        now: LocalDateTime,
    ): Boolean {
        if (now.toLocalTime() !in space.startAt..space.endAt) {
            return false
        }

        if (space.allowOverlap) {
            return true
        }

        return bookedRanges.none {
            now.isAfter(it.startTime) && now.isBefore(it.endTime)
        }
    }

    private fun generateTimeSlots(
        space: Space,
        bookedRanges: List<BookedTimeRange>,
        now: LocalDateTime,
    ): List<SpaceTimeSlot> {
        val slots = mutableListOf<SpaceTimeSlot>()
        val slotDuration = Duration.ofMinutes(30)
        val currentTimeRounded =
            now
                .toLocalTime()
                .let { LocalTime.of(it.hour, if (it.minute < 30) 0 else 30) }

        var currentSlot = now.toLocalDate().atTime(maxOf(space.startAt, currentTimeRounded))
        val threeHoursLater = now.plusHours(3)

        val spaceEndDateTime =
            if (space.endAt <= space.startAt) {
                now.toLocalDate().plusDays(1).atTime(space.endAt)
            } else {
                now.toLocalDate().atTime(space.endAt)
            }

        val endSlot = minOf(threeHoursLater, spaceEndDateTime)

        while (currentSlot.plusMinutes(30) <= endSlot) {
            val slotEnd = currentSlot.plus(slotDuration)

            val isBooked =
                bookedRanges.any { booking ->
                    currentSlot.isBefore(booking.endTime) && slotEnd.isAfter(booking.startTime)
                }

            slots.add(
                SpaceTimeSlot(
                    startTime = currentSlot.toLocalTime(),
                    endTime = slotEnd.toLocalTime(),
                    isAvailable = !isBooked || space.allowOverlap,
                ),
            )

            currentSlot = slotEnd
        }

        return slots
    }
}
