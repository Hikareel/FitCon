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
): UserService {
    override fun saveUser(userDto: UserDto){
        val user = User()
        user.name = userDto.firstName + " " + userDto.lastName
        user.email = userDto.email
        user.password = passwordEncoder.encode(userDto.password)

        var role = roleRepository.findByName("CLIENT")
        if(role == null){
            role = checkRoleExist()
        }
        user.roles = listOf(role)
        userRepository.save(user)
    }
    override fun findUserByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    override fun findAllUsers(): List<UserDto>{
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
            if(!passwordEncoder.matches(oldPassword, user.password)){
                return false
            }
            user.password = passwordEncoder.encode(newPassword)
            userRepository.save(user)
            return true
        }
        return false
    }
    fun mapToUserDto(user: User): UserDto{
        val userDto = UserDto()
        val str = user.name!!.split(" ")
        userDto.firstName = str[0]
        userDto.lastName = str[1]
        userDto.email = user.email!!
        return userDto
    }
    fun checkRoleExist(): Role{
        val role = Role()
        role.name = "CLIENT"
        return roleRepository.save(role)
    }
}