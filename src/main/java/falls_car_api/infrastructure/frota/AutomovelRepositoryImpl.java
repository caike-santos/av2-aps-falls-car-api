package falls_car_api.infrastructure.frota;

import falls_car_api.domain.frota.Automovel;
import falls_car_api.domain.frota.IAutomovelRepository;
import falls_car_api.domain.shared.StatusAutomovel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AutomovelRepositoryImpl implements IAutomovelRepository {

    private final AutomovelJpaRepository jpaRepository;

    @Override
    public void salvar(Automovel entidade) {
        jpaRepository.save(converterParaEntity(entidade));
    }

    @Override
    public Optional<Automovel> buscarPorId(UUID id) {
        return jpaRepository.findById(id).map(this::converterParaDominio);
    }

    @Override
    public List<Automovel> listarTodos() {
        List<Automovel> automoveis = new ArrayList<>();
        for (AutomovelEntity entity : jpaRepository.findAll()) {
            automoveis.add(converterParaDominio(entity));
        }
        return automoveis;
    }

    @Override
    public void atualizar(Automovel entidade) {
        salvar(entidade);
    }

    @Override
    public void removerPorId(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existePorPlaca(String placa) {
        return jpaRepository.existsByPlaca(placa);
    }

    @Override
    public List<Automovel> listarPorFiltro(StatusAutomovel status, Collection<UUID> lojasIds) {
        List<Automovel> automoveis = new ArrayList<>();
        for (AutomovelEntity entity : jpaRepository.findAll()) {
            boolean statusOk = status == null || entity.getStatusAutomovel() == status;
            boolean lojaOk = lojasIds == null || lojasIds.contains(entity.getLojaAtualId());
            if (statusOk && lojaOk) {
                automoveis.add(converterParaDominio(entity));
            }
        }
        return automoveis;
    }

    @Override
    public List<Automovel> buscarDisponiveisPorLoja(UUID lojaId) {
        List<Automovel> automoveis = new ArrayList<>();
        for (AutomovelEntity entity : jpaRepository.findByLojaAtualIdAndStatusAutomovel(lojaId, StatusAutomovel.LIVRE)) {
            automoveis.add(converterParaDominio(entity));
        }
        return automoveis;
    }

    private AutomovelEntity converterParaEntity(Automovel a) {
        AutomovelEntity e = new AutomovelEntity();
        e.setId(a.getId());
        e.setPlaca(a.getPlaca());
        e.setModelo(a.getModelo());
        e.setMarca(a.getMarca());
        e.setAno(a.getAno());
        e.setValorDiaria(a.getValorDiaria());
        e.setStatusAutomovel(a.getStatusAutomovel());
        e.setLojaAtualId(a.getLojaAtualId());
        return e;
    }

    private Automovel converterParaDominio(AutomovelEntity e) {
        Automovel a = new Automovel();
        a.setId(e.getId());
        a.setPlaca(e.getPlaca());
        a.setModelo(e.getModelo());
        a.setMarca(e.getMarca());
        a.setAno(e.getAno());
        a.setValorDiaria(e.getValorDiaria());
        a.setStatusAutomovel(e.getStatusAutomovel());
        a.setLojaAtualId(e.getLojaAtualId());
        return a;
    }
}
