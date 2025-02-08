import io.shubham0204.text2summary.Text2Summary
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class Text2SummaryTests {
    private val text =
        "Flowers are nature's vibrant masterpieces, adorning the world with their diverse colors, shapes, and fragrances. " +
            "They play a crucial role in ecosystems, attracting pollinators like bees and butterflies, which help plants reproduce. " +
            "Beyond their ecological significance, flowers hold deep cultural and emotional meanings, often symbolizing love, beauty, and " +
            "renewal across various societies. From the delicate petals of a rose to the bold, sunlit blooms of a sunflower, each " +
            "flower carries its unique charm and message. Whether used in gardens, bouquets, or ceremonies, flowers have an " +
            "undeniable ability to uplift spirits and connect people through shared appreciation for their natural elegance."

    private val expectedSummary06 =
        "Flowers are nature's vibrant masterpieces, adorning the world with their diverse colors, shapes, and fragrances. Beyond their ecological significance, flowers hold deep cultural and emotional meanings, often symbolizing love, beauty, and renewal across various societies."

    private val expectedSummary02 =
        "Flowers are nature's vibrant masterpieces, adorning the world with their diverse colors, shapes, and fragrances."

    @Test
    fun summarize_works() =
        runBlocking {
            val predictedSummary06 = Text2Summary.summarize(text, compressionRate = 0.6f)
            assertEquals(expectedSummary06, predictedSummary06)
            val predictedSummary02 = Text2Summary.summarize(text, compressionRate = 0.2f)
            assertEquals(expectedSummary02, predictedSummary02)
        }

    @Test
    fun invalidCompressRatio_throws() =
        runBlocking {
            val exception =
                assertThrows<Exception> {
                    Text2Summary.summarize(text, compressionRate = 1.0f)
                }
            assertEquals("The compression rate must lie in the interval ( 0 , 1 ).", exception.message)
        }
}
