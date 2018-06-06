package android.asiantech.vn.springfinalmusic.model

object Constant {
    const val ACTION_PLAY_MUSIC = "play"
    const val ACTION_PAUSE_MUSIC = "pause"
    const val ACTION_NEXT_MUSIC = "next"
    const val ACTION_BACK_MUSIC = "back"
    const val ACTION_TIMER_START = "start_countdown"
    const val ACTION_RESUME_MUSIC = "continue"
    const val ACTION_DISPLAY_MUSIC = "show_info_music"
    const val ACTION_MODE_CHANGE = "mode_play_change"
    const val ACTION_SEEKBAR_CHANGED = "on_tracking_touch"
    const val ACTION_START_SERVICE = "run_service"
    const val ACTION_SONG_IS_CHANGED = "music_is_next"
    const val ACTION_CLOSE_MUSIC = "close_service"
    const val ACTION_TIMER_TICK = "timer_on_tick"
    const val ACTION_TIMER_STOP = "stop_countdown"
    const val ACTION_TIMER_FINISHED = "timer_on_finished"

    const val KEY_SONG = "song_current"
    const val KEY_POSITION_MEDIA = "media_current_positon"
    const val KEY_LIST_SONG = "list_of_song"
    const val KEY_POSITION_SONG = "position_song"
    const val KEY_TIME = "key_time_minutes"
    const val KEY_SONG_INDEX = "play_song_of_list"
    const val KEY_POSITION_SELECTED = "positon_song_select"
    const val KEY_PROGRESS = "seekbar_progress"
    const val KEY_MODE = "mode_play"
    const val KEY_HEADSET_STATE = "state"
    const val KEY_MEDIA_IS_PAUSE = "media_is_pause"

    const val MODE_NORM = 0
    const val MODE_REPEAT_ALBUM = 1
    const val MODE_REPEAT_SONG = 2
    const val MODE_RANDOM_ALBUM = 3
    const val NAME_MODE_NORM = "Phát theo thứ tự"
    const val NAME_MODE_REPEAT_ALBUM = "Lặp lại danh sách"
    const val NAME_MODE_REPEAT_SONG = "Lặp lại bài hát"
    const val NAME_MODE_RANDOM_ALBUM = "Xáo trộn danh sách"
    const val PHONE_ISDICONNECTED = 0
    const val PHONE_ISCONNECTED = 1
}
