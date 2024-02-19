package fitcon.repository

import fitcon.entity.Role
import fitcon.entity.User
import org.springframework.data.jpa.repository.JpaRepository


interface UserRepository : JpaRepository<User?, Long?> {
    fun findByEmail(email: String?): User?
    fun findByName(name: String?): User?
    fun findAllByRole(role: Role): List<User>
}