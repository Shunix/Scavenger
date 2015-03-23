#include <jni.h>
#include <android/log.h>

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved)
{
    __android_log_print(ANDROID_LOG_VERBOSE, "Scavenger Jni", "JNI_OnLoad is called");
}