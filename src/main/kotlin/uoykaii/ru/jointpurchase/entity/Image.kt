package uoykaii.ru.jointpurchase.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.util.*

@Entity
open class Image {
    @Id
    open var id: UUID? = null

    @ManyToOne
    @JoinColumn(name = "item_id")
    open var item: Item? = null

    open var suffix: String = ""


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Image) return false

        if (id != other.id) return false
        if (item != other.item) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (item?.hashCode() ?: 0)
        return result
    }
}
