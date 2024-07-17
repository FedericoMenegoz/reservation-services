package com.cherry.fm.reservationservices

import io.helidon.webserver.http.HttpRules
import io.helidon.webserver.http.HttpService
import io.helidon.webserver.staticcontent.StaticContentService

class UiController : HttpService {
    override fun routing(rules: HttpRules) {
        //rules.get("/home", this::homePageRequest)
        rules
            .register("/", StaticContentService.builder("www")
                .welcomeFileName("index.html")
                .build());
    }
}