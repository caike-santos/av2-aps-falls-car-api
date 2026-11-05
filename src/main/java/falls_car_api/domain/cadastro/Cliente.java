package falls_car_api.domain.cadastro;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class Cliente {
    private UUID id;
    private String nomeCompleto;
    private String cpf;
    private String email;
    private String telefone;
    private String numeroCnh;
}
