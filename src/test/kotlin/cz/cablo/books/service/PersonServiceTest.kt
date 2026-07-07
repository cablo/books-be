package cz.cablo.books.service

import  cz.cablo.books.api.CreateBookRequest
import  cz.cablo.books.api.CreatePersonRequest
import  cz.cablo.books.domain.Book
import  cz.cablo.books.domain.Person
import  cz.cablo.books.repository.PersonRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PersonServiceTest {

    // Vytvoření mocku pomocí MockK
    private val repository: PersonRepository = mockk()
    private val service = PersonService(repository)

    @Test
    fun `create maps request to PersonResponse with all fields`() {
        val saved = Person(id = 1L, firstName = "Jan", lastName = "Novak").also {
            it.addBook(Book(id = 10L, title = "Kotlin in Action", isbn = "978-1617293290"))
        }
        // Každé chování definujeme pomocí "every" a "returns"
        every { repository.save(any()) } returns saved

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
    fun `create with multiple books maps all books to response`() {
        val saved = Person(id = 1L, firstName = "Jan", lastName = "Novak").also {
            it.addBook(Book(id = 10L, title = "Book A", isbn = "ISBN-A"))
            it.addBook(Book(id = 11L, title = "Book B", isbn = "ISBN-B"))
        }
        every { repository.save(any()) } returns saved

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
    fun `create with no books returns response with empty books list`() {
        every { repository.save(any()) } returns Person(id = 2L, firstName = "Eva", lastName = "Novakova")

        val response = service.create(CreatePersonRequest(firstName = "Eva", lastName = "Novakova"))

        assertEquals(2L, response.id)
        assertTrue(response.books.isEmpty())
    }

    @Test
    fun `findAll returns mapped response for each person`() {
        every { repository.findAll() } returns listOf(
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
    fun `findAll returns empty list when no persons exist`() {
        every { repository.findAll() } returns emptyList()

        val result = service.findAll()

        assertTrue(result.isEmpty())
    }
}