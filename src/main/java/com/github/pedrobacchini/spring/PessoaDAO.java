package com.github.pedrobacchini.spring;

import com.github.pedrobacchini.domain.Pessoa;
import org.springframework.data.redis.core.RedisTemplate;

public class PessoaDAO {

    private RedisTemplate<String, Pessoa> redisTemplate;

    public RedisTemplate<String, Pessoa> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, Pessoa> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getChave(Pessoa pessoa){
        return getClass().getSimpleName()+":"+pessoa.getNome();
    }

    public Pessoa obterPessoa(String chave) {
        return redisTemplate.opsForValue().get(chave);
    }

    public void salvarPessoa(Pessoa pessoa) {
        String chave = getChave(pessoa);
        redisTemplate.opsForValue().set(chave, pessoa);
    }
}
