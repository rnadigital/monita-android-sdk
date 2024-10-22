package com.example.monita_adapter_library

import net.bytebuddy.asm.Advice
import net.bytebuddy.build.Plugin
import net.bytebuddy.description.type.TypeDescription
import net.bytebuddy.dynamic.DynamicType
import net.bytebuddy.matcher.ElementMatchers
import okhttp3.OkHttpClient
import net.bytebuddy.dynamic.ClassFileLocator


class OkHttpInstrumentationPlugin : Plugin {

    @Suppress("INAPPLICABLE_JVM_NAME")
    override fun matches(target: TypeDescription): Boolean {
        // Target the OkHttpClient class
        return target.name == OkHttpClient::class.java.name

        }

    override fun apply(
        builder: DynamicType.Builder<*>,
        typeDescription: TypeDescription,
        classFileLocator: ClassFileLocator
    ): DynamicType.Builder<*> {
        // Intercept the newCall method and delegate to a custom interceptor
        return builder
            .visit(Advice.to(OHttpCallAdvice::class.java).on(ElementMatchers.named("newCall")))


    }

    override fun close() {
        // No resources to close in this example
    }





}
