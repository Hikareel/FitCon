package fitcon.entity

import jakarta.persistence.*

@Entity
@Table(name = "exercises")
class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(nullable = false)
    var exerciseName: String? = null

    @Column(nullable = false)
    var reps: Int? = null

    @Column(nullable = false)
    var series: Int? = null

    @Column(nullable = true)
    var info: String? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = [CascadeType.ALL])
    @JoinColumn(name = "WORKOUT_ID", nullable = false)
    var workoutId: Workout? = null
}