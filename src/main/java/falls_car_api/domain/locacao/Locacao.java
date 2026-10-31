package falls_car_api.domain.locacao;

import falls_car_api.domain.shared.DomainException;
import falls_car_api.domain.shared.MetodoPagamento;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Locacao {
    private final UUID id;
    private final UUID reservaId;
    private final UUID clienteId;
    private final UUID automovelId;
    private final UUID lojaRetiradaId;
    private UUID lojaDevolucaoId;
    private final UUID motoristaId; // nullable
    private final VoCondicaoLocacao condicaoLocacao;
    private final Pagamento pagamento;
    private final Integer periodoDias; // 7, 15 ou 30
    private final LocalDateTime dataInicio;
    private final LocalDateTime dataFimPrevista;
    private LocalDateTime dataDevolucaoReal;
    private final BigDecimal valorTotalPago;
    private StatusLocacao statusLocacao;

    public Locacao(
            UUID id,
            UUID reservaId,
            UUID clienteId,
            UUID automovelId,
            UUID lojaRetiradaId,
            UUID motoristaId,
            VoCondicaoLocacao condicaoLocacao,
            Pagamento pagamento,
            Integer periodoDias,
            LocalDateTime dataInicio,
            BigDecimal valorTotalPago
    ) {
        if (pagamento == null || !pagamento.isAprovado() || pagamento.getMetodoPagamento() != MetodoPagamento.CARTAO_CREDITO) {
            throw new DomainException("Uma Locação não pode ser iniciada sem um Pagamento APROVADO via CARTAO_CREDITO.");
        }
        if (!List.of(7, 15, 30).contains(periodoDias)) {
            throw new DomainException("O período de locação deve ser obrigatoriamente de 7, 15 ou 30 dias.");
        }
        this.id = (id != null) ? id : UUID.randomUUID();
        this.reservaId = reservaId;
        this.clienteId = clienteId;
        this.automovelId = automovelId;
        this.lojaRetiradaId = lojaRetiradaId;
        this.motoristaId = motoristaId;
        this.condicaoLocacao = condicaoLocacao;
        this.pagamento = pagamento;
        this.periodoDias = periodoDias;
        this.dataInicio = (dataInicio != null) ? dataInicio : LocalDateTime.now();
        this.dataFimPrevista = this.dataInicio.plusDays(periodoDias);
        this.valorTotalPago = valorTotalPago;
        this.statusLocacao = StatusLocacao.ATIVA;
    }

    public void finalizar(UUID lojaDevolucaoId) {
        if (this.statusLocacao == StatusLocacao.FINALIZADA) {
            throw new DomainException("Esta locação já foi finalizada.");
        }
        if (lojaDevolucaoId == null) {
            throw new DomainException("A loja de devolução deve ser informada.");
        }
        this.lojaDevolucaoId = lojaDevolucaoId;
        this.dataDevolucaoReal = LocalDateTime.now();
        this.statusLocacao = StatusLocacao.FINALIZADA;
    }
}
