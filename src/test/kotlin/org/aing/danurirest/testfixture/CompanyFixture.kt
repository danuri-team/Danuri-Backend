package org.aing.danurirest.testfixture

import io.mockk.mockk
import org.aing.danurirest.persistence.company.entity.Company
import org.aing.danurirest.persistence.help.entity.HelpSetting
import java.util.*

object CompanyFixture {
    fun createTestCompany(name: String = "TestCompany"): Company {
        val mockCompany = mockk<Company>()

        val helpSetting =
            HelpSetting(
                id = UUID.randomUUID(),
                enable = false,
                company = mockCompany,
                targetAdmins = emptyList(),
            )

        return Company(
            id = UUID.randomUUID(),
            name = name,
            helpSetting = helpSetting,
        )
    }
}
