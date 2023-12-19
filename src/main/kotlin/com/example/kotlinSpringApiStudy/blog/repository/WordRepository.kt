package com.example.kotlinSpringApiStudy.blog.repository

import com.example.kotlinSpringApiStudy.blog.entity.Wordcount
import org.springframework.data.repository.CrudRepository

interface WordRepository : CrudRepository<Wordcount, String>