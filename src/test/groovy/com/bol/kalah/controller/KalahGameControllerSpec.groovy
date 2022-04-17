package com.bol.kalah.controller

import com.bol.kalah.controller.error.RestExceptionHandler
import com.bol.kalah.service.KalahService
import com.bol.kalah.service.exception.InvalidMoveException
import com.bol.kalah.service.exception.KalahFinishedException
import com.bol.kalah.service.exception.KalahNotFoundException
import com.bol.kalah.to.KalahTo
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static com.bol.kalah.service.exception.BusinessErrorsEnum.*
import static com.bol.kalah.service.model.Kalah.PlayerTurn.PLAYER1
import static com.bol.kalah.service.model.Kalah.PlayerTurn.PLAYER2
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class KalahGameControllerSpec extends Specification {

    MockMvc mockMvc
    KalahService kalahService
    String testId = "9529bb11-563c-47cf-b79a-912174f94d6d"

    def setup() {
        kalahService = Mock()
        KalahController gameController = new KalahController(kalahService)
        mockMvc = MockMvcBuilders.standaloneSetup(gameController, new RestExceptionHandler()).build()
    }

    def '"POST /games" should create a new game'() {
        given:
        kalahService.create() >>
                new KalahTo(testId,
                        ["1" : "6", "2": "6", "3": "6", "4": "6", "5": "6", "6": "6", "7": "0", "8": "6", "9": "6",
                         "10": "6", "11": "6", "12": "6", "13": "6", "14": "0"],
                        PLAYER2)

        when:
        def response = mockMvc.perform(post("/games"))

        then:
        response.andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("id").value(testId))
                .andExpect(jsonPath("status")
                        .value(["1" : "6", "2": "6", "3": "6", "4": "6", "5": "6", "6": "6", "7": "0", "8": "6", "9": "6",
                                "10": "6", "11": "6", "12": "6", "13": "6", "14": "0"]))
                .andExpect(jsonPath("turn").value("PLAYER2"))
    }

    def '"PUT /games/{gameId}/pits/{pitId}" when KalahNotFoundException then expect 404'() {
        given:
        kalahService.move('no_id', 1) >> {
            throw new KalahNotFoundException("Game not found!")
        }

        when:
        def response = mockMvc.perform(put("/games/{gameId}/pits/{pitId}", 'no_id', 1))

        then:
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("Game not found!"))
                .andExpect(jsonPath("errorCode").value(GAME_NOT_FOUND.name()))
    }

    def '"PUT /games/{gameId}/pits/{pitId}" when InvalidMoveException then expect 400'() {
        given:
        kalahService.move("xyz", 1) >> {
            throw new InvalidMoveException("Invalid move!")
        }

        when:
        def response = mockMvc.perform(put("/games/{gameId}/pits/{pitId}", "xyz", 1))

        then:
        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Invalid move!"))
                .andExpect(jsonPath("errorCode").value(INVALID_MOVE.name()))
    }

    def '"PUT /games/{gameId}/pits/{pitId}" when KalahFinishedException then expect 409'() {
        given:
        kalahService.move("xyz", 1) >> {
            throw new KalahFinishedException("Game has been finished!")
        }

        when:
        def response = mockMvc.perform(put("/games/{gameId}/pits/{pitId}", "xyz", 1))

        then:
        response.andExpect(status().isConflict())
                .andExpect(jsonPath("message").value("Game has been finished!"))
                .andExpect(jsonPath("errorCode").value(GAME_FINISHED.name()))
    }

    def '"PUT /games/{gameId}/pits/{pitId}" normal behavior should returns id, status and turn'() {
        given:
        kalahService.move(testId, 3) >>
                new KalahTo(testId, ["1": "6", "2": "6", "3": "0", "4": "7", "5": "7", "6": "7", "7": "1",
                                     "8": "7", "9": "7", "10": "6", "11": "6", "12": "6", "13": "6", "14": "0"], PLAYER1)

        when:
        def response = mockMvc.perform(put("/games/{gameId}/pits/{pitId}", testId, 3))

        then:
        response.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("id").value(testId))
                .andExpect(jsonPath("status")
                        .value(["1": "6", "2": "6", "3": "0", "4": "7", "5": "7", "6": "7", "7": "1",
                                "8": "7", "9": "7", "10": "6", "11": "6", "12": "6", "13": "6", "14": "0"]))
                .andExpect(jsonPath("turn").value("PLAYER1"))
    }

    def '"GET /games/{gameId}" normal behavior should returns id, status and turn'() {
        given:
        kalahService.get(testId) >>
                new KalahTo(testId, ["1": "6", "2": "6", "3": "0", "4": "7", "5": "7", "6": "7", "7": "1",
                                     "8": "7", "9": "7", "10": "6", "11": "6", "12": "6", "13": "6", "14": "0"], PLAYER2)

        when:
        def response = mockMvc.perform(get("/games/{gameId}", testId))

        then:
        response.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("id").value(testId))
                .andExpect(jsonPath("status")
                        .value(["1": "6", "2": "6", "3": "0", "4": "7", "5": "7", "6": "7", "7": "1",
                                "8": "7", "9": "7", "10": "6", "11": "6", "12": "6", "13": "6", "14": "0"]))
                .andExpect(jsonPath("turn").value("PLAYER2"))
    }

    def '"GET /games/{gameId}" when KalahNotFoundException then expect 404'() {
        given:
        kalahService.get(testId) >> {
            throw new KalahNotFoundException("Game not found!")
        }

        when:
        def response = mockMvc.perform(get("/games/{gameId}", testId))

        then:
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("Game not found!"))
                .andExpect(jsonPath("errorCode").value(GAME_NOT_FOUND.name()))
    }


}
