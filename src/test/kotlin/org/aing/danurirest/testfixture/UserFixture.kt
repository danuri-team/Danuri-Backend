package org.aing.danurirest.testfixture

import org.aing.danurirest.persistence.company.entity.Company
import org.aing.danurirest.persistence.form.entity.FormResult
import org.aing.danurirest.persistence.user.entity.User
import java.util.*

object UserFixture {
    fun createTestUser(
        company: Company = CompanyFixture.createTestCompany(),
        phone: String = "010-1234-5678",
        signUpForm: FormResult? = null,
    ): User {
        return User(
            id = UUID.randomUUID(),
            company = company,
            phone = phone,
            signUpForm = signUpForm,
        )
    }

    fun createTestUserWithSignUp(
        company: Company = CompanyFixture.createTestCompany(),
        phone: String = "010-1234-5678",
    ): User {
        val user = User(
            id = UUID.randomUUID(),
            company = company,
            phone = phone,
            signUpForm = null,
        )
        val formResult = FormResultFixture.createTestFormResult(user = user)
        return User(
            id = user.id,
            company = user.company,
            phone = user.phone,
            signUpForm = formResult,
        )
    }
}