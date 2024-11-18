package uoykaii.ru.jointpurchase.dto

import org.springframework.web.multipart.MultipartFile

data class ItemCreateRequest(
    var name: String,
    var description: String,
    var price: Double,
    var type: String,
    var images: List<MultipartFile>
)
