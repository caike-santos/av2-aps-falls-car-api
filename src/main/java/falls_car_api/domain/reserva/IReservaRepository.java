package falls_car_api.domain.reserva;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IReservaRepository {

    void salvar(Reserva entidade);
    Optional<Reserva> buscarPorId(UUID id);
    List<Reserva> listarTodos();
    void atualizar(Reserva entidade);
    void removerPorId(UUID id);
}
