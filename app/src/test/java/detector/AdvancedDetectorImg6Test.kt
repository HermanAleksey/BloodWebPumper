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


internal class AdvancedDetectorImg6Test {

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
    fun `analyzeBloodWebPageState, img_6, assert level state`() {
        val testImageName = "img_6.png"
        val bi = helper.getBufferedImageByFileName(testImageName)

        val result = detector.analyzeBloodWebPageState(bi)

        val expected = BloodWeb.PageState.LEVEL

        assert(result == expected)
    }

    @Test
    fun `processNodeStateQuality, img_6, process all nodes`() {
        val testImageName = "img_6.png"
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
                    quality = InfoNode.Quality.PURPLE,
                    state = InfoNode.State.LOCKED
                )
            )
        )
        assert(
            notEmptyResults[1].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.Circle.INNER,
                    position = 2,
                    quality = InfoNode.Quality.YELLOW,
                    state = InfoNode.State.LOCKED
                )
            )
        )
        assert(
            notEmptyResults[2].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.Circle.INNER,
                    position = 3,
                    quality = InfoNode.Quality.GREEN,
                    state = InfoNode.State.BOUGHT
                )
            )
        )
        assert(
            notEmptyResults[3].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.Circle.INNER,
                    position = 4,
                    quality = InfoNode.Quality.YELLOW,
                    state = InfoNode.State.BOUGHT
                )
            )
        )
        assert(
            notEmptyResults[4].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.Circle.INNER,
                    position = 5,
                    quality = InfoNode.Quality.BROWN,
                    state = InfoNode.State.BOUGHT
                )
            )
        )
        assert(
            notEmptyResults[5].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.Circle.INNER,
                    position = 6,
                    quality = InfoNode.Quality.PURPLE,
                    state = InfoNode.State.LOCKED
                )
            )
        )

        assert(
            notEmptyResults[6].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.Circle.MIDDLE,
                    position = 1,
                    quality = InfoNode.Quality.BROWN,
                    state = InfoNode.State.LOCKED
                )
            )
        )
        assert(
            notEmptyResults[7].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.Circle.MIDDLE,
                    position = 4,
                    quality = InfoNode.Quality.GREEN,
                    state = InfoNode.State.BOUGHT
                )
            )
        )
        assert(
            notEmptyResults[8].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.Circle.MIDDLE,
                    position = 5,
                    quality = InfoNode.Quality.GREEN,
                    state = InfoNode.State.BOUGHT
                )
            )
        )
        assert(
            notEmptyResults[9].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.Circle.MIDDLE,
                    position = 6,
                    quality = InfoNode.Quality.GREEN,
                    state = InfoNode.State.BOUGHT
                )
            )
        )
        assert(
            notEmptyResults[10].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.Circle.MIDDLE,
                    position = 7,
                    quality = InfoNode.Quality.YELLOW,
                    state = InfoNode.State.BOUGHT
                )
            )
        )
        assert(
            notEmptyResults[11].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.Circle.MIDDLE,
                    position = 8,
                    quality = InfoNode.Quality.GREEN,
                    state = InfoNode.State.LOCKED
                )
            )
        )
        assert(
            notEmptyResults[12].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.Circle.MIDDLE,
                    position = 9,
                    quality = InfoNode.Quality.YELLOW,
                    state = InfoNode.State.LOCKED
                )
            )
        )
        assert(
            notEmptyResults[13].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.Circle.MIDDLE,
                    position = 10,
                    quality = InfoNode.Quality.BROWN,
                    state = InfoNode.State.AVAILABLE
                )
            )
        )
        assert(
            notEmptyResults[14].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.Circle.MIDDLE,
                    position = 12,
                    quality = InfoNode.Quality.BROWN,
                    state = InfoNode.State.LOCKED
                )
            )
        )

        assert(
            notEmptyResults[15].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.Circle.OUTER,
                    position = 2,
                    quality = InfoNode.Quality.PURPLE,
                    state = InfoNode.State.LOCKED
                )
            )
        )
        assert(
            notEmptyResults[16].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.Circle.OUTER,
                    position = 4,
                    quality = InfoNode.Quality.BROWN,
                    state = InfoNode.State.BOUGHT
                )
            )
        )
        assert(
            notEmptyResults[17].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.Circle.OUTER,
                    position = 5,
                    quality = InfoNode.Quality.BROWN,
                    state = InfoNode.State.BOUGHT
                )
            )
        )
        assert(
            notEmptyResults[18].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.Circle.OUTER,
                    position = 6,
                    quality = InfoNode.Quality.BROWN,
                    state = InfoNode.State.BOUGHT
                )
            )
        )
        assert(
            notEmptyResults[19].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.Circle.OUTER,
                    position = 7,
                    quality = InfoNode.Quality.BROWN,
                    state = InfoNode.State.BOUGHT
                )
            )
        )
        assert(
            notEmptyResults[20].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.Circle.OUTER,
                    position = 8,
                    quality = InfoNode.Quality.BROWN,
                    state = InfoNode.State.BOUGHT
                )
            )
        )
        assert(
            notEmptyResults[21].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.Circle.OUTER,
                    position = 9,
                    quality = InfoNode.Quality.BROWN,
                    state = InfoNode.State.LOCKED
                )
            )
        )
        assert(
            notEmptyResults[22].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.Circle.OUTER,
                    position = 10,
                    quality = InfoNode.Quality.PURPLE,
                    state = InfoNode.State.LOCKED
                )
            )
        )
        assert(
            notEmptyResults[23].isEqualsNode(
                helper.createTestNode(
                    circle = BloodWeb.Circle.OUTER,
                    position = 11,
                    quality = InfoNode.Quality.YELLOW,
                    state = InfoNode.State.LOCKED
                )
            )
        )
    }
}