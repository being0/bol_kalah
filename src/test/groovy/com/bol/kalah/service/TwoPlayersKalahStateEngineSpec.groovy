package com.bol.kalah.service

import com.bol.kalah.service.exception.InvalidMoveException
import com.bol.kalah.service.exception.KalahFinishedException
import com.bol.kalah.service.exception.NotYourTurnException
import com.bol.kalah.service.exception.ValidationException
import com.bol.kalah.service.model.Kalah
import spock.lang.Specification

import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

import static com.bol.kalah.service.model.Kalah.GameState.FINISHED
import static com.bol.kalah.service.model.Kalah.GameState.RUNNING
import static com.bol.kalah.service.model.Kalah.PlayerTurn.PLAYER1
import static com.bol.kalah.service.model.Kalah.PlayerTurn.PLAYER2

/**
 * Check all rules of TwoPlayersKalahStateEngine using this spec
 */
class TwoPlayersKalahStateEngineSpec extends Specification {

    Clock clock = Stub()
    TwoPlayersKalahStateEngine kalahEngine = new TwoPlayersKalahStateEngine(clock)
    LocalDateTime now

    def setup() {
        Clock fixedClock = Clock.fixed(Instant.parse("2022-04-16T22:15:30Z"), ZoneId.of("UTC"))
        clock.instant() >> { fixedClock.instant() }
        clock.getZone() >> { fixedClock.getZone() }
        now = LocalDateTime.now(clock)
    }

    def '"move" when state is FINISHED raise KalahFinishedException'() {

        given:
        Kalah kalah = Kalah.doCreate("2323", 6, 4, now)
        kalah.setState(FINISHED)
        kalah.board = [0, 0, 0, 0, 0, 0, 0, 40, 0, 0, 0, 0, 0, 0, 32] as int[]

        when:
        kalahEngine.move(kalah, 2)

        then:
        thrown(KalahFinishedException)
    }

    def '"move" throw InvalidMoveException when it is Kalah index'() {

        given:
        Kalah kalah = Kalah.doCreate("9529bb11-563c-47cf-b79a-912174f94d6d", 6, 6, now)

        when:
        kalahEngine.move(kalah, kalahIndex as int)

        then:
        thrown(InvalidMoveException)

        where:
        kalahIndex << [
                7,  // Kalah
                14 // Kalah
        ]
    }

    def '"move" throw ValidationException when pitId is not valid'() {

        given:
        Kalah kalah = Kalah.doCreate("9529bb11-563c-47cf-b79a-912174f94d6d", 6, 6, now)

        when:
        kalahEngine.move(kalah, invalidKalahId as int)

        then:
        thrown(ValidationException)

        where:
        invalidKalahId << [
                0,  // zero
                -1, // minus
                15 // more than size of board
        ]
    }

    def '"move" Moving an empty pit will raise InvalidMoveException'() {

        given:
        Kalah kalah = Kalah.doCreate("9529bb11-563c-47cf-b79a-912174f94d6d", 6, 4, now)
        kalah.board = [1, 5, 6, 9, 7, 8, 6, 10, 6, 7, 5, 0, 6, 3] as int[]

        when:
        kalahEngine.move(kalah, 12)

        then:
        thrown(InvalidMoveException)
    }


    def '"move" If not player turn then throw NotYourTurnException'() {

        given:
        Kalah kalah = Kalah.doCreate("9529bb11-563c-47cf-b79a-912174f94d6d", 6, 4, now)
        kalah.setTurn(turnParam)

        when:
        kalahEngine.move(kalah, pitId as int)

        then:
        thrown(NotYourTurnException)

        where:
        pitId | turnParam
        1     | PLAYER2
        2     | PLAYER2
        3     | PLAYER2
        4     | PLAYER2
        5     | PLAYER2
        6     | PLAYER2
        8     | PLAYER1
        9     | PLAYER1
        10    | PLAYER1
        11    | PLAYER1
        12    | PLAYER1
        13    | PLAYER1
    }

    def '"move" Check couple of normal moves'() {

        given:
        Kalah kalah = Kalah.doCreate("9529bb11-563c-47cf-b79a-912174f94d6d", 6, 4, now)
        kalah.setTurn(turn)

        when:
        kalahEngine.move(kalah, pitId as int)

        then:
        kalah.board == expectedBoard

        where:
        pitId | turn    | expectedBoard
        1     | PLAYER1 | [0, 5, 5, 5, 5, 4, 0, 4, 4, 4, 4, 4, 4, 0] as int[]
        2     | PLAYER1 | [4, 0, 5, 5, 5, 5, 0, 4, 4, 4, 4, 4, 4, 0] as int[]
        3     | PLAYER1 | [4, 4, 0, 5, 5, 5, 1, 4, 4, 4, 4, 4, 4, 0] as int[]
        4     | PLAYER1 | [4, 4, 4, 0, 5, 5, 1, 5, 4, 4, 4, 4, 4, 0] as int[]
        5     | PLAYER1 | [4, 4, 4, 4, 0, 5, 1, 5, 5, 4, 4, 4, 4, 0] as int[]
        6     | PLAYER1 | [4, 4, 4, 4, 4, 0, 1, 5, 5, 5, 4, 4, 4, 0] as int[]
        8     | PLAYER2 | [4, 4, 4, 4, 4, 4, 0, 0, 5, 5, 5, 5, 4, 0] as int[]
        9     | PLAYER2 | [4, 4, 4, 4, 4, 4, 0, 4, 0, 5, 5, 5, 5, 0] as int[]
        10    | PLAYER2 | [4, 4, 4, 4, 4, 4, 0, 4, 4, 0, 5, 5, 5, 1] as int[]
        11    | PLAYER2 | [5, 4, 4, 4, 4, 4, 0, 4, 4, 4, 0, 5, 5, 1] as int[]
        12    | PLAYER2 | [5, 5, 4, 4, 4, 4, 0, 4, 4, 4, 4, 0, 5, 1] as int[]
        13    | PLAYER2 | [5, 5, 5, 4, 4, 4, 0, 4, 4, 4, 4, 4, 0, 1] as int[]
    }

