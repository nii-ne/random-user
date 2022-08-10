package com.example.randomuser.application.response

import com.example.randomuser.domain.model.response.UserInfo
import com.fasterxml.jackson.annotation.JsonInclude
import java.io.Serializable


@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserResponse(
    val name: String,
    val gender: String,
    val address: String,
    val seed: String? = null
) : Serializable {
    constructor(userInfo: UserInfo) : this(
        name = "${userInfo.results[0].name.title} ${userInfo.results[0].name.first} ${userInfo.results[0].name.last}",
        gender = userInfo.results[0].gender,
        address = "street ${userInfo.results[0].location.street.number} ${userInfo.results[0].location.street.name}, ${userInfo.results[0].location.city}, ${userInfo.results[0].location.state} ${userInfo.results[0].location.postcode} ${userInfo.results[0].location.country}",
        seed = userInfo.info.seed
    )
}
