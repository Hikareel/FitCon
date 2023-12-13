package fitcon.repository

import fitcon.entity.Role
import org.springframework.data.jpa.repository.JpaRepository


interface RoleRepository : JpaRepository<Role?, Long?> {
    fun findByName(name: String?): Role?
}