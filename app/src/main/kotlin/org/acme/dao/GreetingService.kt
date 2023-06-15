package org.acme.dao

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.acme.model.GreetingEntity

@ApplicationScoped
class GreetingService {

    @Inject
    lateinit var greetingDao: GreetingDao

    fun fetchGreeting(): GreetingEntity = greetingDao.getGreeting()
}
