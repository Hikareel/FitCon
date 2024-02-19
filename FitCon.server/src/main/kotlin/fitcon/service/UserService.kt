package fitcon.service

import fitcon.dto.UserDto
import fitcon.entity.User
import java.util.Optional


interface UserService {
    fun saveUser(userDto: UserDto, role: Boolean)
    fun findUserByEmail(email: String): User?
    fun findAllUsers(): List<UserDto>
    fun findUserByName(name: String): User?
    fun changePassword(email: String, oldPassword: String, newPassword: String): Boolean
    fun findAllTrainers(): List<UserDto>
    fun findAllClients(): List<UserDto>
    fun findUserById(id: Long): User?
    fun findAllUsersByIds(userIds: Set<Long>): List<UserDto>
}