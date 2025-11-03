package org.aing.danurirest.domain.admin.dto

import org.springframework.data.domain.Page

data class GetUsersAndFormHeaderList(
    val userList: Page<UserResponse>,
    val headerList: List<String>,
)
