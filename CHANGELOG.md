# Change Log
All notable changes to this project will be documented in this file.

Group changes to describe their impact on the project, as follows:

    Added for new features.
    Changed for changes in existing functionality.
    Deprecated for once-stable features removed in upcoming releases.
    Removed for deprecated features removed in this release.
    Fixed for any bug fixes.
    Security to invite users to upgrade in case of vulnerabilities.

## [1.0.0-alpha]

### Changed
- Rebranded the app from Linphone to Wadophone: app name, application ID
  (`org.wadophone`), internal Kotlin package (`org.linphone.*` →
  `org.wadophone.*`), default accent color (red), app icon (adaptive icon,
  legacy launcher icons, notification icon), splash screen icon and welcome
  screen logo, all using Wadophone's own artwork.
- The account assistant ("Add account") now goes straight to the third-party
  SIP account form, with no visible flash of the Linphone-brand login screen
  first; creating/logging into a Linphone-brand (Belledonne) SIP account is
  no longer offered.
- After granting permissions on first launch, the app now opens directly into
  the main screen instead of forcing account setup; adding an account is only
  triggered from the drawer menu's "Add account" entry.
- Help screen: the "Version" row stays, but "Check for update" is hidden (it
  depended on Linphone's own release server, which only ever reports Linphone
  updates); the "GNU General Public License v3.0" entry is no longer a link
  to Linphone's site, and its copyright line now reads "© 2026 Wadophone.
  Originally based on the Linphone project by Belledonne Communications."
- Versioning: own scheme starting at `1.0.0-alpha`, computed from git tags
  (see README's "Versioning" section), instead of Linphone's `6.x` numbering.

### Removed
- Group chat (creating group conversations).
- Group video conferencing, including merging ongoing calls into a
  conference, and the "Meetings" section.
- Firebase push notifications / Crashlytics (require a project-specific
  google-services.json, not provided in this fork).
- Help screen: "User guide" and "Privacy policy" entries (both linked to
  Belledonne's own site).
- Play Store / F-Droid metadata describing the original Linphone app
  (`metadata/`), since it no longer applies to Wadophone.

### Fixed
- Several translation issues found while auditing `values-es` and
  `values-fr` against the English base: an empty `vertical_separator` string
  in Spanish (showed as a missing "|" divider on the in-call screen), 6
  untranslated strings in French (the door-opener feature), and pre-existing
  mojibake (mis-encoded "…" and a few emoji) in the English base strings.
- Two AAPT resource warnings about ambiguous string formatting
  (`notification_file_transfer_upload_download_message`,
  `website_open_source_licences_usage_url`).

One-to-one audio/video calls and one-to-one conversations are unaffected by
any of the above.
