package org.aing.danurirest.domain.admin.dto

data class GetUsersAndFormHeaderList(
    val userList: List<UserResponse>,
    val headerList: List<String>,
)
