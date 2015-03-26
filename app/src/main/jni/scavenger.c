#include <jni.h>
#include <signal.h>
#include <android/log.h>

#define CATCHSIG(X) sigaction(X, &handler, &old_sa[X])

static struct sigaction old_sa[NSIG];

void android_sigaction(int signal, siginfo_t *info, void *reserved) {
    // TODO invoke java method
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved)
{
    __android_log_print(ANDROID_LOG_VERBOSE, "Scavenger Jni", "JNI_OnLoad is called");
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