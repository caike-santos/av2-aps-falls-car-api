package falls_car_api.application.locacao;

import falls_car_api.domain.frota.Automovel;
import falls_car_api.domain.frota.IAutomovelRepository;
import falls_car_api.domain.locacao.ILocacaoRepository;
import falls_car_api.domain.locacao.Locacao;
import falls_car_api.domain.locacao.RelatorioDomainService;
import falls_car_api.domain.reserva.IReservaRepository;
import falls_car_api.domain.reserva.Reserva;
import falls_car_api.domain.shared.DomainException;
import falls_car_api.domain.shared.StatusAutomovel;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class RelatorioApplicationService {

    private final ILocacaoRepository locacaoRepository;
    private final IAutomovelRepository automovelRepository;
    private final IReservaRepository reservaRepository;
    private final RelatorioDomainService relatorioDomainService = new RelatorioDomainService();

    public RelatorioApplicationService(
            ILocacaoRepository locacaoRepository,
            IAutomovelRepository automovelRepository,
            IReservaRepository reservaRepository
    ) {
        this.locacaoRepository = locacaoRepository;
        this.automovelRepository = automovelRepository;
        this.reservaRepository = reservaRepository;
    }

    public RelatorioEstatisticoResponseDTO emitirRelatorioEstatistico(LocalDate dataInicio, LocalDate dataFim) {

        if (dataInicio != null && dataFim != null && dataInicio.isAfter(dataFim)) {
            throw new DomainException("A data de início não pode ser posterior à data de fim.");
        }

        List<Locacao> todasLocacoes = locacaoRepository.listarTodos();
        var consolidado = relatorioDomainService.consolidar(todasLocacoes, dataInicio, dataFim);

        List<Automovel> todosAutomoveis = automovelRepository.listarTodos();
        Map<String, Integer> statusAtualFrota = new HashMap<>();
        statusAtualFrota.put("LIVRE", (int) todosAutomoveis.stream().filter(a -> a.getStatusAutomovel() == StatusAutomovel.LIVRE).count());
        statusAtualFrota.put("RESERVADO", (int) todosAutomoveis.stream().filter(a -> a.getStatusAutomovel() == StatusAutomovel.RESERVADO).count());
        statusAtualFrota.put("ALUGADO", (int) todosAutomoveis.stream().filter(a -> a.getStatusAutomovel() == StatusAutomovel.ALUGADO).count());
        statusAtualFrota.put("MANUTENCAO", (int) todosAutomoveis.stream().filter(a -> a.getStatusAutomovel() == StatusAutomovel.MANUTENCAO).count());

        List<Reserva> todasReservas = reservaRepository.listarTodos();

        Map<String, Long> locacoesPorCanal = new HashMap<>();
        locacoesPorCanal.put("INTERNET", todasReservas.stream().filter(r -> r.getCanalReserva() != null && "INTERNET".equalsIgnoreCase(r.getCanalReserva().name())).count());
        locacoesPorCanal.put("TELEFONE", todasReservas.stream().filter(r -> r.getCanalReserva() != null && "TELEFONE".equalsIgnoreCase(r.getCanalReserva().name())).count());
        locacoesPorCanal.put("LOJA", todasReservas.stream().filter(r -> r.getCanalReserva() != null && "LOJA".equalsIgnoreCase(r.getCanalReserva().name())).count());

        int totalDeslocamentos = (int) todasReservas.stream()
                .filter(r -> r.getLojaOrigemAutomovelId() != null
                        && r.getLojaRetiradaId() != null
                        && !r.getLojaOrigemAutomovelId().equals(r.getLojaRetiradaId()))
                .count();

        return RelatorioEstatisticoResponseDTO.builder()
                .periodoAnalisado(RelatorioEstatisticoResponseDTO.PeriodoAnalisadoDTO.builder()
                        .dataInicio(dataInicio)
                        .dataFim(dataFim)
                        .build())
                .totalLocacoesRealizadas(consolidado.totalLocacoes())
                .receitaTotalAntecipada(consolidado.receitaTotal())
                .locacoesPorPeriodo(consolidado.locacoesPorPeriodo())
                .locacoesPorCanal(locacoesPorCanal)
                .percentualComMotorista(consolidado.percentualComMotorista())
                .totalDeslocamentosEntreLojasMesmaCidade(totalDeslocamentos)
                .statusAtualFrota(statusAtualFrota)
                .build();
    }
}