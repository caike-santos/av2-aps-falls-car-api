package falls_car_api.application.locacao;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;
@Data
public class EfetivarLocacaoRequestDTO {
    @NotNull(message = "ID da reserva é obrigatório")
    private UUID reservaId;
    @NotNull
    @Valid
    private CondicaoLocacaoDTO condicaoLocacao;
    @NotNull
    @Valid
    private DadosCartaoCreditoDTO dadosCartaoCredito;
}
