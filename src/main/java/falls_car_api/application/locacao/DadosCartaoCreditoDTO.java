package falls_car_api.application.locacao;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DadosCartaoCreditoDTO {
    @NotBlank(message = "Número do cartão é obrigatório")
    private String numeroCartao;
    @NotBlank(message = "Nome do titular é obrigatório")
    private String nomeTitular;
    @NotBlank(message = "Validade é obrigatória")
    private String validade;
    @NotBlank(message = "CVV é obrigatório")
    private String cvv;
}
