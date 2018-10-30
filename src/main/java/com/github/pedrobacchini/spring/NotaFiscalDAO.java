package com.github.pedrobacchini.spring;

import com.github.pedrobacchini.domain.NotaFiscal;
import com.github.pedrobacchini.domain.Pessoa;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;

public class NotaFiscalDAO {

    private RedisTemplate<String, NotaFiscal> redisTemplate;

    public RedisTemplate<String, NotaFiscal> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, NotaFiscal> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getChave(Pessoa pessoa) {
        return getClass().getSimpleName()+":"+pessoa.getNome();
    }

    public Set<NotaFiscal> getNotasPessoa(Pessoa pessoa) {
        String chave = getChave(pessoa);
        return getRedisTemplate().opsForSet().members(chave);
    }

    public void salvarNotaFiscal(Pessoa pessoa, NotaFiscal notaFiscal) {
        String chave = getChave(pessoa);
        getRedisTemplate().opsForSet().add(chave, notaFiscal);
    }
}
