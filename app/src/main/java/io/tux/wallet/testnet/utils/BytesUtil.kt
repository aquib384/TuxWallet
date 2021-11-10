package io.tux.wallet.testnet.utils

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer

class BytesUtil {
    companion object {

        private const val BUFFER_CAPACITY = 1024

        @JvmStatic
        fun readBytesFromStream(inputStream: InputStream): ByteArray {

            // this dynamically extends to take the bytes you read
            val byteBuffer = ByteArrayOutputStream()

            // this is storage overwritten on each iteration with bytes
            val buffer = ByteArray(BUFFER_CAPACITY)

            // we need to know how may bytes were read to write them to the byteBuffer
            var len = 0
            try {
                while ({ len = inputStream.read(buffer); len }() != -1) {
                    byteBuffer.write(buffer, 0, len)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    byteBuffer.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                try {
                    inputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            // and then we can return your byte array.
            return byteBuffer.toByteArray()

        }

        fun intToBytes(x: Int): ByteArray {
            val buffer = ByteBuffer.allocate(4)
            buffer.putInt(x)
            return buffer.array()
        }

        fun bytesToInt(bytes: ByteArray): Int {
            val buffer = ByteBuffer.allocate(4)
            buffer.put(bytes)
            buffer.flip()
            return buffer.int
        }
    }
}