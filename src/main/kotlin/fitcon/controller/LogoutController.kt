package fitcon.controller

import fitcon.service.GoogleCalendarService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LogoutController(
    private val calendarService: GoogleCalendarService
) {
    @GetMapping("/logout/success")
    fun logoutCallback(): String {
        calendarService.credential = null
        return "login"
    }
}