package com.example.kotlinSpringApiStudy.blog.service

import com.example.kotlinSpringApiStudy.blog.dto.BlogDto
import com.example.kotlinSpringApiStudy.core.exception.InvalidInputException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Service
class BlogService(
    @Value("\${restapi.kakao.baseUrl}")
    var baseUrl: String,
    @Value("\${restapi.kakao.key}")
    var key: String,
) {
    fun searchKakao(blogDto: BlogDto): String? {
        val messageList = mutableListOf<ExceptionMessage>()

        if (blogDto.query?.trim()?.isEmpty() ?: false) {
            messageList.add(ExceptionMessage.EMPTY_QUERY)
        }

        if (blogDto.sort?.trim() !in arrayOf("accuracy", "recency")) {
            messageList.add(ExceptionMessage.NOT_IN_SORT)
        }

        when {
            blogDto.page!! < 1 -> messageList.add(ExceptionMessage.LESS_THEN_MIN_PAGE)
            blogDto.page!! > 50 -> messageList.add(ExceptionMessage.MORE_THEN_MAX_PAGE)
        }

        if (messageList.isNotEmpty()) {
            throw InvalidInputException(messageList.joinToString(", ") { it.message })
        }

        println(">> baseUrl: $baseUrl")
        println(">> key: $key")

        val webClient = WebClient.builder()
            .baseUrl("https://dapi.kakao.com")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()

        val response = webClient.get()
            .uri {
                it.path("/v2/search/blog")
                    .queryParam("query", blogDto.query)
                    .queryParam("sort", blogDto.sort)
                    .queryParam("page", blogDto.page)
                    .queryParam("size", blogDto.size)
                    .build()
            }
            .header(HttpHeaders.AUTHORIZATION, "KakaoAK 55141a520bff563c05dbe3b445d36f04")
            .retrieve()
            .bodyToMono<String>()

        val result = response.block()
        println(result)
        return result
    }
}

private enum class ExceptionMessage(val message: String) {
    EMPTY_QUERY("query가 비어있습니다."),
    NOT_IN_SORT("sort가 accuracy, recency가 아닙니다."),
    LESS_THEN_MIN_PAGE("page는 1보다 작을 수 없습니다."),
    MORE_THEN_MAX_PAGE("page는 50보다 클 수 없습니다."),
    INVALID_INPUT("잘못된 입력입니다."),
}
