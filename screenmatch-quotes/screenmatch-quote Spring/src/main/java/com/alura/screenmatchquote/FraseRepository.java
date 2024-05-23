package com.alura.screenmatchquote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FraseRepository extends JpaRepository<Frase,Long> {

    @Query("SELECT f FROM Frase f ORDER BY function('RANDOM')LIMIT 1")

    public Frase obtenerFraseAleatoria();
}
