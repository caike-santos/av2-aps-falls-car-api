package falls_car_api.domain.frota;

import falls_car_api.domain.shared.StatusAutomovel;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IAutomovelRepository {

    void salvar(Automovel entidade);
    Optional<Automovel> buscarPorId(UUID id);
    List<Automovel> listarTodos();
    void atualizar(Automovel entidade);
    void removerPorId(UUID id);

    boolean existePorPlaca(String placa);

    List<Automovel> listarPorFiltro(StatusAutomovel status, Collection<UUID> lojasIds);

    List<Automovel> buscarDisponiveisPorLoja(UUID lojaId);
}
