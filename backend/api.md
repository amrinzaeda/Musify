# Musify API Specification

The backend API specification for the Musify app. The backend is deployed at
https://musifyappdev.herokuapp.com.

---

## Get All Users

**GET** /api/users/

```yaml
Response: <HTTP STATUS CODE 200>
{
  "users": [
    {
      "id": <USER ID>,
      "name": <USER NAME>,
      "username": <USERNAME>,
      "playlists": <SERIALIZED LIST OF PLAYLISTS>
    }
  ]
}
```

## Create User

**POST** /api/users/

```yaml
Request:
{
  "name": <USER'S NAME>,
  "username": <USERNAME>
}
```

```yaml
Response: <HTTP STATUS CODE 201>
{
  "id": <USER ID>,
  "name": <USER'S NAME>,
  "username": <USERNAME>,
  "playlists": []
}
```

## Get Specific User

**GET** /api/users/{id}/

```yaml
Response: <HTTP STATUS CODE 200>
{
  "id": <USER ID>,
  "name": <USER'S NAME>,
  "username": <USERNAME>,
  "playlists": [
    <SERIALIZED PLAYLIST>,
    <SERIALIZED PLAYLIST>,
    ...
  ]
}
```

## Delete User

**DELETE** /api/users/{id}/

```yaml
Response: <HTTP STATUS CODE 200>
{
  "id": <USER ID>,
  "name": <USER'S NAME>,
  "username": <USERNAME>,
  "playlists": [
    <SERIALIZED PLAYLIST>,
    <SERIALIZED PLAYLIST>,
    ...
  ]
}
```

---

## Get All Playlists

**GET** /api/users/{id}/playlists

```yaml
Response: <HTTP STATUS CODE 200>
{
  "playlists": [
    <SERIALIZED PLAYLIST>,
    <SERIALIZED PLAYLIST>,
    ...
  ]
}
```

## Create Playlist

**POST** /api/users/{id}/playlists

```yaml
Request:
{
    "name": <PLAYLIST NAME>,
    "image": <PLAYLIST IMAGE (URL)>
}
```

```yaml
Response: <HTTP STATUS CODE 201>
{
    "id": <PLAYLIST ID>,
    "name": <PLAYLIST NAME>,
    "image": <PLAYLIST IMAGE (URL)>,
    "songs": []
}
```

## Get Specific Playlist

**GET** /api/users/{id}/playlists/{pid}/

```yaml
Response: <HTTP STATUS CODE 200>
{
    "id": <PLAYLIST ID>,
    "name": <PLAYLIST NAME>,
    "image": <PLAYLIST IMAGE (URL)>,
    "songs": [
      <SERIALIZED SONG>,
      <SERIALIZED SONG>,
      ...
    ]
}
```

## Delete Playlist

**DELETE** /api/users/{id}/playlists/{pid}/

```yaml
Response: <HTTP STATUS CODE 200>
{
  "success": true,
  "data": {
    "id": <PLAYLIST ID>,
    "name": <PLAYLIST NAME>,
    "image": <PLAYLIST IMAGE (URL)>,
    "songs": [
      <SERIALIZED SONG>,
      <SERIALIZED SONG>,
      ...
    ]
  }
}
```

---

## Create Song to Playlist

**POST** /api/users/{id}/playlists/{pid}/

```yaml
Request:
{
    "name": <SONG NAME>,
    "artist": <ARTIST NAME>,
    "youtube": <YOUTUBE LINK>,
    "image": <SONG IMAGE (URL)>
}
```

```yaml
Response: <HTTP STATUS CODE 201>
{
    "id": <PLAYLIST ID>,
    "name": <PLAYLIST NAME>,
    "image": <PLAYLIST IMAGE (URL)>,
    "songs": [
      <SERIALIZED SONG>,
      <SERIALIZED SONG>,
      ...
      {
        "id": <SONG ID>,
        "name": <SONG NAME>,
        "artist": <SONG ARTIST>,
        "youtube": <YOUTUBE LINK>,
        "image": <SONG IMAGE (URL)>
      }
    ]
}
```

## Add (pre-existing) Song to Playlist

**POST** /api/users/{id}/playlists/{pid}/

```yaml
Request:
{
    "id": <SONG ID>
}
```

```yaml
Response: <HTTP STATUS CODE 201>
{
    "id": <PLAYLIST ID>,
    "name": <PLAYLIST NAME>,
    "image": <PLAYLIST IMAGE (URL)>,
    "songs": [
      <SERIALIZED SONG>,
      <SERIALIZED SONG>,
      ...
      {
        "id": <SONG ID>,
        "name": <SONG NAME>,
        "artist": <SONG ARTIST>,
        "youtube": <YOUTUBE LINK>,
        "image": <SONG IMAGE (URL)>
      }
    ]
}
```

## Get Song in Playlist

**GET** /api/users/{id}/playlists/{pid}/{sid}

```yaml
Response: <HTTP STATUS CODE 200>
{
  "id": <SONG ID>,
  "name": <SONG NAME>,
  "artist": <SONG ARTIST>,
  "youtube": <YOUTUBE LINK>,
  "image": <SONG IMAGE (URL)>,
  "playlists": [
    <SERIALIZED PLAYLIST>,
    <SERIALIZED PLAYLIST>,
    ...
  ]
}
```

## Delete Song from Playlist

**DELETE** /api/users/{id}/playlists/{pid}/{sid}

```yaml
Response: <HTTP STATUS CODE 200>
{
  "success": true,
  "data": {
    "id": <SONG ID>,
    "name": <SONG NAME>,
    "artist": <SONG ARTIST>,
    "youtube": <YOUTUBE LINK>,
    "image": <SONG IMAGE (URL)>,
    "playlists": [
      <SERIALIZED PLAYLIST (THAT STILL HAS SONG)>,
      <SERIALIZED PLAYLIST (THAT STILL HAS SONG)>,
      ...
    ]
  }
}
```
