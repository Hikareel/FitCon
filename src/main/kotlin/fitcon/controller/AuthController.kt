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

@Controller
class AuthController(
    private val userService: UserService,
){
    @GetMapping("/index")
    fun home(): String{
        return "index"
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
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user")
    fun users(model: Model, @AuthenticationPrincipal userDetails: UserDetails): String{
        val user = userService.findUserByEmail(userDetails.username)
        model.addAttribute("user", user)
        return "user"
    }
}