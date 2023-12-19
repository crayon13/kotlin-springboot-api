package com.example.kotlinSpringApiStudy.blog.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class Wordcount(
    @Id
    val word: String,
    val cnt: Int
)