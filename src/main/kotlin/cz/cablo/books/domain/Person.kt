package cz.cablo.books.domain

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.Relation

@MappedEntity("person")
data class Person(
    @field:Id
    @field:GeneratedValue(GeneratedValue.Type.IDENTITY)
    val id: Long? = null,
    val firstName: String,
    val lastName: String,
    @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "personId")
    val books: List<Book> = emptyList(),
)