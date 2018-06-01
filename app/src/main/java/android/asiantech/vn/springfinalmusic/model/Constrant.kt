package android.asiantech.vn.springfinalmusic.model

object Constrant {
    const val ACTION_PLAY_MUSIC = "play"
    const val ACTION_PAUSE_MUSIC = "pause"
    const val ACTION_NEXT_MUSIC = "next"
    const val ACTION_BACK_MUSIC = "back"
    const val ACTION_TIMER = "timer_auto_off_app"
    const val ACTION_RESUME_MUSIC = "continue"
    const val ACTION_DISPLAY_MUSIC = "show_info_music"
    const val ACTION_MODE_CHANGE = "mode_play_change"
    const val ACTION_SEEKBAR_CHANGED = "on_tracking_touch"
    const val ACTION_START_SERVICE = "run_service"

    const val KEY_SONG = "song_current"
    const val KEY_POSITION_MEDIA = "media_current_positon"
    const val KEY_LIST_SONG = "list_of_song"
    const val KEY_POSITION_SONG = "position_song"
    const val KEY_TIME = "key_time_minutes"
    const val KEY_PROGRESS = "seekbar_progress"
    const val KEY_MODE = "mode_play"

    const val MODE_NORM = 0
    const val MODE_REPEAT_ALBUM = 1
    const val MODE_REPEAT_SONG = 2
    const val MODE_RANDOM_ALBUM = 3
    const val NAME_MODE_NORM = "Phát theo thứ tự"
    const val NAME_MODE_REPEAT_ALBUM = "Lặp lại danh sách"
    const val NAME_MODE_REPEAT_SONG = "Lặp lại bài hát"
    const val NAME_MODE_RANDOM_ALBUM = "Xáo trộn danh sách"
}
