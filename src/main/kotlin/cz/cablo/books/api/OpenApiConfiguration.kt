package cz.cablo.books.api

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License
import io.swagger.v3.oas.annotations.servers.Server

@OpenAPIDefinition(
    info = Info(
        title = "Books App API",
        version = "1.0.0",
        description = "REST API for managing persons and their books.",
        contact = Contact(name = "API Support"),
        license = License(name = "Proprietary"),
    ),
    servers = [
        Server(url = "http://localhost:8080", description = "Local development"),
    ],
)
class OpenApiConfiguration
