package com.example.bptracker.data

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfExporter {

    fun exportToPdf(
        context: Context, 
        title: String, 
        userName: String,
        vitals: List<VitalRecord>, 
        waterLogs: List<WaterLog> = emptyList(),
        medications: List<Medication> = emptyList(),
        medLogs: List<MedicationLog> = emptyList()
    ): File? {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 Size
        var page = pdfDocument.startPage(pageInfo)
        var canvas: Canvas = page.canvas
        val paint = Paint()

        var yPosition = 80f

        // Draw Header
        paint.color = Color.BLACK
        paint.textSize = 24f
        paint.isFakeBoldText = true
        canvas.drawText("BP Tracker: $title", 150f, yPosition, paint)
        yPosition += 30f
        paint.textSize = 18f
        canvas.drawText("Patient: $userName", 150f, yPosition, paint)
        yPosition += 50f

        val dateFormat = SimpleDateFormat("MM/dd/yy HH:mm", Locale.getDefault())

        if (vitals.isNotEmpty()) {
            // Draw Table Headers
            paint.textSize = 12f
            paint.isFakeBoldText = true
            canvas.drawText("Date", 30f, yPosition, paint)
            canvas.drawText("BP (SYS/DIA)", 140f, yPosition, paint)
            canvas.drawText("HR", 240f, yPosition, paint)
            canvas.drawText("Sugar (F/PP)", 280f, yPosition, paint)
            canvas.drawText("Tags/Meds", 380f, yPosition, paint)
            yPosition += 30f

            paint.isFakeBoldText = false
            for (record in vitals) {
                if (yPosition > 800f) {
                    pdfDocument.finishPage(page)
                    page = pdfDocument.startPage(pageInfo)
                    canvas = page.canvas
                    yPosition = 50f
                }
                val dateStr = dateFormat.format(Date(record.timestamp))
                val bpStr = if (record.systolic > 0 && record.diastolic > 0) "${record.systolic}/${record.diastolic}" else "--/--"
                val hrStr = if (record.heartRate > 0) "${record.heartRate}" else "--"
                val fStr = if (record.bloodSugar > 0) "${record.bloodSugar}" else "--"
                val ppStr = if (record.bloodSugarPP > 0) "${record.bloodSugarPP}" else "--"
                val sugarStr = "$fStr/$ppStr"
                
                var tags = record.tags.ifEmpty { record.medications }
                if (tags.length > 25) {
                    tags = tags.substring(0, 22) + "..."
                }

                canvas.drawText(dateStr, 30f, yPosition, paint)
                canvas.drawText(bpStr, 140f, yPosition, paint)
                canvas.drawText(hrStr, 240f, yPosition, paint)
                canvas.drawText(sugarStr, 280f, yPosition, paint)
                canvas.drawText(tags, 380f, yPosition, paint)
                
                yPosition += 30f
            }
            yPosition += 40f
        }

        if (waterLogs.isNotEmpty()) {
            if (yPosition > 700f) {
                pdfDocument.finishPage(page)
                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas
                yPosition = 50f
            }
            
            paint.textSize = 16f
            paint.isFakeBoldText = true
            canvas.drawText("Hydration Logs", 30f, yPosition, paint)
            yPosition += 30f

            paint.textSize = 12f
            canvas.drawText("Date", 30f, yPosition, paint)
            canvas.drawText("Amount (ml)", 180f, yPosition, paint)
            yPosition += 30f

            paint.isFakeBoldText = false
            for (log in waterLogs) {
                if (yPosition > 800f) {
                    pdfDocument.finishPage(page)
                    page = pdfDocument.startPage(pageInfo)
                    canvas = page.canvas
                    yPosition = 50f
                }
                canvas.drawText(dateFormat.format(Date(log.timestamp)), 30f, yPosition, paint)
                canvas.drawText("${log.amountMl} ml", 180f, yPosition, paint)
                yPosition += 30f
            }
            yPosition += 40f
        }

        if (medLogs.isNotEmpty()) {
            if (yPosition > 700f) {
                pdfDocument.finishPage(page)
                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas
                yPosition = 50f
            }
            
            paint.textSize = 16f
            paint.isFakeBoldText = true
            canvas.drawText("Medication Logs", 30f, yPosition, paint)
            yPosition += 30f

            paint.textSize = 12f
            canvas.drawText("Date", 30f, yPosition, paint)
            canvas.drawText("Medication", 180f, yPosition, paint)
            canvas.drawText("Dosage", 380f, yPosition, paint)
            yPosition += 30f

            paint.isFakeBoldText = false
            for (log in medLogs) {
                if (yPosition > 800f) {
                    pdfDocument.finishPage(page)
                    page = pdfDocument.startPage(pageInfo)
                    canvas = page.canvas
                    yPosition = 50f
                }
                val med = medications.find { it.id == log.medicationId }
                val medName = med?.name ?: "Unknown"
                val medDosage = med?.dosage ?: ""
                
                canvas.drawText(dateFormat.format(Date(log.timestamp)), 30f, yPosition, paint)
                canvas.drawText(medName, 180f, yPosition, paint)
                canvas.drawText(medDosage, 380f, yPosition, paint)
                yPosition += 30f
            }
        }

        pdfDocument.finishPage(page)

        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val fileName = "BPTracker_${title.replace(" ", "_")}_${System.currentTimeMillis()}.pdf"
        val file = File(downloadsDir, fileName)

        return try {
            pdfDocument.writeTo(FileOutputStream(file))
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            pdfDocument.close()
        }
    }
}
