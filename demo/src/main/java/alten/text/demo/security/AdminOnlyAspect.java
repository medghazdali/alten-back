package alten.text.demo.security;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Aspect
@Component
public class AdminOnlyAspect {

    @Around("@annotation(adminOnly)")
    public Object checkAdminAccess(ProceedingJoinPoint joinPoint, AdminOnly adminOnly) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User user = 
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            
            // Check if the user has the ADMIN role
            if (user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return joinPoint.proceed();
            }
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin access required");
    }
} 