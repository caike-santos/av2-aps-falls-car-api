package falls_car_api.application.cadastro;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AlterarDisponibilidadeMotoristaDTO {

    @NotNull(message = "O campo isDisponivel é obrigatório.")
    private Boolean isDisponivel;
}
