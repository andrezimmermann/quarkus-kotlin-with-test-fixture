package org.acme.dao

import jakarta.enterprise.context.ApplicationScoped
import org.acme.model.GreetingEntity

@ApplicationScoped
class GreetingDao {

    fun getGreeting(): GreetingEntity =
        GreetingEntity("Hello!")
}
