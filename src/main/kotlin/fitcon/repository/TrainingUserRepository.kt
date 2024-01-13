package fitcon.repository

import fitcon.entity.TrainingUser
import org.springframework.data.jpa.repository.JpaRepository

interface TrainingUserRepository : JpaRepository<TrainingUser?, Long?> {
    fun deleteAllByTrainingId(trainingId: Long)
    fun findAllByTrainingId(trainingId: Long): List<TrainingUser>
    fun findAllByUserId(userId: Long): List<TrainingUser>
}