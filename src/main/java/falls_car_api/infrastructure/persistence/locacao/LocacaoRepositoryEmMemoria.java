package falls_car_api.infrastructure.persistence.locacao;
import falls_car_api.domain.locacao.ILocacaoRepository;
import falls_car_api.domain.locacao.Locacao;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
@Repository
public class LocacaoRepositoryEmMemoria implements ILocacaoRepository {
    private final Map<UUID, Locacao> tabela = new ConcurrentHashMap<>();
    @Override
    public Locacao salvar(Locacao locacao) {
        tabela.put(locacao.getId(), locacao);
        return locacao;
    }
    @Override
    public Optional<Locacao> buscarPorId(UUID id) {
        return Optional.ofNullable(tabela.get(id));
    }
    @Override
    public List<Locacao> listarTodos() {
        return new ArrayList<>(tabela.values());
    }
    @Override
    public Locacao atualizar(Locacao locacao) {
        tabela.put(locacao.getId(), locacao);
        return locacao;
    }
    @Override
    public void removerPorId(UUID id) {
        tabela.remove(id);
    }
}
