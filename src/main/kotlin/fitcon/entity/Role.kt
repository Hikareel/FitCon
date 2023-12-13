package fitcon.entity

import jakarta.persistence.*


@Entity
@Table(name = "roles")
class Role {
    private val serialVersionUID = 1L

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(nullable = false, unique = true)
    var name: String? = null

    @ManyToMany(mappedBy = "roles")
    val users: List<User>? = null
}