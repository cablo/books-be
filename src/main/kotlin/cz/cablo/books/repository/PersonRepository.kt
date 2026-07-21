package cz.cablo.books.repository

import cz.cablo.books.domain.Person
import io.micronaut.data.annotation.Join
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.r2dbc.annotation.R2dbcRepository
import io.micronaut.data.repository.kotlin.CoroutineCrudRepository
import kotlinx.coroutines.flow.Flow

@R2dbcRepository(dialect = Dialect.POSTGRES)
interface PersonRepository : CoroutineCrudRepository<Person, Long> {
    @Join(value = "books", type = Join.Type.LEFT_FETCH)
    override fun findAll(): Flow<Person>
}
