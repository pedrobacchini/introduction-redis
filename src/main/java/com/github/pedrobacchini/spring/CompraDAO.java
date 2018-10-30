package com.github.pedrobacchini.spring;

import com.github.pedrobacchini.domain.Compra;
import com.github.pedrobacchini.domain.Pessoa;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

public class CompraDAO {

    private RedisTemplate<String, Compra> redisTemplate;

    public RedisTemplate<String, Compra> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, Compra> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getChave(Pessoa pessoa) {
        return getClass().getSimpleName()+":"+pessoa.getNome();
    }

    public List<Compra> obterCompras(Pessoa pessoa, long inicio, long itens) {
        String chave = getChave(pessoa);
        return redisTemplate.opsForList().range(chave, inicio, itens);
    }

    public void salvarCompra(Pessoa pessoa, Compra compra) {
        String chave = getChave(pessoa);
        getRedisTemplate().opsForList().rightPush(chave, compra);
    }
}
