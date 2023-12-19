package fitcon.repository

import fitcon.entity.User
import fitcon.entity.Workout
import org.springframework.data.jpa.repository.JpaRepository

interface WorkoutRepository: JpaRepository<Workout?, Long?> {
    fun findByName(name: String?): Workout?
    fun findByCreatedBy(user: User): List<Workout>
}