package fitcon.controller

import fitcon.dto.TrainingAddDto
import fitcon.service.TrainingService
import fitcon.service.UserService
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@Controller
class TrainingController(
    private val userService: UserService,
    private val trainingService: TrainingService
) {

    @PreAuthorize("hasRole('TRAINER')")
    @GetMapping("/user/group-trainings")
    fun groupTrainings(model: Model, @AuthenticationPrincipal userDetails: UserDetails): String {
        val user = userService.findUserByEmail(userDetails.username)
        model.addAttribute("trainingType", "GROUP")
        model.addAttribute("trainings", trainingService.findAllTrainings(user?.id!!, "GROUP"))
        return "userProfile/trainings"
    }

    @PreAuthorize("hasRole('TRAINER')")
    @GetMapping("/user/personal-trainings")
    fun personalTrainings(model: Model, @AuthenticationPrincipal userDetails: UserDetails): String {
        val user = userService.findUserByEmail(userDetails.username)
        model.addAttribute("trainingType", "PERSONAL")
        model.addAttribute("trainings", trainingService.findAllTrainings(user?.id!!, "PERSONAL"))
        return "userProfile/trainings"
    }

    @PreAuthorize("hasRole('TRAINER')")
    @GetMapping("/user/add-training")
    fun addTrainingForm(model: Model, @RequestParam("type") trainingType: String?): String {
        val training = TrainingAddDto()
        model.addAttribute("trainingType", trainingType)
        model.addAttribute("training", training)
        return "userProfile/addTraining"
    }

    @PreAuthorize("hasRole('TRAINER')")
    @PostMapping("/user/add-training/save")
    fun addTraining(
        @Valid @ModelAttribute("training") trainingDto: TrainingAddDto,
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestParam("type") trainingType: String?,
        model: Model,
        result: BindingResult
    ): String {
        val user = userService.findUserByEmail(userDetails.username)
        trainingService.saveTraining(trainingDto, trainingType!!, user?.id!!)
        model.addAttribute("trainingType", trainingType)
        model.addAttribute("trainings", trainingService.findAllTrainings(user.id, trainingType))
        return "userProfile/trainings"
    }
}