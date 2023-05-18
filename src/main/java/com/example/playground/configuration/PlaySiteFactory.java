package com.example.playground.configuration;

import com.example.playground.model.PlaySite;
import com.example.playground.model.PlaySiteType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class PlaySiteFactory {

    @Bean
    @Qualifier("doubleSwings")
    private PlaySite doubleSwings() {
        return new PlaySite(PlaySiteType.DOUBLE_SWINGS, 2);
    }

    @Bean
    @Qualifier("carousel")
    private PlaySite carousel() {
        return new PlaySite(PlaySiteType.CAROUSEL, 9);
    }

    @Bean
    @Qualifier("slide")
    private PlaySite slide() {
        return new PlaySite(PlaySiteType.SLIDE, 5);
    }

    @Bean
    @Qualifier("ballPit")
    private PlaySite ballPit() {
        return new PlaySite(PlaySiteType.BALL_PIT, 15);
    }

    public PlaySite getPlaySite(final String playSiteType) {
        return switch (playSiteType) {
            case "DOUBLE_SWINGS" -> this.doubleSwings();
            case "CAROUSEL" -> this.carousel();
            case "SLIDE" -> this.slide();
            case "BALL_PIT" -> this.ballPit();
            default -> throw new IllegalArgumentException("No bean available for the type " + playSiteType);
        };
    }
}