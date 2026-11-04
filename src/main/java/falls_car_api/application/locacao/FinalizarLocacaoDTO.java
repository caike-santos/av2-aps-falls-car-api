package falls_car_api.application.locacao;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;
@Data
public class FinalizarLocacaoDTO {
    @NotNull(message = "ID da loja de devolução é obrigatório")
    private UUID lojaDevolucaoId;
    private Boolean temAvaria;
}
