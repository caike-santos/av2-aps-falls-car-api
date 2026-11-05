package falls_car_api.application.auth;

import falls_car_api.domain.usuario.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class RegistroRequestDTO {
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "E-mail é obrigatório")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    private String senha;

    private Role role;

    private UUID clienteId;

    private String cpf;
    private String telefone;
    private String numeroCnh;
}
