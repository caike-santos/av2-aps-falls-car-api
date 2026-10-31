package falls_car_api.domain.locacao;

import java.math.BigDecimal;

public interface IPagamentoGateway {
    Pagamento processarCartaoCredito(
            String numeroCartao,
            String nomeTitular,
            String validade,
            String cvv,
            BigDecimal valor
    );
}
