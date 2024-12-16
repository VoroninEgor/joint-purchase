package uoykaii.ru.jointpurchase.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uoykaii.ru.jointpurchase.dto.item.ItemCreateRequest
import uoykaii.ru.jointpurchase.reflection.OrganizerApi
import uoykaii.ru.jointpurchase.security.user
import uoykaii.ru.jointpurchase.service.ItemService

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/item")
class ItemController(val itemService: ItemService) {

    @PostMapping(consumes = ["multipart/form-data"])
    @OrganizerApi
    fun create(
        @ModelAttribute itemCreatRequest: ItemCreateRequest
    ) {
        logger.info { "Creating item for user with email: ${user.email} for purchase: ${itemCreatRequest.purchaseId}" }
        itemService.create(itemCreatRequest)
    }
}
