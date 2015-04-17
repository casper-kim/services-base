include $(CLEAR_VARS)
LOCAL_MODULE    := aj-check
include $(BUILD_SHARED_LIBRARY)
$(info AJ> TOOLCHAIN_PREFIX = $(TOOLCHAIN_PREFIX))
$(info AJ> TOOLCHAIN_PREBUILT_ROOT = $(TOOLCHAIN_PREBUILT_ROOT))
$(info AJ> CFLAGS = $(TARGET_CFLAGS) $(TARGET_NO_EXECUTE_CFLAGS))
$(info AJ> CXXFLAGS = $(TARGET_CXXFLAGS) $(TARGET_NO_EXECUTE_CFLAGS))
$(info AJ> LDFLAGS = $(TARGET_LDFLAGS) $(TARGET_NO_EXECUTE_LDFLAGS) $(TARGET_NO_UNDEFINED_LDFLAGS))
$(info AJ> INCLUDES = $(TARGET_C_INCLUDES) $(__ndk_modules.$(APP_STL).EXPORT_C_INCLUDES))
$(info AJ> debug_FLAGS = $(TARGET_$(TARGET_ARCH)_debug_CFLAGS))
$(info AJ> release_FLAGS = $(TARGET_$(TARGET_ARCH)_release_CFLAGS))
$(info AJ> SYSROOT = $(NDK_PLATFORM_$(NDK_APP_PLATFORM)_$(TARGET_ARCH)_SYSROOT))

