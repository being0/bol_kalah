package com.bol.kalah.service

import com.bol.kalah.service.exception.KalahNotFoundException
import com.bol.kalah.service.lock.LockProvider
import com.bol.kalah.service.mapper.KalahGameMapper
import com.bol.kalah.service.model.Kalah
import com.bol.kalah.service.repository.KalahRepository
import com.bol.kalah.to.KalahTo
import spock.lang.Specification

import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.function.Function

import static com.bol.kalah.service.model.Kalah.PlayerTurn.PLAYER1
import static com.bol.kalah.service.model.Kalah.PlayerTurn.PLAYER2

class DefaultKalahGameServiceSpec extends Specification {

    DefaultKalahService kalahGameService
    Clock clock = Stub()
    LocalDateTime now
    String testId = "9529bb11-563c-47cf-b79a-912174f94d6d"
    KalahRepository kalahRepository = Mock()
    KalahStateEngine kalahStateEngine = Mock()

    def setup() {
        Clock fixedClock = Clock.fixed(Instant.parse("2022-04-16T22:15:30Z"), ZoneId.of("UTC"))
        clock.instant() >> { fixedClock.instant() }
        clock.getZone() >> { fixedClock.getZone() }
        now = LocalDateTime.now(clock)

        // Create DefaultKalahService
        KalahGameMapper kalahGameMapper = new KalahGameMapper() // a little integration is okay
        kalahRepository.save(_) >> { Kalah kalahGame -> kalahGame }
        LockProvider lockProvider = Stub()
        lockProvider.doInLock(_, _) >> { Kalah kalah, Function<Kalah, Kalah> kalahFunc ->
            kalahFunc.apply(kalah)
        }
        IdGenerator idGenerator = Stub()
        idGenerator.generateId() >> { testId }

        kalahGameService = new DefaultKalahService(kalahGameMapper, kalahRepository, lockProvider, idGenerator, kalahStateEngine, clock)
        kalahGameService.noOfPits = 6
        kalahGameService.noOfStones = 6
    }

    def '"create" should create an initial game'() {

        when:
        KalahTo kalahTo = kalahGameService.create()

        then:
        kalahTo.id == testId
        kalahTo.status == ["1": "6", "2": "6", "3": "6", "4": "6", "5": "6", "6": "6", "7": "0",
                           "8": "6", "9": "6", "10": "6", "11": "6", "12": "6", "13": "6", "14": "0"]
        kalahTo.turn == null

    }

    def '"move" If game not found throw KalahNotFoundException'() {

        given:
        kalahRepository.findById(_) >> Optional.empty()

        when:
        kalahGameService.move(testId, 1)

        then:
        thrown(KalahNotFoundException)
    }

    def '"move" Normal case that moves game to next state'() {

        given:
        kalahRepository.findById(_) >> Optional.of(new Kalah())
        Kalah movedKalah = new Kalah();
        movedKalah.board = [7, 7, 7, 6, 6, 6, 0, 6, 6, 6, 0, 7, 7, 1]
        movedKalah.id = testId
        movedKalah.turn = PLAYER1
        kalahStateEngine.move(_, _) >> movedKalah

        when:
        KalahTo kalahTo = kalahGameService.move(testId, 11)

        then:
        kalahTo.id == testId
        kalahTo.turn == PLAYER1
        kalahTo.status == ["1": "7", "2": "7", "3": "7", "4": "6", "5": "6", "6": "6", "7": "0",
                           "8": "6", "9": "6", "10": "6", "11": "0", "12": "7", "13": "7", "14": "1"] as Map
    }

    def '"get" If game not found throw KalahNotFoundException'() {
        given:
        kalahRepository.findById(_) >> Optional.empty()

        when:
        kalahGameService.get(testId)

        then:
        thrown(KalahNotFoundException)
    }

    def '"get" returns kalah'() {
        given:
        Kalah kalah = new Kalah();
        kalah.board = [7, 7, 7, 6, 6, 6, 0, 6, 6, 6, 0, 7, 7, 1]
        kalah.id = testId
        kalah.turn = PLAYER2
        kalahRepository.findById(_) >> Optional.of(kalah)

        when:
        KalahTo kalahTo = kalahGameService.get(testId)

        then:
        kalahTo.id == testId
        kalahTo.turn == PLAYER2
        kalahTo.status == ["1": "7", "2": "7", "3": "7", "4": "6", "5": "6", "6": "6", "7": "0",
                           "8": "6", "9": "6", "10": "6", "11": "0", "12": "7", "13": "7", "14": "1"] as Map
    }


}
