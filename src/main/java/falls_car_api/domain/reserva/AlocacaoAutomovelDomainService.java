package falls_car_api.domain.reserva;

import falls_car_api.domain.frota.Automovel;
import falls_car_api.domain.frota.IAutomovelRepository;
import falls_car_api.domain.loja.ILojaRepository;
import falls_car_api.domain.loja.Loja;
import falls_car_api.domain.shared.DomainException;
import falls_car_api.domain.shared.EntidadeNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlocacaoAutomovelDomainService {

    private final ILojaRepository lojaRepository;
    private final IAutomovelRepository automovelRepository;

    public record ResultadoAlocacao(Automovel automovel, UUID lojaOrigemAutomovelId, UUID lojaRetiradaId) {}

    public ResultadoAlocacao alocarAutomovelMaisProximo(UUID lojaRetiradaId) {
        if (lojaRetiradaId == null) {
            throw new DomainException("A loja de retirada (lojaRetiradaId) é obrigatória.");
        }

        Loja lojaRetirada = lojaRepository.buscarPorId(lojaRetiradaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Loja de retirada não encontrada."));

        if (!Boolean.TRUE.equals(lojaRetirada.getIsAtivo())) {
            throw new DomainException("A loja de retirada informada está inativa.");
        }

        // 1. Verifica se há automóvel com statusAutomovel == LIVRE onde lojaAtualId == lojaRetiradaId
        List<Automovel> disponiveisNaLojaRetirada = automovelRepository.buscarDisponiveisPorLoja(lojaRetiradaId);
        if (!disponiveisNaLojaRetirada.isEmpty()) {
            Automovel selecionado = disponiveisNaLojaRetirada.get(0);
            selecionado.reservar();
            automovelRepository.atualizar(selecionado);
            return new ResultadoAlocacao(selecionado, lojaRetiradaId, lojaRetiradaId);
        }

        // 2. Se não houver, consulta as demais lojas da mesma cidade em ordem crescente de distância
        List<Loja> lojasProximas = lojaRepository.buscarLojasMesmaCidadeOrdenadasPorProximidade(lojaRetiradaId);

        // 3. Seleciona o primeiro automóvel LIVRE da loja mais próxima, marca como RESERVADO e registra lojaOrigemAutomovelId e lojaRetiradaId
        for (Loja lojaProxima : lojasProximas) {
            if (!Boolean.TRUE.equals(lojaProxima.getIsAtivo())) {
                continue;
            }
            List<Automovel> disponiveisNaLojaProxima = automovelRepository.buscarDisponiveisPorLoja(lojaProxima.getId());
            if (!disponiveisNaLojaProxima.isEmpty()) {
                Automovel selecionado = disponiveisNaLojaProxima.get(0);
                selecionado.reservar();
                automovelRepository.atualizar(selecionado);
                return new ResultadoAlocacao(selecionado, lojaProxima.getId(), lojaRetiradaId);
            }
        }

        // 4. Caso não exista veículo livre em nenhuma loja daquela cidade, lança SemFrotaDisponivelNaCidadeException
        throw new SemFrotaDisponivelNaCidadeException(
                "Não há automóvel livre disponível em nenhuma loja da cidade de " + lojaRetirada.getCidade() + "."
        );
    }
}
