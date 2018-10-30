package com.github.pedrobacchini.spring;

import com.github.pedrobacchini.domain.Pessoa;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.Jedis;

import static org.junit.Assert.*;

public class PessoaDAOTest {

    private ApplicationContext context;
    private PessoaDAO pessoaDAO;
    private Jedis jedis;
    private Pessoa pessoa = new Pessoa("Pedro", "Bacchini");

    @Before
    public void before(){
        context = new ClassPathXmlApplicationContext("spring-redis.xml");
        pessoaDAO = context.getBean(PessoaDAO.class);
        jedis = context.getBean(JedisConnectionFactory.class).getConnection().getNativeConnection();
        context.getBean(StringRedisTemplate.class).delete(pessoaDAO.getChave(pessoa));
    }

    @Test
    public void testeParseJSON() {

        pessoaDAO.salvarPessoa(pessoa);

        String chave = pessoaDAO.getChave(pessoa);
        assertTrue("Pessoa não foi persistida", jedis.exists(chave));

        Pessoa pessoaPersistida = pessoaDAO.obterPessoa(chave);
        assertNotNull("A pessoa não foi persistida", pessoaPersistida);
        assertEquals(pessoa.getNome(), pessoaPersistida.getNome());
        assertEquals(pessoa.getSobrenome(), pessoaPersistida.getSobrenome());


    }
}