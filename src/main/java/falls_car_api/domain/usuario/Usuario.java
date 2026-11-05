package falls_car_api.domain.usuario;

import falls_car_api.domain.shared.DomainException;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Usuario {
    private UUID id;
    private String nome;
    private String email;
    private String senha;
    private Role role;
    private UUID clienteId;

    public Usuario(UUID id, String nome, String email, String senha, Role role, UUID clienteId) {
        if (email == null || email.isBlank()) {
            throw new DomainException("E-mail é obrigatório.");
        }
        if (senha == null || senha.isBlank()) {
            throw new DomainException("Senha é obrigatória.");
        }
        this.id = id != null ? id : UUID.randomUUID();
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.role = role != null ? role : Role.CLIENTE;
        this.clienteId = clienteId;
    }

    public Usuario(UUID id, String nome, String email, String senha, Role role) {
        this(id, nome, email, senha, role, null);
    }
}
