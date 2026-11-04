package falls_car_api.domain.locacao;

import falls_car_api.domain.shared.DomainException;
import falls_car_api.domain.shared.MetodoPagamento;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Pagamento {
    private final UUID id;
    private final BigDecimal valor;
    private final LocalDateTime dataPagamento;
    private final MetodoPagamento metodoPagamento;
    private final StatusPagamento statusPagamento;
    private final VoCartaoCredito cartaoCredito;
    public Pagamento(UUID id, BigDecimal valor, MetodoPagamento metodoPagamento, StatusPagamento statusPagamento, VoCartaoCredito cartaoCredito) {
        if (id == null) {
            this.id = UUID.randomUUID();
        } else {
            this.id = id;
        }
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("O valor do pagamento deve ser maior que zero.");
        }
        if (metodoPagamento != MetodoPagamento.CARTAO_CREDITO) {
            throw new DomainException("O pagamento antecipado deve ser exclusivamente via CARTAO_CREDITO.");
        }
        if (statusPagamento == null) {
            throw new DomainException("Status do pagamento é obrigatório.");
        }
        this.valor = valor;
        this.dataPagamento = LocalDateTime.now();
        this.metodoPagamento = metodoPagamento;
        this.statusPagamento = statusPagamento;
        this.cartaoCredito = cartaoCredito;
    }
    public boolean isAprovado() {
        return this.statusPagamento == StatusPagamento.APROVADO;
    }
}
