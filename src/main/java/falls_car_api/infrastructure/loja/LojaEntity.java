package falls_car_api.infrastructure.loja;

import falls_car_api.domain.shared.TipoLoja;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "lojas")
public class LojaEntity {
    
    @Id
    private UUID id;
    
    private String nome;
    
    @Enumerated(EnumType.STRING)
    private TipoLoja tipoLoja;
    
    private String cidade;
    private String estado;
    private String enderecoCompleto;
    
    // Guardamos a coordenada separada na BD
    private Double latitude;
    private Double longitude;
    
    private Boolean isAtiva;
}