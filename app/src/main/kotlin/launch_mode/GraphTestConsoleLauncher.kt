package launch_mode

import blood_web.BloodWeb
import blood_web.Node
import blood_web.createFullGraph
import com.mxgraph.layout.mxCircleLayout
import com.mxgraph.layout.mxIGraphLayout
import com.mxgraph.util.mxCellRenderer
import org.jgrapht.*
import org.jgrapht.ext.JGraphXAdapter
import org.jgrapht.graph.*
import org.jgrapht.nio.*
import org.jgrapht.nio.dot.*
import org.jgrapht.traverse.*
import java.awt.Color
import java.awt.Point
import java.awt.image.BufferedImage
import java.io.*
import java.net.*
import java.net.URISyntaxException
import java.util.*
import javax.imageio.ImageIO


class GraphTestConsoleLauncher : AppLauncher {


    private fun GraphTestConsoleLauncher() {} // ensure non-instantiability.


    /**
     * The starting point for the demo.
     *
     * @param args ignored.
     *
     * @throws URISyntaxException if invalid URI is constructed.
     * @throws ExportException if graph cannot be exported.
     */

    override fun run() {
        val imgFile = File("resources/graph.png")
        imgFile.createNewFile()

        val nodeGraph = createFullGraph()

        nodeGraph.edgeSet().forEach{ println(it) }

        println()
        println("-- visualisation")
        val graphAdapter = JGraphXAdapter<Node, DefaultEdge>(nodeGraph)

        val layout: mxIGraphLayout = mxCircleLayout(graphAdapter)

        layout.execute(graphAdapter.defaultParent)
        val image: BufferedImage = mxCellRenderer.createBufferedImage(graphAdapter, null, 2.0, Color.WHITE, true, null)
        ImageIO.write(image, "PNG", imgFile)

//        assertTrue(imgFile.exists())
    }



    /**
     * Create a toy graph based on String objects.
     *
     * @return a graph based on String objects.
     */
    private fun createStringGraph(): Graph<Node, DefaultEdge> {
        val g: Graph<Node, DefaultEdge> = SimpleGraph<Node, DefaultEdge>(DefaultEdge::class.java)
        val v1 = Node(Node.OrderedNumber(BloodWeb.BloodWebCircle.INNER,1), Point(0, 0))
        val v2 = Node(Node.OrderedNumber(BloodWeb.BloodWebCircle.INNER,2), Point(0, 0))
        val v3 = Node(Node.OrderedNumber(BloodWeb.BloodWebCircle.SECONDARY,1), Point(0, 0))
        val v4 = Node(Node.OrderedNumber(BloodWeb.BloodWebCircle.OUTER,1), Point(0, 0))

        // add the vertices
        g.addVertex(v1)
        g.addVertex(v2)
        g.addVertex(v3)
        g.addVertex(v4)

        // add edges to create a circuit
        g.addEdge(v1, v3)
        g.addEdge(v2, v3)
        g.addEdge(v3, v4)
        return g
    }
}