package ua.money_manager.MoneyManager.security.DTOAuth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class LoginRequest {
    private String username;
    private String password;
}
