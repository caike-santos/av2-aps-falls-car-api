package falls_car_api.application.frota;

import falls_car_api.domain.shared.StatusAutomovel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AutomovelResponseDTO {
    private UUID id;
    private String placa;
    private String modelo;
    private String marca;
    private Integer ano;
    private BigDecimal valorDiaria;
    private StatusAutomovel statusAutomovel;
    private UUID lojaAtualId;
}
