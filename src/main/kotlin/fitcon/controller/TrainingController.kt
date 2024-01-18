package fitcon.controller

import fitcon.dto.AddedUsersDto
import fitcon.dto.TrainingAddDto
import fitcon.service.GoogleCalendarService
import fitcon.service.TrainingService
import fitcon.service.TrainingUserService
import fitcon.service.UserService
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Controller
class TrainingController(
    private val userService: UserService,
    private val trainingService: TrainingService,
    private val trainingUserService: TrainingUserService,
    private val googleCalendarService: GoogleCalendarService
) {

    @PreAuthorize("hasRole('TRAINER') or hasRole('CLIENT')")
    @GetMapping("/user/group-trainings")
    fun groupTrainings(model: Model, @AuthenticationPrincipal userDetails: UserDetails): String {
        val user = userService.findUserByEmail(userDetails.username)
        return if (userDetails.authorities.toString().contains("TRAINER")) {
            returnTrainingsForTrainer(model, user?.id!!, "GROUP")
        } else returnTrainingsForClient(model, user?.id!!, "GROUP")
    }

    @PreAuthorize("hasRole('TRAINER') or hasRole('CLIENT')")
    @GetMapping("/user/personal-trainings")
    fun personalTrainings(model: Model, @AuthenticationPrincipal userDetails: UserDetails): String {
        val user = userService.findUserByEmail(userDetails.username)
        return if (userDetails.authorities.toString().contains("TRAINER")) {
            returnTrainingsForTrainer(model, user?.id!!, "PERSONAL")
        } else returnTrainingsForClient(model, user?.id!!, "PERSONAL")
    }

    @PreAuthorize("hasRole('TRAINER')")
    @GetMapping("/user/add-training")
    fun addTrainingForm(model: Model, @RequestParam("type") trainingType: String?): String {
        val trainingAdd = TrainingAddDto()
        model.addAttribute("trainingType", trainingType)
        model.addAttribute("trainingAdd", trainingAdd)
        return "userProfile/addTraining"
    }

    @PreAuthorize("hasRole('TRAINER')")
    @GetMapping("/user/add-users-to-training")
    fun addUsersToTraining(model: Model, @RequestParam("id") trainingId: Long?): String {
        model.addAttribute("training", trainingService.findTrainingById(trainingId!!))
        model.addAttribute("users", userService.findAllClients())
        model.addAttribute("selectedUsers", AddedUsersDto())
        return "userProfile/addUsersToTraining"
    }

    @PreAuthorize("hasRole('TRAINER')")
    @PostMapping("/user/add-users-to-training/save")
    fun addUsersToTraining(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestParam("type") trainingType: String?,
        @RequestParam("id") trainingId: Long?,
        @ModelAttribute addedUsersDto: AddedUsersDto,
        model: Model
    ): String {
        val user = userService.findUserByEmail(userDetails.username)
        trainingUserService.saveAllUsersToTraining(trainingId!!, addedUsersDto.ids!!)
        return returnTrainingsForTrainer(model, user?.id!!, trainingType!!)
    }

    @PreAuthorize("hasRole('TRAINER')")
    @PostMapping("/user/add-training/save")
    fun addTraining(
        @Valid @ModelAttribute("trainingAdd") trainingDto: TrainingAddDto,
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestParam("type") trainingType: String?,
        model: Model,
        result: BindingResult
    ): String {
        if (trainingDto.startDate!! > trainingDto.endDate){
            result.rejectValue("startDate", "",
                "End date must occure after start date!")
            model.addAttribute("trainingType", trainingType)
            model.addAttribute("trainingAdd", trainingDto)
            return "userProfile/addTraining"
        }
        val user = userService.findUserByEmail(userDetails.username)
        trainingService.saveTraining(trainingDto, trainingType!!, user?.id!!)
        googleCalendarService.addEventToGoogleCalendar(
            trainingDto.name!!,
            trainingDto.startDate!!,
            trainingDto.endDate!!,
            trainingDto.description!!
        )
        return returnTrainingsForTrainer(model, user.id, trainingType)
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/synchronize-training")
    fun synchronizeTraining(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestParam("id") id: Long?,
        @RequestParam("type") trainingType: String?,
        model: Model
    ): String {
        val user = userService.findUserByEmail(userDetails.username)

        if (googleCalendarService.credential == null) {
            return returnTrainingsForClient(model, user?.id!!, trainingType!!)
        }

        val training = trainingService.findTrainingById(id!!)
        googleCalendarService.addEventToGoogleCalendar(
            training?.name!!,
            LocalDateTime.parse(training.startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            LocalDateTime.parse(training.endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            training.description!!
        )
        trainingService.setSynchronized(id)
        return returnTrainingsForClient(model, user?.id!!, trainingType!!)
    }

    private fun returnTrainingsForTrainer(model: Model, userId: Long, trainingType: String): String {
        model.addAttribute("trainingType", trainingType)
        model.addAttribute("trainings", trainingService.findAllTrainings(userId, trainingType))
        model.addAttribute("isTrainer", true)
        model.addAttribute("connectedToGoogleCalendar", googleCalendarService.credential != null)
        return "userProfile/trainings"
    }

    private fun returnTrainingsForClient(model: Model, userId: Long, trainingType: String): String {
        model.addAttribute("trainingType", trainingType)
        val trainingIds = trainingUserService.findTrainingIdsByUserId(userId)
        model.addAttribute("trainings", trainingService.findTrainingByIdsAndType(trainingIds, trainingType))
        model.addAttribute("isTrainer", false)
        model.addAttribute("connectedToGoogleCalendar", googleCalendarService.credential != null)
        return "userProfile/trainings"
    }
}