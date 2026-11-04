package falls_car_api.domain.frota;

import falls_car_api.domain.shared.DomainException;
import falls_car_api.domain.shared.StatusAutomovel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Automovel {
    private UUID id;
    private String placa;
    private String modelo;
    private String marca;
    private Integer ano;
    private BigDecimal valorDiaria;
    private StatusAutomovel statusAutomovel;
    private UUID lojaAtualId;

    public void reservar() {
        if (statusAutomovel != StatusAutomovel.LIVRE) {
            throw new DomainException("Só é possível reservar um automóvel LIVRE. Status atual: " + statusAutomovel + ".");
        }
        this.statusAutomovel = StatusAutomovel.RESERVADO;
    }

    public void alugar() {
        if (statusAutomovel != StatusAutomovel.LIVRE && statusAutomovel != StatusAutomovel.RESERVADO) {
            throw new DomainException("Só é possível alugar um automóvel LIVRE ou RESERVADO. Status atual: " + statusAutomovel + ".");
        }
        this.statusAutomovel = StatusAutomovel.ALUGADO;
    }

    public void enviarParaManutencao() {
        this.statusAutomovel = StatusAutomovel.MANUTENCAO;
    }

    public void liberarVeiculo(UUID novaLojaId) {
        if (novaLojaId == null) {
            throw new DomainException("É necessário informar a loja em que o automóvel será liberado.");
        }
        this.statusAutomovel = StatusAutomovel.LIVRE;
        this.lojaAtualId = novaLojaId;
    }

    public void transferirPara(UUID novaLojaId) {
        if (novaLojaId == null) {
            throw new DomainException("É necessário informar a loja de destino.");
        }
        if (statusAutomovel == StatusAutomovel.ALUGADO) {
            throw new DomainException("Não é possível transferir um automóvel que está ALUGADO.");
        }
        if (novaLojaId.equals(lojaAtualId)) {
            throw new DomainException("O automóvel já está nessa loja.");
        }
        this.lojaAtualId = novaLojaId;
    }
}
