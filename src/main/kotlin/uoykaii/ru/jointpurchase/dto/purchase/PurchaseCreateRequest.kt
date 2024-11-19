package uoykaii.ru.jointpurchase.dto.purchase

import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

data class PurchaseCreateRequest(
    val name: String,
    val description: String,
    val moneyGoal: Double,
    val organizationalFee: Short,
    val stopDate: LocalDateTime?,
    val deliveryMethod: String,
    val paymentMethod: String,
    val image: MultipartFile
)
