# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/yixuanxuan/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keep public class * extends android.content.BroadcastReceiver
-keep class com.scrat.app.bus.push.MiMessageReceiver {*;}

-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip
# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}
# Keep native methods
-keepclassmembers class * {
    native <methods>;
}
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**

-keep class com.scrat.app.bus.model.**{*;}
-keep class * implements java.io.Serializable
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.os.*
-keep public class * extends android.view.*
-keep public class * extends android.widget.*
-keep public class * extends android.content.*
-keep interface * extends android.*
-keep interface * extends com.android.*

-keep class com.google.gson.**{*;}
-keepattributes Signature
# For using GSON @Expose annotation
-keepattributes *Annotation*