package cz.cablo.books.service

import cz.cablo.books.api.BookResponse
import cz.cablo.books.api.CreatePersonRequest
import cz.cablo.books.api.PersonResponse
import cz.cablo.books.domain.Book
import cz.cablo.books.domain.Person
import cz.cablo.books.repository.BookRepository
import cz.cablo.books.repository.PersonRepository
import io.micronaut.transaction.annotation.Transactional
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

@Singleton
open class PersonService(
    private val personRepository: PersonRepository,
    private val bookRepository: BookRepository,
) {
    @Transactional
    open suspend fun create(request: CreatePersonRequest): PersonResponse {
        val person = personRepository.save(
            Person(
                firstName = request.firstName,
                lastName = request.lastName,
            )
        )
        val books = request.books.map { bookRequest ->
            bookRepository.save(
                Book(
                    title = bookRequest.title,
                    isbn = bookRequest.isbn,
                    personId = person.id,
                )
            )
        }
        return person.copy(books = books).toResponse()
    }

    @Transactional(readOnly = true)
    open suspend fun findAll(): List<PersonResponse> =
        personRepository.findAll().map { it.toResponse() }.toList()

    private fun Person.toResponse(): PersonResponse =
        PersonResponse(
            id = id,
            firstName = firstName,
            lastName = lastName,
            books = books.map { book ->
                BookResponse(
                    id = book.id,
                    title = book.title,
                    isbn = book.isbn,
                )
            },
        )
}