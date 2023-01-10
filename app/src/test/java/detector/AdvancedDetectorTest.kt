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
}