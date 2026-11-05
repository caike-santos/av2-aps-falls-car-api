package falls_car_api.infrastructure.payment;

import falls_car_api.domain.locacao.IPagamentoGateway;
import falls_car_api.domain.locacao.Pagamento;
import falls_car_api.domain.locacao.StatusPagamento;
import falls_car_api.domain.locacao.VoCartaoCredito;
import falls_car_api.domain.shared.DomainException;
import falls_car_api.domain.shared.MetodoPagamento;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.UUID;

@Component
public class CartaoCreditoGatewaySimulado implements IPagamentoGateway{

    @Override
    public Pagamento processarCartaoCredito(String numeroCartao, String nomeTitular, String validade, String cvv, BigDecimal valor) {
        if (numeroCartao == null || numeroCartao.replaceAll("\\s+", "").length() < 16) {
            throw new DomainException("Número do cartão de crédito inválido.");
        }
        if (cvv == null || cvv.length() < 3) {
            throw new DomainException("CVV do cartão de crédito inválido.");
        }

        String digitosLimpos = numeroCartao.replaceAll("\\s+", "");
        String ultimosQuatro = digitosLimpos.substring(digitosLimpos.length() - 4);
        String numeroMascarado = "**** **** **** " + ultimosQuatro;

        String codigoAutorizacao = "AUTH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        VoCartaoCredito voCartao = new VoCartaoCredito(
                numeroMascarado,
                nomeTitular,
                validade,
                codigoAutorizacao
        );
        return new Pagamento(
                UUID.randomUUID(),
                valor,
                MetodoPagamento.CARTAO_CREDITO,
                StatusPagamento.APROVADO,
                voCartao
        );
    }
}
