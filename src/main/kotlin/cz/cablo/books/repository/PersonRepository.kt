package cz.cablo.books.repository

import cz.cablo.books.domain.Person
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface PersonRepository : JpaRepository<Person, Long>
