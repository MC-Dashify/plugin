<p align="center">
  <img width="128" align="center" src="https://github.com/MC-Dashify/plugin/blob/master/.github/assets/logo-512.png">
</p>
<h1 align="center">Dashify</h1>
<h3 align="center">Easily and quickly monitor Minecraft servers</h3>
<p align="center">Standalone monitoring system without any additional installation</p>
<p align="center">
  <a href="https://github.com/MC-Dashify/plugin/actions/workflows/codeql.yml">
    <img src="https://github.com/MC-Dashify/plugin/actions/workflows/codeql.yml/badge.svg" alt="CodeQL" />
  </a>  
  <a href="https://github.com/MC-Dashify/plugin/actions/workflows/main.yml">
    <img src="https://github.com/MC-Dashify/plugin/actions/workflows/main.yml/badge.svg" alt="Build & Publish to Release" />
  </a>
  <a href="https://app.codacy.com/gh/MC-Dashify/plugin/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade"><img src="https://app.codacy.com/project/badge/Grade/f0e17e2ea7184420b0e8998e0cafd27d" alt="code quality"/></a>
</p>

<p align="center"><a href="https://github.com/MC-Dashify/plugin/blob/master/README.md">English</a> · <a href="https://github.com/MC-Dashify/plugin/blob/master/.github/documents/README.ko_KR.md">한국어</a></p>

<h1 align="center">THIS IS A PLUGIN REPOSITORY FOR DASHIFY.</h1>

## Troubleshooting

### Firstly, check server software
Dashify plugin is only compatible from PaperMC and it's forks. The recommended server software is PaperMC.

## API Endpoints

-   NOTE: All HTTP requests requires `Authorization` header that looks like: `Bearer your_key_here`.
  >   Key information can be found in the file `Dashify/config.yml` in the plugins folder.

| Request Method | Path                   | Requirement   | Response body           | Description                                           |
|----------------|------------------------|---------------|-------------------------|-------------------------------------------------------|
| GET            | `/worlds`              | N/A           | N/A                     | Returns a list of all world's UUID and world's name   |
| GET            | `/worlds/<UUID>`       | World's UUID  | N/A                     | Returns world's info                                  |
| GET            | `/players`             | N/A           | N/A                     | Returns a list of all player's UUID and player's name |
| GET            | `/players/<UUID>`      | Player's UUID | N/A                     | Returns player's info                                 |
| GET            | `/stats`               | N/A           | N/A                     | Returns system's info                                 |
| POST           | `/players/<UUID>/kick` | Player's UUID | `{"reason":"<string>"}` | Kicks player                                          |
| POST           | `/players/<UUID>/ban`  | Player's UUID | `{"reason":"<string>"}` | Bans player                                           |

## Code of Conduct

See the [CODE_OF_CONDUCT.md](https://github.com/MC-Dashify/plugin/blob/master/CODE_OF_CONDUCT.md) file for Code of Conduct information.

## Contributing

See the [CONTRIBUTING.md](https://github.com/MC-Dashify/plugin/blob/master/CONTRIBUTING.md) file for contributing information.

## License

See the [LICENSE](https://github.com/MC-Dashify/plugin/blob/master/LICENSE) file for licensing information.
