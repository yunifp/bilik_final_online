package com.bit.bilikdigitalkarawang.features.pemilihan.domain.usecase

import com.bit.bilikdigitalkarawang.common.Resource
import com.bit.bilikdigitalkarawang.features.pemilihan.domain.model.Pemilih
import com.bit.bilikdigitalkarawang.features.pemilihan.domain.repository.PemilihanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CekNikValidUseCase @Inject constructor(
    private val repository: PemilihanRepository
) {
    // -> UBAH RETURN TYPE menjadi Resource<Pemilih> agar kita dapat melempar NIK asli kembali ke ViewModel
    operator fun invoke(identifier: String): Flow<Resource<Pemilih>> = flow {
        try {
            emit(Resource.Loading())

            // 1. Cari berdasarkan NIK ATAU UUID
            val pemilih = repository.getPemilihByNikOrUuid(identifier)

            if(pemilih == null) {
                emit(Resource.Error("Data tidak terdaftar di data pemilih"))
                return@flow
            }

            // 2. Gunakan NIK asli dari database untuk mengecek apakah sudah memilih
            if (repository.hasAlreadyVoted(pemilih.nik)) {
                emit(Resource.Error("Anda sudah melakukan pemilihan"))
                return@flow
            }

            // 3. Return object pemilih
            emit(Resource.Success(pemilih))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Terjadi kesalahan"))
        }
    }
}