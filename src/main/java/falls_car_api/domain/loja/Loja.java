package falls_car_api.domain.loja;

import falls_car_api.domain.shared.TipoLoja;
import falls_car_api.domain.shared.DomainException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class Loja {
    private UUID id;
    private String nome;
    private TipoLoja tipo;
    private String cidade;
    private String estado;
    private String enderecoCompleto;
    private VoCoordenada coordenada;
    private Boolean isAtivo;

    public Double calcularDistanciaAte(Loja outraLoja){
        if (this.coordenada == null || outraLoja.getCoordenada() == null) {
            throw new DomainException("Não é possível calcular a distância entre lojas sem coordenadas.");
        }
        return this.coordenada.calcularDistanciaEmKm(outraLoja.getCoordenada());
    }
}
