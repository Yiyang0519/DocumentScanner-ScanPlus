package com.example.mobileapplicationassignment.frontEndUi.screen.components

import android.content.ClipData
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.mobileapplicationassignment.R
import com.example.mobileapplicationassignment.utils.deleteFile
import com.example.mobileapplicationassignment.utils.renameFile
import com.example.mobileapplicationassignment.frontEndUi.viewmodels.PdfViewModel
import com.example.mobileapplicationassignment.utils.getFileUri
import com.example.mobileapplicationassignment.utils.showToast
import java.util.Date

@Composable
fun RenameDeleteDialog(pdfViewModel: PdfViewModel) {

    var newNameText by remember(pdfViewModel.currentPdfEntity) {
        mutableStateOf(pdfViewModel.currentPdfEntity?.name ?: "")
    }

    val context = LocalContext.current

    if (pdfViewModel.showRenameDialog) {
        Dialog(onDismissRequest = { pdfViewModel.showRenameDialog = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium, color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        stringResource(R.string.rename_pdf),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newNameText,
                        onValueChange = { newText -> newNameText = newText },
                        label = { Text(stringResource(R.string.pdf_name)) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        IconButton(onClick = {
                            pdfViewModel.currentPdfEntity?.let {
                                pdfViewModel.showRenameDialog = false
                                val getFileUri= getFileUri(context, it.name)
                                val shareIntent = Intent(Intent.ACTION_SEND)
                                shareIntent.type = "application/pdf"
                                shareIntent.clipData = ClipData.newRawUri("", getFileUri)
                                shareIntent.putExtra(Intent.EXTRA_STREAM, getFileUri)
                                shareIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                                context.startActivity(Intent.createChooser(shareIntent, "Share PDF"))
                            }
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_share),
                                contentDescription = null,
                                tint = Color.Red
                            )
                        }
                        IconButton(onClick = {
                            pdfViewModel.currentPdfEntity?.let {
                                pdfViewModel.showRenameDialog = false
                                if (deleteFile(context, it.name)){
                                    pdfViewModel.deletePdf(it)
                                }else{
                                    context.showToast("Something went wrong")
                                }
                            }
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_delete),
                                contentDescription = null,
                                tint = Color.Red
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = {
                            pdfViewModel.showRenameDialog = false
                        }) {
                            Text(stringResource(R.string.cancel))
                        }
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = {
                            pdfViewModel.currentPdfEntity?.let { pdf->
                                if (!pdf.name.equals(newNameText, true)) {
                                pdfViewModel.showRenameDialog = false
                                renameFile(
                                    context,
                                    pdf.name,
                                    newNameText
                                )
                                val updatePdf = pdf.copy(
                                    name = newNameText, lastModifiedTime = Date()
                                )
                                pdfViewModel.updatePdf(updatePdf)
                            }else{
                                pdfViewModel.showRenameDialog = false
                            }
                            }
                        }) {
                            Text(stringResource(R.string.update))
                        }
                    }
                }
            }
        }
    }
}
