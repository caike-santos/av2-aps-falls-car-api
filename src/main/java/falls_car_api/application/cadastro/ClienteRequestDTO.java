package falls_car_api.application.cadastro;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ClienteRequestDTO {

    @NotBlank(message = "O nome completo é obrigatório.")
    private String nomeCompleto;

    @NotBlank(message = "O CPF é obrigatório.")
    @Pattern(regexp = "^(\\d{11}|\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2})$", message = "CPF inválido. Use 11 dígitos ou o formato 000.000.000-00.")
    private String cpf;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "E-mail inválido.")
    private String email;

    @Pattern(regexp = "^\\+?[0-9()\\s-]{8,20}$", message = "Telefone inválido.")
    private String telefone;

    @NotBlank(message = "O número da CNH é obrigatório.")
    @Pattern(regexp = "^\\d{11}$", message = "A CNH deve ter 11 dígitos.")
    private String numeroCnh;
}
