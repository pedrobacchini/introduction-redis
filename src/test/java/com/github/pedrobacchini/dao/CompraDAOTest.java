package com.github.pedrobacchini.dao;

import com.github.pedrobacchini.domain.Compra;
import com.github.pedrobacchini.domain.Pessoa;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CompraDAOTest {

    private CompraDAO compraDAO = new CompraDAO();

    @Test
    public void test1() throws ParseException {
        Pessoa pessoa = new Pessoa("Pedro", "Bacchini");

        Compra compra1 = new Compra("Computador", 100);
        Compra compra2 = new Compra("Tablet", 800);

        compraDAO.salvar(pessoa, compra1);
        compraDAO.salvar(pessoa, compra2);

        List<Compra> compras = compraDAO.obter(pessoa, 0, 10);

        assertNotNull(compras);
        assertFalse(compras.isEmpty());
        assertTrue(compras.contains(compra1));
        assertTrue(compras.contains(compra2));
    }

    @Test
    public void test2() {
        Pessoa pessoa = new Pessoa("Remover", "Removido");

        Compra compra = new Compra("Celular", 1800);
        compraDAO.salvar(pessoa, compra);

        compraDAO.remover(pessoa, 0, compra);
    }
}