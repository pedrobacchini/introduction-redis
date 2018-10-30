package com.github.pedrobacchini;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class Conexao {

    public static Jedis getConexao() {
        return new Conexao().getConexao("localhost", 6379);
    }

    public Jedis getConexao(String host, int porta) {
        return new Jedis(host, porta);
    }

    public Jedis getConexao(String host, int porta, int timeout) {
        return new Jedis(host, porta, timeout);
    }

    public JedisPool getJedisPool(String host, int porta) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxActive(10);
        config.setMaxIdle(10);
        config.setMinIdle(4);

        return new JedisPool(config, host, porta);
    }
}
