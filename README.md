
Wadophone is an open source softphone for voice and video over IP calling and instant messaging.

It is fully SIP-based, for all calling, presence and IM features.

Wadophone is a fork of [Linphone](https://linphone.org), developed and maintained
by Belledonne Communications. All credit for the original design, SIP stack and
core functionality goes to the Linphone project; this fork focuses on its own
branding, and on a reduced feature set tailored to connecting to a personal or
private SIP server (e.g. Asterisk) rather than Belledonne's own SIP service.

Compared to upstream Linphone, this fork:
- Only supports third-party SIP accounts (no Linphone-brand account creation/login).
- Has no group chat, group video conferencing, or "Meetings" section — only
  one-to-one calls and one-to-one conversations.
- Ships its own name, application ID, color scheme and icon (see
  [CHANGELOG.md](CHANGELOG.md) for the full list of changes).

### How to get it

There is no dedicated Google Play / F-Droid listing for Wadophone yet — build
it yourself following the instructions below. If you're looking for the
original Linphone app instead, it's available on
[Google Play](https://play.google.com/store/apps/details?id=org.linphone) and
[F-Droid](https://f-droid.org/en/packages/org.linphone/).

### License

Copyright © Wadophone contributors.

Like Linphone, Wadophone is licensed under the [GNU/GPLv3 license](https://www.gnu.org/licenses/gpl-3.0.en.html),
free and open source. Please make sure that you understand and agree with the
terms of this license before using it (see the LICENSE file for details).

Linphone itself is dual licensed and also available under a proprietary license
for closed source applications; that option applies to the original Linphone
project only. Contact [Belledonne Communications](https://linphone.org/contact)
for any question about it.

### Documentation

This fork reuses Linphone's SIP engine (liblinphone) as a Maven dependency, so
the underlying protocol documentation from the upstream project still applies:

- Supported features and RFCs: https://www.linphone.org/linphone-softphone/#linphone-fonctionnalites
- Linphone public wiki: https://wiki.linphone.org/xwiki/wiki/public/view/Linphone/

# Versioning

Wadophone has its own version numbering, independent from Linphone's, starting
at `1.0.0-alpha`. The version name shown in the app and in the generated APK
file name (`wadophone-android-<buildType>-<version>.apk`) is computed from the
latest git tag reachable from the current commit (see the `gitVersion` logic
in [app/build.gradle.kts](app/build.gradle.kts)): a build made exactly on a
tagged commit shows that tag as-is (e.g. `1.0.0-alpha`), while later commits
show `<tag>.<commits since tag>+<short commit hash>`.

The project is currently on the **alpha** channel while the fork is being
tested end-to-end. Tag new releases as they're validated
(`git tag -a x.y.z-alpha -m "..."`) and record what changed in
[CHANGELOG.md](CHANGELOG.md).

This release only works on Android OS 9.0 and newer.

# Building the app

If you have Android Studio, simply open the project, wait for the gradle synchronization and then build/install the app.  
It will download the Linphone SDK from Belledonne's Maven repository as an AAR file, so you don't have to build it yourself.

If you don't have Android Studio, you can build and install the app using gradle:
```
./gradlew assembleDebug
```
will compile the APK file (`assembleRelease` instead if you want to build a release package), and then
```
./gradlew installDebug
```
to install the generated APK from the previous step (use `installRelease` instead if you built a release package).

APK files are stored within ```./app/build/outputs/apk/debug/``` and ```./app/build/outputs/apk/release/``` directories.

When building a release AppBundle, use the `releaseAppBundle` target instead of `release`.  
Also make sure you have an NDK installed and an environment variable named ```ANDROID_NDK_HOME``` that contains the path to the NDK, to be able to include native library symbols in the app bundle.

### Release signing

`keystore.properties` is a template with your own release keystore's info
(`storePassword`, `keyPassword`, `keyAlias`, `storeFile`). It's gitignored by
convention (`app/wadophone-release.keystore`), so generate your own keystore
and fill in the values before building a signed `release` variant — the debug
build doesn't need it.

## Building a local SDK

Wadophone depends on the same `linphone-sdk-android` engine as Linphone. To build it locally instead of downloading the prebuilt AAR:

1. Clone the linphone-sdk repository:
```
git clone https://gitlab.linphone.org/BC/public/linphone-sdk.git --recursive
```

2. Follow the instructions in the linphone-sdk/README file to build the SDK.

3. Create or edit the gradle.properties file in $GRADLE_USER_HOME (usually ~/.gradle/) and add the absolute path to your linphone-sdk build directory, for example:
```
LinphoneSdkBuildDir=/home/<username>/linphone-sdk/build/
```

4. Rebuild the app in Android Studio.

## Native debugging

1. Install LLDB from SDK Tools in Android Studio.

2. In Android Studio go to Run -> Edit Configurations -> Debugger.

3. Select 'Dual' or 'Native' and add the path to the linphone-sdk debug libraries (`build/libs-debug/` for example).

4. Open the native file and set your breakpoint.

5. Make sure you're using the debug AAR (the release AAR is used by default even for debug builds, for faster build times).

6. Debug the app.

### Native crash symbolication

To get a symbolized stack trace from a native crash, you need the debug version of liblinphone (see "Building a local SDK" above, or download `linphone-sdk-android-<version>-libs-debug.zip` from https://download.linphone.org/maven_repository/org/linphone/linphone-sdk-android/ for the SDK version this app uses), plus the `ndk-stack` tool from the Android NDK.

```
adb logcat -d | ndk-stack -sym ./libs-debug/arm64-v8a/
```
(replace `arm64-v8a` with your device's actual CPU architecture if different; `adb shell getprop ro.product.cpu.abi` will tell you). This won't print anything until you reproduce the crash.

## Known issues

- If you hit a build error about `drawable/linphone_logo_tinted` not being found, delete `app/src/main/res/xml/contacts.xml` (`git clean -f` works) and rebuild.
- If you get a `couldn't find "libc++_shared.so"` crash on startup, clean the project in Android Studio and rebuild, and double-check the SDK was built for the right CPU architecture.

## Firebase push notifications

This fork ships without a `google-services.json` file (it was tied to Linphone's own Firebase project), so Firebase Cloud Messaging and Crashlytics are disabled by default. To enable push notifications, create your own Firebase project for your `applicationId` and drop your own `app/google-services.json` in place.

## Translations

Translation files live directly under `app/src/main/res/values-<locale>/strings.xml`, maintained as regular resource files (this fork doesn't use Linphone's Weblate instance). Contributions welcome via pull request.

## Create an APK with a different package name

Edit the `packageName` variable in `app/build.gradle.kts` — the rest of the build (applicationId, manifest placeholders, file provider authority, etc.) picks it up automatically.
