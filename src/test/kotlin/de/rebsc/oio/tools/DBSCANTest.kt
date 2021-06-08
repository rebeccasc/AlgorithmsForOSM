package de.rebsc.oio.tools

/******************************************************************************
 * Copyright (C) 2021  de.rebsc
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see {@literal<http://www.gnu.org/licenses/>}.
 *****************************************************************************/

import de.rebsc.oio.data.Point2D
import de.rebsc.oio.utils.Importer
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class DBSCANTest {

    @Test
    fun performClusteringTest() {
        val s = File.separator
        val filename = "${System.getProperty("user.dir")}${s}src${s}test${s}resources${s}merge_close_nodes_test.osm"
        val dataset = Importer.importOSM(filename)
        val points = ArrayList<Point2D>()
        dataset.nodes.forEach { node -> points.add(Point2D(node.x, node.y)) }

        val clusteredData = DBSCAN().performClustering(points, 0.2, 1)
        var pointSize = 0
        clusteredData.forEach { cluster -> pointSize += cluster.size }

        assertEquals(pointSize, points.size)
        assertEquals(clusteredData.size, 11)
        assertEquals(clusteredData[0].size, 15)
        assertEquals(clusteredData[1].size, 2)
        assertEquals(clusteredData[4].size, 1)
        assertEquals(clusteredData[10].size, 8)
    }

}