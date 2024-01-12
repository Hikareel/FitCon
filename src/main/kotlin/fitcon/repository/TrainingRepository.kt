package fitcon.repository

import fitcon.entity.Training
import org.springframework.data.jpa.repository.JpaRepository

interface TrainingRepository : JpaRepository<Training?, Long?> {
    fun findAllByUserIdAndType(userId: Long, type: String): List<Training>
}