# Contributing

<p align="center"><a href="https://github.com/MC-Dashify/plugin/blob/master/CONTRIBUTING.md">English</a> · <a href="https://github.com/MC-Dashify/plugin/blob/master/.github/documents/CONTRIBUTING.ko_KR.md">한국어</a></p>

## 기여 가이드라인

Dashify에 기여하기로 결정하셨군요. 수많은 훌륭한 오픈소스 프로젝트가 있음에도 Dashify에 기여하는데 관심을 가져주셔서 감사합니다.

## Issue 해결하기

해결되지 않은 Issue를 해결하는 데 도움이 되고 싶다면. `good first issue`, `help wanted`, `open for contribution` 등의 태그를 찾아보세요.

## 개발 환경 설정

우리는 Amazon Corretto 버전 "17.0.6"을 프로젝트 SDK로 사용하고 Kotlin@1.8.21을 프로젝트 언어로 사용합니다.

우리는 Gradle Kotlin DSL을 빌드 시스템으로 사용합니다.

플러그인을 빌드하고 서버 플러그인 폴더 (`./server/plugins/`)에 복사:
```bash
./gradlew clean buildJar
```

## 코드 포매팅

우리는 코드 포매팅을 위해 Intellij Kotlin 스타일 가이드를 사용합니다.
Intellij IDEA를 사용하면 코드 포매팅이 더 쉬울 것입니다.
