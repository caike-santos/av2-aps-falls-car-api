package falls_car_api.application.frota;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class TransferirLojaDTO {

    @NotNull(message = "A loja de destino é obrigatória.")
    private UUID novaLojaId;
}
