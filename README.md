# MC-Dashify plugin
- api-version: 1.19.4
- bukkit: Paper
- [![Build & Publish to Release](https://github.com/MC-Dashify/plugin/actions/workflows/main.yml/badge.svg)](https://github.com/MC-Dashify/plugin/actions/workflows/main.yml)


## API
| Method | URL                    | Required      | Response body           | Description                         |
|--------|------------------------|---------------|-------------------------|-------------------------------------|
| GET    | `/worlds`              | N/A           | N/A                     | Returns a list of all worlds' uid   |
| GET    | `/worlds/<uid>`        | World's uid   | N/A                     | Returns world's info                |
| GET    | `/players`             | N/A           | N/A                     | Returns a list of all players' uuid |
| GET    | `/players/<uuid>`      | Player's uuid | N/A                     | Returns player's info               |
| GET    | `/stats`               | N/A           | N/A                     | Returns system's info               |
| POST   | `/players/<uuid>/kick` | Player's uuid | `{"reason":"<string>"}` | Kicks player                        |
| POST   | `/players/<uuid>/ban`  | Player's uuid | `{"reason":"<string>"}` | Bans player                         |