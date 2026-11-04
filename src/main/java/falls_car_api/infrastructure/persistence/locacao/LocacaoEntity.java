package falls_car_api.infrastructure.persistence.locacao;

import falls_car_api.domain.locacao.Locacao;
import falls_car_api.domain.locacao.Pagamento;
import falls_car_api.domain.locacao.StatusLocacao;
import falls_car_api.domain.locacao.VoCondicaoLocacao;
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

        return entity;
    }

    // Converte a Entidade de Banco (JPA) -> Entidade de Domínio Pura
    public Locacao toDomain(Pagamento pagamento) {
        VoCondicaoLocacao voCondicao = new VoCondicaoLocacao(
                this.incluiMotorista,
                this.termoResponsabilidadeAceito,
                this.observacoesCondicaoVeiculo
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