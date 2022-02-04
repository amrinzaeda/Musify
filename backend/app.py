from sqlalchemy.ext.declarative.api import DeclarativeMeta
import json
from flask import Flask, request
from db import db
from db import User
from db import Playlist
from db import Song
import os

#########################
# INITIALIZING DATABASE #
#########################

app = Flask(__name__)
db_filename = "cms.db"

app.config["SQLALCHEMY_DATABASE_URI"] = "sqlite:///%s" % db_filename
app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = False
app.config["SQLALCHEMY_ECHO"] = True

db.init_app(app)
with app.app_context():
    db.create_all()

##########
# ROUTES #
##########

API = "/api/"

USER = API + "users/"
SPECIFIC_USER = USER + "<int:uid>/"
ADD_PLAYLIST_TO_USER = SPECIFIC_USER + "add/"

PLAYLIST = SPECIFIC_USER + "playlists/"
SPECIFIC_PLAYLIST = PLAYLIST + "<int:pid>/"
ADD_SONG_TO_PLAYLIST = SPECIFIC_PLAYLIST + "add/"
SPECIFIC_SONG = SPECIFIC_PLAYLIST + "<int:sid>/"

####################
# HELPER FUNCTIONS #
####################

def success_response(data, code=200):
    return json.dumps(data), code

def failure_response(message, code=404):
    return json.dumps({"error": message}), code

def success_delete_response(data, code = 200):
    return json.dumps({"success": True, "data": data}), code

def invalid_query(name, id):
    msg = f"{name} {id} Not Found"
    return failure_response(msg)

def missing_field(name):
    msg = f"Invalid {name} Field(s)"
    return failure_response(msg, 400)

def _getuser(uid):
    return User.query.filter_by(id = uid).first()

def _getlist(user, pid):
    return next((x for x in user.playlists if x.id == pid), None)

def _getsong(sid):
    return Song.query.filter_by(id = sid).first()
    
##################
# ROUTE HANDLING #
##################

@app.route(USER)
def get_users():
    result = {"users": [user.serialize() for user in User.query.all()]}
    return success_response(result)

@app.route(USER, methods = ["POST"])
def create_user():
    body = json.loads(request.data)
    name = body.get("name")
    username = body.get("username")
    if not name or not username:
        return missing_field("User")
    user = User(**body)
    db.session.add(user)
    db.session.commit()
    return success_response(user.serialize(), code = 201)

@app.route(SPECIFIC_USER)
def get_specific_user(uid):
    user = _getuser(uid)
    if user is None:
        return invalid_query("User", uid)
    return success_response(user.serialize())

@app.route(SPECIFIC_USER, methods = ["DELETE"])
def delete_user(uid):
    user = _getuser(uid)
    if user is None:
        return invalid_query("User", uid)
    db.session.delete(user)
    db.session.commit()
    return success_delete_response(user.serialize())

@app.route(PLAYLIST)
def get_playlists(uid):
    user = _getuser(uid)
    if user is None:
        return invalid_query("User", uid)
    result = {"playlists": [playlist.serialize() for playlist in user.playlists]}
    return success_response(result)

@app.route(PLAYLIST, methods = [ "POST" ])
def create_playlist(uid):
    user = _getuser(uid)
    if user is None:
        return invalid_query("User", uid)
    body = json.loads(request.data)
    name = body.get("name")
    if name is None:
        return missing_field("Playlist")
    playlist = Playlist(**body, uid = uid)
    db.session.add(playlist)
    db.session.commit()
    return success_response(playlist.serialize(), code = 201)

@app.route(SPECIFIC_PLAYLIST)
def get_specific_playlist(uid, pid):
    user = _getuser(uid)
    if user is None:
        return invalid_query("User", uid)
    playlist = _getlist(user, pid)
    if playlist is None:
        return invalid_query("Playlist", pid)
    return success_response(playlist.serialize())

@app.route(SPECIFIC_PLAYLIST, methods = [ "DELETE" ])
def delete_playlist(uid, pid):
    user = _getuser(uid)
    if user is None:
        return invalid_query("User", uid)
    playlist = _getlist(user, pid)
    if playlist is None:
        return invalid_query("Playlist", pid)
    db.session.delete(playlist)
    db.session.commit()
    return success_delete_response(playlist.serialize())

@app.route(SPECIFIC_PLAYLIST, methods = [ "POST" ])
def create_song_to_playlist(uid, pid):
    user = _getuser(uid)
    if user is None:
        return invalid_query("User", uid)
    playlist = _getlist(user, pid)
    if playlist is None:
        return invalid_query("Playlist", pid)
    body = json.loads(request.data)
    name = body.get("name")
    artist = body.get("artist")
    youtube = body.get("youtube")
    if name is None or artist is None or youtube is None:
        return missing_field("Song")
    song = Song(**body, pid = pid)
    playlist.songs.append(song)
    db.session.commit()
    return success_response(playlist.serialize(), 201)

@app.route(ADD_SONG_TO_PLAYLIST, methods = [ "POST" ])
def add_song_to_playlist(uid, pid):
    user = _getuser(uid)
    if user is None:
        return invalid_query("User", uid)
    playlist = _getlist(user, pid)
    if playlist is None:
        return invalid_query("Playlist", pid)
    body = json.loads(request.data)
    sid = body.get("id")
    if sid is None:
        return missing_field("Song ID")
    song = _getsong(sid)
    playlist.songs.append(song)
    db.session.commit()
    return success_response(playlist.serialize(), 201)

@app.route(SPECIFIC_SONG)
def get_song_from_playlist(uid, pid, sid):
    user = _getuser(uid)
    if user is None:
        return invalid_query("User", uid)
    playlist = _getlist(user, pid)
    if playlist is None:
        return invalid_query("Playlist", pid)
    song = _getsong(sid)
    if song is None:
        return invalid_query("Song", sid)
    return success_response(song.serialize())

@app.route(SPECIFIC_SONG, methods = [ "DELETE" ])
def delete_song_from_playlist(uid, pid, sid):
    user = _getuser(uid)
    if user is None:
        return invalid_query("User", uid)
    playlist = _getlist(user, pid)
    if playlist is None:
        return invalid_query("Playlist", pid)
    song = _getsong(sid)
    if song is None:
        return invalid_query("Song", sid)
    playlist.songs = [x for x in playlist.songs if x is not song]
    db.session.commit()
    return success_delete_response(song.serialize())

if __name__ == "__main__":
    port = int(os.environ.get("PORT", 5000))
    app.run(host='0.0.0.0', port=port)