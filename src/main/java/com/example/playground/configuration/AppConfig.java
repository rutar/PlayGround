package com.example.playground.configuration;

import com.example.playground.model.PlaySite;
import com.example.playground.model.PlaySiteType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

// Both types of object instantiation - from AppContext and from Factory method
@SpringBootConfiguration
public class AppConfig {

    @Bean
    @Qualifier("DOUBLE_SWINGS")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    protected PlaySite DOUBLE_SWINGS() {
        return new PlaySite(PlaySiteType.DOUBLE_SWINGS, 2);
    }

    @Bean
    @Qualifier("CAROUSEL")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    protected PlaySite CAROUSEL() {
        return new PlaySite(PlaySiteType.CAROUSEL, 9);
    }

    @Bean
    @Qualifier("SLIDE")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    protected PlaySite SLIDE() {
        return new PlaySite(PlaySiteType.SLIDE, 5);
    }

    @Bean
    @Qualifier("BALL_PIT")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    protected PlaySite BALL_PIT() {
        return new PlaySite(PlaySiteType.BALL_PIT, 15);
    }
}
