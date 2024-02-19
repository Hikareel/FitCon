package fitcon.dto

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

class TrainingAddDto {
    @NotEmpty(message = "Must have name!")
    var name: String? = null

    @NotNull(message = "Must have start date!")
    var startDate: LocalDateTime? = null

    @NotNull(message = "Must have end date!")
    var endDate: LocalDateTime? = null

    @NotEmpty(message = "Must have description!")
    var description: String? = null
}