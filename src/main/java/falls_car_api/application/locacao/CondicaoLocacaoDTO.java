package falls_car_api.application.locacao;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class CondicaoLocacaoDTO {
    private Boolean incluiMotorista;

    @NotNull(message = "Termo de responsabilidade é obrigatório")
    private Boolean termoResponsabilidadeAceito;

    private String observacoesCondicaoVeiculo;
}
