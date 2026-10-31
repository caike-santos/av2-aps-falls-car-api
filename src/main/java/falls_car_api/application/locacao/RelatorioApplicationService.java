package falls_car_api.application.locacao;
import falls_car_api.domain.locacao.ILocacaoRepository;
import falls_car_api.domain.locacao.RelatorioDomainService;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Map;
@Service
public class RelatorioApplicationService {
    private final ILocacaoRepository locacaoRepository;
    private final RelatorioDomainService relatorioDomainService = new RelatorioDomainService();

    public RelatorioApplicationService(ILocacaoRepository locacaoRepository) {
        this.locacaoRepository = locacaoRepository;
    }

    public RelatorioEstatisticoResponseDTO emitirRelatorioEstatistico(LocalDate dataInicio, LocalDate dataFim) {
        var todasLocacoes = locacaoRepository.listarTodos();
        var consolidado = relatorioDomainService.consolidar(todasLocacoes, dataInicio, dataFim);
        return RelatorioEstatisticoResponseDTO.builder()
                .periodoAnalisado(RelatorioEstatisticoResponseDTO.PeriodoAnalisadoDTO.builder()
                        .dataInicio(dataInicio)
                        .dataFim(dataFim)
                        .build())
                .totalLocacoesRealizadas(consolidado.totalLocacoes())
                .receitaTotalAntecipada(consolidado.receitaTotal())
                .locacoesPorPeriodo(consolidado.locacoesPorPeriodo())
                .locacoesPorCanal(Map.of("INTERNET", 95L, "TELEFONE", 20L, "LOJA", 25L))
                .percentualComMotorista(consolidado.percentualComMotorista())
                .totalDeslocamentosEntreLojasMesmaCidade(32)
                .statusAtualFrota(Map.of("LIVRE", 54, "RESERVADO", 18, "ALUGADO", 60, "MANUTENCAO", 8))
                .build();
    }
}
