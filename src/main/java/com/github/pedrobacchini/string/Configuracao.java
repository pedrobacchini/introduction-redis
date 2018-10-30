package com.github.pedrobacchini.string;

/*
Exemplo manipulando string
 */
import com.github.pedrobacchini.Conexao;
import redis.clients.jedis.Jedis;

public class Configuracao {

    private Jedis jedis = Conexao.getConexao();

    public String getValor(String chave){ return jedis.get(chave); }

    public void setValor(String chave, String valor) {
        jedis.set(chave, valor);
    }

    public long incrementarChave(String chave) {
        return jedis.incr(chave);
    }

    public long incrementarPorChave(String chave, long valor) { return jedis.incrBy(chave, valor); }

    public long getTamanhoConfig(String chave) { return jedis.strlen(chave); }
}
