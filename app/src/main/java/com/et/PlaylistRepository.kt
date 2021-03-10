/*
 r the License.
 */

package com.et

import android.content.Context

interface PlaylistRepository {
    @Throws(IllegalStateException::class)
//    fun createPlaylist(name: String, songs: List<Song>): Long
//    fun addToPlaylist(id: Long, songs: List<Song>): Long
//    fun getPlayLists(): List<Playlist>
//    fun getPlaylist(id: Long): Playlist
//    fun existPlaylist(name: String): Boolean
//    fun getPlayListsCount(): Int
//    fun getSongsInPlaylist(playlistId: Long): List<Song>
    fun removeFromPlaylist(playlistId: Long, id: Long)
    fun deletePlaylist(playlistId: Long): Int
}

class PlaylistRepositoryImplementation(context: Context) :/* DBHelper(context),*/
    PlaylistRepository {

    companion object {
        const val TABLE_PLAYLIST = "playlist"
        const val TABLE_SONGS = "playlist_songs"

        const val COLUMN_ID = "id"

        const val COLUMN_NAME = "name"
        const val COLUMN_SONG_COUNT = "song_count"

        const val COLUMN_COVER_ID = "cover_id"

        const val COLUMN_TITLE = "title"
        const val COLUMN_ARTIST = "artist"
        const val COLUMN_ALBUM = "album"
        const val COLUMN_DURATION = "duration"
        const val COLUMN_TRACK = "track_num"
        const val COLUMN_ARTIST_ID = "artist_id"
        const val COLUMN_ALBUM_ID = "album_id"
        const val COLUMN_PLAYLIST = "playlist"
    }

    override fun removeFromPlaylist(playlistId: Long, id: Long) {
//        deleteRow(TABLE_SONGS, "$COLUMN_ID = ?", arrayOf("$id"))
    }

    override fun deletePlaylist(playlistId: Long): Int {
//        return deleteRow(TABLE_PLAYLIST, "$COLUMN_ID = ?", arrayOf("$playlistId"))
        return 0
        }

    private fun getCreateFavoritesQuery(): String {
        return "CREATE TABLE $TABLE_PLAYLIST (" +
                "$COLUMN_ID INTEGER PRIMARY KEY, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_COVER_ID INTEGER, " +
                "$COLUMN_SONG_COUNT INTEGER" +
                ")"
    }

    private fun getCreateSongsQuery(): String {
        return "CREATE TABLE $TABLE_SONGS (" +
                "$COLUMN_ID INTEGER, " +
                "$COLUMN_TITLE TEXT, " +
                "$COLUMN_ARTIST TEXT, " +
                "$COLUMN_ALBUM TEXT, " +
                "$COLUMN_DURATION INTEGER, " +
                "$COLUMN_TRACK INTEGER, " +
                "$COLUMN_ARTIST_ID INTEGER, " +
                "$COLUMN_ALBUM_ID INTEGER, " +
                "$COLUMN_PLAYLIST INTEGER, " +
                "FOREIGN KEY($COLUMN_PLAYLIST) REFERENCES $TABLE_PLAYLIST($COLUMN_ID), " +
                "PRIMARY KEY($COLUMN_ID, $COLUMN_PLAYLIST)" +
                ")"
    }

    private fun getPlusTriggerQuery(): String {
        return "CREATE TRIGGER PLUS_${TABLE_SONGS}_COUNT\n" +
                "AFTER INSERT ON $TABLE_SONGS\n" +
                "BEGIN\n" +
                "    UPDATE $TABLE_PLAYLIST SET $COLUMN_SONG_COUNT = $COLUMN_SONG_COUNT + 1 WHERE $COLUMN_ID = NEW.$COLUMN_PLAYLIST;\n" +
                "END"
    }

    private fun getMinusTriggerQuery(): String {
        return "CREATE TRIGGER MINUS_${TABLE_SONGS}_COUNT\n" +
                "AFTER DELETE ON $TABLE_SONGS\n" +
                "BEGIN\n" +
                "    UPDATE $TABLE_PLAYLIST SET $COLUMN_SONG_COUNT = $COLUMN_SONG_COUNT - 1 WHERE $COLUMN_ID = OLD.$COLUMN_PLAYLIST;\n" +
                "END"
    }

    private fun getDeleteSongs(): String {
        return "CREATE TRIGGER DELETE_$TABLE_SONGS\n" +
                "AFTER DELETE ON $TABLE_PLAYLIST\n" +
                "BEGIN\n" +
                "    DELETE FROM $TABLE_SONGS WHERE $COLUMN_PLAYLIST = OLD.$COLUMN_ID;\n" +
                "END"
    }

    private fun getIndexQuery(): String {
        return "CREATE INDEX idx_$TABLE_SONGS ON $TABLE_SONGS($COLUMN_ID, $COLUMN_PLAYLIST)"
    }

    private fun getPlaylistCoverIdTrigger(): String {
        return "CREATE TRIGGER ${TABLE_PLAYLIST}_COVER_ID\n" +
                "AFTER UPDATE ON ${TABLE_PLAYLIST}\n" +
                "BEGIN\n" +
                "   UPDATE $TABLE_PLAYLIST " +
                "       SET $COLUMN_COVER_ID = (" +
                "           SELECT $COLUMN_ALBUM_ID " +
                "               FROM $TABLE_SONGS " +
                "               WHERE $COLUMN_PLAYLIST = OLD.${COLUMN_ID} LIMIT 1) " +
                "   WHERE $COLUMN_ID = OLD.${COLUMN_ID};\n" +
                "END"
    }
}