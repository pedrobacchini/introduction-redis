package com.github.pedrobacchini.spring;

import com.github.pedrobacchini.domain.Pessoa;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;

import java.util.HashMap;
import java.util.Map;

public class SpringDataRedisString {

    private ApplicationContext context;

    @Before
    public void before() {
        context = new ClassPathXmlApplicationContext("spring-redis.xml");
        context.getBean(StringRedisTemplate.class).delete("chaveAppend");
    }

    @Test
    public void testRawTemplaStrings() {
        StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) context.getBean("stringRedisTemplate");

        String CHAVE_TESTE = "valorTeste";

        stringRedisTemplate.opsForValue().set(CHAVE_TESTE, "valor de teste");
        Assert.assertEquals("valor de teste", stringRedisTemplate.opsForValue().get(CHAVE_TESTE));

        String oldValue = stringRedisTemplate.opsForValue().getAndSet(CHAVE_TESTE, "Bruto");
        Assert.assertEquals("valor de teste", oldValue);

        Map<String, String> valores = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            valores.put("chave:"+i, "valor "+i);
        }
        stringRedisTemplate.opsForValue().multiSet(valores);
        for (String chave: valores.keySet()) {
            Assert.assertEquals(valores.get(chave), stringRedisTemplate.opsForValue().get(chave));
        }

        String chaveOriginal = "chaveAppend";
        String valorInicial = "Ola";
        stringRedisTemplate.opsForValue().append(chaveOriginal, valorInicial);
        String valorAgregado = " mundo";
        stringRedisTemplate.opsForValue().append(chaveOriginal, valorAgregado);
        Assert.assertEquals(valorInicial + valorAgregado, stringRedisTemplate.opsForValue().get(chaveOriginal));

        BoundValueOperations<String, String> operations = stringRedisTemplate.boundValueOps("chave");
        operations.set("valor de teste");
        long tamanhoInserido = operations.size();
        Assert.assertEquals(14, tamanhoInserido);
    }

    @Test
    public void testSerializer() {
        RedisTemplate redisTemplate = (RedisTemplate) context.getBean("defaultRedisTemplate");

        redisTemplate.setValueSerializer(new JacksonJsonRedisSerializer<>(Pessoa.class));
        Pessoa pessoa = new Pessoa("Pedro", "Bacchini");
        redisTemplate.opsForValue().set("pessoa:spring:"+pessoa.getNome(), pessoa);
        Pessoa pessoaPersistida = (Pessoa) redisTemplate.opsForValue().get("pessoa:spring:"+pessoa.getNome());
        Assert.assertNotNull(pessoaPersistida);
        Assert.assertEquals(pessoa.getNome(), pessoaPersistida.getNome());
        Assert.assertEquals(pessoa.getSobrenome(), pessoaPersistida.getSobrenome());
    }
}
