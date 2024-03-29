package fitcon.service.impl

import fitcon.dto.UserDto
import fitcon.entity.Role
import fitcon.entity.User
import fitcon.repository.RoleRepository
import fitcon.repository.UserRepository
import fitcon.service.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class UserServiceImpl(
    val userRepository: UserRepository,
    val roleRepository: RoleRepository,
    val passwordEncoder: PasswordEncoder
) : UserService {
    override fun saveUser(userDto: UserDto, role: Boolean) {
        val user = User()
        user.name = userDto.firstName + " " + userDto.lastName
        user.email = userDto.email
        user.password = passwordEncoder.encode(userDto.password)
        user.phoneNumber = userDto.phoneNumber
        val roleName = if (role) "TRAINER" else "CLIENT"

        var role = roleRepository.findByName(roleName)
        if (role == null) {
            role = createNewRole(roleName)
        }
        user.role = role
        userRepository.save(user)
    }

    override fun findUserByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    override fun findAllUsers(): List<UserDto> {
        val users = userRepository.findAll()
        return users
            .stream()
            .map { user -> mapToUserDto(user!!) }
            .collect(Collectors.toList())
    }

    override fun findUserByName(name: String): User? {
        return userRepository.findByName(name)
    }

    override fun changePassword(email: String, oldPassword: String, newPassword: String): Boolean {
        val user = userRepository.findByEmail(email)
        if (user != null) {
            if (!passwordEncoder.matches(oldPassword, user.password)) {
                return false
            }
            user.password = passwordEncoder.encode(newPassword)
            userRepository.save(user)
            return true
        }
        return false
    }

    override fun findAllTrainers(): List<UserDto> {
        val role = roleRepository.findByName("TRAINER")
        return userRepository.findAllByRole(role!!)
            .stream()
            .map { trainer -> mapToUserDto(trainer) }
            .collect(Collectors.toList())
    }

    override fun findAllClients(): List<UserDto> {
        val role = roleRepository.findByName("CLIENT")
        return userRepository.findAllByRole(role!!)
            .stream()
            .map { client -> mapToUserDto(client) }
            .collect(Collectors.toList())
    }

    override fun findUserById(id: Long): User? {
        return userRepository.findById(id).get()
    }

    override fun findAllUsersByIds(userIds: Set<Long>): List<UserDto> {
        return userRepository.findAllById(userIds)
            .stream()
            .map { user -> mapToUserDto(user!!) }
            .collect(Collectors.toList())
    }

    fun mapToUserDto(user: User): UserDto {
        val userDto = UserDto()
        val str = user.name!!.split(" ")
        userDto.id = user.id
        userDto.firstName = str[0]
        userDto.lastName = str[1]
        userDto.email = user.email!!
        userDto.phoneNumber = user.phoneNumber
        return userDto
    }

    fun createNewRole(roleName: String): Role {
        val role = Role()
        role.name = roleName
        return roleRepository.save(role)
    }
}