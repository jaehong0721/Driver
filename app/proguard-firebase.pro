# Begin: Proguard rules for Firebase

#database
-keepattributes Signature
-keepclassmembers class com.rena21c.driver.model.** {*;}

#auth
-keepattributes *Annotation*

#crash

# End: Proguard rules for Firebase