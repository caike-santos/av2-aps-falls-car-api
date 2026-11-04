package falls_car_api.application.locacao;

import falls_car_api.domain.locacao.*;
import falls_car_api.domain.shared.DomainException;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Service
public class LocacaoApplicationService {
    private final ILocacaoRepository locacaoRepository;
    private final IPagamentoGateway pagamentoGateway;

    public LocacaoApplicationService(ILocacaoRepository locacaoRepository, IPagamentoGateway pagamentoGateway) {
        this.locacaoRepository = locacaoRepository;
        this.pagamentoGateway = pagamentoGateway;
    }

    public LocacaoResponseDTO efetivarLocacao(EfetivarLocacaoRequestDTO dto) {
        // Dados simulados calculados que viriam da Reserva:
        BigDecimal valorEstimadoReserva = new BigDecimal("1750.00");
        UUID automovelIdSimulado = UUID.randomUUID();
        UUID clienteIdSimulado = UUID.randomUUID();
        UUID lojaRetiradaSimulada = UUID.randomUUID();
        int periodoDiasSimulado = 15;

        Pagamento pagamento = pagamentoGateway.processarCartaoCredito(
                dto.getDadosCartaoCredito().getNumeroCartao(),
                dto.getDadosCartaoCredito().getNomeTitular(),
                dto.getDadosCartaoCredito().getValidade(),
                dto.getDadosCartaoCredito().getCvv(),
                valorEstimadoReserva
        );

        VoCondicaoLocacao voCondicao = new VoCondicaoLocacao(
                dto.getCondicaoLocacao().getIncluiMotorista(),
                dto.getCondicaoLocacao().getTermoResponsabilidadeAceito(),
                dto.getCondicaoLocacao().getObservacoesCondicaoVeiculo()
        );

        Locacao locacao = new Locacao(
                UUID.randomUUID(),
                dto.getReservaId(),
                clienteIdSimulado,
                automovelIdSimulado,
                lojaRetiradaSimulada,
                null,
                voCondicao,
                pagamento,
                periodoDiasSimulado,
                LocalDateTime.now(),
                pagamento.getValor()
        );
        Locacao salva = locacaoRepository.salvar(locacao);
        return LocacaoResponseDTO.fromEntity(salva);
    }
    public LocacaoResponseDTO buscarPorId(UUID id) {
        Locacao locacao = locacaoRepository.buscarPorId(id)
                .orElseThrow(() -> new DomainException("Locação não encontrada com o ID: " + id));
        return LocacaoResponseDTO.fromEntity(locacao);
    }
    public LocacaoResponseDTO finalizarLocacao(UUID id, FinalizarLocacaoDTO dto) {
        Locacao locacao = locacaoRepository.buscarPorId(id)
                .orElseThrow(() -> new DomainException("Locação não encontrada com o ID: " + id));
        locacao.finalizar(dto.getLojaDevolucaoId());
        Locacao atualizada = locacaoRepository.atualizar(locacao);
        return LocacaoResponseDTO.fromEntity(atualizada);
    }
}
