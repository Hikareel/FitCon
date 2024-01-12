package fitcon.service.impl

import fitcon.dto.TrainingAddDto
import fitcon.dto.TrainingDto
import fitcon.entity.Training
import fitcon.repository.TrainingRepository
import fitcon.service.TrainingService
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class TrainingServiceImpl(
    val trainingRepository: TrainingRepository
) : TrainingService {
    override fun findAllTrainings(): List<TrainingDto> {
        return trainingRepository.findAll()
            .stream()
            .map {
                toTrainingDto(it!!)
            }
            .toList()
    }

    override fun findAllTrainings(userId: Long, type: String): List<TrainingDto> {
        return trainingRepository.findAllByUserIdAndType(userId, type)
            .stream()
            .map {
                toTrainingDto(it)
            }
            .toList()
    }

    override fun saveTraining(training: TrainingAddDto, type: String, userId: Long) {
        val trainingEntity = Training()
        trainingEntity.startAt = training.startDate
        trainingEntity.endAt = training.endDate
        trainingEntity.type = type
        trainingEntity.description = training.description
        trainingEntity.userId = userId
        trainingRepository.save(trainingEntity)
    }

    private fun formatDate(date: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return date.format(formatter)
    }

    private fun toTrainingDto(training: Training): TrainingDto {
        val trainingDto = TrainingDto()
        trainingDto.startDate = formatDate(training.startAt!!)
        trainingDto.endDate = formatDate(training.endAt!!)
        trainingDto.description = training.description
        trainingDto.type = training.type
        trainingDto.userId = training.userId
        return trainingDto
    }
}