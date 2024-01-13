package fitcon.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "trainings")
class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column
    var name: String? = null

    @Column
    var startAt: LocalDateTime? = null

    @Column
    var endAt: LocalDateTime? = null

    @Column
    var type: String? = null

    @Column
    var description: String? = null

    @Column
    var userId: Long? = null
}