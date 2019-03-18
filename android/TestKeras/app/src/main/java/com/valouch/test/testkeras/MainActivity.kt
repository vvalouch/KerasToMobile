package com.valouch.test.testkeras

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        thread(start = true) {
            runInference()
        }
    }

    fun runInference() {
        val tfliteModel = loadModelFile(this)
        val tflite = Interpreter(tfliteModel, Interpreter.Options())

        var inputData: ByteBuffer = ByteBuffer.allocateDirect(
            1
                    * 6 //6 attributes/columns
                    * 1 //1 row
                    * 4 //4 bytes per number as the number is float
        )
        inputData.order(ByteOrder.nativeOrder())

        floatArrayOf(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f).forEach {
            inputData.putFloat(it)
        }

        val labelProbArray: Array<FloatArray> = Array(1) { FloatArray(1) }

        tflite.run(inputData, labelProbArray)

        var prediction = (labelProbArray[0][0])
        runOnUiThread { result.text = "Prediction $prediction" }
    }


    private fun loadModelFile(activity: Activity): MappedByteBuffer {
        val fileDescriptor = activity.assets.openFd("trained_model.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }
}
