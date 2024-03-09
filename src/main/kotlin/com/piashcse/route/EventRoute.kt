package com.piashcse.route

import com.papsign.ktor.openapigen.route.path.auth.get
import com.papsign.ktor.openapigen.route.path.auth.post
import com.papsign.ktor.openapigen.route.path.auth.principal
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute

import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import com.piashcse.controller.EventController
import com.piashcse.models.PagingData
import com.piashcse.models.event.AddEvent
import com.piashcse.models.user.body.JwtTokenBody
import com.piashcse.plugins.RoleManagement
import com.piashcse.utils.ApiResponse
import com.piashcse.utils.PageResult
import com.piashcse.utils.Response
import com.piashcse.utils.authenticateWithJwt
import com.piashcse.utils.extension.fileNameInServer
import io.ktor.http.*

fun NormalOpenAPIRoute.eventRoute(eventController: EventController) {
    route("event") {
        authenticateWithJwt(RoleManagement.USER.role, RoleManagement.SELLER.role, RoleManagement.ADMIN.role) {
            get<PagingData, Response, JwtTokenBody> { params ->
                params.validation()
                var pageResult: PageResult? = null
                val response = eventController.getEvents(params) {
                    pageResult = it
                }
                respond(
                    ApiResponse.success(
                        data = response,
                        statsCode = HttpStatusCode.OK,
                        pageResult = pageResult
                    )
                )
            }
        }
        authenticateWithJwt(RoleManagement.SELLER.role) {
            post<Unit, Response, AddEvent, JwtTokenBody> { _, requestBody ->
                requestBody.validation()
                val fileNameInServer = fileNameInServer(requestBody)
                respond(
                    ApiResponse.success(
                        eventController.addEvent(
                            principal().userId, fileNameInServer, requestBody
                        ), HttpStatusCode.OK
                    )
                )
            }
        }
    }
}