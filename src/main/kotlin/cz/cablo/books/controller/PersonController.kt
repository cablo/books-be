package cz.cablo.books.controller

import cz.cablo.books.api.CreatePersonRequest
import cz.cablo.books.api.PersonResponse
import cz.cablo.books.service.PersonService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid

@Tag(name = "Persons")
@Controller("/persons")
open class PersonController(
    private val personService: PersonService,
) {
    @Get
    @Operation(summary = "List all persons")
    @ApiResponse(responseCode = "200", description = "Persons returned successfully")
    fun list(): List<PersonResponse> = personService.findAll()

    @Post
    @Operation(summary = "Create a person with optional books")
    @ApiResponse(responseCode = "201", description = "Person created successfully")
    open fun create(@Body @Valid request: CreatePersonRequest): HttpResponse<PersonResponse> =
        HttpResponse.created(personService.create(request))
}
