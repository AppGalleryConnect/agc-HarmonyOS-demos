# config module specific ProGuard rules here.
-verbose
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-optimizations !library/gson
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

-printmapping xyz.mapping
-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep public class * extends *.aafwk.ability.Ability
-keep public class * extends *.ace.ability.AceAbility
-keep public class * extends *.aafwk.ability.AbilitySlice
-keep public class * extends *.aafwk.ability.AbilityPackage

-keepattributes Exceptions, Signature, InnerClasses, LineNumberTable

-ignorewarnings
-keep class com.huawei.agconnect.**{*;}
-keep class com.huawei.apm.crash.**{*;}