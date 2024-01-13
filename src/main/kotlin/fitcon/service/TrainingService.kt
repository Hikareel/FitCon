package fitcon.service

import fitcon.dto.TrainingAddDto
import fitcon.dto.TrainingDto

interface TrainingService {
    fun findAllTrainings(): List<TrainingDto>
    fun findAllTrainings(userId: Long, type: String): List<TrainingDto>
    fun saveTraining(training: TrainingAddDto, type: String, userId: Long)
    fun findTrainingById(trainingId: Long): TrainingDto?
    fun findTrainingByIdsAndType(ids: List<Long>, trainingType: String): List<TrainingDto>
}