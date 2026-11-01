package falls_car_api.infrastructure.persistence;

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
        // Converte domínio -> tabela do banco
        LocacaoEntity entity = LocacaoEntity.fromDomain(locacao);
        jpaRepository.save(entity);
        return locacao;
    }

    @Override
    public Optional<Locacao> buscarPorId(UUID id) {
        // Busca do banco e converte tabela -> domínio
        return jpaRepository.findById(id)
                .map(entity -> entity.toDomain(null)); // Você pode passar o pagamento associado
    }

    @Override
    public List<Locacao> listarTodos() {
        return jpaRepository.findAll().stream()
                .map(entity -> entity.toDomain(null))
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