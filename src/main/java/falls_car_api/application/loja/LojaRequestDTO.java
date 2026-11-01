package falls_car_api.application.loja;

import falls_car_api.domain.shared.TipoLoja;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LojaRequestDTO {
    
    @NotBlank(message = "O nome da loja é obrigatório.")
    private String nome;
    
    @NotNull(message = "O tipo da loja (AEROPORTO ou CIDADE) é obrigatório.")
    private TipoLoja tipoLoja;
    
    @NotBlank(message = "A cidade é obrigatória.")
    private String cidade;
    
    @NotBlank(message = "O estado é obrigatório.")
    private String estado;
    
    @NotBlank(message = "O endereço completo é obrigatório.")
    private String enderecoCompleto;
    
    @NotNull(message = "A latitude é obrigatória.")
    private Double latitude;
    
    @NotNull(message = "A longitude é obrigatória.")
    private Double longitude;
    
    @NotNull(message = "O status de atividade (isAtiva) é obrigatório.")
    private Boolean isAtiva;
}