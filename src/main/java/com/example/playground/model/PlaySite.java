package com.example.playground.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Getter
@Setter
@RequiredArgsConstructor
public class PlaySite {

    @NonNull
    @JsonProperty
    PlaySiteType playSiteType;

    @NonNull
    @JsonProperty
    int maxCapacity;

    @JsonProperty
    int currentCapacity;
    List<Kid> kids = new ArrayList<>();
    Queue<Kid> kidsQueue = new LinkedList<>();

    @JsonIgnore
    Logger logger = LoggerFactory.getLogger(PlaySite.class);

    boolean addKid(final Kid kid) {

        if (kids.size() >= getMaxCapacity()) { //if play-site have no capacity, put kid in queue
            if (kid.acceptsWaiting()) {
                kidsQueue.add(kid);
                logger.info("Kid with ticket {} is successfully added to waiting queue  of play site {}", kid.ticketNumber, this.playSiteType);
                return true;
            } else {
                logger.info("Kid with ticket {} is not added to waiting queue, because of not accepting of waiting", kid.ticketNumber);
                return false;
            }
        } else {
            try {
                this.kids.add(kid);
                logger.info("Kid with ticket {} is successfully added to play site {}", kid.ticketNumber, this.playSiteType);
                currentCapacity = kids.size();
                return true;
            } catch (final Exception ex) {
                logger.error("Can't add kid to playground", ex);
                return false;
            }
        }
    }

    boolean removeKid(final long ticketNumberOfKidToRemove) {
        Kid kidToRemove = getKidToRemove(kidsQueue, ticketNumberOfKidToRemove);

        if (kidToRemove != null) {
            kidsQueue.remove(kidToRemove);
            logger.info("Kid with ticket {} is successfully removed from waiting queue of play site {}", ticketNumberOfKidToRemove, this.playSiteType);
            return true;
        } else {
            kidToRemove = getKidToRemove(kids, ticketNumberOfKidToRemove);
            if (kidToRemove != null) {
                kids.remove(kidToRemove);
                currentCapacity = kids.size();
                logger.info("Kid with ticket {} is successfully removed from play site {}", ticketNumberOfKidToRemove, this.playSiteType);

               // remove one kid from queue and put to play site
                if (kidsQueue.size() > 0) {
                    kids.add(kidsQueue.remove());
                }
                return true;

            } else {
                return false;
            }
        }
    }

    private Kid getKidToRemove(final Collection<Kid> kids, long ticketNumberOfKidToRemove) {
        return kids.stream().filter(k -> k.ticketNumber == ticketNumberOfKidToRemove).findAny().orElse(null);
    }

    public int getUtilization() {
        if (playSiteType.equals(PlaySiteType.DOUBLE_SWINGS)) {
            if (currentCapacity < 2) {
                return 0;
            } else {
                return 100;
            }
        } else {
            return 100 * currentCapacity / maxCapacity;
        }
    }
}