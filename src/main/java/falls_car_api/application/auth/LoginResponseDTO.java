package falls_car_api.application.auth;

import falls_car_api.domain.usuario.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String tipo;
    private UUID usuarioId;
    private UUID clienteId;
    private String nome;
    private String email;
    private Role role;
}
