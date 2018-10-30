package com.github.pedrobacchini.dao;

import com.github.pedrobacchini.domain.Pessoa;
import org.junit.Test;

import static org.junit.Assert.*;

public class PessoaDAOTest {

    private PessoaDAO pessoaDAO = new PessoaDAO();
    private Pessoa pessoa = new Pessoa("Pedro Hash", "Bacchini");

    @Test
    public void testObter() {
        pessoaDAO.salvar(pessoa);
        Pessoa persistida = pessoaDAO.obter(pessoaDAO.getChave(pessoa));

        assertNotNull(persistida);
        assertEquals(pessoa.getNome(), persistida.getNome());
        assertEquals(pessoa.getSobrenome(), persistida.getSobrenome());
    }

    @Test
    public void testObterPerformatico() {
        pessoaDAO.salvar(pessoa);
        Pessoa persistida = pessoaDAO.obterPerformatico(pessoaDAO.getChave(pessoa));

        assertNotNull(persistida);
        assertEquals(pessoa.getNome(), persistida.getNome());
        assertEquals(pessoa.getSobrenome(), persistida.getSobrenome());
    }

    @Test
    public void testRemover() {
        Pessoa pessoa = new Pessoa("Remover", "Removido");
        pessoaDAO.salvar(pessoa);
        pessoaDAO.remover(pessoa);
        Pessoa persistida = pessoaDAO.obter(pessoaDAO.getChave(pessoa));

        assertNull(persistida);
    }
}