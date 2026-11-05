package falls_car_api.infrastructure.usuario;

import falls_car_api.domain.usuario.Role;
import falls_car_api.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
public class UsuarioEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "cliente_id")
    private UUID clienteId;

    public static UsuarioEntity fromDomain(Usuario domain) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setEmail(domain.getEmail());
        entity.setSenha(domain.getSenha());
        entity.setRole(domain.getRole());
        entity.setClienteId(domain.getClienteId());
        return entity;
    }

    public Usuario toDomain() {
        return new Usuario(this.id, this.nome, this.email, this.senha, this.role, this.clienteId);
    }
}
