package falls_car_api.application.frota;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AutomovelRequestDTO {

    @NotBlank(message = "A placa é obrigatória.")
    @Pattern(regexp = "^[A-Za-z]{3}-?[0-9][A-Za-z0-9][0-9]{2}$", message = "Placa inválida. Use o formato ABC1234 ou ABC1D23.")
    private String placa;

    @NotBlank(message = "O modelo é obrigatório.")
    private String modelo;

    @NotBlank(message = "A marca é obrigatória.")
    private String marca;

    @NotNull(message = "O ano é obrigatório.")
    @Min(value = 1900, message = "O ano informado é inválido.")
    private Integer ano;

    @NotNull(message = "O valor da diária é obrigatório.")
    @DecimalMin(value = "0.00", inclusive = false, message = "O valor da diária deve ser maior que zero.")
    private BigDecimal valorDiaria;

    private UUID lojaAtualId;
}
