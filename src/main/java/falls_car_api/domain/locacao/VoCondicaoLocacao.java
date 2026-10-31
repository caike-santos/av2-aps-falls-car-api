package falls_car_api.domain.locacao;

import falls_car_api.domain.shared.DomainException;

public record VoCondicaoLocacao(
        Boolean incluiMotorista,
        Boolean termoResponsabilidadeAceito,
        String observacoesCondicaoVeiculo
) {
    public VoCondicaoLocacao {
        if (termoResponsabilidadeAceito == null || !termoResponsabilidadeAceito) {
            throw new DomainException("O termo de responsabilidade deve ser aceito para efetivar a locação.");
        }
        if (incluiMotorista == null) {
            incluiMotorista = false;
        }
    }
}
