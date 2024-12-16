package uoykaii.ru.jointpurchase.entity

import jakarta.persistence.*
import org.springframework.web.multipart.MultipartFile
import uoykaii.ru.jointpurchase.util.PurchaseStatus
import java.time.LocalDateTime
import java.util.*

@Entity
open class Purchase {
    @Id
    open var id: UUID? = null
    open var name: String = ""
    open var description: String = ""
    open var moneyGoal: Double = 0.0
    open var collectedMoney: Double = 0.0
    open var organizationalFee: Short = 0
    open var stopDate: LocalDateTime? = null
    open var deliveryMethod: String = ""
    open var paymentMethod: String = ""

    @Enumerated(EnumType.STRING)
    open var status: PurchaseStatus? = null
    open var createdDate: LocalDateTime? = null
    open var publishedDate: LocalDateTime? = null

    @OneToOne(mappedBy = "purchase")
    open var image: Image? = null

    @OneToMany(mappedBy = "purchase")
    open var items: MutableList<Item> = mutableListOf()

    @ManyToOne
    @JoinColumn(name = "user_id")
    open var user: User? = null
}