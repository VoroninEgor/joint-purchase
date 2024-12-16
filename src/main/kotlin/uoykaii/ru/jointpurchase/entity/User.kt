package uoykaii.ru.jointpurchase.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "user_data")
class User {
    @Id
    lateinit var uuid: UUID
    lateinit var email: String
    lateinit var password: String
    lateinit var salt: String
    lateinit var firstName: String
    lateinit var lastName: String

    @Enumerated(EnumType.STRING)
    lateinit var authority: UserAuthority

    @OneToMany(mappedBy = "user")
    var orders: MutableList<Order> = mutableListOf()

    @OneToMany(mappedBy = "user")
    var purchases: MutableList<Purchase> = mutableListOf()
}

enum class UserAuthority {
    USER,
    ORGANIZER
}