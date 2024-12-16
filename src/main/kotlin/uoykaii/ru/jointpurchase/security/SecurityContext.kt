package uoykaii.ru.jointpurchase.security

import ru.uoykaii.security.user.UserDataResponse
import uoykaii.ru.jointpurchase.exception.AuthenticationException
import uoykaii.ru.jointpurchase.security.SecurityContextHolder.securityContext

object SecurityContextHolder {
    val securityContext = ThreadLocal<SecurityContext>()
    fun createContext(user: UserDataResponse) {
        securityContext.set(SecurityContext(user))
    }
}

class SecurityContext(
    var userInfo: UserDataResponse,
)

val user: UserDataResponse
    get() = securityContext.get()?.userInfo ?: throw AuthenticationException("user are not authenticated")

val nullableUser: UserDataResponse?
    get() = securityContext.get()?.userInfo