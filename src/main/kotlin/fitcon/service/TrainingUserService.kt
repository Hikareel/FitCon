package fitcon.service

interface TrainingUserService {
    fun saveAllUsersToTraining(trainingId: Long, userIds: List<Long>)
    fun findTrainingIdsByUserId(userId: Long): List<Long>
}