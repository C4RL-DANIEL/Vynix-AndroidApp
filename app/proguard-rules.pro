# Proguard rules for Vynix Android app.
-keepattributes Signature
-keepattributes *Annotation*

# Keep Retrofit services
-keep,allowobfuscation @interface retrofit2.http.*

# Kotlin Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keep,includedescriptorclasses class com.vynix.android.**$$serializer { *; }
-keepclassmembers class com.vynix.android.** {
    *** Companion;
}
-keepclasseswithmembers class com.vynix.android.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep data model classes and API interfaces
-keep class com.vynix.android.model.** { *; }
-keep class com.vynix.android.data.remote.api.** { *; }
