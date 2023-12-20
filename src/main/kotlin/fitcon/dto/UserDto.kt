package fitcon.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

class UserDto {
    var id: Long? = null

    @NotEmpty(message = "Name cannot be empty")
    var firstName: String? = null

    @NotEmpty(message = "Surname cannot be empty")
    var lastName: String? = null

    @NotEmpty(message = "Email should not be empty")
    @Email
    var email: String? = null

    @NotEmpty(message = "Password should not be empty")
    var password: String? = null
}