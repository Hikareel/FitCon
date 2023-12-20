package fitcon.controller

import com.fasterxml.jackson.databind.BeanDescription
import fitcon.dto.UserDto
import fitcon.dto.WorkoutDto
import fitcon.service.UserService
import fitcon.service.WorkoutService
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@Controller
class AuthController(
    private val userService: UserService,
    private val workoutService: WorkoutService
){
    @GetMapping("/home")
    fun home(): String{
        return "home"
    }
    @GetMapping("/about")
    fun about(model: Model): String{
        val trainers = userService.findAllTrainers()
        model.addAttribute("trainers", trainers)
        return "about"
    }
    @GetMapping("/schedule")
    fun schedule(): String{
        return "schedule"
    }
    @GetMapping("/faq")
    fun faq(): String{
        return "faq"
    }
    @GetMapping("/contact")
    fun contact(): String{
        return "contact"
    }
    @GetMapping("/login")
    fun login(): String{
        return "login"
    }
    @GetMapping("/register")
    fun showRegistrationForm(model: Model): String{
        val user = UserDto()
        model.addAttribute("user", user)
        return "register"
    }
    @PostMapping("/register/save")
    fun registration(
        @Valid @ModelAttribute("user") userDto: UserDto,
        @RequestParam(required = false) role: Boolean,
        result: BindingResult,
        model: Model
    ):String{
        val existingUser = userService.findUserByEmail(userDto.email!!)
        if(existingUser?.email != null && existingUser.email!!.isNotEmpty()){
            result.rejectValue("email", "",
                "There is already an account registered with the same email")
        }
        if(result.hasErrors()){
            model.addAttribute("user", userDto)
            return "/register"
        }
        userService.saveUser(userDto, role)
        return "redirect:/register?success"
    }
    @PreAuthorize("hasRole('CLIENT') or hasRole('TRAINER')")
    @GetMapping("/user")
    fun users(model: Model, @AuthenticationPrincipal userDetails: UserDetails): String{
        val user = userService.findUserByEmail(userDetails.username)
        model.addAttribute("user", user)
        return "userProfile/user"
    }
    @PreAuthorize("hasRole('CLIENT') or hasRole('TRAINER')")
    @GetMapping("/user/change-password")
    fun passwordChangeForm(): String{
        return "userProfile/passChange"
    }
    @PreAuthorize("hasRole('CLIENT') or hasRole('TRAINER')")
    @PostMapping("/user/change-password")
    fun passwordChange(
        @RequestParam oldPassword: String,
        @RequestParam newPassword: String,
        @RequestParam confirmPassword: String,
        @AuthenticationPrincipal userDetails: UserDetails,
        model: Model
    ): String{
        if(newPassword != confirmPassword){
            model.addAttribute("error", "New and confirmation passwords must be the same!")
            return "userProfile/passChange"
        }
        val userEmail = userDetails.username
        val passwordChange = userService.changePassword(userEmail, oldPassword, newPassword)
        return if(passwordChange){
            "redirect:/user"
        } else {
            model.addAttribute("error", "Old password incorrect")
            "userProfile/passChange"
        }
    }
    @PreAuthorize("hasRole('CLIENT') or hasRole('TRAINER')")
    @GetMapping("/user/pass")
    fun userPasses(model: Model, @AuthenticationPrincipal userDetails: UserDetails): String{
        return "userProfile/passes"
    }
    @PreAuthorize("hasRole('CLIENT') or hasRole('TRAINER')")
    @GetMapping("/user/add-workout")
    fun addWorkoutForm(model: Model): String{
        val workout = WorkoutDto()
        model.addAttribute("workout", workout)
        return "userProfile/addWorkout"
    }
    @PreAuthorize("hasRole('CLIENT') or hasRole('TRAINER')")
    @PostMapping("/user/add-workout/save")
    fun addWorkout(
        @Valid @ModelAttribute("workout") workoutDto: WorkoutDto,
        @AuthenticationPrincipal userDetails: UserDetails,
        model: Model,
        result: BindingResult
    ): String{
        val existingWorkout = workoutService.findWorkoutByName(workoutDto.name!!)
        if (existingWorkout != null ){
            result.rejectValue("name", "", "There is already workout with that name!")
        }
        if (result.hasErrors()){
            model.addAttribute("workout", workoutDto)
            return "userProfile/addWorkout"
        }
        workoutService.saveWorkout(workoutDto, userDetails.username)
        return "redirect:/user/workouts"
    }
    @PreAuthorize("hasRole('CLIENT') or hasRole('TRAINER')")
    @GetMapping("/user/workouts")
    fun userWorkouts(model: Model, @AuthenticationPrincipal userDetails: UserDetails): String{
        val userWorkouts = workoutService.findWorkoutsByUserEmail(userDetails.username)
        model.addAttribute("userWorkouts", userWorkouts)
        return "userProfile/workouts"
    }
    @GetMapping("/user/{id}")
    fun showTrainerProfile(
        @PathVariable id: Long,
        model: Model
    ): String{
        val trainer = userService.findUserById(id)
        model.addAttribute("trainer", trainer)
        return "userProfile/trainerCard"
    }
}