package com.example.kotlinSpringApiStudy.blog.dto

import com.example.kotlinSpringApiStudy.core.annotation.ValidEnum
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

class BlogDto(
    @field:NotBlank(message = "query is required")
    val query: String?,

    @field:NotBlank(message = "sort is required")
    @field:ValidEnum(enumClass = EnumSort::class, message = "sort one of [ACCURACY, RECENCY")
    val sort: String?,

    @field:NotNull(message = "page is required")
    @field:Max(value = 50, message = "page must be less than 50")
    @field:Min(value = 1, message = "page must be greater than 0")
    val page: Int?,

    @field:NotNull(message = "size is required")
    val size: Int?,
) {
    private enum class EnumSort {
        ACCURACY, RECENCY
    }
}
