package com.plweegie.heritage

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.plweegie.heritage.ui.components.PermissionRequest
import dagger.hilt.android.AndroidEntryPoint
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import kotlin.math.sin

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            PermissionRequest(applicationContext)

            ComposeHeritageAppEntryPoint()
        }

        try {
            val interpreter = getInterpreter(this, "voice_model.tflite")
            interpreter.allocateTensors()

            val inputBuffer = processAudio(interpreter)

            val outputTensor = interpreter.getOutputTensor(0)
            val outputShape = outputTensor.shape()
            val outputType = outputTensor.dataType()

            val outputBuffer = TensorBuffer.createFixedSize(outputShape, outputType)

            interpreter.run(inputBuffer.buffer, outputBuffer.buffer)

            val embeddings = outputBuffer.floatArray
            Log.d("TFLite", "Generated ${embeddings.size} embeddings")
        } catch (e: Exception) {
            Log.e("TFLite", "Error running model: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun processAudio(interpreter: Interpreter): TensorBuffer {
        val samples = generateSineWave()

        val inputTensor = interpreter.getInputTensor(0)
        val inputType = inputTensor.dataType()

        interpreter.resizeInput(0, intArrayOf(1, samples.size))
        interpreter.allocateTensors()

        val inputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, samples.size), inputType)

        inputBuffer.loadArray(samples)

        return inputBuffer
    }

    private fun generateSineWave(): FloatArray {
        val sampleRate = 16000
        val frequency = 440
        val duration = 1.0
        val numSamples = (duration * sampleRate).toInt()

        val samples = FloatArray(numSamples) { 0f }

        for (i in 0 until numSamples) {
            val time = i.toDouble() / sampleRate
            samples[i] = sin(2.0 * Math.PI * frequency * time).toFloat()
        }

        return samples
    }

    @Throws(IOException::class)
    private fun loadModelFile(context: Context, modelFile: String): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(modelFile)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        val retFile = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        fileDescriptor.close()
        return retFile
    }

    @Throws(IOException::class)
    private fun getInterpreter(
        context: Context,
        modelName: String
    ): Interpreter {
        val options = Interpreter.Options()
        options.setNumThreads(4)

        return Interpreter(loadModelFile(context, modelName), options)
    }
}