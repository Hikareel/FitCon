package fitcon.entity

import jakarta.persistence.*


@Entity
@Table(name = "roles")
class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(nullable = false, unique = true)
    var name: String? = null

    @OneToMany(mappedBy = "role")
    val users: List<User>? = null
}