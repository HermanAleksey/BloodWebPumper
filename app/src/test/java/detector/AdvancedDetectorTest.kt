package detector

import NodeHelper
import blood_web.*
import generateListOfPerksFromImage
import isEqualsNode
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AdvancedDetectorTest {

    private val detector = AdvancedDetector()
    private var presets = Presets()
    private var helper = NodeHelper()

    @BeforeEach
    fun setUp() {
        presets = Presets()
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun `analyzeBloodWebPageState, img, assert level state`() {
        val testImageName = "img.png"
        val bi = helper.getBufferedImageByFileName(testImageName)

        val result = detector.analyzeBloodWebPageState(bi)

        val expected = BloodWebPageState.LEVEL

        assert(result == expected)
    }

    @Test
    fun `processNodeStateQuality, img, process all nodes`() {
        val testImageName = "img.png"
        val bi = helper.getBufferedImageByFileName(testImageName)

        val notEmptyResults = detector.generateListOfPerksFromImage(bi).let {
            it.filter { node ->
                node.state != InfoNode.State.EMPTY
            }
        }

        assert(
            notEmptyResults[0].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.BloodWebCircle.INNER,
                    position = 1,
                    quality = InfoNode.Quality.YELLOW,
                    state = InfoNode.State.AVAILABLE
                )
            )
        )
        assert(
            notEmptyResults[1].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.BloodWebCircle.INNER,
                    position = 2,
                    quality = InfoNode.Quality.BROWN,
                    state = InfoNode.State.AVAILABLE
                )
            )
        )
        assert(
            notEmptyResults[2].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.BloodWebCircle.INNER,
                    position = 4,
                    quality = InfoNode.Quality.BROWN,
                    state = InfoNode.State.AVAILABLE
                )
            )
        )
        assert(
            notEmptyResults[3].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.BloodWebCircle.MIDDLE,
                    position = 11,
                    quality = InfoNode.Quality.GREEN,
                    state = InfoNode.State.UNAVAILABLE
                )
            )
        )
    }

    @Test
    fun `analyzeBloodWebPageState, img_1, assert level state`() {
        val testImageName = "img_1.png"
        val bi = helper.getBufferedImageByFileName(testImageName)

        val result = detector.analyzeBloodWebPageState(bi)

        val expected = BloodWebPageState.LEVEL

        assert(result == expected)
    }

    @Test
    fun `processNodeStateQuality, img_1, process all nodes`() {
        val testImageName = "img_1.png"
        val bi = helper.getBufferedImageByFileName(testImageName)

        val notEmptyResults = detector.generateListOfPerksFromImage(bi).let {
            it.filter { node ->
                node.state != InfoNode.State.EMPTY
            }
        }

        assert(
            notEmptyResults[0].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.BloodWebCircle.INNER,
                    position = 1,
                    quality = InfoNode.Quality.BROWN,
                    state = InfoNode.State.AVAILABLE
                )
            )
        )
        assert(
            notEmptyResults[1].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.BloodWebCircle.INNER,
                    position = 4,
                    quality = InfoNode.Quality.BROWN,
                    state = InfoNode.State.AVAILABLE
                )
            )
        )
        assert(
            notEmptyResults[2].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.BloodWebCircle.INNER,
                    position = 6,
                    quality = InfoNode.Quality.YELLOW,
                    state = InfoNode.State.AVAILABLE
                )
            )
        )
        assert(
            notEmptyResults[3].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.BloodWebCircle.MIDDLE,
                    position = 12,
                    quality = InfoNode.Quality.BROWN,
                    state = InfoNode.State.UNAVAILABLE
                )
            )
        )
    }

    @Test
    fun `analyzeBloodWebPageState, img_5, assert level state`() {
        val testImageName = "img_5.png"
        val bi = helper.getBufferedImageByFileName(testImageName)

        val result = detector.analyzeBloodWebPageState(bi)

        val expected = BloodWebPageState.LEVEL

        assert(result == expected)
    }

    @Test
    fun `processNodeStateQuality, img_5, process all nodes`() {
        val testImageName = "img_5.png"
        val bi = helper.getBufferedImageByFileName(testImageName)

        val notEmptyResults = detector.generateListOfPerksFromImage(bi).let {
            it.filter { node ->
                node.state != InfoNode.State.EMPTY
            }
        }

        assert(
            notEmptyResults[0].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.BloodWebCircle.INNER,
                    position = 1,
                    quality = InfoNode.Quality.YELLOW,
                    state = InfoNode.State.BOUGHT
                )
            )
        )
        assert(
            notEmptyResults[1].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.BloodWebCircle.INNER,
                    position = 2,
                    quality = InfoNode.Quality.YELLOW,
                    state = InfoNode.State.BOUGHT
                )
            )
        )
        assert(
            notEmptyResults[2].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.BloodWebCircle.INNER,
                    position = 3,
                    quality = InfoNode.Quality.YELLOW,
                    state = InfoNode.State.AVAILABLE
                )
            )
        )
        assert(
            notEmptyResults[3].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.BloodWebCircle.INNER,
                    position = 4,
                    quality = InfoNode.Quality.PURPLE,
                    state = InfoNode.State.LOCKED
                )
            )
        )
        assert(
            notEmptyResults[4].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.BloodWebCircle.INNER,
                    position = 5,
                    quality = InfoNode.Quality.BROWN,
                    state = InfoNode.State.AVAILABLE
                )
            )
        )
        assert(
            notEmptyResults[5].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.BloodWebCircle.INNER,
                    position = 6,
                    quality = InfoNode.Quality.YELLOW,
                    state = InfoNode.State.BOUGHT
                )
            )
        )

        assert(
            notEmptyResults[6].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.BloodWebCircle.MIDDLE,
                    position = 1,
                    quality = InfoNode.Quality.BROWN,
                    state = InfoNode.State.BOUGHT
                )
            )
        )
        assert(
            notEmptyResults[7].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.BloodWebCircle.MIDDLE,
                    position = 2,
                    quality = InfoNode.Quality.BROWN,
                    state = InfoNode.State.AVAILABLE
                )
            )
        )
        assert(
            notEmptyResults[8].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.BloodWebCircle.MIDDLE,
                    position = 4,
                    quality = InfoNode.Quality.GREEN,
                    state = InfoNode.State.BOUGHT
                )
            )
        )
        assert(
            notEmptyResults[9].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.BloodWebCircle.MIDDLE,
                    position = 6,
                    quality = InfoNode.Quality.GREEN,
                    state = InfoNode.State.UNAVAILABLE
                )
            )
        )
        assert(
            notEmptyResults[10].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.BloodWebCircle.MIDDLE,
                    position = 7,
                    quality = InfoNode.Quality.BROWN,
                    state = InfoNode.State.LOCKED
                )
            )
        )
        assert(
            notEmptyResults[11].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.BloodWebCircle.MIDDLE,
                    position = 8,
                    quality = InfoNode.Quality.BROWN,
                    state = InfoNode.State.LOCKED
                )
            )
        )
        assert(
            notEmptyResults[12].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.BloodWebCircle.MIDDLE,
                    position = 9,
                    quality = InfoNode.Quality.GREEN,
                    state = InfoNode.State.AVAILABLE
                )
            )
        )
        assert(
            notEmptyResults[13].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.BloodWebCircle.MIDDLE,
                    position = 11,
                    quality = InfoNode.Quality.PURPLE,
                    state = InfoNode.State.BOUGHT
                )
            )
        )
        assert(
            notEmptyResults[14].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.BloodWebCircle.MIDDLE,
                    position = 12,
                    quality = InfoNode.Quality.BROWN,
                    state = InfoNode.State.BOUGHT
                )
            )
        )

        assert(
            notEmptyResults[15].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.BloodWebCircle.OUTER,
                    position = 1,
                    quality = InfoNode.Quality.YELLOW,
                    state = InfoNode.State.BOUGHT
                )
            )
        )
        assert(
            notEmptyResults[16].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.BloodWebCircle.OUTER,
                    position = 2,
                    quality = InfoNode.Quality.GREEN,
                    state = InfoNode.State.BOUGHT
                )
            )
        )
        assert(
            notEmptyResults[17].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.BloodWebCircle.OUTER,
                    position = 4,
                    quality = InfoNode.Quality.BROWN,
                    state = InfoNode.State.AVAILABLE
                )
            )
        )
        assert(
            notEmptyResults[18].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.BloodWebCircle.OUTER,
                    position = 5,
                    quality = InfoNode.Quality.GREEN,
                    state = InfoNode.State.AVAILABLE
                )
            )
        )
        assert(
            notEmptyResults[19].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.BloodWebCircle.OUTER,
                    position = 6,
                    quality = InfoNode.Quality.YELLOW,
                    state = InfoNode.State.LOCKED
                )
            )
        )
        assert(
            notEmptyResults[20].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.BloodWebCircle.OUTER,
                    position = 8,
                    quality = InfoNode.Quality.PURPLE,
                    state = InfoNode.State.LOCKED
                )
            )
        )
        assert(
            notEmptyResults[21].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.BloodWebCircle.OUTER,
                    position = 11,
                    quality = InfoNode.Quality.BROWN,
                    state = InfoNode.State.AVAILABLE
                )
            )
        )
        assert(
            notEmptyResults[22].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.BloodWebCircle.OUTER,
                    position = 12,
                    quality = InfoNode.Quality.BROWN,
                    state = InfoNode.State.AVAILABLE
                )
            )
        )
    }
}