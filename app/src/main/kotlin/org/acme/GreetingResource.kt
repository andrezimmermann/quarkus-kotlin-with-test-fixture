package org.acme

import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.acme.dao.GreetingService

@Path("/hello")
class GreetingResource {

    @Inject
    lateinit var service: GreetingService

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun hello(): String {
        val greeting = service.fetchGreeting().greeting
        return "Hello from RESTEasy Reactive: $greeting"
    }
}
