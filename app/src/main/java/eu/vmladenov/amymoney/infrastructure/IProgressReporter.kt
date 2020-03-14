package eu.vmladenov.amymoney.infrastructure

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import java.io.Closeable

interface IProgressReporter: Closeable {
    suspend fun progress(message: String)

    val messages: ReceiveChannel<String>
}

class ProgressReporter: IProgressReporter {
    private val channel = Channel<String>()

    override suspend fun progress(message: String) {
        channel.send(message)
    }

    override val messages: ReceiveChannel<String> = channel

    override fun close() {
        channel.close()
    }
}
