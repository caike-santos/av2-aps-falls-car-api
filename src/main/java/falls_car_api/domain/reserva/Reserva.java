package falls_car_api.domain.reserva;

import falls_car_api.domain.shared.CanalReserva;
import falls_car_api.domain.shared.DomainException;
import falls_car_api.domain.shared.PeriodoLocacao;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Reserva {
    private UUID id;
    private String codigoReserva;
    private UUID clienteId;
    private UUID automovelId;
    private UUID lojaOrigemAutomovelId;
    private UUID lojaRetiradaId;
    private CanalReserva canalReserva;
    private PeriodoLocacao periodoLocacao;
    private Boolean incluiMotorista;
    private UUID motoristaId;
    private Instant dataRetirada;
    private Instant dataDevolucaoPrevista;
    private BigDecimal valorTotalEstimado;
    private StatusReserva statusReserva;

    public static PeriodoLocacao converterEValidarPeriodoDias(Integer periodoDias) {
        if (periodoDias == null) {
            throw new DomainException("O período de locação deve ser obrigatoriamente de 7, 15 ou 30 dias.");
        }
        return switch (periodoDias) {
            case 7 -> PeriodoLocacao.SETE_DIAS;
            case 15 -> PeriodoLocacao.QUINZE_DIAS;
            case 30 -> PeriodoLocacao.TRINTA_DIAS;
            default -> throw new DomainException("O período de locação deve ser obrigatoriamente de 7, 15 ou 30 dias.");
        };
    }

    public void calcularDatasEValorEstimado(BigDecimal valorDiariaAutomovel, BigDecimal valorDiariaMotorista) {
        if (this.dataRetirada == null) {
            throw new DomainException("A data de retirada (dataRetirada) é obrigatória.");
        }
        if (this.periodoLocacao == null) {
            throw new DomainException("O período de locação deve ser obrigatoriamente de 7, 15 ou 30 dias.");
        }
        if (valorDiariaAutomovel == null || valorDiariaAutomovel.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("O valor da diária do automóvel é inválido para cálculo da reserva.");
        }

        int dias = this.periodoLocacao.getDias();
        this.dataDevolucaoPrevista = this.dataRetirada.plus(dias, ChronoUnit.DAYS);

        BigDecimal valorTotal = valorDiariaAutomovel.multiply(BigDecimal.valueOf(dias));
        if (Boolean.TRUE.equals(this.incluiMotorista) && valorDiariaMotorista != null) {
            valorTotal = valorTotal.add(valorDiariaMotorista.multiply(BigDecimal.valueOf(dias)));
        }
        this.valorTotalEstimado = valorTotal;
    }

    public void confirmar() {
        if (this.statusReserva == StatusReserva.CANCELADA) {
            throw new DomainException("Não é possível confirmar uma reserva que já foi CANCELADA.");
        }
        this.statusReserva = StatusReserva.CONFIRMADA;
    }

    public void cancelar() {
        if (this.statusReserva == StatusReserva.CANCELADA) {
            throw new DomainException("A reserva já se encontra CANCELADA.");
        }
        this.statusReserva = StatusReserva.CANCELADA;
    }
}
