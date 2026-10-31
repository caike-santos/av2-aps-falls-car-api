package falls_car_api.application.locacao;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
@Data
@Builder
public class RelatorioEstatisticoResponseDTO {
    private PeriodoAnalisadoDTO periodoAnalisado;
    private int totalLocacoesRealizadas;
    private BigDecimal receitaTotalAntecipada;
    private Map<String, Long> locacoesPorPeriodo;
    private Map<String, Long> locacoesPorCanal;
    private double percentualComMotorista;
    private int totalDeslocamentosEntreLojasMesmaCidade;
    private Map<String, Integer> statusAtualFrota;

    @Data
    @Builder
    public static class PeriodoAnalisadoDTO {
        private LocalDate dataInicio;
        private LocalDate dataFim;
    }
}
