package com.piashcse.route

import com.papsign.ktor.openapigen.route.path.auth.*
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import com.piashcse.controller.ProductController
import com.piashcse.models.product.request.*
import com.piashcse.models.user.body.JwtTokenBody
import com.piashcse.models.user.body.MultipartImage
import com.piashcse.plugins.RoleManagement
import com.piashcse.utils.ApiResponse
import com.piashcse.utils.PageResult
import com.piashcse.utils.Response
import com.piashcse.utils.authenticateWithJwt
import com.piashcse.utils.extension.fileNameInServer
import io.ktor.http.*

fun NormalOpenAPIRoute.productRoute(productController: ProductController) {
    route("product") {
        authenticateWithJwt(RoleManagement.USER.role, RoleManagement.SELLER.role, RoleManagement.ADMIN.role) {
            route("/{productId}").get<ProductDetail, Response, JwtTokenBody> { params ->
                params.validation()
                respond(ApiResponse.success(productController.productDetail(params), HttpStatusCode.OK))
            }
            get<ProductWithFilter, Response, JwtTokenBody> { params ->
                params.validation()
                var pageResult: PageResult? = null
                val response = productController.getProduct(params) {
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
            route("seller").get<ProductWithFilter, Response, JwtTokenBody> { params ->
                params.validation()
                respond(
                    ApiResponse.success(
                        productController.getProductById(principal().userId, params), HttpStatusCode.OK
                    )
                )
            }
            post<Unit, Response, AddProduct, JwtTokenBody> { _, requestBody ->
                requestBody.validation()
                val fileNameInServer = fileNameInServer(requestBody)
                respond(
                    ApiResponse.success(
                        productController.addProduct(
                            principal().userId, requestBody, fileNameInServer
                        ), HttpStatusCode.OK
                    )
                )
            }
            route("/{productId}").put<ProductIdPathParam, Response, UpdateProduct, JwtTokenBody> { params, requestBody ->
                respond(
                    ApiResponse.success(
                        productController.updateProduct(principal().userId, params.productId, requestBody),
                        HttpStatusCode.OK
                    )
                )
            }
            delete<ProductId, Response, JwtTokenBody> { params ->
                params.validation()
                respond(
                    ApiResponse.success(
                        productController.deleteProduct(principal().userId, params), HttpStatusCode.OK
                    )
                )
            }
            route("photo-upload").post<ProductId, Response, MultipartImage, JwtTokenBody> { params, multipartData ->
                params.validation()
                multipartData.validation()


                val fileNameInServer = fileNameInServer(multipartData)
                respond(
                    ApiResponse.success(
                        productController.uploadProductImages(
                            principal().userId,
                            params.productId,
                            fileNameInServer
                        ),
                        HttpStatusCode.OK
                    )
                )
            }
        }
    }
}