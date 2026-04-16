package com.bit.bilikdigitalkarawang.features.pemilihan.domain.model

data class AuditTrailRecord(
    val timestamp: Long = System.currentTimeMillis(),
    val nik: String,
    val noUrutList: List<String>,
    val namaKandidatList: List<String>,
    val idStatus: Int,
    val deviceId: String,
    val tpsNo: String,
    val bilikNo: String,
    val votingMethod: String
)