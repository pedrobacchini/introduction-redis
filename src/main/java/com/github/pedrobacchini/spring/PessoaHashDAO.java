package com.github.pedrobacchini.spring;

import com.github.pedrobacchini.domain.Pessoa;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;

public class PessoaHashDAO {

    public static final String CAMPO_NOME = "nome";
    public static final String CAMPO_SOBRENOME = "sobrenome";

    private StringRedisTemplate stringRedisTemplate;

    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public StringRedisTemplate getStringRedisTemplate() {
        return stringRedisTemplate;
    }

    public String getChave(Pessoa pessoa) {
        return getClass().getSimpleName()+":"+pessoa.getNome();
    }

    public void salvar(Pessoa pessoa) {
        String chave = getChave(pessoa);
        BoundHashOperations<String, String, String> operations = getStringRedisTemplate().boundHashOps(chave);
        operations.put(CAMPO_NOME, pessoa.getNome());
        operations.put(CAMPO_SOBRENOME, pessoa.getSobrenome());
    }

    public void salvarPerformatico(Pessoa pessoa) {
        String chave = getChave(pessoa);
        Map<String, String> valores = new HashMap<>();
        valores.put(CAMPO_NOME, pessoa.getNome());
        valores.put(CAMPO_SOBRENOME, pessoa.getSobrenome());
        getStringRedisTemplate().opsForHash().putAll(chave, valores);
    }

    public Pessoa obter(String chave) {
        if(!getStringRedisTemplate().hasKey(chave)) return null;
        BoundHashOperations<String, String, String> operations = getStringRedisTemplate().boundHashOps(chave);
        Map<String, String> valores = operations.entries();

        Pessoa pessoa = new Pessoa(valores.get(CAMPO_NOME), valores.get(CAMPO_SOBRENOME));
        return pessoa;
    }
}
