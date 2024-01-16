package fitcon.security

import fitcon.entity.Role
import fitcon.repository.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*
import java.util.stream.Collectors


@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
): UserDetailsService {
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String): UserDetails{
        val user = userRepository.findByEmail(email)
        return if (user != null) {
            org.springframework.security.core.userdetails.User(
                user.email,
                user.password,
                mapRoleToAuthorities(user.role!!)
            )
        } else {
            throw UsernameNotFoundException("Invalid username or password.")
        }
    }
    private fun mapRoleToAuthorities(role: Role): Collection<GrantedAuthority?> {
        return Collections.singleton(SimpleGrantedAuthority(role.name))
    }
}