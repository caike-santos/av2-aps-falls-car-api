package falls_car_api.domain.cadastro;

import falls_car_api.domain.shared.DomainException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Motorista {
    private UUID id;
    private String nomeCompleto;
    private String cpf;
    private String numeroCnh;
    private UUID lojaBaseId;
    private BigDecimal valorDiariaMotorista;
    private Boolean isDisponivel;

    public void alocarMotorista() {
        if (!Boolean.TRUE.equals(isDisponivel)) {
            throw new DomainException("O motorista não está disponível para ser alocado.");
        }
        this.isDisponivel = false;
    }

    public void liberarMotorista() {
        this.isDisponivel = true;
    }
}
