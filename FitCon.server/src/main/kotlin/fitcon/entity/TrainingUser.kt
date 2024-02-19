package fitcon.entity

import jakarta.persistence.*

@Entity
@Table(name = "training_user")
class TrainingUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column
    var trainingId: Long? = null

    @Column
    var userId: Long? = null
}