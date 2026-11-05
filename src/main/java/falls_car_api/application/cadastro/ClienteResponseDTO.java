package falls_car_api.application.cadastro;

import lombok.Data;

import java.util.UUID;

@Data
public class ClienteResponseDTO {
    private UUID id;
    private String nomeCompleto;
    private String cpf;
    private String email;
    private String telefone;
    private String numeroCnh;
}
