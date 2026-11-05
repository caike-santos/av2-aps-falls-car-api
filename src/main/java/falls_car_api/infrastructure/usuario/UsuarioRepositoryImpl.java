package falls_car_api.infrastructure.usuario;

import falls_car_api.domain.usuario.IUsuarioRepository;
import falls_car_api.domain.usuario.Usuario;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;

@Component
public class UsuarioRepositoryImpl implements IUsuarioRepository {

    private final SpringDataUsuarioRepository jpaRepository;

    public UsuarioRepositoryImpl(SpringDataUsuarioRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        UsuarioEntity entity = UsuarioEntity.fromDomain(usuario);
        jpaRepository.save(entity);
        return usuario;
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return jpaRepository.findByEmail(email).map(UsuarioEntity::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return jpaRepository.findById(id).map(UsuarioEntity::toDomain);
    }
}
