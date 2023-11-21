package com.mauroalexandro.dogsareawesome

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mauroalexandro.dogsareawesome.models.Breed
import com.mauroalexandro.dogsareawesome.models.Height
import com.mauroalexandro.dogsareawesome.models.Weight
import com.mauroalexandro.dogsareawesome.utils.Utils

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    private var utils: Utils = Utils.getInstance()
    @Test
    fun listSavedToSharedPreferencesReturned() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val testKey = "TEST"

        //List to send to SharedPreferences
        val breedList = ArrayList<Breed>()

        val breedHeight = Height("1","1")
        val breedWeight = Weight("1","1")
        val breed = Breed(
            "hunting",
            "hunters",
            breedHeight,
            0,
            "100",
            "Dog",
            "Space",
            "NoNe",
            "Easy Going",
            breedWeight
        )

        breedList.add(breed)
        utils.setList(testKey,breedList, appContext)

        //List got from SharedPreferences
        val outputList = utils.getList(testKey)

        assertTrue(outputList.containsAll(breedList) && breedList.containsAll(outputList))
    }
}