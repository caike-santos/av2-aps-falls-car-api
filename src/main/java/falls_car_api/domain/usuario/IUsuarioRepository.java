package falls_car_api.domain.usuario;

import java.util.Optional;
import java.util.UUID;
public interface IUsuarioRepository {
    Usuario salvar(Usuario usuario);
    Optional<Usuario> buscarPorEmail(String email);
    Optional<Usuario> buscarPorId(UUID id);
}
