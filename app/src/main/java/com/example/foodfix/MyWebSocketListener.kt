import android.content.Context
import okhttp3.*
import okio.ByteString

class MyWebSocketListener(private val context: Context) : WebSocketListener() {

    private val sharedPref = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        val user_id = sharedPref.getString("user_id", "").toString() // 예시로 초기 메시지 가져오기
        webSocket.send(user_id) // SharedPreferences에서 가져온 값을 전송
        println("웹소켓 연결 성공, user: $user_id")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        println("서버로부터 메시지 수신: $text")
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket, bytes)
        println("서버로부터 바이트 데이터 수신: ${bytes.hex()}")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        println("웹소켓 연결 종료: $code / $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        println("웹소켓 연결 실패: ${t.message}")
    }
}
