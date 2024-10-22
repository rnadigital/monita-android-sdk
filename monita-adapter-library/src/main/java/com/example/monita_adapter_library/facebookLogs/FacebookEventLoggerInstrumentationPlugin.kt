package com.example.monita_adapter_library.facebookLogs

import net.bytebuddy.build.Plugin
import net.bytebuddy.description.type.TypeDescription
import net.bytebuddy.dynamic.DynamicType
import net.bytebuddy.matcher.ElementMatchers
import net.bytebuddy.asm.Advice
import com.facebook.appevents.AppEventsLogger
import net.bytebuddy.description.method.MethodDescription
import net.bytebuddy.dynamic.ClassFileLocator

class FacebookEventLoggerInstrumentationPlugin : Plugin {

    override fun matches(target: TypeDescription): Boolean {
        // Instrument the AppEventsLogger class
        return target.name == AppEventsLogger::class.java.name
    }

    override fun apply(
        builder: DynamicType.Builder<*>,
        typeDescription: TypeDescription,
        classFileLocator: ClassFileLocator
    ): DynamicType.Builder<*> {
        return builder
            // Intercept the logEvent(String, Bundle) method
            .visit(Advice.to(LogEventAdviceFb::class.java).on(ElementMatchers.named<MethodDescription?>("logEvent")
                .and(ElementMatchers.takesArguments(String::class.java, android.os.Bundle::class.java))))

    }

    override fun close() {
        // No resources to close in this example
    }
}
