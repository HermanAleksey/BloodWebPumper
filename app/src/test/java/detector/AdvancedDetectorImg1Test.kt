package detector

import NodeHelper
import generateListOfPerksFromImage
import isEqualsNode
import model.BloodWeb
import model.InfoNode
import model.Presets
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


internal class AdvancedDetectorImg1Test {

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
    fun `analyzeBloodWebPageState, img_1, assert level state`() {
        val testImageName = "img_1.png"
        val bi = helper.getBufferedImageByFileName(testImageName)

        val result = detector.analyzeBloodWebPageState(bi)

        val expected = BloodWeb.PageState.LEVEL

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
                    circle = BloodWeb.Circle.INNER,
                    position = 1,
                    quality = InfoNode.Quality.BROWN,
                    state = InfoNode.State.AVAILABLE
                )
            )
        )
        println(notEmptyResults[1])
        assert(
            notEmptyResults[1].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.Circle.INNER,
                    position = 4,
                    quality = InfoNode.Quality.BROWN,
                    state = InfoNode.State.AVAILABLE
                )
            )
        )
        assert(
            notEmptyResults[2].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.Circle.INNER,
                    position = 6,
                    quality = InfoNode.Quality.YELLOW,
                    state = InfoNode.State.AVAILABLE
                )
            )
        )
        assert(
            notEmptyResults[3].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.Circle.MIDDLE,
                    position = 12,
                    quality = InfoNode.Quality.BROWN,
                    state = InfoNode.State.UNAVAILABLE
                )
            )
        )
    }
}