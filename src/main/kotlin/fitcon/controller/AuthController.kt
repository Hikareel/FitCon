package fitcon.controller

import fitcon.dto.UserDto
import fitcon.service.UserService
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class AuthController(
    private val userService: UserService,
){
    @GetMapping("/home")
    fun home(): String{
        return "home"
    }
    @GetMapping("/about")
    fun about(): String{
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
        userService.saveUser(userDto)
        return "redirect:/register?success"
    }
    @PreAuthorize("hasRole('CLIENT') or hasRole('TREINER')")
    @GetMapping("/user")
    fun users(model: Model, @AuthenticationPrincipal userDetails: UserDetails): String{
        val user = userService.findUserByEmail(userDetails.username)
        model.addAttribute("user", user)
        return "userProfile/user"
    }
    @PreAuthorize("hasRole('CLIENT') or hasRole('TREINER')")
    @GetMapping("/user/change-password")
    fun passwordChangeForm(): String{
        return "userProfile/passChange"
    }
    @PreAuthorize("hasRole('CLIENT') or hasRole('TREINER')")
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
    @GetMapping("/user/workouts")
    fun userWorkouts(model: Model, @AuthenticationPrincipal userDetails: UserDetails): String{
        return "userProfile/workouts"
    }
}