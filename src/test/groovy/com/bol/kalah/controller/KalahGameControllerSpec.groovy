package com.bol.kalah.controller

import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

class KalahGameControllerSpec extends Specification {

//    MockMvc mockMvc
//    KalahGameService kalahGameService
//
//    def setup() {
//        kalahGameService = Stub()
//        KalahGameController gameController = new KalahGameController()
//        gameController.kalahGameService = kalahGameService
//        mockMvc = MockMvcBuilders.standaloneSetup(gameController).build()
//    }
//
//    def '"POST /games" should create a new game'() {
//        given:
//        Long id = 12L
//        kalahGameService.create() >>
//                new KalahGameTo(id, [1: 6, 2: 6, 3: 6, 4: 6, 5: 6, 6: 6, 7: 0, 8: 6, 9: 6, 10: 6, 11: 6, 12: 6, 13: 6, 14: 0])
//
//        when:
//        def response = mockMvc.perform(MockMvcRequestBuilders.post("/games"))
//
//        then:
//        response.andExpect(MockMvcResultMatchers.status().isCreated())
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("id").value(id))
//                .andExpect(MockMvcResultMatchers.jsonPath("url").value("http://localhost/games/" + id))
//    }
//
//    def '"PUT /games/{gameId}/pits/{pitId}" when KalahGameNotFoundException then expect 404'() {
//        given:
//        kalahGameService.move(_, _) >> { ID ->
//            throw new KalahGameNotFoundException()
//        }
//
//        when:
//        def response = mockMvc.perform(MockMvcRequestBuilders.put("/games/{gameId}/pits/{pitId}", 10, 1))
//
//        then:
//        response.andExpect(MockMvcResultMatchers.status().isNotFound())
//    }
//
//    def '"PUT /games/{gameId}/pits/{pitId}" when EmptyPitException then expect 409 conflict'() {
//        given:
//        Long id = 12L
//        kalahGameService.move(_, _) >> { ID ->
//            throw new EmptyPitException()
//        }
//
//        when:
//        def response = mockMvc.perform(MockMvcRequestBuilders.put("/games/{gameId}/pits/{pitId}", 10, 1))
//
//        then:
//        response.andExpect(MockMvcResultMatchers.status().isConflict())
//    }
//
//    def '"PUT /games/{gameId}/pits/{pitId}" when NotYourTurnException then expect 409 conflict'() {
//        given:
//        kalahGameService.move(_, _) >> { ID ->
//            throw new NotYourTurnException()
//        }
//
//        when:
//        def response = mockMvc.perform(MockMvcRequestBuilders.put("/games/{gameId}/pits/{pitId}", 10, 1))
//
//        then:
//        response.andExpect(MockMvcResultMatchers.status().isConflict())
//    }
//
//    def '"PUT /games/{gameId}/pits/{pitId}" when ValidationException then expect 400'() {
//        given:
//        kalahGameService.move(_, _) >> { ID ->
//            throw new ValidationException()
//        }
//
//        when:
//        def response = mockMvc.perform(MockMvcRequestBuilders.put("/games/{gameId}/pits/{pitId}", 10, 1))
//
//        then:
//        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
//    }
//
//    def '"PUT /games/{gameId}/pits/{pitId}" normal behavior should returns id, url and status'() {
//        given:
//        Long id = 12L
//        Integer pitId = 3
//        kalahGameService.move(id, pitId) >>
//                new KalahGameTo(id, ["1": "6", "2": "6", "3": "0", "4": "7", "5": "7", "6": "7", "7": "1",
//                                     "8": "7", "9": "7", "10": "6", "11": "6", "12": "6", "13": "6", "14": "0"])
//
//        when:
//        def response = mockMvc.perform(MockMvcRequestBuilders.put("/games/{gameId}/pits/{pitId}", id, pitId))
//
//        then:
//        response.andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("id").value(id))
//                .andExpect(MockMvcResultMatchers.jsonPath("url").value("http://localhost/games/" + id))
//                .andExpect(MockMvcResultMatchers.jsonPath("status")
//                        .value(["1": "6", "2": "6", "3": "0", "4": "7", "5": "7", "6": "7", "7": "1",
//                                "8": "7", "9": "7", "10": "6", "11": "6", "12": "6", "13": "6", "14": "0"]))
//    }

}
