package cz.cablo.books.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PersonTest {

    @Test
    fun `new person has empty books list`() {
        val person = Person(firstName = "Jan", lastName = "Novak")

        assertTrue(person.books.isEmpty())
    }

    @Test
    fun `person id is null by default`() {
        val person = Person(firstName = "Jan", lastName = "Novak")

        assertNull(person.id)
    }

    @Test
    fun `person copy with books holds all books`() {
        val book1 = Book(id = 1L, title = "Kotlin in Action", isbn = "978-1617293290", personId = 1L)
        val book2 = Book(id = 2L, title = "Clean Code", isbn = "978-0132350884", personId = 1L)
        val person = Person(id = 1L, firstName = "Jan", lastName = "Novak").copy(books = listOf(book1, book2))

        assertEquals(2, person.books.size)
        assertTrue(person.books.containsAll(listOf(book1, book2)))
    }

    @Test
    fun `book personId links book to person`() {
        val book = Book(title = "Kotlin in Action", isbn = "978-1617293290", personId = 42L)

        assertEquals(42L, book.personId)
    }
}
