package cz.cablo.books.repository

import cz.cablo.books.domain.Book
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.r2dbc.annotation.R2dbcRepository
import io.micronaut.data.repository.kotlin.CoroutineCrudRepository

@R2dbcRepository(dialect = Dialect.POSTGRES)
interface BookRepository : CoroutineCrudRepository<Book, Long>
