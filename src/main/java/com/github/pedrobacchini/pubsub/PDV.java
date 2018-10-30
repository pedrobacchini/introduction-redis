package com.github.pedrobacchini.pubsub;

import com.github.pedrobacchini.Conexao;
import redis.clients.jedis.Jedis;

public class PDV {

    public static final String NOME_CANAL = "venda";
    private Jedis jedis = Conexao.getConexao();

    public void registrarVenda(String produto, String cliente, int quantidade, double totalVenda) {
        String mensagem = produto+":"+cliente+":"+quantidade+":"+totalVenda;
        jedis.publish(NOME_CANAL, mensagem);
    }
}
