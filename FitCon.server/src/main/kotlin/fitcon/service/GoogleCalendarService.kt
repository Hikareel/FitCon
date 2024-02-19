package fitcon.service

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventDateTime
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class GoogleCalendarService {
    var credential: Credential? = null

    fun addEventToGoogleCalendar(name: String, startDate: LocalDateTime, endDate: LocalDateTime, description: String) {
        if (credential == null) {
            return
        }

        val client = Calendar.Builder(
            GoogleNetHttpTransport.newTrustedTransport(),
            GsonFactory.getDefaultInstance(),
            credential
        )
            .setApplicationName("FitCon")
            .build()

        val event = Event()
            .setSummary(name)
            .setDescription(description)

        val startDateTime = DateTime(startDate.toEpochSecond(ZoneOffset.UTC) * 1000)
        val start = EventDateTime().setDateTime(startDateTime).setTimeZone("UTC")
        event.start = start

        val endDateTime = DateTime(endDate.toEpochSecond(ZoneOffset.UTC) * 1000)
        val end = EventDateTime().setDateTime(endDateTime).setTimeZone("UTC")
        event.end = end

        val createdEvent = client.events().insert("primary", event).execute()
        println("Event added: ${createdEvent.htmlLink}")
    }
}