package org.aing.danurirest.global.util

import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.client.j2se.MatrixToImageWriter
import org.hibernate.query.sqm.tree.SqmNode.log
import java.io.ByteArrayOutputStream

object GenerateQrCode {
    private const val WIDTH = 200
    private const val HEIGHT = 200

    fun execute(usageId: String): Result<ByteArray> =
        try {
            val encode = MultiFormatWriter().encode(usageId, BarcodeFormat.QR_CODE, WIDTH, HEIGHT)
            val out = ByteArrayOutputStream()
            MatrixToImageWriter.writeToStream(encode, "JPG", out)
            Result.success(out.toByteArray())
        } catch (e: Exception) {
            log.error(e.message, e)
            Result.failure(e)
        }
}
