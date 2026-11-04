package falls_car_api.application.reserva;

import falls_car_api.domain.reserva.StatusReserva;
import falls_car_api.domain.shared.CanalReserva;
import falls_car_api.domain.shared.PeriodoLocacao;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
public class ReservaResponseDTO {
    private UUID id;
    private String codigoReserva;
    private UUID clienteId;
    private UUID automovelId;
    private UUID lojaOrigemAutomovelId;
    private UUID lojaRetiradaId;
    private CanalReserva canalReserva;
    private PeriodoLocacao periodoLocacao;
    private Integer periodoDias;
    private Boolean incluiMotorista;
    private UUID motoristaId;
    private Instant dataRetirada;
    private Instant dataDevolucaoPrevista;
    private BigDecimal valorTotalEstimado;
    private StatusReserva statusReserva;
}
