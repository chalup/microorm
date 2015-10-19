-keepclasseswithmembers,allowobfuscation class * {
     <init>(...);
     @org.chalup.microorm.annotations.Column <fields>;
}

-keepclasseswithmembers,allowobfuscation class * {
     <init>(...);
     @org.chalup.microorm.annotations.Embedded <fields>;
}

-keepattributes *Annotation*