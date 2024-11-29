package uoykaii.ru.jointpurchase.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import java.time.LocalDateTime
import java.util.*

@Entity
open class Item {
    @Id
    open var id: UUID? = null
    open var name: String = ""
    open var description: String = ""
    open var price: Double = 0.0
    open var type: String = ""
    open var createdDate: LocalDateTime? = null

    @OneToMany(mappedBy = "item")
    open var images: MutableList<Image> = mutableListOf()

    @ManyToOne
    @JoinColumn(name = "purchase_id")
    open var purchase: Purchase? = null

    @OneToMany(mappedBy = "item")
    open var orders: MutableList<Order> = mutableListOf()
}
