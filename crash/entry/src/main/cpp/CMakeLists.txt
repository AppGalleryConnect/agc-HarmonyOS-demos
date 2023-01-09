# the minimum version of CMake.
cmake_minimum_required(VERSION 3.4.1)
project(apms_crash_harmony)

add_library(hello SHARED hello.cpp)
target_link_libraries(hello libhilog_ndk.z.so)