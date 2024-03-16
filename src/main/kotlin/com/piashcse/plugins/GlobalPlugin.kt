package com.piashcse.plugins

import com.papsign.ktor.openapigen.OpenAPIGen
import com.papsign.ktor.openapigen.schema.namer.DefaultSchemaNamer
import com.papsign.ktor.openapigen.schema.namer.SchemaNamer
import com.piashcse.models.global.ServerConfig
import com.piashcse.serverConfig
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.util.network.*
import java.net.InetAddress
import kotlin.reflect.KType

fun Application.configureBasic() {
   /* install(Compression)
    install(CORS) {
        anyHost()
    }*/
    install(CallLogging)
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            // serializeNulls()
        }
    }
    val serverPort = environment.config.propertyOrNull("ktor.deployment.port")?.getString()
    val serverHost = environment.config.propertyOrNull("ktor.deployment.host")?.getString()

    serverConfig = ServerConfig(
        host = serverHost,
        port = serverPort,
        url = "http://$serverHost:$serverPort/"
    )

    println("sos - server: $serverConfig")

    // Open api configuration
    install(OpenAPIGen) {
        // basic info
        info {
            version = "1.0.0"
            title = "Ktor E-Commerce"
            description = "API Documentation for Ktor Ecommerce App"
            contact {
                name = "Nguyen Bao Lam"
                email = "baolam.nguyen2610@gmail.com"
            }
        }
        // describe the server, add as many as you want
        server(serverConfig.url!!) {
            description = "Base URL"
        }
        //optional custom schema object name
        replaceModule(DefaultSchemaNamer, object : SchemaNamer {
            val regex = Regex("[A-Za-z0-9_.]+")
            override fun get(type: KType): String {
                return type.toString().replace(regex) { it.value.split(".").last() }.replace(Regex(">|<|, "), "_")
            }
        })
    }
}