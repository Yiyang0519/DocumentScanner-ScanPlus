package com.example.mobileapplicationassignment.frontEndUi.screen.components

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mobileapplicationassignment.R
import com.example.mobileapplicationassignment.utils.getFileUri
import com.example.mobileapplicationassignment.frontEndUi.viewmodels.PdfViewModel
import com.example.mobileapplicationassignment.models.PdfEntity
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PdfLayout(pdfEntity: PdfEntity, pdfViewModel: PdfViewModel) {
    val context = LocalContext.current
    val activity = LocalContext.current as Activity

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp),
        onClick ={
            val getFileUri= getFileUri(context, pdfEntity.name)
            val browserIntent = Intent(Intent.ACTION_VIEW, getFileUri)
            browserIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            activity.startActivity(browserIntent)
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.pdf_icon),
                contentDescription = null,
                modifier = Modifier.size(60.dp) // Adjust size as needed
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = pdfEntity.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Size: ${pdfEntity.size}",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = "Date: ${SimpleDateFormat("dd/MM/yyyy HH:mm:ss a", Locale.getDefault()).format(pdfEntity.lastModifiedTime)}",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            IconButton(onClick = {
                pdfViewModel.currentPdfEntity = pdfEntity
                pdfViewModel.showRenameDialog= true
            }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "more")
            }
        }
    }
}
