package fitcon.entity

import jakarta.persistence.*


@Entity
@Table(name="users")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(nullable = false)
    var name: String? = null

    @Column(nullable = false, unique = true)
    var email: String? = null

    @Column(nullable = false)
    var password: String? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = [CascadeType.ALL])
    @JoinColumn(name = "ROLE_ID", nullable = false)
    var role: Role? = null

    @OneToMany(mappedBy = "createdBy", cascade = [CascadeType.ALL])
    val workouts: List<Workout>? = null

}