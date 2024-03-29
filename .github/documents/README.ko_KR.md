<p align="center">
  <img width="128" align="center" src="https://github.com/MC-Dashify/plugin/blob/main/.github/assets/logo-512.png">
</p>
<h1 align="center">Dashify</h1>
<h3 align="center">Minecraft 서버 모니터링을 쉽고 빠르게</h3>
<p align="center">별도의 설치 없이 실행되는 Standalone 모니터링 시스템</p>
<p align="center">
  <a href="https://github.com/MC-Dashify/plugin/actions/workflows/main.yml">
    <img src="https://github.com/MC-Dashify/plugin/actions/workflows/main.yml/badge.svg" alt="Build & Publish to Release" />
  </a>
</p>

<p align="center"><a href="https://github.com/MC-Dashify/plugin/blob/main/README.md">English</a> · <a href="https://github.com/MC-Dashify/plugin/blob/main/.github/documents/README.ko_KR.md">한국어</a></p>

<h1 align="center">이 레포지토리는 Dashify의 plugin 레포지토리 입니다.</h1>

## 문제 해결

### 서버의 Bukkit을 확인하세요.
Dashify 플러그인은 PaperMC에만 호환됩니다.

## API Endpoints
-   참고: 모든 HTTP 요청에는 `Authorization` 헤더에 `Bearer your_key_here` 와 같은 형식의 토큰 인증이 필요합니다.
  >   키 정보는 서버 플러그인 폴더 내 `Dashify/config.yml` 파일에서 확인할 수 있습니다.

| 요청 메서드  | 경로                     | 필요한 경로 파라미터 | Response body           | 설명                                 |
|---------|------------------------|-------------|-------------------------|------------------------------------|
| GET     | `/worlds`              | N/A         | N/A                     | 모든 세계의 uuid와 월드 이름의 리스트를 반환합니다     |
| GET     | `/worlds/<uid>`        | 세계의 uid     | N/A                     | 세계의 정보를 반환합니다                      |
| GET     | `/players`             | N/A         | N/A                     | 모든 플레이어의 uuid와 플레이어 이름의 리스트를 반환합니다 |
| GET     | `/players/<uuid>`      | 플레이어의 uuid  | N/A                     | 플레이어의 정보를 반환합니다                    |
| GET     | `/stats`               | N/A         | N/A                     | 시스템의 정보를 반환합니다                     |
| POST    | `/players/<uuid>/kick` | 플레이어의 uuid  | `{"reason":"<string>"}` | 플레이어를 추방합니다                        |
| POST    | `/players/<uuid>/ban`  | 플레이어의 uuid  | `{"reason":"<string>"}` | 플레이어를 차단합니다                        |

## Code of Conduct

Code of Conduct 파일을 보거나 내용을 확인하려면 [CODE_OF_CONDUCT.md](https://github.com/MC-Dashify/plugin/blob/master/.github/documents/CODE_OF_CONDUCT.ko_KR.md) 파일을 확인하세요.

## Contributing

기여 가이드라인을 확인하려면 [CONTRIBUTING.md](https://github.com/MC-Dashify/plugin/blob/master/.github/documents/CONTRIBUTING.ko_KR.md) 파일을 확인하세요.

## License

라이센스를 확인하려면 [LICENSE](https://github.com/MC-Dashify/plugin/blob/master/LICENSE) 파일을 확인하세요.
