package com.github.pedrobacchini;

import com.github.pedrobacchini.dao.CompraDAO;
import com.github.pedrobacchini.dao.NotaFiscalDAO;
import com.github.pedrobacchini.dao.PessoaDAO;
import com.github.pedrobacchini.domain.Compra;
import com.github.pedrobacchini.domain.NotaFiscal;
import com.github.pedrobacchini.domain.Pessoa;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.Set;
import java.util.UUID;

public class Transacao {

    private Jedis jedis = Conexao.getConexao();

    private boolean simularErro = false;

    public void setSimularErro(boolean simularErro) {
        this.simularErro = simularErro;
    }

    public void persistirCompras(Pessoa pessoa, Set<Compra> compras) {
        PessoaDAO pessoaDAO = new PessoaDAO();
        CompraDAO compraDAO = new CompraDAO();
        NotaFiscalDAO notaFiscalDAO = new NotaFiscalDAO();
        Transaction transacao = jedis.multi();
        try {
            String chavePessoa = pessoaDAO.getChave(pessoa);
            transacao.hset(chavePessoa, PessoaDAO.CAMPO_NOME, pessoa.getNome());
            transacao.hset(chavePessoa, PessoaDAO.CAMPO_SOBRENOME, pessoa.getSobrenome());

            String chaveCompras = compraDAO.getChave(pessoa);
            double valorTotal = 0D;
            for (Compra compra : compras) {
                valorTotal += compra.getValorPago();
                String jsonCompra = compraDAO.compraParaJSON(compra);
                transacao.lpush(chaveCompras, jsonCompra);
            }

            if(simularErro) {
                throw new Exception("Erro simulado");
            }

            String chaveNotaFiscal = notaFiscalDAO.getChave(pessoa);
            NotaFiscal notaFiscal = new NotaFiscal("nota:"+ UUID.randomUUID(), valorTotal);
            String jsonNotaFiscal = notaFiscalDAO.notaFiscalParaJSON(notaFiscal);
            transacao.zadd(chaveNotaFiscal, valorTotal, jsonNotaFiscal);

            transacao.exec();

        } catch (Throwable t) {
            transacao.discard();
        }
    }
}
