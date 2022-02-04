from os import name
from re import UNICODE
from typing import NamedTuple
from flask_sqlalchemy import SQLAlchemy

db = SQLAlchemy()

SONG_PLAYLIST_ASSOCIATION = db.Table(
    "song_table",
    db.Model.metadata,
    db.Column("pid", db.Integer, db.ForeignKey("playlist.id")),
    db.Column("sid", db.Integer, db.ForeignKey("song.id"))
)


class User(db.Model):
    __tablename__   = "user"
    id          = db.Column(db.Integer, primary_key = True)
    name        = db.Column(db.String, nullable = False)
    username    = db.Column(db.String, nullable = False)
    playlists   = db.relationship("Playlist", cascade = "delete")

    def __init__(self, **kwargs):
        self.name       = kwargs.get("name")
        self.username   = kwargs.get("username")

    def serialize(self):
        return {
            "id":       self.id,
            "name":     self.name,
            "username": self.username,
            "playlists": [playlist.serialize() for playlist in self.playlists]
        }

    def subserialize(self):
        return {
            "id":   self.id,
            "name": self.name,
            "username": self.username
        }


class Playlist(db.Model):
    __tablename__ = "playlist"
    id          = db.Column(db.Integer, primary_key = True)
    name        = db.Column(db.String, nullable = False)
    image       = db.Column(db.String)
    uid         = db.Column(db.Integer, db.ForeignKey("user.id"), nullable = False)
    songs       = db.relationship("Song", secondary = SONG_PLAYLIST_ASSOCIATION,
                                    back_populates = "playlists")

    def __init__(self, **kwargs):
        self.name  = kwargs.get("name")
        self.image = kwargs.get("image")
        self.uid   = kwargs.get("uid")

    def serialize(self, **kwargs):
        return {
            "id": self.id,
            "name": self.name,
            "image": self.image,
            "songs": [song.subserialize() for song in self.songs]
        }

    def subserialize(self, **kwargs):
        return {
            "id": self.id,
            "name": self.name,
            "image": self.image,
        }


class Song(db.Model):
    __tablename__ = "song"
    id          = db.Column(db.Integer, primary_key = True)
    name        = db.Column(db.String, nullable = False)
    artist      = db.Column(db.String, nullable = False)
    youtube     = db.Column(db.String, nullable = False)
    image       = db.Column(db.String)
    playlists   = db.relationship("Playlist", secondary = SONG_PLAYLIST_ASSOCIATION,
                                    back_populates = "songs")
    
    def __init__(self, **kwargs):
        self.name = kwargs.get("name")
        self.artist = kwargs.get("artist")
        self.youtube = kwargs.get("youtube")
        self.image = kwargs.get("image")

    def serialize(self):
        return {
            "id": self.id,
            "name": self.name,
            "artist": self.artist,
            "youtube": self.youtube,
            "image": self.image,
            "playlists": [playlist.subserialize() for playlist in self.playlists]
        }

    def subserialize(self):
        return {
            "id": self.id,
            "name": self.name,
            "artist": self.artist,
            "youtube": self.youtube,
            "image": self.image,
        }
