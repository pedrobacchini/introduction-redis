package com.github.pedrobacchini;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class ConexaoTest {

    @Test
    public void testeObterConexao() {

        Conexao conexao = new Conexao();

        Jedis conexaoSimples = conexao.getConexao("localhost", 6379);

        conexaoSimples.set("jedis", "e legal");

        JedisPool pool = conexao.getJedisPool("localhost", 6379);

        Jedis conexaoDoPool = null;

        try {
            conexaoDoPool = pool.getResource();
        } finally {
            pool.returnResource(conexaoDoPool);
        }

        conexaoDoPool.set("jedis pool", "e legal com pool");
    }
}