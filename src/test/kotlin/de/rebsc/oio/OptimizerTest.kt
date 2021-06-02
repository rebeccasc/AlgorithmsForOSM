package de.rebsc.oio

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

import de.rebsc.oio.data.OSMDataSet
import de.rebsc.oio.data.OSMNode
import de.rebsc.oio.data.OSMWay
import de.rebsc.oio.utils.Exporter
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class OptimizerTest {

    // Setup test data
    private val nodes = ArrayList<OSMNode>().apply {
        add(OSMNode(-101784, 12.91258056743, 50.82462614568, ArrayList()))
        add(OSMNode(-101785, 12.91252180111, 50.81963290817, ArrayList()))
        add(OSMNode(-101786, 12.92878241257, 50.81955652993, ArrayList()))
        add(OSMNode(-101787, 12.92884117889, 50.82454977561, ArrayList()))
        add(OSMNode(-101789, 12.91681234849, 50.82954070074, ArrayList()))
        add(OSMNode(-101790, 12.91727033143, 50.82230825012, ArrayList()))
        add(OSMNode(-101791, 12.92490995762, 50.82250129621, ArrayList()))
        add(OSMNode(-101792, 12.92445197468, 50.82973371692, ArrayList()))
    }

    private val nodesWay1 = ArrayList<OSMNode>().apply {
        add(OSMNode(-101784, 12.91258056743, 50.82462614568, ArrayList()))
        add(OSMNode(-101785, 12.91252180111, 50.81963290817, ArrayList()))
        add(OSMNode(-101786, 12.92878241257, 50.81955652993, ArrayList()))
        add(OSMNode(-101787, 12.92884117889, 50.82454977561, ArrayList()))
        add(OSMNode(-101784, 12.91258056743, 50.82462614568, ArrayList()))
    }

    private val nodesWay2 = ArrayList<OSMNode>().apply {
        add(OSMNode(-101789, 12.91681234849, 50.82954070074, ArrayList()))
        add(OSMNode(-101790, 12.91727033143, 50.82230825012, ArrayList()))
        add(OSMNode(-101791, 12.92490995762, 50.82250129621, ArrayList()))
        add(OSMNode(-101792, 12.92445197468, 50.82973371692, ArrayList()))
        add(OSMNode(-101789, 12.91681234849, 50.82954070074, ArrayList()))
    }

    private val ways = ArrayList<OSMWay>().apply {
        add(OSMWay(-101810, nodesWay1))
        add(OSMWay(-101814, nodesWay2))
    }

    private val actualNodes = ArrayList<OSMNode>().apply {
        add(OSMNode(-101784, 12.924779032410207, 50.82456885400762, ArrayList()))
        add(OSMNode(-101785, 12.92884117889, 50.82454977561, ArrayList()))
        add(OSMNode(-101786, 12.92878241257, 50.81955652993, ArrayList()))
        add(OSMNode(-101787, 12.91252180111, 50.81963290817, ArrayList()))
        add(OSMNode(-101796, 12.91258056743, 50.82462614568, ArrayList()))
        add(OSMNode(-101792, 12.91712490605291, 50.82460480260446, ArrayList()))
        add(OSMNode(-101789, 12.91681234849, 50.82954070074, ArrayList()))
        add(OSMNode(-101795, 12.92445197468, 50.82973371692, ArrayList()))
        add(OSMNode(-101784, 12.924779032410207, 50.82456885400762, ArrayList()))
    }

    private val actualWays = ArrayList<OSMWay>().apply {
        add(OSMWay(-101827, actualNodes))
    }


    @Test
    fun mergeOverlaps() {
        val data = OSMDataSet(nodes, ways)
        val optimizedData = Optimizer().mergeOverlaps(data)

        assertEquals(optimizedData.ways.size, actualWays.size)
        for (i in 0 until actualWays.size) {
            assertTrue(assertEqualsPoints(optimizedData.ways[i].points, actualWays[i].points))
        }

        // export for development purpose only
        val filename = "${System.getProperty("user.dir")}\\src\\test\\output\\mergeOverlapsTest.osm"
        Exporter.exportOSM(filename, optimizedData, addTimestamp = false)
    }

    @Test
    fun mergeCloseNodes() {
    }

    @Test
    fun orthogonalizeShape() {
    }

    // helper

    private fun assertEqualsPoints(points1: ArrayList<OSMNode>, points2: ArrayList<OSMNode>): Boolean {
        if (points1.size != points2.size) return false
        for (i in points1.indices) {
            if (points1[i].x != points2[i].x) return false
            if (points1[i].y != points2[i].y) return false
        }
        return true
    }

}