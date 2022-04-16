package com.bol.kalah.service.mapper

import com.bol.kalah.service.model.Kalah
import com.bol.kalah.to.KalahTo
import spock.lang.Specification

import static com.bol.kalah.service.model.Kalah.PlayerTurn.PLAYER2

class KalahGameMapperSpec extends Specification {

    def 'mapToDto null'() {

        given:
        KalahGameMapper kalahGameMapper = new KalahGameMapper()

        when:
        KalahTo kalahTo = kalahGameMapper.mapToDto(null);

        then:
        kalahTo == null
    }

    def 'mapToDto should convert domain to a dto with status as a map of indexes(starting by 1) to values'() {

        given:
        KalahGameMapper kalahGameMapper = new KalahGameMapper()
        Kalah kalah = new Kalah()
        int[] board = [6, 8, 5, 4, 1, 7, 9, 0, 11, 13, 16, 18, 20, 23]
        kalah.board = board
        kalah.turn = PLAYER2

        when:
        KalahTo kalahTo = kalahGameMapper.mapToDto(kalah);

        then:
        kalahTo.getStatus().size() == board.size()
        for (int i = 1; i <= board.length; i++)
            assert kalahTo.getStatus().get(String.valueOf(i)) == String.valueOf(board[i - 1])
        kalahTo.turn == PLAYER2
    }

}
