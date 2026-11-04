package falls_car_api.application.reserva;

import falls_car_api.domain.shared.CanalReserva;
import falls_car_api.domain.shared.PeriodoLocacao;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class ReservaRequestDTO {

    @NotNull(message = "O ID do cliente (clienteId) é obrigatório.")
    private UUID clienteId;

    @NotNull(message = "O ID da loja de retirada (lojaRetiradaId) é obrigatório.")
    private UUID lojaRetiradaId;

    @NotNull(message = "O canal de reserva (canalReserva) é obrigatório.")
    private CanalReserva canalReserva;

    private Integer periodoDias;

    private PeriodoLocacao periodoLocacao;

    @NotNull(message = "A indicação de motorista (incluiMotorista) é obrigatória.")
    private Boolean incluiMotorista;

    private UUID motoristaId;

    @NotNull(message = "A data de retirada (dataRetirada) é obrigatória.")
    private Instant dataRetirada;
}
