package cz.cablo.books.controller

import cz.cablo.books.api.CreateBookRequest
import cz.cablo.books.api.CreatePersonRequest
import cz.cablo.books.api.PersonResponse
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.util.*

@MicronautTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@Disabled("testuje to bezici backend")
class PersonControllerTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    @Order(1)
    fun `GET persons returns seed data persons`() {
        val response = client.toBlocking().exchange(
            HttpRequest.GET<Any>("/persons"),
            Array<PersonResponse>::class.java,
        )

        val persons = response.body()!!
        assertTrue(persons.size >= 10, "Expected at least 10 seeded persons, got ${persons.size}")
        assertTrue(persons.all { it.books.isNotEmpty() }, "Every seeded person should have books")
    }

    @Test
    fun `GET persons returns 200 with non-empty list`() {
        val response = client.toBlocking().exchange(
            HttpRequest.GET<Any>("/persons"),
            Array<PersonResponse>::class.java,
        )

        assertEquals(HttpStatus.OK, response.status)
        val persons = response.body()!!
        assertTrue(persons.isNotEmpty())
        assertTrue(persons.all { it.id != null })
    }

    @Test
    fun `POST persons returns 201 with created person`() {
        val isbn = uniqueIsbn()
        val request = HttpRequest.POST(
            "/persons",
            CreatePersonRequest(
                firstName = "Karel",
                lastName = "Capek",
                books = listOf(CreateBookRequest(title = "R.U.R.", isbn = isbn)),
            ),
        )

        val response = client.toBlocking().exchange(request, PersonResponse::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        val body = response.body()!!
        assertNotNull(body.id)
        assertEquals("Karel", body.firstName)
        assertEquals("Capek", body.lastName)
        assertEquals(1, body.books.size)
        assertEquals("R.U.R.", body.books[0].title)
        assertEquals(isbn, body.books[0].isbn)
        assertNotNull(body.books[0].id)
    }

    @Test
    fun `POST persons without books creates person with empty books list`() {
        val request = HttpRequest.POST(
            "/persons",
            CreatePersonRequest(firstName = "Jan", lastName = "Novak"),
        )

        val response = client.toBlocking().exchange(request, PersonResponse::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        val body = response.body()!!
        assertNotNull(body.id)
        assertEquals("Jan", body.firstName)
        assertEquals("Novak", body.lastName)
        assertTrue(body.books.isEmpty())
    }

    @Test
    fun `POST persons with multiple books creates all books`() {
        val request = HttpRequest.POST(
            "/persons",
            CreatePersonRequest(
                firstName = "Franz",
                lastName = "Kafka",
                books = listOf(
                    CreateBookRequest(title = "Proces", isbn = uniqueIsbn()),
                    CreateBookRequest(title = "Zámek", isbn = uniqueIsbn()),
                ),
            ),
        )

        val response = client.toBlocking().exchange(request, PersonResponse::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        val body = response.body()!!
        assertEquals(2, body.books.size)
    }

    @Test
    fun `POST persons returns 500 for blank firstName`() {
        val request = HttpRequest.POST(
            "/persons",
            CreatePersonRequest(firstName = "", lastName = "Novak"),
        )

        val ex = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange(request, PersonResponse::class.java)
        }

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.status)
    }

    @Test
    fun `POST persons returns 500 for blank lastName`() {
        val request = HttpRequest.POST(
            "/persons",
            CreatePersonRequest(firstName = "Jan", lastName = ""),
        )

        val ex = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange(request, PersonResponse::class.java)
        }

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.status)
    }

    @Test
    fun `POST persons returns 500 for book with blank isbn`() {
        val request = HttpRequest.POST(
            "/persons",
            CreatePersonRequest(
                firstName = "Jan",
                lastName = "Novak",
                books = listOf(CreateBookRequest(title = "Nejaky Titul", isbn = "")),
            ),
        )

        val ex = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange(request, PersonResponse::class.java)
        }

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.status)
    }

    private fun uniqueIsbn(): String = UUID.randomUUID().toString().replace("-", "").take(20)
}