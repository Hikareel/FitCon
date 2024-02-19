package fitcon.controller

import fitcon.dto.TrainerToTrainingsDto
import fitcon.service.TrainingService
import fitcon.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
class ScheduleController(
    private val userService: UserService,
    private val trainingService: TrainingService
) {

    @GetMapping("/schedule")
    fun schedule(model: Model): String {
        val trainings = trainingService.findAllTrainings()
        val userIds = trainings.map { it.userId!! }.toSet()
        val users = userService.findAllUsersByIds(userIds)
        val userIdToUser = users.associateBy { it.id }
        val trainerToTrainings: List<TrainerToTrainingsDto> = trainings.groupBy { userIdToUser[it.userId] }
            .map {
                val dto = TrainerToTrainingsDto()
                dto.trainer = it.key
                dto.trainings = it.value
                dto
            }
            .toList()

        model.addAttribute("trainingsCollection", trainerToTrainings)
        return "schedule"
    }
}