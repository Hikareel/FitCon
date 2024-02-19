package fitcon.entity

import jakarta.persistence.*

@Entity
@Table(name = "workouts")
class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(nullable = false)
    var name: String? = null

    @Column(nullable = false)
    var description: String? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = [CascadeType.ALL])
    @JoinColumn(name = "USER_ID", nullable = false)
    var createdBy: User? = null
}