package com.example.randomuser.domain.model.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserInfo(
    val info: Info,
    val results: List<Result>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Result(
    val cell: String,
    val dob: Dob,
    val email: String,
    val gender: String,
    val id: Id,
    val location: Location,
    val login: Login,
    val name: Name,
    val nat: String,
    val phone: String,
    val picture: Picture,
    val registered: Registered
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Dob(
    val age: Int,
    val date: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Location(
    val city: String,
    val coordinates: Coordinates,
    val country: String,
    val postcode: String,
    val state: String,
    val street: Street,
    val timezone: Timezone
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Coordinates(
    val latitude: String,
    val longitude: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Id(
    val name: String,
    val value: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Info(
    val page: Int,
    val results: Int,
    val seed: String,
    val version: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Login(
    val md5: String,
    val password: String,
    val salt: String,
    val sha1: String,
    val sha256: String,
    val username: String,
    val uuid: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Name(
    val first: String,
    val last: String,
    val title: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Picture(
    val large: String,
    val medium: String,
    val thumbnail: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Registered(
    val age: Int,
    val date: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Street(
    val name: String,
    val number: Int
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Timezone(
    val description: String,
    val offset: String
)