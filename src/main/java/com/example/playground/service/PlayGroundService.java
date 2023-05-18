package com.example.playground.service;

import com.example.playground.configuration.AppConfig;
import com.example.playground.configuration.PlaySiteFactory;
import com.example.playground.model.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayGroundService {
    AnnotationConfigApplicationContext context
            = new AnnotationConfigApplicationContext(AppConfig.class);
    private final PlayGround playGround = new PlayGround();
    private final PlaySiteFactory playSiteFactory = new PlaySiteFactory();

    public PlaySite addPlaySite(final String playSiteType) {

        // there are two ways of PlaySite installation from app context of from play site factory

        //PlaySite playSiteToAdd = (PlaySite)context.getBean(playSiteType);
        PlaySite playSiteToAdd = this.playSiteFactory.getPlaySite(playSiteType);

        this.playGround.addPlaySite(playSiteToAdd);
        return playSiteToAdd;
    }

    public Kid addKid(final Kid kid, final PlaySiteType playSiteType) {
       return this.playGround.addKid(kid, playSiteType);
    }

    public Kid addKid(final Kid kid) {
       return this.playGround.addKid(kid);
    }

    public boolean removeKid(final long ticketNumberOfKidToRemove) {
       return this.playGround.removeKid(ticketNumberOfKidToRemove);
    }

    public String getTotalVisitorsCount() {
        return String.valueOf(this.playGround.getTotalVisitorsCount());
    }

    public List<PlaySiteUtilization> getPlaySitesUtilization() {
        return this.playGround.getPlaySitesUtilization();
    }
}
