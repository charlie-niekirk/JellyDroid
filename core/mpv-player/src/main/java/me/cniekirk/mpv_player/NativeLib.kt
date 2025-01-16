package me.cniekirk.mpv_player

class NativeLib {

    /**
     * A native method that is implemented by the 'mpv_player' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'mpv_player' library on application startup.
        init {
            System.loadLibrary("mpv_player")
        }
    }
}