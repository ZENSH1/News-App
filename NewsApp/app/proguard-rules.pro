# R8 rules for the release build (isMinifyEnabled = true).
# Firebase, Hilt, kotlinx.serialization and Navigation 3 ship their own consumer rules, and the
# Firestore mapping in this app is written by hand (no reflection), so only these are needed.

# Keep line numbers in crash reports, but hide the original file names.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
