package falls_car_api.domain.locacao;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class RelatorioDomainService {

    public DadosEstatistico consolidar(List<Locacao> locacoes, LocalDate inicio, LocalDate fim) {
        List<Locacao> filtradas = locacoes.stream()
                .filter(l -> {
                    LocalDate dataLoc = l.getDataInicio().toLocalDate();
                    return !dataLoc.isBefore(inicio) && !dataLoc.isAfter(fim);
                })
                .toList();
        int total = filtradas.size();
        BigDecimal receita = filtradas.stream()
                .map(Locacao::getValorTotalPago)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Map<String, Long> porPeriodo = new HashMap<>();
        porPeriodo.put("SETE_DIAS", filtradas.stream().filter(l -> l.getPeriodoDias() == 7).count());
        porPeriodo.put("QUINZE_DIAS", filtradas.stream().filter(l -> l.getPeriodoDias() == 15).count());
        porPeriodo.put("TRINTA_DIAS", filtradas.stream().filter(l -> l.getPeriodoDias() == 30).count());
        long comMotorista = filtradas.stream()
                .filter(l -> l.getCondicaoLocacao() != null && Boolean.TRUE.equals(l.getCondicaoLocacao().incluiMotorista()))
                .count();
        double percentualMotorista = total == 0 ? 0.0 : (comMotorista * 100.0) / total;
        return new DadosEstatistico(total, receita, porPeriodo, percentualMotorista);
    }

    public record DadosEstatistico(
            int totalLocacoes,
            BigDecimal receitaTotal,
            Map<String, Long> locacoesPorPeriodo,
            double percentualComMotorista
    ) {}
}
