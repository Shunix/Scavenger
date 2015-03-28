#include <jni.h>
#include <signal.h>
#include <android/log.h>

#define CATCHSIG(X) sigaction(X, &handler, &old_sa[X])

static struct sigaction old_sa[NSIG];
static JNIEnv *env;
static jobject obj;
static jmethodID callback;
static const char *classPathName = "com/shunix/scavenger/app/ScavengerApplication";

void JNICALL saveApplicationNative(JNIEnv* _env, jobject _obj)
{
    obj = _obj;
}

static JNINativeMethod methods[] = {
    {"saveApplicationNative", "()V", (void *)&saveApplicationNative}
};

void android_sigaction(int signal, siginfo_t *info, void *reserved) {
    (*env)->CallVoidMethod(env, obj, callback);
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved)
{
    __android_log_print(ANDROID_LOG_VERBOSE, "Scavenger Jni", "JNI_OnLoad is called");
    if ((*vm)->GetEnv(vm, (void **)&env, JNI_VERSION_1_4))
    {
       return JNI_ERR;
    }
    jclass clazz = (*env)->FindClass(env, classPathName);
    (*env)->RegisterNatives(env, clazz, methods, sizeof(methods)/sizeof(methods[0]));
    callback = (*env)->GetMethodID(env, clazz, "handleNativeCrash", "()V");
    struct sigaction handler;
    memset(&handler, 0, sizeof(sigaction));
    handler.sa_sigaction = android_sigaction;
    handler.sa_flags = SA_RESETHAND;
    CATCHSIG(SIGILL);
    CATCHSIG(SIGABRT);
    CATCHSIG(SIGBUS);
    CATCHSIG(SIGFPE);
    CATCHSIG(SIGSEGV);
    CATCHSIG(SIGPIPE);
    return JNI_VERSION_1_4;
}