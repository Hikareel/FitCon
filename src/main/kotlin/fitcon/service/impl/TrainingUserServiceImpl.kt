package fitcon.service.impl

import fitcon.entity.TrainingUser
import fitcon.repository.TrainingUserRepository
import fitcon.service.TrainingUserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TrainingUserServiceImpl(val trainingUserRepository: TrainingUserRepository) : TrainingUserService {
    @Transactional
    override fun saveAllUsersToTraining(trainingId: Long, userIds: List<Long>) {
        trainingUserRepository.deleteAllByTrainingId(trainingId)
        userIds.forEach {
            val entity = TrainingUser()
            entity.trainingId = trainingId
            entity.userId = it
            trainingUserRepository.save(entity)
        }
    }

    override fun findTrainingIdsByUserId(userId: Long): List<Long> {
        return trainingUserRepository.findAllByUserId(userId)
            .stream()
            .map {
                it!!.trainingId!!
            }
            .toList()
    }

}