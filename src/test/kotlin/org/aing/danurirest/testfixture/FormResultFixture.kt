package org.aing.danurirest.testfixture

import org.aing.danurirest.persistence.form.entity.FormResult
import org.aing.danurirest.persistence.user.entity.User
import java.util.*

object FormResultFixture {
    fun createTestFormResult(
        result: String = """{"name":"홍길동","age":"30"}""",
        isSignUpResult: Boolean = true,
        user: User,
    ): FormResult {
        return FormResult(
            id = UUID.randomUUID(),
            result = result,
            user = user,
            isSignUpResult = isSignUpResult,
        )
    }
}