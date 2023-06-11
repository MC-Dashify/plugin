# MC-Dashify plugin
- api-version: 1.19.4
- bukkit: Paper

## API
- `GET` `/worlds` - Returns a list of all worlds' uid
- `GET` `/worlds/{uid}` - Returns world's info


- `GET` `/players` - Returns a list of all players' uuid
- `GET` `/players/{uuid}` - Returns player's info
- `POST` `/players/{uuid}/kick` - Kicks player
    - Body: `{"reason": "string"}` - Raw JSON, Required.
- `POST` `/players/{uuid}/ban` - Bans player
    - Body: `{"reason": "string"}` - Raw JSON, Required.


- `GET` `/jvm` - Returns JVM
- `GET` `/tps` - Returns TPS