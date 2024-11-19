package uoykaii.ru.jointpurchase.controller

import org.springframework.web.bind.annotation.*
import uoykaii.ru.jointpurchase.dto.item.ItemCreateRequest
import uoykaii.ru.jointpurchase.service.ItemService

@RestController
@CrossOrigin(origins = ["http://127.0.0.1:5500"])
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
