package com.piashcse.plugins

import com.papsign.ktor.openapigen.APITag
import com.papsign.ktor.openapigen.openAPIGen
import com.papsign.ktor.openapigen.route.apiRouting
import com.papsign.ktor.openapigen.route.tag
import com.piashcse.controller.*
import com.piashcse.route.*
import com.piashcse.utils.AppConstants
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Application.configureRouting() {
    install(Routing) {
        // open api json loading
        get("/openapi.json") {
            call.respond(application.openAPIGen.api.serialize())
        }
        get("/") {
            call.respondRedirect("/swagger-ui/index.html?url=/openapi.json", true)
        }
        get("product-image/{name}") {
            // get filename from request url
            val filename = call.parameters["name"]!!
            println("lamnb: $filename")
            // construct reference to file
            // ideally this would use a different filename
            val file = File("${AppConstants.Image.PRODUCT_IMAGE_LOCATION}$filename")
            if (file.exists()) {
                call.respondFile(file)
            } else call.respond(HttpStatusCode.NotFound)
        }
        get("profile-image/{name}") {
            // get filename from request url
            val filename = call.parameters["name"]!!
            // construct reference to file
            // ideally this would use a different filename
            val file = File("${AppConstants.Image.PROFILE_IMAGE_LOCATION}$filename")
            if (file.exists()) {
                call.respondFile(file)
            } else call.respond(HttpStatusCode.NotFound)
        }
        // Api routing
        apiRouting {
            tag(Tags.USER) {
                userRoute(UserController())
            }
            tag(Tags.PROFILE) {
                profileRouting(ProfileController())
            }
            tag(Tags.SHOP) {
                shopRoute(ShopController())
            }
            tag(Tags.PRODUCT) {
                productRoute(ProductController())
            }
            tag(Tags.PRODUCT_CATEGORY) {
                productCategoryRoute(ProductCategoryController())
            }
            tag(Tags.PRODUCT_SUB_CATEGORY) {
                productSubCategoryRoute(ProductSubCategoryController())
            }
            tag(Tags.BRAND) {
                brandRoute(BrandController())
            }
            tag(Tags.WISHLIST) {
                wishListRoute(WishListController())
            }
            tag(Tags.CART) {
                cartRoute(CartController())
            }
            tag(Tags.ORDER) {
                orderRoute(OrderController())
            }
            tag(Tags.SHIPPING) {
                shippingRoute(ShippingController())
            }
            tag(Tags.EVENT) {
                eventRoute(EventController())
            }
        }
    }
}

enum class Tags(override val description: String) : APITag {
    USER(""),
    PROFILE(""),
    SHOP(""),
    PRODUCT(""),
    PRODUCT_CATEGORY(""),
    PRODUCT_SUB_CATEGORY(""),
    BRAND(""),
    CART(""),
    ORDER(""),
    WISHLIST(""),
    SHIPPING(""),
    EVENT(""),
    IMAGE(""),
}
