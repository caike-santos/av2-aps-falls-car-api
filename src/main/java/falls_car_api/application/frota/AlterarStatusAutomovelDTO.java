package falls_car_api.application.frota;

import falls_car_api.domain.shared.StatusAutomovel;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AlterarStatusAutomovelDTO {

    @NotNull(message = "O novo status do automóvel é obrigatório.")
    private StatusAutomovel statusAutomovel;

    private UUID novaLojaId;
}
