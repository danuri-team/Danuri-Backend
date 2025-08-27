package org.aing.danurirest.global.third_party.s3

enum class BucketType(
    val bucketName: String? = "danuri-cloud",
    val folder: String,
) {
    QR_LINK(folder = "qr-images"),
    FORM_IMAGES(folder = "form-images"),
}
