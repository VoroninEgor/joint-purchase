package uoykaii.ru.jointpurchase.repository

import org.springframework.data.jpa.repository.JpaRepository
import uoykaii.ru.jointpurchase.entity.User
import java.util.UUID

interface UserRepository: JpaRepository<User, UUID> {
    fun findByEmail(email: String): User?
}