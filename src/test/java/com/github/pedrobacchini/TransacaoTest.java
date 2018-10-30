package com.github.pedrobacchini;

import com.github.pedrobacchini.dao.CompraDAO;
import com.github.pedrobacchini.dao.NotaFiscalDAO;
import com.github.pedrobacchini.dao.PessoaDAO;
import com.github.pedrobacchini.domain.Compra;
import com.github.pedrobacchini.domain.NotaFiscal;
import com.github.pedrobacchini.domain.Pessoa;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class TransacaoTest {

    private Jedis jedis = Conexao.getConexao();

    private Pessoa pessoa = new Pessoa("Pedro Henrique", "Bacchini");
    private Set<Compra> compras = new HashSet<>();

    private PessoaDAO pessoaDAO = new PessoaDAO();
    private CompraDAO compraDAO = new CompraDAO();
    private NotaFiscalDAO notaFiscalDAO = new NotaFiscalDAO();

    @Before
    public void before(){
        jedis.flushAll();
        compras.add(new Compra("Computador", 600));
        compras.add(new Compra("Disco", 20));
        compras.add(new Compra("Papel", 10));
    }

    @Test
    public void testPersistirComprasCorretamente() throws ParseException {
        Transacao transacao = new Transacao();
        transacao.persistirCompras(pessoa, compras);

        Pessoa pessoaPersistida = pessoaDAO.obterPerformatico(pessoaDAO.getChave(pessoa));
        assertNotNull(pessoaPersistida);
        List<Compra> comprasPersistidas = compraDAO.obter(pessoa, 0, 1000);
        assertFalse(comprasPersistidas.isEmpty());
        for (Compra compra : compras) {
            assertTrue(comprasPersistidas.contains(compra));
        }

        Set<NotaFiscal> notasFiscais = notaFiscalDAO.obterNotasOrdenadas(pessoa, 0, 1000);
        assertFalse(notasFiscais.isEmpty());
        assertEquals(1, notasFiscais.size());
        NotaFiscal notaFiscal = notasFiscais.iterator().next();
        assertEquals(630.0, notaFiscal.getValor(),0.000);
    }

    @Test
    public void testPersistirComprasErro() throws Exception {
        Transacao transacao = new Transacao();
        transacao.setSimularErro(true);
        transacao.persistirCompras(pessoa, compras);

        Pessoa registroPessoa = pessoaDAO.obter(pessoaDAO.getChave(pessoa));
        assertNull(registroPessoa);

        List<Compra> registroCompras = compraDAO.obter(pessoa, 0, 1000);
        assertTrue(registroCompras.isEmpty());

        Set<NotaFiscal> registroNotas = notaFiscalDAO.obterNotasOrdenadas(pessoa, 0, 1000);
        assertTrue(registroNotas.isEmpty());
    }
}