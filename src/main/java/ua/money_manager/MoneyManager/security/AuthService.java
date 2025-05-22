package ua.money_manager.MoneyManager.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.money_manager.MoneyManager.User.User;
import ua.money_manager.MoneyManager.User.UserRepository;
import ua.money_manager.MoneyManager.User.UserService;
import ua.money_manager.MoneyManager.security.DTOAuth.AuthResponse;
import ua.money_manager.MoneyManager.security.DTOAuth.LoginRequest;
import ua.money_manager.MoneyManager.security.DTOAuth.RegisterRequest;
import ua.money_manager.MoneyManager.security.jwt.JwtService;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthResponse register(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userRepository.save(user);
        String token = jwtService.generateToken(userService.loadUserByUsername(user.getUsername()));
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        return new AuthResponse(token);
    }
}
