package fitcon.service.impl

import fitcon.dto.TrainingAddDto
import fitcon.dto.TrainingDto
import fitcon.entity.Training
import fitcon.repository.TrainingRepository
import fitcon.repository.TrainingUserRepository
import fitcon.repository.UserRepository
import fitcon.service.TrainingService
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class TrainingServiceImpl(
    val trainingRepository: TrainingRepository,
    val trainingUserRepository: TrainingUserRepository,
    val userRepository: UserRepository
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
                addAssignedUsers(toTrainingDto(it))
            }
            .toList()
    }

    override fun saveTraining(training: TrainingAddDto, type: String, userId: Long) {
        val trainingEntity = Training()
        trainingEntity.name = training.name
        trainingEntity.startAt = training.startDate
        trainingEntity.endAt = training.endDate
        trainingEntity.type = type
        trainingEntity.description = training.description
        trainingEntity.userId = userId
        trainingRepository.save(trainingEntity)
    }

    override fun findTrainingById(trainingId: Long): TrainingDto? {
        val training = trainingRepository.findById(trainingId)
        return if (training.isPresent) {
            toTrainingDto(training.get())
        } else {
            null
        }
    }

    override fun findTrainingByIdsAndType(ids: List<Long>, trainingType: String): List<TrainingDto> {
        return trainingRepository.findAllByIdInAndType(ids, trainingType)
            .stream()
            .map {
                toTrainingDto(it)
            }
            .toList()
    }

    private fun formatDate(date: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return date.format(formatter)
    }

    private fun toTrainingDto(training: Training): TrainingDto {
        val trainingDto = TrainingDto()
        trainingDto.id = training.id
        trainingDto.name = training.name!!
        trainingDto.startDate = formatDate(training.startAt!!)
        trainingDto.endDate = formatDate(training.endAt!!)
        trainingDto.description = training.description
        trainingDto.type = training.type
        trainingDto.userId = training.userId
        return trainingDto
    }

    fun addAssignedUsers(dto: TrainingDto): TrainingDto {
        val assignedUserIds = trainingUserRepository.findAllByTrainingId(dto.id!!)
            .stream()
            .map {
                it!!.userId
            }
            .toList()
        val assignedUsersNames = userRepository.findAllById(assignedUserIds)
            .stream()
            .map {
                it!!.name
            }
            .toList()

        val string = assignedUsersNames.joinToString(", ")
        dto.assignedUsers = string
        return dto
    }
}