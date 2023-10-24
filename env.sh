if [ -z "$ANDROID_HOME" ]; then
    echo 'Error: $ANDROID_HOME is undefined.'
    exit 1
fi

# Credits: https://tonsky.me/blog/python-build/
export CLASSPATH="$ANDROID_HOME/platforms/android-33/android.jar"\
":$MEPATH/app/platforms/android/CordovaLib/build/intermediates/runtime_library_classes_jar/debug/classes.jar"\
":json-20231013.jar"
