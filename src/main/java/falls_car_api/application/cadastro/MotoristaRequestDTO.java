package falls_car_api.application.cadastro;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class MotoristaRequestDTO {

    @NotBlank(message = "O nome completo é obrigatório.")
    private String nomeCompleto;

    @NotBlank(message = "O CPF é obrigatório.")
    @Pattern(regexp = "^(\\d{11}|\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2})$", message = "CPF inválido. Use 11 dígitos ou o formato 000.000.000-00.")
    private String cpf;

    @NotBlank(message = "O número da CNH é obrigatório.")
    @Pattern(regexp = "^\\d{11}$", message = "A CNH deve ter 11 dígitos.")
    private String numeroCnh;

    @NotNull(message = "A loja base (lojaBaseId) é obrigatória.")
    private UUID lojaBaseId;

    @NotNull(message = "O valor da diária do motorista é obrigatório.")
    @DecimalMin(value = "0.00", inclusive = false, message = "O valor da diária do motorista deve ser maior que zero.")
    private BigDecimal valorDiariaMotorista;
}
