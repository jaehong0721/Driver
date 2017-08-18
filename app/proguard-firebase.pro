# Begin: Proguard rules for Firebase

#database
-keepattributes Signature
-keepclassmembers class com.rena21.driver.models.** {*;}

#auth
-keepattributes *Annotation*

#crash

# End: Proguard rules for Firebase