package com.github.pedrobacchini;

import com.github.pedrobacchini.string.Configuracao;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class ConfiguracaoTest {

    private Configuracao configuracao = new Configuracao();

    @Test
    public void testGetValor() {
        String chave = UUID.randomUUID().toString();
        String valor = UUID.randomUUID().toString();
        configuracao.setValor(chave, valor);
        String valorObtido = configuracao.getValor(chave);
        assertNotNull(valorObtido);
        assertEquals(valorObtido, valor);
    }

    @Test
    public void testIncrementar() {
        String chave = UUID.randomUUID().toString();
        long valor = configuracao.incrementarChave(chave);
        assertEquals(1L, valor);
        assertEquals(2L, configuracao.incrementarChave(chave));
    }

    @Test
    public void testIncrementarPor() {
        String chave = UUID.randomUUID().toString();
        long valor = configuracao.incrementarChave(chave);
        assertEquals(1L, valor);
        assertEquals(3L, configuracao.incrementarPorChave(chave, 2L));
    }

    @Test
    public void testTamanhoConfig() {
        String valor = UUID.randomUUID().toString();
        long tamanho = valor.length();
        String chave = UUID.randomUUID().toString();
        configuracao.setValor(chave, valor);
        assertEquals(tamanho, configuracao.getTamanhoConfig(chave));
    }
}