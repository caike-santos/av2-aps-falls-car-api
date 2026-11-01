package falls_car_api.infrastructure.loja;

import falls_car_api.domain.loja.ILojaRepository;
import falls_car_api.domain.loja.Loja;
import falls_car_api.domain.loja.VoCoordenada;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LojaRepositoryImpl implements ILojaRepository {

    private final LojaJpaRepository jpaRepository;

    @Override
    public void salvar(Loja entidade) {
        LojaEntity entity = converterParaEntity(entidade);
        jpaRepository.save(entity);
    }

    @Override
    public Optional<Loja> buscarPorId(UUID id) {
        return jpaRepository.findById(id).map(this::converterParaDominio);
    }

    @Override
    public List<Loja> listarTodos() {
        return jpaRepository.findAll().stream()
                .map(this::converterParaDominio)
                .collect(Collectors.toList());
    }

    @Override
    public void atualizar(Loja entidade) {
        salvar(entidade); // No JPA o save serve tanto para criar como atualizar
    }

    @Override
    public void removerPorId(UUID id) {
        jpaRepository.deleteById(id);
    }

    // --- Método adicionado para corrigir o erro da interface ---
    @Override
    public List<Loja> buscarLojasMesmaCidadeOrdenadasPorProximidade(UUID lojaRetiradaId) {
        // 1. Encontrar a loja original para saber a cidade e as coordenadas
        LojaEntity lojaBaseEntity = jpaRepository.findById(lojaRetiradaId)
                .orElseThrow(() -> new IllegalArgumentException("Loja base não encontrada."));
        Loja lojaBase = converterParaDominio(lojaBaseEntity);

        // 2. Pedir à base de dados todas as lojas dessa mesma cidade
        List<LojaEntity> lojasNaMesmaCidade = jpaRepository.findByCidade(lojaBase.getCidade());

        // 3. Converter, remover a loja original da lista, e ordenar pela distância matemática
        return lojasNaMesmaCidade.stream()
                .map(this::converterParaDominio)
                .filter(loja -> !loja.getId().equals(lojaBase.getId())) // Remove a própria loja da lista
                .sorted((loja1, loja2) -> {
                    Double dist1 = lojaBase.calcularDistanciaAte(loja1);
                    Double dist2 = lojaBase.calcularDistanciaAte(loja2);
                    return dist1.compareTo(dist2);
                })
                .collect(Collectors.toList());
    }

    // Métodos de conversão
    private LojaEntity converterParaEntity(Loja loja) {
        LojaEntity entity = new LojaEntity();
        entity.setId(loja.getId());
        entity.setNome(loja.getNome());
        entity.setTipoLoja(loja.getTipo());
        entity.setCidade(loja.getCidade());
        entity.setEstado(loja.getEstado());
        entity.setEnderecoCompleto(loja.getEnderecoCompleto());
        
        if (loja.getCoordenada() != null) {
            entity.setLatitude(loja.getCoordenada().latitude());
            entity.setLongitude(loja.getCoordenada().longitude());
        }
        
        entity.setIsAtiva(loja.getIsAtivo());
        return entity;
    }

    private Loja converterParaDominio(LojaEntity entity) {
        Loja loja = new Loja();
        loja.setId(entity.getId());
        loja.setNome(entity.getNome());
        loja.setTipo(entity.getTipoLoja());
        loja.setCidade(entity.getCidade());
        loja.setEstado(entity.getEstado());
        loja.setEnderecoCompleto(entity.getEnderecoCompleto());
        
        if (entity.getLatitude() != null && entity.getLongitude() != null) {
            loja.setCoordenada(new VoCoordenada(entity.getLatitude(), entity.getLongitude()));
        }
        
        loja.setIsAtivo(entity.getIsAtiva());
        return loja;
    }
}