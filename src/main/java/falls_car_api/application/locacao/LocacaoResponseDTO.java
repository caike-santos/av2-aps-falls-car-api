package falls_car_api.application.locacao;
import falls_car_api.domain.locacao.Locacao;
import falls_car_api.domain.locacao.StatusLocacao;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Builder
public class LocacaoResponseDTO {
    private UUID id;
    private UUID reservaId;
    private UUID clienteId;
    private UUID automovelId;
    private UUID lojaRetiradaId;
    private UUID lojaDevolucaoId;
    private UUID motoristaId;
    private Integer periodoDias;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFimPrevista;
    private LocalDateTime dataDevolucaoReal;
    private BigDecimal valorTotalPago;
    private StatusLocacao statusLocacao;
    private String cartaoMascarado;
    private String comprovanteAutorizacao;
    public static LocacaoResponseDTO fromEntity(Locacao locacao) {
        return LocacaoResponseDTO.builder()
                .id(locacao.getId())
                .reservaId(locacao.getReservaId())
                .clienteId(locacao.getClienteId())
                .automovelId(locacao.getAutomovelId())
                .lojaRetiradaId(locacao.getLojaRetiradaId())
                .lojaDevolucaoId(locacao.getLojaDevolucaoId())
                .motoristaId(locacao.getMotoristaId())
                .periodoDias(locacao.getPeriodoDias())
                .dataInicio(locacao.getDataInicio())
                .dataFimPrevista(locacao.getDataFimPrevista())
                .dataDevolucaoReal(locacao.getDataDevolucaoReal())
                .valorTotalPago(locacao.getValorTotalPago())
                .statusLocacao(locacao.getStatusLocacao())
                .cartaoMascarado(locacao.getPagamento().getCartaoCredito().numeroMascarado())
                .comprovanteAutorizacao(locacao.getPagamento().getCartaoCredito().codigoAutorizacao())
                .build();
    }
}
