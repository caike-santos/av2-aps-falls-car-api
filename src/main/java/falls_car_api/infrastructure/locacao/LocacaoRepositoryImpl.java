package falls_car_api.infrastructure.locacao;

import falls_car_api.domain.locacao.ILocacaoRepository;
import falls_car_api.domain.locacao.Locacao;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Primary
public class LocacaoRepositoryImpl implements ILocacaoRepository {

    private final SpringDataLocacaoRepository jpaRepository;

    public LocacaoRepositoryImpl(SpringDataLocacaoRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Locacao salvar(Locacao locacao) {
        LocacaoEntity entity = LocacaoEntity.fromDomain(locacao);
        jpaRepository.save(entity);
        return locacao;
    }

    @Override
    public Optional<Locacao> buscarPorId(UUID id) {
        return jpaRepository.findById(id)
                .map(LocacaoEntity::toDomain);
    }
    @Override
    public List<Locacao> listarTodos() {
        return jpaRepository.findAll().stream()
                .map(LocacaoEntity::toDomain)
                .toList();
    }

    @Override
    public Locacao atualizar(Locacao locacao) {
        LocacaoEntity entity = LocacaoEntity.fromDomain(locacao);
        jpaRepository.save(entity);
        return locacao;
    }

    @Override
    public void removerPorId(UUID id) {
        jpaRepository.deleteById(id);
    }
}