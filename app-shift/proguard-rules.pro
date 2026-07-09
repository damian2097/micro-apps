# Keep serialization classes intact
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod
-keep class com.microapps.** { *; }
-keep class kotlinx.serialization.** { *; }
-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
