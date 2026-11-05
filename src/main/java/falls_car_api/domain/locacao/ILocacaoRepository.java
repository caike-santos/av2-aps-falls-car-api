package falls_car_api.domain.locacao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ILocacaoRepository {
    Locacao salvar(Locacao locacao);
    Optional<Locacao> buscarPorId(UUID id);
    List<Locacao> listarTodos();
    Locacao atualizar(Locacao locacao);
    void removerPorId(UUID id);
}
