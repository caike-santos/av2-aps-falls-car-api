package falls_car_api.application.loja;

import falls_car_api.domain.shared.TipoLoja;
import lombok.Data;

import java.util.UUID;

@Data
public class LojaResponseDTO {
    private UUID id;
    private String nome;
    private TipoLoja tipoLoja;
    private String cidade;
    private String estado;
    private String enderecoCompleto;
    private Double latitude;
    private Double longitude;
    private Boolean isAtiva;
}