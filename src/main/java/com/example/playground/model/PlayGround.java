package com.example.playground.model;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Getter
@Setter
public class PlayGround {

    private final Logger logger = LoggerFactory.getLogger(PlayGround.class);
    private int totalVisitorsCount = 0;
    private final List<PlaySite> playSites = new ArrayList<>();

    @Scheduled(cron = "0 0 0 * * *") // every twenty-four hours at midnight
    public void resetTotalCount() {
        this.totalVisitorsCount = 0;
        logger.info("Total visitors count is reset to 0.");
    }

    private int getMaxCapacity() {
        return playSites.stream().mapToInt(PlaySite::getMaxCapacity).sum();
    }

    public void addPlaySite(final PlaySite playSite) {
        this.playSites.add(playSite);
    }

    public Kid addKid(final Kid kid, final PlaySiteType playSiteName) {

        //find required play sites
        List<PlaySite> playSitesToAddKid = this.playSites.stream().filter(p -> p.getPlaySiteType().equals(playSiteName)).toList();

        //find first required play site with free slots
        PlaySite playSiteToAddKid = playSitesToAddKid.stream().filter(p -> p.getCurrentCapacity() < p.getMaxCapacity()).findFirst().orElse(null);

        // if all required play sites are occupied add kid to required play site with the shortest queue
        if (playSiteToAddKid == null) {
            playSiteToAddKid = playSitesToAddKid.stream().min(Comparator.comparing(p -> p.kidsQueue.size()))
                    .orElseThrow(NoSuchElementException::new);
        }

        return addKidToPlaySite(kid, playSiteToAddKid);
    }

    public Kid addKid(final Kid kid) {

        PlaySite playSiteToAddKid;

        try {
            // find less utilized play site
            playSiteToAddKid = this.playSites.stream().filter(p -> p.getUtilization() < 100).min(Comparator.comparing(PlaySite::getUtilization)).orElseThrow(NoSuchElementException::new);
        } catch (Exception ex) {
            playSiteToAddKid = this.playSites.stream().min(Comparator.comparing(p -> p.kidsQueue.size()))
                    .orElseThrow(NoSuchElementException::new);
        }

        return addKidToPlaySite(kid, playSiteToAddKid);
    }

    public boolean removeKid(final long ticketNumberOfKidToRemove) {
        boolean result = false;
        for (final PlaySite playSite : playSites) {
            result |= playSite.removeKid(ticketNumberOfKidToRemove);
        }
        if (!result) {
            logger.error("Kid with ticket {} is not found on play ground.", ticketNumberOfKidToRemove);
        }
        return result;
    }

    private Kid addKidToPlaySite(final Kid kid, final PlaySite playSiteToAddKid) {
        if (playSiteToAddKid != null) {

            Kid kidAlreadyOnPlayground = null;

            for (PlaySite playsite : this.playSites) {
                kidAlreadyOnPlayground = playsite.kidsQueue.stream().filter(k -> k.ticketNumber == kid.ticketNumber).findFirst().orElse(null);
                if (kidAlreadyOnPlayground != null) {
                    break;
                }
                kidAlreadyOnPlayground = playsite.kids.stream().filter(k -> k.ticketNumber == kid.ticketNumber).findFirst().orElse(null);
                if (kidAlreadyOnPlayground != null) {
                    break;
                }
            }

            if (kidAlreadyOnPlayground == null) {

                try {
                    if (playSiteToAddKid.addKid(kid)) {
                        totalVisitorsCount++;
                    }
                    return kid;
                } catch (Exception ex) {
                    logger.error("Exception occurred while adding kid with ticket {}  to play site {}", kid.ticketNumber, playSiteToAddKid.playSiteType);
                    return null;
                }
            } else {
                logger.error("Kid with ticket {}  is already on play ground", kid.ticketNumber);
                return null;
            }
        } else {
            logger.error("Error occurred while adding kid with ticket {}. Play site is null.", kid.ticketNumber);
            return null;
        }
    }

    public int getTotalVisitorsCount() {
        return totalVisitorsCount;
    }

    public List<PlaySiteUtilization> getPlaySitesUtilization() {

        return this.playSites.stream().map(p -> new PlaySiteUtilization(p.getPlaySiteType().name(), p.getUtilization())).collect(Collectors.toList());
    }
}
