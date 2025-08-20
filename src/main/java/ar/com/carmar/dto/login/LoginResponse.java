package ar.com.carmar.dto.login;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private Instant expiresAt;
    private String tokenType = "Bearer";
    private String username;
    private List<String> authorities;
}
