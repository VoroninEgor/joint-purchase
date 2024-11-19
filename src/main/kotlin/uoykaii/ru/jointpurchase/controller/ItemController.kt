package uoykaii.ru.jointpurchase.controller

import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uoykaii.ru.jointpurchase.dto.item.ItemCreateRequest
import uoykaii.ru.jointpurchase.service.ItemService

@RestController
@RequestMapping("/item")
class ItemController(val itemService: ItemService) {

    @PostMapping(consumes = ["multipart/form-data"])
    fun create(
        @ModelAttribute itemCreatRequest: ItemCreateRequest
    ) {
        println("Создание товара...")
        println(itemCreatRequest)
        itemService.create(itemCreatRequest)
    }
}
