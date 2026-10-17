# Libraries ship their own R8 rules; only app-specific ones go here.

# Keep line numbers in crash reports, but hide the original file names.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
