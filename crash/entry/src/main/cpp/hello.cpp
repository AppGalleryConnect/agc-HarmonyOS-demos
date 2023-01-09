#include <jni.h>
#include <string>
#include <pthread.h>
#include <hilog/log.h>

int apm_crash_test_call_4(int v) {
    std::string hello = "hello";
    char array[3]="12";
    hello.copy(array, 6);

    return v;
}

int apm_crash_test_call_3(int v) {
    int r = apm_crash_test_call_4(v + 1);
    return r;
}

int apm_crash_test_call_2(int v) {
    int r = apm_crash_test_call_3(v + 1);
    return r;
}

void apm_crash_test_call_1(void) {
    int r = apm_crash_test_call_2(1);
    r = 0;
}
static void *apm_crash_test_new_thread(void *arg) {
    (void)arg;
    pthread_detach(pthread_self());
    pthread_setname_np(pthread_self(), "apm_crash_test_cal");

    apm_crash_test_call_1();

    return NULL;
}
extern "C" JNIEXPORT void JNICALL
Java_com_huawei_hag_jsapplication_TestCrash_createNativeException(JNIEnv* env, jobject obj, jint newThread) {
    HILOG_DEBUG(LOG_APP, "=====>start jnicall");
    pthread_t tid;
    if (newThread) {
        pthread_create(&tid, NULL, &apm_crash_test_new_thread, NULL);
    } else {
        apm_crash_test_call_1();
    }
}
