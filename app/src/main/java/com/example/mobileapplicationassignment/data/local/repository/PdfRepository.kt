package com.example.mobileapplicationassignment.data.local.repository

import android.app.Application
import com.example.mobileapplicationassignment.data.local.PdfDatabase
import com.example.mobileapplicationassignment.data.local.dao.PdfDao
import com.example.mobileapplicationassignment.models.PdfEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class PdfRepository(application:Application) {
    private val pdfDao = PdfDatabase.getInstance(application).pdfDao
    fun getPdfList(): Flow<List<PdfEntity>> = pdfDao.getAllPdfs().flowOn(Dispatchers.IO)

    suspend fun insertPdf(pdfEntity: PdfEntity): Long {
        return pdfDao.insertPdf(pdfEntity)
    }
    suspend fun deletePdf (pdfEntity: PdfEntity): Int {
        return pdfDao.deletePdf(pdfEntity)
    }
    suspend fun updatePdf (pdfEntity: PdfEntity): Int {
        return pdfDao.updatePdf (pdfEntity)
    }
}