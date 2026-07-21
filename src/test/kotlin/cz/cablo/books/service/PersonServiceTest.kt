package cz.cablo.books.service

import cz.cablo.books.api.CreateBookRequest
import cz.cablo.books.api.CreatePersonRequest
import cz.cablo.books.domain.Book
import cz.cablo.books.domain.Person
import cz.cablo.books.repository.BookRepository
import cz.cablo.books.repository.PersonRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PersonServiceTest {

    private val personRepository: PersonRepository = mockk()
    private val bookRepository: BookRepository = mockk()
    private val service = PersonService(personRepository, bookRepository)

    @Test
    fun `create maps request to PersonResponse with all fields`() = runBlocking {
        val savedPerson = Person(id = 1L, firstName = "Jan", lastName = "Novak")
        val savedBook = Book(id = 10L, title = "Kotlin in Action", isbn = "978-1617293290", personId = 1L)

        coEvery { personRepository.save(any()) } returns savedPerson
        coEvery { bookRepository.save(any()) } returns savedBook

        val response = service.create(
            CreatePersonRequest(
                firstName = "Jan",
                lastName = "Novak",
                books = listOf(CreateBookRequest(title = "Kotlin in Action", isbn = "978-1617293290")),
            )
        )

        assertEquals(1L, response.id)
        assertEquals("Jan", response.firstName)
        assertEquals("Novak", response.lastName)
        assertEquals(1, response.books.size)
        with(response.books[0]) {
            assertEquals(10L, id)
            assertEquals("Kotlin in Action", title)
            assertEquals("978-1617293290", isbn)
        }
    }

    @Test
    fun `create with multiple books maps all books to response`() = runBlocking {
        val savedPerson = Person(id = 1L, firstName = "Jan", lastName = "Novak")
        coEvery { personRepository.save(any()) } returns savedPerson
        coEvery { bookRepository.save(match { it.title == "Book A" }) } returns
            Book(id = 10L, title = "Book A", isbn = "ISBN-A", personId = 1L)
        coEvery { bookRepository.save(match { it.title == "Book B" }) } returns
            Book(id = 11L, title = "Book B", isbn = "ISBN-B", personId = 1L)

        val response = service.create(
            CreatePersonRequest(
                firstName = "Jan",
                lastName = "Novak",
                books = listOf(
                    CreateBookRequest(title = "Book A", isbn = "ISBN-A"),
                    CreateBookRequest(title = "Book B", isbn = "ISBN-B"),
                ),
            )
        )

        assertEquals(2, response.books.size)
        assertEquals("Book A", response.books[0].title)
        assertEquals("Book B", response.books[1].title)
    }

    @Test
    fun `create with no books returns response with empty books list`() = runBlocking {
        coEvery { personRepository.save(any()) } returns Person(id = 2L, firstName = "Eva", lastName = "Novakova")

        val response = service.create(CreatePersonRequest(firstName = "Eva", lastName = "Novakova"))

        assertEquals(2L, response.id)
        assertTrue(response.books.isEmpty())
    }

    @Test
    fun `findAll returns mapped response for each person`() = runBlocking {
        every { personRepository.findAll() } returns flowOf(
            Person(id = 1L, firstName = "Jan", lastName = "Novak"),
            Person(id = 2L, firstName = "Eva", lastName = "Novakova"),
        )

        val result = service.findAll()

        assertEquals(2, result.size)
        assertEquals("Jan", result[0].firstName)
        assertEquals("Novak", result[0].lastName)
        assertEquals("Eva", result[1].firstName)
        assertEquals("Novakova", result[1].lastName)
    }

    @Test
    fun `findAll returns empty list when no persons exist`() = runBlocking {
        every { personRepository.findAll() } returns flowOf()

        val result = service.findAll()

        assertTrue(result.isEmpty())
    }
}
