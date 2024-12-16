package uoykaii.ru.jointpurchase.reflection

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component
import ru.uoykaii.security.user.UserAuthority
import uoykaii.ru.jointpurchase.exception.AuthenticationException
import uoykaii.ru.jointpurchase.security.user

@Aspect
@Component
class ApiValidationAspect {

    @Pointcut("@annotation(organizerApi)")
    fun methodWithOrganizerApiAnnotation(organizerApi: OrganizerApi) {
    }

    @Before("@annotation(organizerApi)")
    fun checkOrganizerAuthority(joinPoint: JoinPoint, organizerApi: OrganizerApi) {
        try {
            if (user.authority != UserAuthority.ORGANIZER) {
                throw AuthenticationException("User does not have organizer authority")
            }
        } catch (e: IllegalArgumentException) {
            throw AuthenticationException("Invalid user authority: ${e.message}")
        } catch (e: NullPointerException) {
            throw AuthenticationException("User authentication context is missing")
        }
    }

    @Pointcut("@annotation(authenticatedApi)")
    fun methodWithAuthenticatedApiAnnotation(authenticatedApi: AuthenticatedApi) {
    }

    @Before("@annotation(authenticatedApi)")
    fun checkAnyAuthority(joinPoint: JoinPoint, authenticatedApi: AuthenticatedApi) {
        try {
            if (user.authority == null) {
                throw AuthenticationException("User does not have any authority")
            }
        } catch (e: IllegalArgumentException) {
            throw AuthenticationException("Invalid user authority: ${e.message}")
        } catch (e: NullPointerException) {
            throw AuthenticationException("User authentication context is missing")
        }
    }
}