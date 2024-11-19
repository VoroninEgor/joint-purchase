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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Item) return false

        if (id != other.id) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (price != other.price) return false
        if (type != other.type) return false
        if (images != other.images) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + images.hashCode()
        return result
    }
}
