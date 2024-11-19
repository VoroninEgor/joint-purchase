package uoykaii.ru.jointpurchase.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import uoykaii.ru.jointpurchase.dto.item.ItemCreateRequest
import uoykaii.ru.jointpurchase.entity.Item
import uoykaii.ru.jointpurchase.repository.ItemRepository
import uoykaii.ru.jointpurchase.repository.PurchaseRepository
import uoykaii.ru.jointpurchase.util.ImageOwnerType
import java.time.LocalDateTime
import java.util.*
import kotlin.jvm.optionals.getOrElse

@Service
class ItemService(
    private val itemRepository: ItemRepository,
    private val purchaseRepository: PurchaseRepository,
    private val imageService: ImageService
) {

    @Transactional
    fun create(itemCreatRequest: ItemCreateRequest) {
        val purchaseById = purchaseRepository
            .findById(itemCreatRequest.purchaseId)
            .getOrElse { throw IllegalArgumentException("Не существует закупки: ${itemCreatRequest.purchaseId}") }
        val item: Item = Item().apply {
            id = UUID.randomUUID()
            name = itemCreatRequest.name
            description = itemCreatRequest.description
            price = itemCreatRequest.price
            type = itemCreatRequest.type
            purchase = purchaseById
            createdDate = LocalDateTime.now()
        }.also { itemRepository.save(it) }

        itemCreatRequest.images.forEach {
            imageService.upload(it, item.id!!, ImageOwnerType.ITEM)
        }
    }
}