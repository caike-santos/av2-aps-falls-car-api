package falls_car_api.infrastructure.reserva;

import falls_car_api.domain.reserva.StatusReserva;
import falls_car_api.domain.shared.CanalReserva;
import falls_car_api.domain.shared.PeriodoLocacao;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "reservas")
public class ReservaEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String codigoReserva;

    @Column(nullable = false)
    private UUID clienteId;

    @Column(nullable = false)
    private UUID automovelId;

    @Column(nullable = false)
    private UUID lojaOrigemAutomovelId;

    @Column(nullable = false)
    private UUID lojaRetiradaId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CanalReserva canalReserva;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PeriodoLocacao periodoLocacao;

    @Column(nullable = false)
    private Boolean incluiMotorista;

    private UUID motoristaId;

    @Column(nullable = false)
    private Instant dataRetirada;

    @Column(nullable = false)
    private Instant dataDevolucaoPrevista;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotalEstimado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusReserva statusReserva;
}
