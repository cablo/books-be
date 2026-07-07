package cz.cablo.books.api

import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank

@Serdeable
data class CreateBookRequest(
    @field:NotBlank val title: String,
    @field:NotBlank val isbn: String,
)

@Serdeable
data class CreatePersonRequest(
    @field:NotBlank val firstName: String,
    @field:NotBlank val lastName: String,
    @field:Valid val books: List<CreateBookRequest> = emptyList(),
)

@Serdeable
data class BookResponse(
    val id: Long?,
    val title: String,
    val isbn: String,
)

@Serdeable
data class PersonResponse(
    val id: Long?,
    val firstName: String,
    val lastName: String,
    val books: List<BookResponse>,
)
