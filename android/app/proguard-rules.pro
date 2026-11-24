# Retrofit / Gson model classes are accessed reflectively.
-keep class com.mpholo.project.grocery.android.data.model.** { *; }

# Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
