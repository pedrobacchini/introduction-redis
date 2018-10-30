package com.github.pedrobacchini.spring;

import com.github.pedrobacchini.domain.NotaFiscal;
import com.github.pedrobacchini.domain.Pessoa;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.Jedis;

import java.util.Set;

import static org.junit.Assert.*;

public class NotaFiscalDAOTest {

    private ApplicationContext context;
    private NotaFiscalDAO notaFiscalDAO;
    private Jedis jedis;
    private Pessoa pessoa = new Pessoa("Pedro", "Bacchini");

    @Before
    public void before() {
        context = new ClassPathXmlApplicationContext("spring-redis.xml");
        notaFiscalDAO = context.getBean(NotaFiscalDAO.class);
        jedis = context.getBean(JedisConnectionFactory.class).getConnection().getNativeConnection();
        context.getBean(StringRedisTemplate.class).delete(notaFiscalDAO.getChave(pessoa));
    }

    @Test
    public void testConjuntos() {
        NotaFiscal notaFiscal1 = new NotaFiscal("codigo1", 34D);
        NotaFiscal notaFiscal2 = new NotaFiscal("codigo2", 34D);

        notaFiscalDAO.salvarNotaFiscal(pessoa, notaFiscal1);
        notaFiscalDAO.salvarNotaFiscal(pessoa, notaFiscal2);

        String chave = notaFiscalDAO.getChave(pessoa);
        assertTrue("Nota fiscais não foram persistidas", jedis.exists(chave));
        assertEquals(2, jedis.smembers(chave).size());

        Set<NotaFiscal> notasFiscais = notaFiscalDAO.getNotasPessoa(pessoa);
        assertNotNull("As notas não foram persistidas", notasFiscais);
        assertTrue(notasFiscais.contains(notaFiscal1));
        assertTrue(notasFiscais.contains(notaFiscal2));
    }
}