package falls_car_api.infrastructure.cadastro;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "clientes")
public class ClienteEntity {

    @Id
    private UUID id;

    private String nomeCompleto;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    private String email;
    private String telefone;
    private String numeroCnh;
}
