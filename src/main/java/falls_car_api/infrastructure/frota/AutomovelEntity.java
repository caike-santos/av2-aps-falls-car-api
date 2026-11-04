package falls_car_api.infrastructure.frota;

import falls_car_api.domain.shared.StatusAutomovel;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "automoveis")
public class AutomovelEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 8)
    private String placa;

    private String modelo;
    private String marca;
    private Integer ano;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorDiaria;

    @Enumerated(EnumType.STRING)
    private StatusAutomovel statusAutomovel;

    private UUID lojaAtualId;
}
