package com.bit.bilikdigitalkarawang.features.pemilihan.domain.usecase

import android.content.Context
import androidx.core.content.ContextCompat
import com.bit.bilikdigitalkarawang.features.pemilihan.domain.model.AuditTrailRecord
import com.bit.bilikdigitalkarawang.utils.Encrypt
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class AppendAuditTrailUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) {
    suspend operator fun invoke(
        nik: String,
        noUrutList: List<String>,
        namaKandidatList: List<String>,
        idStatus: Int,
        deviceId: String,
        tpsNo: String,
        bilikNo: String,
        votingMethod: String
    ) {
        withContext(Dispatchers.IO) {
            try {
                val record = AuditTrailRecord(
                    nik = nik,
                    noUrutList = noUrutList,
                    namaKandidatList = namaKandidatList,
                    idStatus = idStatus,
                    deviceId = deviceId,
                    tpsNo = tpsNo,
                    bilikNo = bilikNo,
                    votingMethod = votingMethod
                )

                val jsonString = gson.toJson(record)
                val encryptedRecord = Encrypt.encrypt(jsonString)

                if (encryptedRecord.isEmpty()) return@withContext

                val fileName = "audit_trail_pemilihan.jsonl"
                val externalStorageVolumes: Array<File?> = ContextCompat.getExternalFilesDirs(context, null)

                externalStorageVolumes.forEach { fileDir ->
                    if (fileDir != null) {
                        val file = File(fileDir, fileName)
                        try {
                            FileOutputStream(file, true).use { output ->
                                val dataToWrite = "$encryptedRecord\n".toByteArray(Charsets.UTF_8)
                                output.write(dataToWrite)
                                output.flush()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}