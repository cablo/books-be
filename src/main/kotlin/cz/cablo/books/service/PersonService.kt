package cz.cablo.books.service

import cz.cablo.books.api.BookResponse
import cz.cablo.books.api.CreatePersonRequest
import cz.cablo.books.api.PersonResponse
import cz.cablo.books.domain.Book
import cz.cablo.books.domain.Person
import cz.cablo.books.repository.PersonRepository
import io.micronaut.transaction.annotation.Transactional
import jakarta.inject.Singleton

@Singleton
open class PersonService(
    private val personRepository: PersonRepository,
) {
    @Transactional
    open fun create(request: CreatePersonRequest): PersonResponse {
        val person = Person(
            firstName = request.firstName,
            lastName = request.lastName,
        )

        request.books.forEach { bookRequest ->
            person.addBook(
                Book(
                    title = bookRequest.title,
                    isbn = bookRequest.isbn,
                ),
            )
        }

        return personRepository.save(person).toResponse()
    }

    @Transactional(readOnly = true)
    open fun findAll(): List<PersonResponse> = personRepository.findAll().map { it.toResponse() }

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
