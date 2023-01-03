rm -rf ./jre
jlink --no-header-files --no-man-pages --strip-debug --compress=2 --add-modules java.base,java.desktop,java.datatransfer,java.naming,java.prefs,java.xml,java.management,jdk.unsupported --output ./jre

