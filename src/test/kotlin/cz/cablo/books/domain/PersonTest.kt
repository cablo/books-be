package cz.cablo.books.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PersonTest {

    @Test
    fun `new person has empty books list`() {
        val person = Person(firstName = "Jan", lastName = "Novak")

        assertTrue(person.books.isEmpty())
    }

    @Test
    fun `addBook sets author on the book`() {
        val person = Person(firstName = "Jan", lastName = "Novak")
        val book = Book(title = "Kotlin in Action", isbn = "978-1617293290")

        person.addBook(book)

        assertSame(person, book.author)
    }

    @Test
    fun `addBook appends book to persons list`() {
        val person = Person(firstName = "Jan", lastName = "Novak")
        val book1 = Book(title = "Kotlin in Action", isbn = "978-1617293290")
        val book2 = Book(title = "Clean Code", isbn = "978-0132350884")

        person.addBook(book1)
        person.addBook(book2)

        assertEquals(2, person.books.size)
        assertTrue(person.books.containsAll(listOf(book1, book2)))
    }

    @Test
    fun `addBook keeps both sides of association in sync`() {
        val person = Person(firstName = "Jan", lastName = "Novak")
        val book = Book(title = "Kotlin in Action", isbn = "978-1617293290")

        person.addBook(book)

        assertSame(person, book.author)
        assertTrue(person.books.contains(book))
    }
}