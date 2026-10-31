package falls_car_api.domain.locacao;

import falls_car_api.domain.shared.DomainException;

public record VoCartaoCredito(
        String numeroMascarado,
        String nomeTitular,
        String validade,
        String codigoAutorizacao) {
    public VoCartaoCredito {
        if (numeroMascarado == null || numeroMascarado.isBlank()) {
            throw new DomainException("Número mascarado do cartão é obrigatório.");
        }
        if (codigoAutorizacao == null || codigoAutorizacao.isBlank()) {
            throw new DomainException("Código de autorização é obrigatório.");
        }
    }
}
