package com.erdemserhat.authenticationguide.repository

import com.erdemserhat.authenticationguide.data.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface UserRepository : CrudRepository<User?, Int?> {
    fun findByEmail(email: String?): Optional<User?>?
}