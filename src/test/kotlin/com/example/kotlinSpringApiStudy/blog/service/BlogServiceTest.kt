package com.example.kotlinSpringApiStudy.blog.service

import com.example.kotlinSpringApiStudy.blog.dto.BlogDto
import com.example.kotlinSpringApiStudy.core.exception.InvalidInputException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [BlogService::class])
class BlogServiceTest(
    private val blogService: BlogService,
) : ShouldSpec({
    context("BlogService 테스트") {
        should("카카오 블로그 검색 API가 잘 호출되는지 테스트") {
            // given
            val blogDto = BlogDto(
                query = "코틀린",
                sort = "accuracy",
                page = 1,
                size = 10,
            )

            // when
            val result = blogService.searchKakao(blogDto)

            // then
            result shouldNotBe null
        }
        should("query가 없을 경우 Exception이 발생하는지 테스트") {
            // given
            val blogDto = BlogDto(
                query = "",
                sort = "accuracy",
                page = 1,
                size = 10,
            )

            // when
            shouldThrowExactly<InvalidInputException> {
                blogService.searchKakao(blogDto)
            }.message shouldBe "query가 비어있습니다."
        }
        should("sort가 accuracy, recency가 아닐 경우 Exception이 발생하는지 테스트") {
            // given
            val blogDto = BlogDto(
                query = "코틀린",
                sort = "test",
                page = 1,
                size = 10,
            )

            // when
            shouldThrowExactly<InvalidInputException> {
                blogService.searchKakao(blogDto)
            }.message shouldBe "sort가 accuracy, recency가 아닙니다."
        }
        should("page가 1보다 작을 경우 Exception이 발생하는지 테스트") {
            // given
            val blogDto = BlogDto(
                query = "코틀린",
                sort = "accuracy",
                page = 0,
                size = 10,
            )

            // when
            shouldThrowExactly<InvalidInputException> {
                blogService.searchKakao(blogDto)
            }.message shouldBe "page는 1보다 작을 수 없습니다."
        }
        should("page가 50보다 클 경우 Exception이 발생하는지 테스트") {
            // given
            val blogDto = BlogDto(
                query = "코틀린",
                sort = "accuracy",
                page = 51,
                size = 10,
            )

            // when
            shouldThrowExactly<InvalidInputException> {
                blogService.searchKakao(blogDto)
            }.message shouldBe "page는 50보다 클 수 없습니다."
        }
    }
})
