package com.piashcse.route

import com.papsign.ktor.openapigen.route.path.auth.delete
import com.papsign.ktor.openapigen.route.path.auth.get
import com.papsign.ktor.openapigen.route.path.auth.post
import com.papsign.ktor.openapigen.route.path.auth.put
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import com.piashcse.controller.ProductCategoryController
import com.piashcse.models.PagingData
import com.piashcse.models.category.AddProductCategory
import com.piashcse.models.category.DeleteProductCategory
import com.piashcse.models.category.UpdateProductCategory
import com.piashcse.models.user.body.JwtTokenBody
import com.piashcse.plugins.RoleManagement
import com.piashcse.utils.ApiResponse
import com.piashcse.utils.AppConstants
import com.piashcse.utils.Response
import com.piashcse.utils.authenticateWithJwt
import com.piashcse.utils.extension.fileNameInServer
import io.ktor.http.*

fun NormalOpenAPIRoute.productCategoryRoute(productCategoryController: ProductCategoryController) {
    route("product-category") {
        authenticateWithJwt(RoleManagement.USER.role, RoleManagement.SELLER.role, RoleManagement.ADMIN.role) {
            get<Unit, Response, JwtTokenBody> { params ->
//                params.validation()
                respond(ApiResponse.success(productCategoryController.getProductCategory(), HttpStatusCode.OK))
            }
        }
        authenticateWithJwt(RoleManagement.ADMIN.role) {
            post<Unit, Response, AddProductCategory, JwtTokenBody> { _, requestBody ->
                requestBody.validation()
                var fileNameInServer: String? = null
                if (requestBody.file != null)
                    fileNameInServer = fileNameInServer(requestBody, AppConstants.Image.CATEGORY_IMAGE_LOCATION)
                respond(
                    ApiResponse.success(
                        productCategoryController.createProductCategory(requestBody, fileNameInServer),
                        HttpStatusCode.OK
                    )
                )
            }
            put<UpdateProductCategory, Response, Unit, JwtTokenBody> { params, _ ->
                params.validation()
                respond(ApiResponse.success(productCategoryController.updateProductCategory(params), HttpStatusCode.OK))
            }
            delete<DeleteProductCategory, Response, JwtTokenBody> { params ->
                params.validation()
                respond(
                    ApiResponse.success(
                        productCategoryController.deleteProductCategory(params), HttpStatusCode.OK
                    )
                )
            }
        }
    }
}