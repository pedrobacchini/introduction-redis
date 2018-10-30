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

public class PessoaHashDAOTest {

    private ApplicationContext context;
    private Jedis jedis;
    private PessoaHashDAO pessoaHashDao;
    private Pessoa pessoa = new Pessoa("Pedro", "Bacchini");


    @Before
    public void before() {
        context = new ClassPathXmlApplicationContext("spring-redis.xml");
        pessoaHashDao = context.getBean(PessoaHashDAO.class);
        jedis = ((JedisConnectionFactory) context.getBean("jedisConnectionFactory")).getConnection().getNativeConnection();
        context.getBean(StringRedisTemplate.class).delete(pessoaHashDao.getChave(pessoa));
    }

    @Test
    public void testSalvarHash() {
        pessoaHashDao.salvar(pessoa);
        String chave = pessoaHashDao.getChave(pessoa);
        assertTrue("Registro não foi persistido. Chave não existe.", jedis.exists(chave));
        assertEquals(pessoa.getNome(), jedis.hget(chave, PessoaHashDAO.CAMPO_NOME));
        assertEquals(pessoa.getSobrenome(), jedis.hget(chave, PessoaHashDAO.CAMPO_SOBRENOME));
    }

    @Test
    public void testSalvarHashPerformatico() {
        pessoaHashDao.salvarPerformatico(pessoa);
        String chave = pessoaHashDao.getChave(pessoa);
        assertTrue("Registro não foi persistido. Chave não existe.", jedis.exists(chave));
        assertEquals(pessoa.getNome(), jedis.hget(chave, PessoaHashDAO.CAMPO_NOME));
        assertEquals(pessoa.getSobrenome(), jedis.hget(chave, PessoaHashDAO.CAMPO_SOBRENOME));
    }

    @Test
    public void testObterHash() {
        pessoaHashDao.salvar(pessoa);
        String chave = pessoaHashDao.getChave(pessoa);
        Pessoa pessoaPersistida = pessoaHashDao.obter(chave);
        assertNotNull(pessoaPersistida);
        assertEquals(pessoa.getNome(), pessoaPersistida.getNome());
        assertEquals(pessoa.getSobrenome(), pessoaPersistida.getSobrenome());
    }
}