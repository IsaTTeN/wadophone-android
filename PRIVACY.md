# Wadophone – Privacy Policy

_Last updated: 2026-08-03_

Wadophone is a free, open-source SIP client. It is a fork of
[Linphone](https://linphone.org) with the built-in Linphone account
creation/registration removed: Wadophone only connects to a SIP server
that **you** configure yourself. This policy describes what happens
to your data when you use Wadophone.

## Who is responsible for your data

Wadophone does not operate any backend server. The app is a client
only: when you set up a SIP account, your credentials and
calls/messages are exchanged directly between your device and the SIP
server you chose to connect to. The operator of that SIP server (which
may be you, your employer, or a third-party provider) is responsible
for any data that transits or is stored on their server — not the
Wadophone developer.

The Wadophone developer does not receive, log, or have access to your
SIP credentials, call audio/video, or chat messages.

## Information accessed on your device

Depending on the permissions you grant, Wadophone accesses:

- **Microphone and Camera** – used only while you are making or
  receiving an audio/video call.
- **Contacts** – read locally to match phone numbers/SIP addresses to
  names and photos in your address book. Contacts are never uploaded
  anywhere by the app itself.
- **Phone state and phone number** – used to integrate SIP calls with
  Android's native call screen (ConnectionService).
- **Notifications** – used to alert you of incoming calls and
  messages.

All of the above stays on your device unless you explicitly export or
share it. Call history and chat messages are stored locally in the
app's private storage and are removed when you uninstall the app or
clear its data.

## Analytics, ads, and third-party trackers

Wadophone does not display ads and does not include any analytics or
crash-reporting SDK (such as Firebase/Crashlytics) in the version
distributed on Google Play. If this ever changes in a future version,
this policy will be updated first.

## Data shared with third parties

The only third party that ever sees your communications is the SIP
server you configure the app to connect to. Wadophone itself does not
share any data with the developer or with any other third party.

## Children's privacy

Wadophone is not directed at children and does not knowingly collect
personal information from children.

## Open source

Wadophone's full source code is public and can be audited by anyone:
https://github.com/IsaTTeN/wadophone-android

## Changes to this policy

Any change to this policy will be published in this same file, and its
history can be reviewed through the project's git history on GitHub.

## Contact

Questions about this policy can be opened as an issue on the project's
GitHub repository: https://github.com/IsaTTeN/wadophone-android/issues
