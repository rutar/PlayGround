package com.example.playground.controller;


import com.example.playground.model.Kid;
import com.example.playground.model.PlaySite;
import com.example.playground.model.PlaySiteType;
import com.example.playground.model.PlaySiteUtilization;
import com.example.playground.service.PlayGroundService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.ServerException;
import java.util.List;

@RestController
@RequestMapping("/api/playground/v1")
public class PlayGroundController {

    final PlayGroundService playGroundService;

    public PlayGroundController(PlayGroundService playGroundService) {
        this.playGroundService = playGroundService;
    }

    @GetMapping(value = "/visitors/")
    ResponseEntity<String> getTotalVisitorsCount() {
        return new ResponseEntity<>(playGroundService.getTotalVisitorsCount(), HttpStatus.OK);
    }

    @GetMapping(value = "/playsites/utilization/")
    ResponseEntity<List<PlaySiteUtilization>> getPlaySitesUtilization() {
        return new ResponseEntity<>(playGroundService.getPlaySitesUtilization(), HttpStatus.OK);
    }

    @PostMapping(path = "/playsites",
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlaySite> addPlaySite(@RequestParam String newPlaySiteType) throws ServerException {

        PlaySite playSite = playGroundService.addPlaySite(newPlaySiteType.toUpperCase());
        if (playSite == null) {
            throw new ServerException("Error adding playSite");
        } else {
            return new ResponseEntity<>(playSite, HttpStatus.CREATED);
        }
    }

    @PostMapping(path = "/kids",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Kid> addKidToPlaySite(@RequestBody Kid newKid, @RequestParam(value = "playSiteType", required = false) String playSiteType) {

        Kid kidToAdd;
        try {
            kidToAdd = (playSiteType != null) ?
                    playGroundService.addKid(newKid, PlaySiteType.valueOf(playSiteType.toUpperCase()))
                    : playGroundService.addKid(newKid);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return (kidToAdd == null) ? new ResponseEntity<>(HttpStatus.CONFLICT)
                : new ResponseEntity<>(kidToAdd, HttpStatus.CREATED);
    }

    @DeleteMapping("/kids")
    public boolean deleteKidByTicketNumber(@RequestParam(value = "ticketNumber") long ticketNumber) {
        return playGroundService.removeKid(ticketNumber);
    }
}
