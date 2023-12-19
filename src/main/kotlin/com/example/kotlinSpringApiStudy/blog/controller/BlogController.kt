package com.example.kotlinSpringApiStudy.blog.controller

import com.example.kotlinSpringApiStudy.blog.dto.BlogDto
import com.example.kotlinSpringApiStudy.blog.service.BlogService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/blog")
@RestController
class BlogController(val blogService: BlogService) {
    @RequestMapping("")
    fun search(@RequestBody @Valid blogDto: BlogDto): String? {
        val result = blogService.searchKakao(blogDto)
        return result
    }
}