    def '"move" Check game FINISHED'() {

        given:
        Kalah kalah = Kalah.doCreate("9529bb11-563c-47cf-b79a-912174f94d6d", 6, 4, now)
        kalah.state = RUNNING
        kalah.turn = PLAYER2
        kalah.board = [3, 2, 6, 1, 7, 4, 18, 0, 0, 0, 0, 0, 1, 9] as int[]

        when:
        kalahEngine.move(kalah, 13)

        then:
        kalah.board == [0, 0, 0, 0, 0, 0, 41, 0, 0, 0, 0, 0, 0, 10] as int[]
        kalah.state == FINISHED
    }

    def '"move" Jump over opponent Kalah'() {

        given:
        Kalah kalah = Kalah.doCreate("9529bb11-563c-47cf-b79a-912174f94d6d", 6, 4, now)
        kalah.state = RUNNING
        kalah.turn = PLAYER1
        kalah.board = [3, 2, 6, 1, 2, 8, 18, 4, 3, 4, 7, 3, 1, 9] as int[]

        when:
        Kalah movedKalah = kalahEngine.move(kalah, 6)

        then:
        movedKalah.board == [4, 2, 6, 1, 2, 0, 19, 5, 4, 5, 8, 4, 2, 9] as int[]
    }

    def '"move" Move last into kalah keep the turn as it is'() {

        given:
        Kalah kalah = Kalah.doCreate("9529bb11-563c-47cf-b79a-912174f94d6d", 6, 4, now)
        kalah.state = RUNNING
        kalah.turn = turn
        kalah.board = [3, 2, 6, 1, 2, 4, 18, 4, 3, 4, 7, 3, 1, 9] as int[]

        when:
        kalahEngine.move(kalah, pitId)

        then:
        kalah.turn == turn

        where:
        turn    | pitId
        PLAYER2 | 10
        PLAYER1 | 5
    }

    def '"move" Put last stone into empty pit of the player side should take the stone and its opponent opposite stones'() {

        given:
        Kalah kalah = Kalah.doCreate("9529bb11-563c-47cf-b79a-912174f94d6d", 6, 4, now)
        kalah.state = RUNNING
        kalah.turn = turn
        kalah.board = board

        when:
        kalahEngine.move(kalah, pitId)

        then:
        kalah.board == expectedBoard as int[]
        kalah.turn == turn.opponentSide

        where:
        board                                                | turn    | pitId | expectedBoard
        [3, 2, 6, 1, 2, 4, 18, 4, 3, 4, 7, 0, 1, 9] as int[] | PLAYER2 | 9     | [3, 0, 6, 1, 2, 4, 18, 4, 0, 5, 8, 0, 1, 12] as int[]
        [3, 2, 6, 2, 2, 0, 18, 4, 3, 4, 7, 4, 1, 9] as int[] | PLAYER1 | 4     | [3, 2, 6, 0, 3, 0, 23, 0, 3, 4, 7, 4, 1, 9] as int[]
        [0, 2, 6, 2, 2, 3, 18, 4, 2, 4, 7, 4, 2, 9] as int[] | PLAYER2 | 13    | [1, 2, 6, 2, 2, 3, 18, 4, 2, 4, 7, 4, 0, 10] as int[] // move to 0 of opponent works as normal
        [3, 2, 6, 2, 2, 3, 18, 4, 0, 4, 7, 4, 1, 9] as int[] | PLAYER1 | 6     | [3, 2, 6, 2, 2, 0, 19, 5, 1, 4, 7, 4, 1, 9] as int[] // move to 0 of opponent works as normal
    }

    def '"move" Check boundary case of moving 1'() {

        given:
        Kalah kalah = Kalah.doCreate("9529bb11-563c-47cf-b79a-912174f94d6d", 6, 4, now)
        kalah.board = [1, 1, 0, 0, 10, 0, 16, 0, 0, 0, 0, 0, 1, 19] as int[]

        when:
        kalahEngine.move(kalah, 2)

        then:
        kalah.board == [1, 0, 0, 0, 10, 0, 17, 0, 0, 0, 0, 0, 1, 19] as int[]
    }

}
