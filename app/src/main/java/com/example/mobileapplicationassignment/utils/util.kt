package com.example.mobileapplicationassignment.utils

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

fun Context.showToast(message:String){
    Toast.makeText(this,message, Toast.LENGTH_LONG).show()
}

fun getFileSize(context: Context, fileName: String): String {
    val file = File(context.filesDir, fileName)
    val fileSizeBytes = file.length()
    val fileSizeKB = fileSizeBytes / 1024
    return if (fileSizeKB > 1024) {
        val fileSizeMB= fileSizeKB / 1024
        "$fileSizeMB MB"
    } else {
        "$fileSizeKB KB"
    }
}

fun copyPdfFileToAppDirectory (context: Context, pdfUri: Uri, destinationFileName: String) {
    val inputStream = context.contentResolver.openInputStream(pdfUri)
    val outputFile = File(context.filesDir, destinationFileName)
    FileOutputStream(outputFile).use {
        inputStream?.copyTo(it)
    }
}

fun renameFile(context: Context, oldFileName: String, newFileName: String) {
    val oldFile = File(context.filesDir, oldFileName)
    val newFile = File(context.filesDir, newFileName)
    oldFile.renameTo (newFile)
}

fun deleteFile(context:Context, fileName: String): Boolean{
    val file = File(context.filesDir, fileName)
    return file.deleteRecursively()
}

fun getFileUri(context: Context, fileName: String): Uri{
    val file = File(context.filesDir, fileName)
    return FileProvider.getUriForFile(context, "${context.packageName}.provider",file)
}