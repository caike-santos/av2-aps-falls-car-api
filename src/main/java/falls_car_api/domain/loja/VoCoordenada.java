package falls_car_api.domain.loja;

import falls_car_api.domain.shared.DomainException;

public record VoCoordenada(Double latitude, Double longitude) {
    
    public VoCoordenada {
        if (latitude == null || longitude == null) {
            throw new DomainException("Latitude e longitude são obrigatórias para calcular a distância.");
        }
    }

    // Método obrigatório para encontrar a loja mais próxima[cite: 4]
    public Double calcularDistanciaEmKm(VoCoordenada outraCoordenada) {
        if (outraCoordenada == null) return null;
        
        final int R = 6371; // Raio da terra em km

        double latDistance = Math.toRadians(outraCoordenada.latitude() - this.latitude);
        double lonDistance = Math.toRadians(outraCoordenada.longitude() - this.longitude);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                 + Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(outraCoordenada.latitude()))
                 * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
                 
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }
}