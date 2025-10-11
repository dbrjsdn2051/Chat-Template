package com.example.chatserver.common.config

import com.example.chatserver.chat.service.RedisPubSubService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.listener.PatternTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter

@Configuration
class RedisConfig {

    @Value("\${spring.data.redis.host}")
    lateinit var host: String

    @Value("\${spring.data.redis.port}")
    var port: Int = 0

    @Bean
    @Qualifier("chatPubSub")
    fun chatPubSubFactory(): RedisConnectionFactory {
        return LettuceConnectionFactory(RedisStandaloneConfiguration(host, port))
    }

    @Bean
    @Qualifier("chatPubSub")
    fun stringRedisTemplate(
        @Qualifier("chatPubSub") redisConnectionFactory: RedisConnectionFactory
    ): StringRedisTemplate {
        return StringRedisTemplate(redisConnectionFactory)
    }

    @Bean
    fun redisMessageListenerContainer(
        @Qualifier("chatPubSub") redisConnectionFactory: RedisConnectionFactory,
        messageListenerAdapter: MessageListenerAdapter
    ): RedisMessageListenerContainer {
        return RedisMessageListenerContainer().apply {
            setConnectionFactory(redisConnectionFactory)
            addMessageListener(messageListenerAdapter, PatternTopic("chat"))
        }
    }

    @Bean
    fun messageListenerAdapter(redisPubSubService: RedisPubSubService): MessageListenerAdapter {
        return MessageListenerAdapter(redisPubSubService, "onMessage")
    }
}