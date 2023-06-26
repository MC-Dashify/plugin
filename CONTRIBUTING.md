# Contributing

<p align="center"><a href="https://github.com/MC-Dashify/plugin/blob/master/CONTRIBUTING.md">English</a> · <a href="https://github.com/MC-Dashify/launcher/blob/main/.github/documents/CONTRIBUTING.ko_KR.md">한국어</a></p>

## Contribution Guidelines

Thank you for choosing to contribute in Dashify. There are a ton of great open-source projects out there, so we
appreciate your interest in contributing to Dashify.

## Open Issues

If you would like to help in working on open issues. Lookout for following tags: `good first issue`, `help wanted`,
and `open for contribution`.

## Development setup

We use Amazon Corretto version "17.0.6" as project SDK and Kotlin@1.8.21 as project language.

We use Gradle Kotlin DSL as build system.

Build and copy the plugin to the server plugin folder (`./server/plugins/`):

```bash
./gradlew clean buildJar
```

## Code Formatting

We use Intellij Kotlin style guide
for code formatting. Using Intellij IDEA will make this thing easier.