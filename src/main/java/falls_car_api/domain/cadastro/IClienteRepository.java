package falls_car_api.domain.cadastro;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IClienteRepository {

    void salvar(Cliente entidade);
    Optional<Cliente> buscarPorId(UUID id);
    List<Cliente> listarTodos();
    void atualizar(Cliente entidade);
    void removerPorId(UUID id);

    Optional<Cliente> buscarPorCpf(String cpf);
}
