# CalculadoraPerruna ProGuard Rules

# Keep annotations
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses

# Domain models — used by Firebase Firestore deserialization (toObject)
-keepclassmembers class com.alvaroquintana.edadperruna.core.domain.model.** { *; }
-keep class com.alvaroquintana.edadperruna.core.domain.model.** { *; }

# Room entities
-keep class com.alvaroquintana.edadperruna.core.data.local.entity.** { *; }

# Hilt — generated components
-keep class dagger.hilt.** { *; }

# Kotlin Serialization — keep annotated classes (navigation routes, domain models)
-keepattributes RuntimeVisibleAnnotations
-keep @kotlinx.serialization.Serializable class * { *; }
-keepclassmembers class * {
    @kotlinx.serialization.Serializable *;
}

# Crashlytics
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
