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
    private Perfil perfil;
    private UUID clienteId;

    public Usuario(UUID id, String nome, String email, String senha, Perfil perfil, UUID clienteId) {
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
        this.perfil = perfil != null ? perfil : Perfil.CLIENTE;
        this.clienteId = clienteId;
    }

    public Usuario(UUID id, String nome, String email, String senha, Perfil perfil) {
        this(id, nome, email, senha, perfil, null);
    }
}
