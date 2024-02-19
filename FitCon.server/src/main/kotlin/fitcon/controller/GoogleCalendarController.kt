package fitcon.controller

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl
import com.google.api.client.auth.oauth2.TokenResponse
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.calendar.CalendarScopes
import fitcon.service.GoogleCalendarService
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.view.RedirectView

@Controller
class GoogleCalendarController(private val googleCalendarService: GoogleCalendarService) {
    @Value("\${google.client.client-id}")
    private val clientId: String? = null

    @Value("\${google.client.client-secret}")
    private val clientSecret: String? = null

    @Value("\${google.client.redirectUri}")
    private val redirectURI: String? = null

    private var flow: GoogleAuthorizationCodeFlow? = null

    @PreAuthorize("hasRole('TRAINER') or hasRole('USER')")
    @GetMapping("/connect-google-calendar")
    fun connectGoogleCalendar(
        model: Model, @RequestParam("type") trainingType: String?,
        @AuthenticationPrincipal userDetails: UserDetails
    ): RedirectView {
        return RedirectView(authorize())
    }

    @PreAuthorize("hasRole('TRAINER') or hasRole('USER')")
    @GetMapping("/disconnect-google-calendar")
    fun disconnectGoogleCalendar(): String {
        googleCalendarService.credential = null
        return "redirect:/user/group-trainings"
    }

    @GetMapping("/user")
    fun callbackGoogleApi(@RequestParam code: String): String {
        googleCalendarService.credential = try {
            val response: TokenResponse = flow!!.newTokenRequest(code)
                .setRedirectUri(redirectURI)
                .execute()
            flow!!.createAndStoreCredential(response, "userID")
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

        return "redirect:/user/group-trainings"
    }

    private fun authorize(): String {
        val authorizationUrl: AuthorizationCodeRequestUrl
        val web = GoogleClientSecrets.Details()
        web.clientId = clientId
        web.clientSecret = clientSecret
        val clientSecrets = GoogleClientSecrets()
            .setWeb(web)
        val httpTransport: HttpTransport = GoogleNetHttpTransport.newTrustedTransport()
        flow = GoogleAuthorizationCodeFlow.Builder(
            httpTransport,
            GsonFactory.getDefaultInstance(),
            clientSecrets,
            setOf(CalendarScopes.CALENDAR)
        ).build()
        authorizationUrl = flow!!.newAuthorizationUrl()
            .setRedirectUri(redirectURI)
            .setApprovalPrompt("force")
        println("cal authorizationUrl->$authorizationUrl")
        return authorizationUrl
            .build()
    }
}