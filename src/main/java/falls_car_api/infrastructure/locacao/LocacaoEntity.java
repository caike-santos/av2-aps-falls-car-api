package falls_car_api.infrastructure.locacao;

import falls_car_api.domain.locacao.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "locacoes")
@Getter
@Setter
@NoArgsConstructor
public class LocacaoEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "reserva_id", nullable = false)
    private UUID reservaId;

    @Column(name = "cliente_id", nullable = false)
    private UUID clienteId;

    @Column(name = "automovel_id", nullable = false)
    private UUID automovelId;

    @Column(name = "loja_retirada_id", nullable = false)
    private UUID lojaRetiradaId;

    @Column(name = "loja_devolucao_id")
    private UUID lojaDevolucaoId;

    @Column(name = "motorista_id")
    private UUID motoristaId;

    @Column(name = "periodo_dias", nullable = false)
    private Integer periodoDias;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_fim_prevista", nullable = false)
    private LocalDateTime dataFimPrevista;

    @Column(name = "data_devolucao_real")
    private LocalDateTime dataDevolucaoReal;

    @Column(name = "valor_total_pago", nullable = false)
    private BigDecimal valorTotalPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_locacao", nullable = false)
    private StatusLocacao statusLocacao;

    @Column(name = "inclui_motorista")
    private Boolean incluiMotorista;

    @Column(name = "termo_responsabilidade_aceito")
    private Boolean termoResponsabilidadeAceito;

    @Column(name = "observacoes_condicao_veiculo")
    private String observacoesCondicaoVeiculo;

    @Column(name = "cartao_numero_mascarado")
    private String cartaoNumeroMascarado;

    @Column(name = "cartao_nome_titular")
    private String cartaoNomeTitular;

    @Column(name = "cartao_validade")
    private String cartaoValidade;

    @Column(name = "cartao_codigo_autorizacao")
    private String cartaoCodigoAutorizacao;

    // Converte a Entidade de Domínio Pura -> Entidade de Banco (JPA)
    public static LocacaoEntity fromDomain(Locacao domain) {
        LocacaoEntity entity = new LocacaoEntity();
        entity.setId(domain.getId());
        entity.setReservaId(domain.getReservaId());
        entity.setClienteId(domain.getClienteId());
        entity.setAutomovelId(domain.getAutomovelId());
        entity.setLojaRetiradaId(domain.getLojaRetiradaId());
        entity.setLojaDevolucaoId(domain.getLojaDevolucaoId());
        entity.setMotoristaId(domain.getMotoristaId());
        entity.setPeriodoDias(domain.getPeriodoDias());
        entity.setDataInicio(domain.getDataInicio());
        entity.setDataFimPrevista(domain.getDataFimPrevista());
        entity.setDataDevolucaoReal(domain.getDataDevolucaoReal());
        entity.setValorTotalPago(domain.getValorTotalPago());
        entity.setStatusLocacao(domain.getStatusLocacao());
        if (domain.getCondicaoLocacao() != null) {
            entity.setIncluiMotorista(domain.getCondicaoLocacao().incluiMotorista());
            entity.setTermoResponsabilidadeAceito(domain.getCondicaoLocacao().termoResponsabilidadeAceito());
            entity.setObservacoesCondicaoVeiculo(domain.getCondicaoLocacao().observacoesCondicaoVeiculo());
        }

        if (domain.getPagamento() != null && domain.getPagamento().getCartaoCredito() != null) {
            entity.setCartaoNumeroMascarado(domain.getPagamento().getCartaoCredito().numeroMascarado());
            entity.setCartaoNomeTitular(domain.getPagamento().getCartaoCredito().nomeTitular());
            entity.setCartaoValidade(domain.getPagamento().getCartaoCredito().validade());
            entity.setCartaoCodigoAutorizacao(domain.getPagamento().getCartaoCredito().codigoAutorizacao());
        }
        return entity;
    }
    public Locacao toDomain() {
        VoCondicaoLocacao voCondicao = new VoCondicaoLocacao(
                this.incluiMotorista,
                this.termoResponsabilidadeAceito,
                this.observacoesCondicaoVeiculo
        );
        VoCartaoCredito voCartao = new VoCartaoCredito(
                this.cartaoNumeroMascarado != null ? this.cartaoNumeroMascarado : "**** **** **** 0000",
                this.cartaoNomeTitular != null ? this.cartaoNomeTitular : "TITULAR",
                this.cartaoValidade != null ? this.cartaoValidade : "12/2030",
                this.cartaoCodigoAutorizacao != null ? this.cartaoCodigoAutorizacao : "AUTH-LOCAL"
        );
        Pagamento pagamento = new Pagamento(
                UUID.randomUUID(),
                this.valorTotalPago,
                falls_car_api.domain.shared.MetodoPagamento.CARTAO_CREDITO,
                StatusPagamento.APROVADO,
                voCartao
        );
        Locacao locacao = new Locacao(
                this.id,
                this.reservaId,
                this.clienteId,
                this.automovelId,
                this.lojaRetiradaId,
                this.motoristaId,
                voCondicao,
                pagamento,
                this.periodoDias,
                this.dataInicio,
                this.valorTotalPago
        );
        if (this.statusLocacao == StatusLocacao.FINALIZADA && this.lojaDevolucaoId != null) {
            locacao.finalizar(this.lojaDevolucaoId);
        }
        return locacao;
    }
}