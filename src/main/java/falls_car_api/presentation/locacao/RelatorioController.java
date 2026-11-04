package falls_car_api.presentation.locacao;

import falls_car_api.application.locacao.RelatorioApplicationService;
import falls_car_api.application.locacao.RelatorioEstatisticoResponseDTO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/relatorios")
public class RelatorioController {
    private final RelatorioApplicationService relatorioService;
    public RelatorioController(RelatorioApplicationService relatorioService) {
        this.relatorioService = relatorioService;
    }
    @GetMapping("/estatisticos-locacoes")
    public ResponseEntity<RelatorioEstatisticoResponseDTO> emitirRelatorioEstatistico(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim
    ) {
        return ResponseEntity.ok(relatorioService.emitirRelatorioEstatistico(dataInicio, dataFim));
    }
}
