package ua.money_manager.MoneyManager.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ua.money_manager.MoneyManager.User.UserService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException{
    final String authHeader = request.getHeader("Authorization");
    System.out.println("Authorization Header: " + authHeader);
    final String jwt;
    final String username;

        System.out.println(">>> JwtAuthenticationFilter сработал");

    if(authHeader == null || !authHeader.startsWith("Bearer ")) {
        filterChain.doFilter(request,response);
        return;
    }
    jwt = authHeader.substring(7);
    username = jwtService.extractUsername(jwt);

    if(username !=null && SecurityContextHolder.getContext().getAuthentication() == null){
        UserDetails userDetails = userService.loadUserByUsername(username);

        if(jwtService.isTokenValid(jwt, userDetails)) {
            System.out.println(">>> JWT валиден, аутентификация установлена для пользователя: " + username);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } else{
            System.out.println(">>> JWT НЕ валиден для пользователя: " + username);
        }
    }
    System.out.println("Username from JWT: " + username);
    filterChain.doFilter(request,response);
    }
}
