package uoykaii.ru.jointpurchase.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import ru.uoykaii.security.user.UserDataResponse
import uoykaii.ru.jointpurchase.client.AuthenticationClient
import uoykaii.ru.jointpurchase.security.user

@RestController
class SomeController(
    private val client: AuthenticationClient
) {

    @GetMapping("/jwt/{token}")
    fun some(@PathVariable token: String): UserDataResponse {
        println(user.email)
        return client.getUserByToken(token)
    }
}