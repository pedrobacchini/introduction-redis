package com.github.pedrobacchini.dao;

import com.github.pedrobacchini.Conexao;
import com.github.pedrobacchini.domain.NotaFiscal;
import com.github.pedrobacchini.domain.Pessoa;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class NotaFiscalDAOTest {

    private NotaFiscalDAO notaFiscalDAO = new NotaFiscalDAO();

    @Before
    public void antes(){ Conexao.getConexao().del("NotaFiscal:Pedro"); }

    @Test
    public void testPersistirNota() throws ParseException {

        Pessoa pessoa = new Pessoa("Pedro", "Bacchini");

        NotaFiscal notaFiscal = new NotaFiscal("nota1", 20D);

        notaFiscalDAO.salvar(pessoa, notaFiscal);

        Set<NotaFiscal> notasPersistidas = notaFiscalDAO.obterNotas(pessoa);
        assertNotNull(notasPersistidas);
        assertTrue(notasPersistidas.contains(notaFiscal));
        assertEquals(1, notasPersistidas.size());

        notaFiscalDAO.salvar(pessoa, notaFiscal);
        notasPersistidas = notaFiscalDAO.obterNotas(pessoa);
        assertEquals(1, notasPersistidas.size());

        notaFiscal.setValor(10D);
        notaFiscalDAO.salvar(pessoa, notaFiscal);
        notasPersistidas = notaFiscalDAO.obterNotas(pessoa);
        assertEquals(2, notasPersistidas.size());
    }

    @Test
    public void testRemoverNota() throws ParseException {

        Pessoa pessoa = new Pessoa("Pedro Remover", "Bacchini");

        NotaFiscal notaFiscal1 = new NotaFiscal("nota1", 23D);
        NotaFiscal notaFiscal2 = new NotaFiscal("nota2", 34D);

        notaFiscalDAO.salvar(pessoa, notaFiscal1);
        notaFiscalDAO.salvar(pessoa, notaFiscal2);

        Set<NotaFiscal> notasPersistidas = notaFiscalDAO.obterNotas(pessoa);
        assertTrue(notasPersistidas.contains(notaFiscal1));
        assertTrue(notasPersistidas.contains(notaFiscal2));
        assertEquals(2, notasPersistidas.size());

        notaFiscalDAO.remover(pessoa, notaFiscal1);
        notasPersistidas = notaFiscalDAO.obterNotas(pessoa);
        assertFalse(notasPersistidas.contains(notaFiscal1));
        assertTrue(notasPersistidas.contains(notaFiscal2));
        assertEquals(1, notasPersistidas.size());
    }

    @Test
    public void testPersistirNotasOrdendas() throws ParseException {
        Pessoa pessoa = new Pessoa("Pedro Ordenadas", "Bacchini");

        NotaFiscal notaFiscal1 = new NotaFiscal("nota1", 23D);
        NotaFiscal notaFiscal2 = new NotaFiscal("nota2", 34D);
        NotaFiscal notaFiscal3 = new NotaFiscal("nota3", 80D);
        NotaFiscal notaFiscal4 = new NotaFiscal("nota4", 100D);

        notaFiscalDAO.salvarOrdenado(pessoa, notaFiscal1);
        notaFiscalDAO.salvarOrdenado(pessoa, notaFiscal2);
        notaFiscalDAO.salvarOrdenado(pessoa, notaFiscal3);
        notaFiscalDAO.salvarOrdenado(pessoa, notaFiscal4);

        Set<NotaFiscal> notasFiscais = notaFiscalDAO.obterNotasOrdenadas(pessoa, 0, 1000);
        assertTrue(notasFiscais.contains(notaFiscal1));
        assertTrue(notasFiscais.contains(notaFiscal2));
        assertTrue(notasFiscais.contains(notaFiscal3));
        assertTrue(notasFiscais.contains(notaFiscal4));

        notasFiscais = notaFiscalDAO.obterNotasOrdenadas(pessoa, 34, 80);
        assertTrue(notasFiscais.contains(notaFiscal2));
        assertTrue(notasFiscais.contains(notaFiscal3));

        notaFiscalDAO.removerOrdenado(pessoa, 33, 81);
        notasFiscais = notaFiscalDAO.obterNotasOrdenadas(pessoa, 0, 1000);
        assertFalse(notasFiscais.contains(notaFiscal2));
        assertFalse(notasFiscais.contains(notaFiscal3));
        assertTrue(notasFiscais.contains(notaFiscal1));
        assertTrue(notasFiscais.contains(notaFiscal4));
    }
}