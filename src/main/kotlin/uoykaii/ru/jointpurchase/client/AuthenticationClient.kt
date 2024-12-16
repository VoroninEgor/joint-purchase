package uoykaii.ru.jointpurchase.client

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.runBlocking
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Component
import ru.uoykaii.security.user.UserDataResponse
import ru.uoykaii.security.user.UserDataServiceGrpcKt
import ru.uoykaii.security.user.getUserDataJwtRequest

private val logger = KotlinLogging.logger {}

@Component
class AuthenticationClient {

    @GrpcClient("auth")
    private lateinit var userDateService: UserDataServiceGrpcKt.UserDataServiceCoroutineStub

    fun getUserByToken(token: String) : UserDataResponse = runBlocking {
        logger.info { "getting user data by jwt: $token" }
        userDateService.getUserDataByJwt(getUserDataJwtRequest { jwt = token })
    }
}