package com.github.pedrobacchini;

import com.github.pedrobacchini.domain.Pessoa;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class SerializadorRedisTest {

    private SerializadorRedis serializadorRedis = new SerializadorRedis();
    private Pessoa pessoa = new Pessoa("Pedro", "Bacchini");

    @Test
    public void testSetObjeto() throws IOException, ClassNotFoundException {
        serializadorRedis.setObjeto(serializadorRedis.getChave(pessoa), pessoa);
        Pessoa serializado = (Pessoa) serializadorRedis.getObjeto(serializadorRedis.getChave(pessoa));
        assertNotNull(serializado);
        assertEquals(pessoa.getNome(), serializado.getNome());
        assertEquals(pessoa.getSobrenome(), serializado.getSobrenome());
    }
}