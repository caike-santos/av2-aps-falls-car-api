package falls_car_api.application.cadastro;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class MotoristaResponseDTO {
    private UUID id;
    private String nomeCompleto;
    private String cpf;
    private String numeroCnh;
    private UUID lojaBaseId;
    private BigDecimal valorDiariaMotorista;
    private Boolean isDisponivel;
}
