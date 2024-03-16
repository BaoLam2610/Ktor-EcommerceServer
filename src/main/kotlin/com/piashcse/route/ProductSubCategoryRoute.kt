package com.piashcse.route

import com.papsign.ktor.openapigen.route.path.auth.delete
import com.papsign.ktor.openapigen.route.path.auth.get
import com.papsign.ktor.openapigen.route.path.auth.post
import com.papsign.ktor.openapigen.route.path.auth.put
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import com.piashcse.controller.ProductSubCategoryController
import com.piashcse.models.subcategory.AddProductSubCategory
import com.piashcse.models.subcategory.DeleteSubCategory
import com.piashcse.models.subcategory.PagingDataWithCategoryId
import com.piashcse.models.subcategory.UpdateProductSubCategory
import com.piashcse.models.user.body.JwtTokenBody
import com.piashcse.plugins.RoleManagement
import com.piashcse.utils.ApiResponse
import com.piashcse.utils.AppConstants
import com.piashcse.utils.Response
import com.piashcse.utils.authenticateWithJwt
import com.piashcse.utils.extension.fileNameInServer
import io.ktor.http.*

fun NormalOpenAPIRoute.productSubCategoryRoute(subCategoryController: ProductSubCategoryController) {
    route("product-sub-category") {
        authenticateWithJwt(RoleManagement.USER.role, RoleManagement.SELLER.role, RoleManagement.ADMIN.role) {
            route("/{categoryId}").get<PagingDataWithCategoryId, Response, JwtTokenBody> { params ->
                params.validation()
                respond(ApiResponse.success(subCategoryController.getProductSubCategory(params), HttpStatusCode.OK))
            }
        }

        authenticateWithJwt(RoleManagement.ADMIN.role) {
            post<Unit, Response, AddProductSubCategory, JwtTokenBody> { _, requestBody ->
                requestBody.validation()
                var fileNameInServer: String? = null
                if (requestBody.file != null)
                    fileNameInServer = fileNameInServer(requestBody, AppConstants.Image.CATEGORY_IMAGE_LOCATION)
                respond(
                    ApiResponse.success(
                        subCategoryController.createProductSubCategory(requestBody, fileNameInServer),
                        HttpStatusCode.OK
                    )
                )
            }
            put<UpdateProductSubCategory, Response, Unit, JwtTokenBody> { params, _ ->
                params.validation()
                respond(ApiResponse.success(subCategoryController.updateProductSubCategory(params), HttpStatusCode.OK))
            }
            delete<DeleteSubCategory, Response, JwtTokenBody> { params ->
                params.validation()
                respond(
                    ApiResponse.success(
                        subCategoryController.deleteProductSubCategory(params.subCategoryId), HttpStatusCode.OK
                    )
                )
            }
        }
    }
}