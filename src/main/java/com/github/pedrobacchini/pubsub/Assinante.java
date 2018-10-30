package com.github.pedrobacchini.pubsub;

import com.github.pedrobacchini.Conexao;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public abstract class Assinante extends JedisPubSub {

    private Jedis jedis = Conexao.getConexao();

    public void assinar(String canal) { jedis.subscribe(this, canal); }

    public void assinar(String... canais) { jedis.subscribe(this, canais); }

    @Override
    public abstract void onMessage(String canal, String mensagem);

    @Override
    public void onPMessage(String s, String s1, String s2) { }

    @Override
    public void onSubscribe(String s, int i) { }

    @Override
    public void onUnsubscribe(String s, int i) { }

    @Override
    public void onPUnsubscribe(String s, int i) { }

    @Override
    public void onPSubscribe(String s, int i) { }
}
