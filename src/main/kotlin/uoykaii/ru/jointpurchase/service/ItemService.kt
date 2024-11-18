package uoykaii.ru.jointpurchase.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import uoykaii.ru.jointpurchase.dto.ItemCreateRequest
import uoykaii.ru.jointpurchase.entity.Item
import uoykaii.ru.jointpurchase.repository.ItemRepository
import java.util.*

@Service
class ItemService(
    val itemRepository: ItemRepository,
    val imageService: ImageService
) {

    @Transactional
    fun create(itemCreatRequest: ItemCreateRequest) {
        val item: Item = Item().apply {
            id = UUID.randomUUID()
            name = itemCreatRequest.name
            description = itemCreatRequest.description
            price = itemCreatRequest.price
            type = itemCreatRequest.type
        }.also { itemRepository.save(it) }

        itemCreatRequest.images.forEach {
            imageService.upload(it, item.id!!)
        }
    }
}