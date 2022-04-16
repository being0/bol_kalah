package com.bol.kalah.service.model

import com.bol.kalah.service.model.Kalah
import spock.lang.Specification

import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

import static com.bol.kalah.service.model.Kalah.GameState.CREATED

class KalahSpec extends Specification {

    Clock clock = Stub()

    def setup() {
        Clock fixedClock = Clock.fixed(Instant.parse("2022-04-16T22:15:30Z"), ZoneId.of("UTC"))
        clock.instant() >> { fixedClock.instant() }
        clock.getZone() >> { fixedClock.getZone() }
    }

    def '"doCreate" should prepare Kalah object for creation'() {

        when:
        Kalah kalahGame = Kalah.doCreate("9529bb11-563c-47cf-b79a-912174f94d6d", 6, 4, LocalDateTime.now(clock));

        then:
        kalahGame.getId() == "9529bb11-563c-47cf-b79a-912174f94d6d"
        kalahGame.getState() == CREATED
        kalahGame.getCreated() == LocalDateTime.now(clock)
        kalahGame.getModified() == LocalDateTime.now(clock)
        kalahGame.getTurn() == null
        kalahGame.getNoOfPits() == 6
        kalahGame.getNoOfStones() == 4
        kalahGame.getBoard() == [4, 4, 4, 4, 4, 4, 0, 4, 4, 4, 4, 4, 4, 0] as int[]
    }


}
