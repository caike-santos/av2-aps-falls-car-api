package falls_car_api.infrastructure.reserva;

import falls_car_api.domain.reserva.IReservaRepository;
import falls_car_api.domain.reserva.Reserva;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReservaRepositoryImpl implements IReservaRepository {

    private final ReservaJpaRepository jpaRepository;

    @Override
    public void salvar(Reserva entidade) {
        jpaRepository.save(converterParaEntity(entidade));
    }

    @Override
    public Optional<Reserva> buscarPorId(UUID id) {
        return jpaRepository.findById(id).map(this::converterParaDominio);
    }

    @Override
    public List<Reserva> listarTodos() {
        List<Reserva> reservas = new ArrayList<>();
        for (ReservaEntity entity : jpaRepository.findAll()) {
            reservas.add(converterParaDominio(entity));
        }
        return reservas;
    }

    @Override
    public void atualizar(Reserva entidade) {
        salvar(entidade);
    }

    @Override
    public void removerPorId(UUID id) {
        jpaRepository.deleteById(id);
    }

    private ReservaEntity converterParaEntity(Reserva r) {
        ReservaEntity e = new ReservaEntity();
        e.setId(r.getId());
        e.setCodigoReserva(r.getCodigoReserva());
        e.setClienteId(r.getClienteId());
        e.setAutomovelId(r.getAutomovelId());
        e.setLojaOrigemAutomovelId(r.getLojaOrigemAutomovelId());
        e.setLojaRetiradaId(r.getLojaRetiradaId());
        e.setCanalReserva(r.getCanalReserva());
        e.setPeriodoLocacao(r.getPeriodoLocacao());
        e.setIncluiMotorista(r.getIncluiMotorista());
        e.setMotoristaId(r.getMotoristaId());
        e.setDataRetirada(r.getDataRetirada());
        e.setDataDevolucaoPrevista(r.getDataDevolucaoPrevista());
        e.setValorTotalEstimado(r.getValorTotalEstimado());
        e.setStatusReserva(r.getStatusReserva());
        return e;
    }

    private Reserva converterParaDominio(ReservaEntity e) {
        Reserva r = new Reserva();
        r.setId(e.getId());
        r.setCodigoReserva(e.getCodigoReserva());
        r.setClienteId(e.getClienteId());
        r.setAutomovelId(e.getAutomovelId());
        r.setLojaOrigemAutomovelId(e.getLojaOrigemAutomovelId());
        r.setLojaRetiradaId(e.getLojaRetiradaId());
        r.setCanalReserva(e.getCanalReserva());
        r.setPeriodoLocacao(e.getPeriodoLocacao());
        r.setIncluiMotorista(e.getIncluiMotorista());
        r.setMotoristaId(e.getMotoristaId());
        r.setDataRetirada(e.getDataRetirada());
        r.setDataDevolucaoPrevista(e.getDataDevolucaoPrevista());
        r.setValorTotalEstimado(e.getValorTotalEstimado());
        r.setStatusReserva(e.getStatusReserva());
        return r;
    }
}
