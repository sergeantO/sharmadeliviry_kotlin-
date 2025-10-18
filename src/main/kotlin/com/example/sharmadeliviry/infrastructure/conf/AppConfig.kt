package com.example.sharmadeliviry.conf

import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConf {

    @Bean fun commandRurnner(): CommandLineRunner = StartupRunner()
}
