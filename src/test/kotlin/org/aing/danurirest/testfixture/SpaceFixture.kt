package org.aing.danurirest.testfixture

import org.aing.danurirest.persistence.company.entity.Company
import org.aing.danurirest.persistence.space.entity.Space
import java.time.LocalTime
import java.util.*

object SpaceFixture {
    fun createTestSpace(
        company: Company = CompanyFixture.createTestCompany(),
        name: String = "TestSpace",
        startAt: LocalTime = LocalTime.of(9, 0),
        endAt: LocalTime = LocalTime.of(18, 0),
    ): Space {
        return Space(
            id = UUID.randomUUID(),
            company = company,
            name = name,
            startAt = startAt,
            endAt = endAt,
        )
    }
}