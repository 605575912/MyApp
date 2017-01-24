# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/apple/Applications/android/android-sdk_studio/tools/proguard/proguard-android.txt
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
#}c
#-optimizationpasses 5
-dontoptimize
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose

-keep class com.support.loader.ServiceLoader{ *;}
-keepclassmembers class com.support.loader.ServiceLoader{ *;}

-keep interface com.support.loader.proguard.IProguard {*;}
-keepclassmembers interface com.support.loader.proguard.IProguard {*;}
-keep class com.support.loader.packet.ImageOptions{ *;}
-keepclassmembers class com.support.loader.packet.ImageOptions{ *;}
-keep class com.support.loader.packet.TaskPacketListenner{ *;}
-keepclassmembers class com.support.loader.packet.TaskPacketListenner{ *;}

-keep class * implements com.support.loader.proguard.IProguard {*;}
-keepclassmembers class * implements com.support.loader.proguard.IProguard {*;}

-keep class com.support.loader.utils.ImageLoadingListener{ *;}
-keepclassmembers class com.support.loader.utils.ImageLoadingListener{ *;}

-keep class com.support.loader.utils.ImageUtils{ *;}
-keepclassmembers class com.support.loader.utils.ImageUtils{ *;}

-keep class com.support.loader.adapter.**{ *;}
-keepclassmembers class com.support.loader.adapter.**{ *;}