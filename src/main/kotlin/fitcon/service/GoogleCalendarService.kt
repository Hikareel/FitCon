package fitcon.service

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventDateTime
import fitcon.dto.TrainingAddDto
import org.springframework.stereotype.Service
import java.time.ZoneOffset

@Service
class GoogleCalendarService {
    var credential: Credential? = null

    fun addEventToGoogleCalendar(dto: TrainingAddDto) {
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
            .setSummary(dto.name)
            .setDescription(dto.description)

        val startDateTime = DateTime(dto.startDate!!.toEpochSecond(ZoneOffset.UTC) * 1000)
        val start = EventDateTime().setDateTime(startDateTime).setTimeZone("UTC")
        event.start = start

        val endDateTime = DateTime(dto.endDate!!.toEpochSecond(ZoneOffset.UTC) * 1000)
        val end = EventDateTime().setDateTime(endDateTime).setTimeZone("UTC")
        event.end = end

        val createdEvent = client.events().insert("primary", event).execute()
        println("Event added: ${createdEvent.htmlLink}")
    }
}