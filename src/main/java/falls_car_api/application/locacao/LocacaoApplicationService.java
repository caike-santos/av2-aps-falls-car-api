package falls_car_api.application.locacao;

import falls_car_api.domain.frota.Automovel;
import falls_car_api.domain.frota.IAutomovelRepository;
import falls_car_api.domain.locacao.*;
import falls_car_api.domain.reserva.IReservaRepository;
import falls_car_api.domain.reserva.Reserva;
import falls_car_api.domain.shared.DomainException;
// Imports dos módulos dos seus colegas quando eles subirem na develop:
// import falls_car_api.domain.reserva.IReservaRepository;
// import falls_car_api.domain.reserva.Reserva;
// import falls_car_api.domain.frota.IAutomovelRepository;
// import falls_car_api.domain.frota.Automovel;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class LocacaoApplicationService {

    private final ILocacaoRepository locacaoRepository;
    private final IPagamentoGateway pagamentoGateway;
    private final IReservaRepository reservaRepository;
    private final IAutomovelRepository automovelRepository;

    public LocacaoApplicationService(
            ILocacaoRepository locacaoRepository,
            IPagamentoGateway pagamentoGateway,
            IReservaRepository reservaRepository,
            IAutomovelRepository automovelRepository
    ) {
        this.locacaoRepository = locacaoRepository;
        this.pagamentoGateway = pagamentoGateway;
        this.reservaRepository = reservaRepository;
        this.automovelRepository = automovelRepository;
    }

    @Transactional
    public LocacaoResponseDTO efetivarLocacao(EfetivarLocacaoRequestDTO dto) {

        Reserva reserva = reservaRepository.buscarPorId(dto.getReservaId())
                .orElseThrow(() -> new DomainException("Reserva não encontrada com o ID: " + dto.getReservaId()));

        Pagamento pagamento = pagamentoGateway.processarCartaoCredito(
                dto.getDadosCartaoCredito().getNumeroCartao(),
                dto.getDadosCartaoCredito().getNomeTitular(),
                dto.getDadosCartaoCredito().getValidade(),
                dto.getDadosCartaoCredito().getCvv(),
                reserva.getValorTotalEstimado()
        );

        VoCondicaoLocacao voCondicao = new VoCondicaoLocacao(
                dto.getCondicaoLocacao().getIncluiMotorista(),
                dto.getCondicaoLocacao().getTermoResponsabilidadeAceito(),
                dto.getCondicaoLocacao().getObservacoesCondicaoVeiculo()
        );

        Locacao locacao = new Locacao(
                UUID.randomUUID(),
                reserva.getId(),
                reserva.getClienteId(),
                reserva.getAutomovelId(),
                reserva.getLojaRetiradaId(),
                reserva.getMotoristaId(),
                voCondicao,
                pagamento,
                reserva.getPeriodoLocacao().getDias(),
                LocalDateTime.now(),
                pagamento.getValor()
        );

        Automovel automovel = automovelRepository.buscarPorId(reserva.getAutomovelId())
                .orElseThrow(() -> new DomainException("Automóvel vinculado à reserva não encontrado."));

        automovel.alugar();
        automovelRepository.atualizar(automovel);

        Locacao locacaoSalva = locacaoRepository.salvar(locacao);

        return LocacaoResponseDTO.fromEntity(locacaoSalva);
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