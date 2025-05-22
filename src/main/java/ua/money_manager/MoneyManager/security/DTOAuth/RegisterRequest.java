package ua.money_manager.MoneyManager.security.DTOAuth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class RegisterRequest {
    private String username;
    private String password;
}
