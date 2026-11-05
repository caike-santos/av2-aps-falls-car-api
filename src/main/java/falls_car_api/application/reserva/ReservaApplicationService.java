package falls_car_api.application.reserva;

import falls_car_api.domain.frota.Automovel;
import falls_car_api.domain.frota.IAutomovelRepository;
import falls_car_api.domain.reserva.AlocacaoAutomovelDomainService;
import falls_car_api.domain.reserva.IReservaRepository;
import falls_car_api.domain.reserva.Reserva;
import falls_car_api.domain.reserva.StatusReserva;
import falls_car_api.domain.shared.DomainException;
import falls_car_api.domain.shared.EntidadeNaoEncontradaException;
import falls_car_api.domain.shared.PeriodoLocacao;
import falls_car_api.domain.shared.StatusAutomovel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservaApplicationService {

    private static final BigDecimal VALOR_DIARIA_MOTORISTA_PADRAO = new BigDecimal("150.00");

    private final IReservaRepository reservaRepository;
    private final IAutomovelRepository automovelRepository;
    private final AlocacaoAutomovelDomainService alocacaoAutomovelDomainService;

    @Transactional
    public ReservaResponseDTO criar(ReservaRequestDTO requestDto) {
        PeriodoLocacao periodoLocacao = resolverPeriodoLocacao(requestDto);

        AlocacaoAutomovelDomainService.ResultadoAlocacao alocacao =
                alocacaoAutomovelDomainService.alocarAutomovelMaisProximo(requestDto.getLojaRetiradaId());

        Reserva reserva = new Reserva();
        UUID id = UUID.randomUUID();
        reserva.setId(id);
        reserva.setCodigoReserva(gerarCodigoReserva(id));
        reserva.setClienteId(requestDto.getClienteId());
        reserva.setAutomovelId(alocacao.automovel().getId());
        reserva.setLojaOrigemAutomovelId(alocacao.lojaOrigemAutomovelId());
        reserva.setLojaRetiradaId(alocacao.lojaRetiradaId());
        reserva.setCanalReserva(requestDto.getCanalReserva());
        reserva.setPeriodoLocacao(periodoLocacao);
        reserva.setIncluiMotorista(requestDto.getIncluiMotorista());
        reserva.setMotoristaId(Boolean.TRUE.equals(requestDto.getIncluiMotorista()) ? requestDto.getMotoristaId() : null);
        reserva.setDataRetirada(requestDto.getDataRetirada());
        reserva.setStatusReserva(StatusReserva.PENDENTE_PAGAMENTO);

        reserva.calcularDatasEValorEstimado(alocacao.automovel().getValorDiaria(), VALOR_DIARIA_MOTORISTA_PADRAO);

        reservaRepository.salvar(reserva);
        return converterParaDto(reserva);
    }

    public ReservaResponseDTO buscarPorId(UUID id) {
        return converterParaDto(buscarOuFalhar(id));
    }

    public List<ReservaResponseDTO> listarTodos() {
        List<ReservaResponseDTO> resposta = new ArrayList<>();
        for (Reserva reserva : reservaRepository.listarTodos()) {
            resposta.add(converterParaDto(reserva));
        }
        return resposta;
    }

    @Transactional
    public ReservaResponseDTO cancelarReserva(UUID id) {
        Reserva reserva = buscarOuFalhar(id);
        reserva.cancelar();

        liberarAutomovelDaReservaSeNecessario(reserva);

        reservaRepository.atualizar(reserva);
        return converterParaDto(reserva);
    }

    @Transactional
    public ReservaResponseDTO atualizar(UUID id, ReservaRequestDTO requestDto) {
        Reserva reserva = buscarOuFalhar(id);
        if (reserva.getStatusReserva() == StatusReserva.CANCELADA) {
            throw new DomainException("Não é possível atualizar uma reserva que já foi CANCELADA.");
        }

        PeriodoLocacao periodoLocacao = resolverPeriodoLocacao(requestDto);

        reserva.setClienteId(requestDto.getClienteId());
        reserva.setCanalReserva(requestDto.getCanalReserva());
        reserva.setPeriodoLocacao(periodoLocacao);
        reserva.setIncluiMotorista(requestDto.getIncluiMotorista());
        reserva.setMotoristaId(Boolean.TRUE.equals(requestDto.getIncluiMotorista()) ? requestDto.getMotoristaId() : null);
        reserva.setDataRetirada(requestDto.getDataRetirada());

        Automovel automovel = automovelRepository.buscarPorId(reserva.getAutomovelId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Automóvel vinculado à reserva não encontrado."));

        reserva.calcularDatasEValorEstimado(automovel.getValorDiaria(), VALOR_DIARIA_MOTORISTA_PADRAO);

        reservaRepository.atualizar(reserva);
        return converterParaDto(reserva);
    }

    @Transactional
    public void remover(UUID id) {
        Reserva reserva = buscarOuFalhar(id);
        if (reserva.getStatusReserva() != StatusReserva.CANCELADA) {
            liberarAutomovelDaReservaSeNecessario(reserva);
        }
        reservaRepository.removerPorId(id);
    }

    private void liberarAutomovelDaReservaSeNecessario(Reserva reserva) {
        if (reserva.getAutomovelId() == null) {
            return;
        }
        automovelRepository.buscarPorId(reserva.getAutomovelId()).ifPresent(automovel -> {
            if (automovel.getStatusAutomovel() == StatusAutomovel.RESERVADO) {
                UUID lojaDestino = automovel.getLojaAtualId() != null
                        ? automovel.getLojaAtualId()
                        : reserva.getLojaOrigemAutomovelId();
                automovel.liberarVeiculo(lojaDestino);
                automovelRepository.atualizar(automovel);
            }
        });
    }

    private PeriodoLocacao resolverPeriodoLocacao(ReservaRequestDTO requestDto) {
        if (requestDto.getPeriodoDias() != null) {
            return Reserva.converterEValidarPeriodoDias(requestDto.getPeriodoDias());
        }
        if (requestDto.getPeriodoLocacao() != null) {
            return requestDto.getPeriodoLocacao();
        }
        throw new DomainException("O período de locação deve ser obrigatoriamente de 7, 15 ou 30 dias.");
    }

    private Reserva buscarOuFalhar(UUID id) {
        return reservaRepository.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Reserva não encontrada."));
    }

    private String gerarCodigoReserva(UUID id) {
        return "RES-" + id.toString().replace("-", "").substring(0, 8).toUpperCase(Locale.ROOT);
    }

    private ReservaResponseDTO converterParaDto(Reserva reserva) {
        ReservaResponseDTO dto = new ReservaResponseDTO();
        dto.setId(reserva.getId());
        dto.setCodigoReserva(reserva.getCodigoReserva());
        dto.setClienteId(reserva.getClienteId());
        dto.setAutomovelId(reserva.getAutomovelId());
        dto.setLojaOrigemAutomovelId(reserva.getLojaOrigemAutomovelId());
        dto.setLojaRetiradaId(reserva.getLojaRetiradaId());
        dto.setCanalReserva(reserva.getCanalReserva());
        dto.setPeriodoLocacao(reserva.getPeriodoLocacao());
        dto.setPeriodoDias(reserva.getPeriodoLocacao() != null ? reserva.getPeriodoLocacao().getDias() : null);
        dto.setIncluiMotorista(reserva.getIncluiMotorista());
        dto.setMotoristaId(reserva.getMotoristaId());
        dto.setDataRetirada(reserva.getDataRetirada());
        dto.setDataDevolucaoPrevista(reserva.getDataDevolucaoPrevista());
        dto.setValorTotalEstimado(reserva.getValorTotalEstimado());
        dto.setStatusReserva(reserva.getStatusReserva());
        return dto;
    }
}
