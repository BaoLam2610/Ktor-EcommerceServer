package com.piashcse.route

import com.papsign.ktor.openapigen.route.path.auth.get
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import com.piashcse.controller.HomeController
import com.piashcse.models.PagingData
import com.piashcse.models.user.body.JwtTokenBody
import com.piashcse.plugins.RoleManagement
import com.piashcse.utils.ApiResponse
import com.piashcse.utils.Response
import com.piashcse.utils.authenticateWithJwt
import io.ktor.http.*

fun NormalOpenAPIRoute.homeRoute(homeController: HomeController) {
    route("home") {
        authenticateWithJwt(RoleManagement.USER.role, RoleManagement.SELLER.role, RoleManagement.ADMIN.role) {
            get<PagingData, Response, JwtTokenBody> { params ->
                params.validation()
                respond(
                    ApiResponse.success(
                        homeController.getHome(
                            params
                        ), HttpStatusCode.OK
                    )
                )
            }
        }
    }
}