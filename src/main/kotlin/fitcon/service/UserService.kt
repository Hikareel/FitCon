package fitcon.service

import fitcon.dto.UserDto
import fitcon.entity.User


interface UserService {
    fun saveUser(userDto: UserDto)
    fun findUserByEmail(email: String): User?
    fun findAllUsers(): List<UserDto>
    fun findUserByName(name: String): User?
    fun changePassword(email: String, oldPassword: String, newPassword: String): Boolean
}