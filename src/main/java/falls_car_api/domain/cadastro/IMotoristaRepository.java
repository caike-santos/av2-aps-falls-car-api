package falls_car_api.domain.cadastro;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IMotoristaRepository {

    void salvar(Motorista entidade);
    Optional<Motorista> buscarPorId(UUID id);
    List<Motorista> listarTodos();
    void atualizar(Motorista entidade);
    void removerPorId(UUID id);

    Optional<Motorista> buscarPorCpf(String cpf);

    List<Motorista> listarDisponiveisPorLojas(Collection<UUID> lojasBaseIds);
}
