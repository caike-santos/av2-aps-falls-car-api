package falls_car_api.application.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class LoginRequestDTO {
    @NotBlank(message = "E-mail é obrigatório")
    private String email;
    @NotBlank(message = "Senha é obrigatória")
    private String senha;
}
