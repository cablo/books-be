package cz.cablo.books.repository

import cz.cablo.books.domain.Book
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface BookRepository : JpaRepository<Book, Long>
