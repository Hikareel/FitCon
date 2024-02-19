package fitcon.repository

import fitcon.entity.Training
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface TrainingRepository : JpaRepository<Training?, Long?> {
    fun findAllByUserIdAndType(userId: Long, type: String): List<Training>
    fun findAllByIdInAndType(trainingIds: List<Long>, type: String): List<Training>

    @Query("update Training t set t.synchronized = :synchronized where t.id = :id")
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    fun updateSynchronizedById(id: Long, synchronized: Boolean)
}