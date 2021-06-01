package de.rebsc.oio.data

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory


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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see {@literal<http://www.gnu.org/licenses/>}.
 *****************************************************************************/

/**
 * Data set holding nodes and ways
 */
class OSMDataSet(val nodes: ArrayList<OSMNode>, val ways: ArrayList<OSMWay>) {

    constructor() : this(ArrayList<OSMNode>(), ArrayList<OSMWay>())

    fun addNodes(nodes: List<OSMNode>) {
        this.nodes.addAll(nodes)
    }

    fun addWays(ways: List<OSMWay>) {
        this.ways.addAll(ways)
    }

    fun addWay(way: OSMWay) {
        ways.add(way)
    }
}

/**
 * Node holding coordinates and tags
 */
data class OSMNode(val id: Long, val x: Double, val y: Double, val tags: List<OSMTag>) {

    constructor(id: Long, point: Point2D, tags: List<OSMTag>) : this(id, point.x, point.y, tags)
    constructor(id: Long, point: Point2D) : this(id, point.x, point.y, ArrayList<OSMTag>())
}

/**
 * Way holding nodes and tags
 */
class OSMWay(val id: Long, val points: ArrayList<OSMNode>, val tags: ArrayList<OSMTag>) {

    constructor(id: Long, nodes: ArrayList<OSMNode>) : this(id, nodes, ArrayList<OSMTag>())
    constructor(id: Long) : this(id, ArrayList<OSMNode>(), ArrayList<OSMTag>())

    /**
     * Gets intersection points of this and [way]
     * @param way to get intersections with
     * @return array with intersection points, empty if none
     */
    fun intersects(way: OSMWay): ArrayList<Point2D> {
        val coord1 = arrayOfNulls<Coordinate>(this.points.size)
        for (i in 0 until this.points.size) {
            coord1[i] = Coordinate(this.points[i].x, this.points[i].y)
        }
        val coord2 = arrayOfNulls<Coordinate>(way.points.size)
        for (i in 0 until way.points.size) {
            coord2[i] = Coordinate(way.points[i].x, way.points[i].y)
        }
        val poly1 = GeometryFactory().createLinearRing(coord1)
        val poly2 = GeometryFactory().createLinearRing(coord2)

        val intersectingPoints = poly1.intersection(poly2)

        val intersects = ArrayList<Point2D>()
        intersectingPoints.coordinates.forEach { c ->
            intersects.add(Point2D(c.x, c.y))
        }
        return intersects
    }
}

/**
 * Object tag with key, value pair
 */
data class OSMTag(val k: String, val v: String)

